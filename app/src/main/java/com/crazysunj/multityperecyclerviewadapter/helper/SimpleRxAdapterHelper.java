package com.crazysunj.multityperecyclerviewadapter.helper;

import android.annotation.SuppressLint;

import com.crazysunj.multitypeadapter.entity.HandleBase;
import com.crazysunj.multitypeadapter.helper.RecyclerViewAdapterHelper;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * description
 * <p>
 * Created by sunjian on 2017/5/6.
 */

public abstract class SimpleRxAdapterHelper<T extends MultiHeaderEntity> extends RecyclerViewAdapterHelper<T> {

    public SimpleRxAdapterHelper(List<T> data) {
        super(data);
    }

    public SimpleRxAdapterHelper(List<T> data, @RefreshMode int mode) {
        super(data, mode);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void startRefresh(HandleBase<T> refreshData) {
        Flowable.just(refreshData)
                .onBackpressureDrop()
                .observeOn(Schedulers.computation())
                .map(handleBase -> handleRefresh(handleBase.getNewData(), handleBase.getNewHeader(), handleBase.getNewFooter(), handleBase.getLevel(), handleBase.getRefreshType()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResult);
    }

    public long getHeaderId(int position) {
        int preDataCount = getPreDataCount();
        if (position < preDataCount) {
            return -1;
        }
        T data = mData.get(position - preDataCount);
        return data.getHeaderId();
    }
}
