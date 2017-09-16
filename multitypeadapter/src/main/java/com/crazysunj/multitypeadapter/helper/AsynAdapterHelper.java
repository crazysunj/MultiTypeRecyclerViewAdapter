/**
 * Copyright 2017 Sun Jian
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.crazysunj.multitypeadapter.helper;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;

import com.crazysunj.multitypeadapter.entity.HandleBase;
import com.crazysunj.multitypeadapter.entity.MultiTypeEntity;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 异步适配器，数据差异的计算在子线程完成，避免不必要的数据量过大计算而引起的ANR，如果计算时间过长可考虑直接刷新全部数据
 * 刷新全部数据时注意，不可改变mData的引用，不然刷新无效
 * 但是同步的UI因为数据量太多而卡顿，我就无能为力了
 * 如果是使用本库而crash，一般对LayoutManager的onLayoutChildren()和scrollVerticallyBy()抓住异常就行了
 * 如果出现以上引起的crash，我希望您能反馈给我
 * Created by sunjian on 2017/4/1.
 */

public abstract class AsynAdapterHelper<T extends MultiTypeEntity, A extends RecyclerView.Adapter> extends RecyclerViewAdapterHelper<T, A> {

    private static final int HANDLE_DATA_UPDATE = 1;

    protected ScheduledExecutorService mExecutor = Executors.newSingleThreadScheduledExecutor();
    protected ScheduledFuture<?> mFuture;

    @SuppressLint("HandlerLeak")
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

    public AsynAdapterHelper(List<T> data) {
        super(data);
    }

    public AsynAdapterHelper(List<T> data, @RefreshMode int mode) {
        super(data, mode);
    }

    @Override
    protected void startRefresh(HandleBase<T> refreshData) {
        cancelFuture();
        mFuture = mExecutor.schedule(new HandleTask(refreshData),
                0, TimeUnit.MILLISECONDS);
    }


    protected void cancelFuture() {

        if (mFuture != null && !mFuture.isCancelled()) {
            mFuture.cancel(true);
            mFuture = null;
        }
    }

    /**
     * 重置刷新队列
     */
    public void reset() {
        clearQueue();
        cancelFuture();
    }

    private final class HandleTask implements Runnable {

        private HandleBase<T> mHandleBase;

        HandleTask(HandleBase<T> handleBase) {
            mHandleBase = handleBase;
        }

        @Override
        public void run() {
            final List<T> newData = mHandleBase.getNewData();
            final T newHeader = mHandleBase.getNewHeader();
            final T newFooter = mHandleBase.getNewFooter();
            final int refreshType = mHandleBase.getRefreshType();
            final int type = mHandleBase.getType();
            DiffUtil.DiffResult result = handleRefresh(newData, newHeader, newFooter, type, refreshType);
            Message message = mHandler.obtainMessage(HANDLE_DATA_UPDATE);
            message.obj = result;
            message.sendToTarget();
        }
    }
}
