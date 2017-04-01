package com.crazysunj.multitypeadapter;

import android.os.Handler;
import android.os.Message;
import android.support.v7.util.DiffUtil;

import com.chad.library.adapter.base.BaseViewHolder;
import com.crazysunj.multitypeadapter.entity.HandleBase;
import com.crazysunj.multitypeadapter.entity.MultiHeaderEntity;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 异步适配器，不影响用户使用，但是同步的UI因为数据量太多而卡顿，我就无能为力了
 * 注意：使用时最好采用recyclerview 24.2.0及以下的版本，不然会引发Google的BUG
 * 如果是使用本库而crash，一般对LayoutManager的onLayoutChildren()和scrollVerticallyBy()抓住异常就行了
 * Created by sunjian on 2017/4/1.
 */

public class AsynMultiTypeAdapter<T extends MultiHeaderEntity, K extends BaseViewHolder> extends BaseMultiTypeRecyclerViewAdapter<T, K> {

    private static final int HANDLE_DATA_UPDATE = 1;

    protected ScheduledExecutorService mExecutor = Executors.newSingleThreadScheduledExecutor();
    protected ScheduledFuture<?> mFuture;

    public AsynMultiTypeAdapter(boolean isUseStickyHeader) {
        super(isUseStickyHeader);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == HANDLE_DATA_UPDATE) {
                try {
                    DiffUtil.DiffResult diffResult = (DiffUtil.DiffResult) msg.obj;
                    handleResult(diffResult);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void startRefresh(List<T> newData, T newHeader, int type, int refreshType) {

        cancelFuture();
        showDialog();
        mFuture = mExecutor.schedule(new HandleTask(new HandleBase<T>(newData, newHeader, type, refreshType)),
                0, TimeUnit.MILLISECONDS);
    }

    protected void cancelFuture() {

        if (mFuture != null && !mFuture.isCancelled()) {
            mFuture.cancel(true);
            mFuture = null;
        }
    }

    private final class HandleTask implements Runnable {

        private HandleBase<T> mHandleBase;

        HandleTask(HandleBase<T> handleBase) {
            mHandleBase = handleBase;
        }

        @Override
        public void run() {
            List<T> newData = mHandleBase.getNewData();
            T newHeader = mHandleBase.getNewHeader();
            int refreshType = mHandleBase.getRefreshType();
            int type = mHandleBase.getType();
            DiffUtil.DiffResult result = handleRefresh(newData, newHeader, type, refreshType);
            Message message = mHandler.obtainMessage(HANDLE_DATA_UPDATE);
            message.obj = result;
            message.sendToTarget();
        }
    }
}
