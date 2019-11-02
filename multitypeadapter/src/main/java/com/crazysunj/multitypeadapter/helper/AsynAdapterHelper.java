/*
  Copyright 2017 Sun Jian
  <p>
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  <p>
  http://www.apache.org/licenses/LICENSE-2.0
  <p>
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.crazysunj.multitypeadapter.helper;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.recyclerview.widget.DiffUtil;

import com.crazysunj.multitypeadapter.entity.HandleBase;
import com.crazysunj.multitypeadapter.entity.MultiTypeEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: sunjian
 * created on: 2017/4/1
 * description:
 * 异步适配器，数据差异的计算在子线程完成，避免不必要的数据量过大计算而引起的ANR，如果计算时间过长可考虑直接刷新全部数据
 * 刷新全部数据时注意，不可改变mData的引用，不然刷新无效
 */
public class AsynAdapterHelper<T extends MultiTypeEntity> extends RecyclerViewAdapterHelper<T> {

    private static final int HANDLE_DATA_UPDATE = 1;

    protected ExecutorService mExecutor = Executors.newSingleThreadExecutor();

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == HANDLE_DATA_UPDATE) {
                DiffUtil.DiffResult diffResult = (DiffUtil.DiffResult) msg.obj;
                handleResult(diffResult);
            }
        }
    };

    public AsynAdapterHelper() {
    }

    public AsynAdapterHelper(int mode) {
        super(mode);
    }

    @Override
    protected void startRefresh(HandleBase<T> refreshData) {
        mExecutor.execute(new HandleTask(refreshData));
    }

    @Override
    public void release() {
        if (!mExecutor.isShutdown()) {
            mExecutor.shutdown();
        }
        super.release();
    }

    /**
     * 重置刷新队列
     */
    public void reset() {
        clearQueue();
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
            final int level = mHandleBase.getLevel();
            DiffUtil.DiffResult result = handleRefresh(newData, newHeader, newFooter, level, refreshType);
            Message message = mHandler.obtainMessage(HANDLE_DATA_UPDATE);
            message.obj = result;
            message.sendToTarget();
        }
    }
}
