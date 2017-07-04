package com.crazysunj.multityperecyclerviewadapter.helper;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;

import com.crazysunj.multitypeadapter.entity.HandleBase;
import com.crazysunj.multitypeadapter.entity.MultiHeaderEntity;
import com.crazysunj.multitypeadapter.helper.RecyclerViewAdapterHelper;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * description
 * <p>
 * Created by sunjian on 2017/5/6.
 */

public abstract class SimpleRxAdapterHelper<T extends MultiHeaderEntity, A extends RecyclerView.Adapter> extends RecyclerViewAdapterHelper<T, A> {

    public SimpleRxAdapterHelper(List<T> data) {
        super(data);
    }

    public SimpleRxAdapterHelper(List<T> data, @RefreshMode int mode) {
        super(data, mode);
    }

    @Override
    protected void startRefresh(HandleBase<T> refreshData) {
        Flowable.just(refreshData)
                .onBackpressureDrop()
                .observeOn(Schedulers.computation())
                .map(new Function<HandleBase<T>, DiffUtil.DiffResult>() {
                    @Override
                    public DiffUtil.DiffResult apply(@NonNull HandleBase<T> handleBase) throws Exception {
                        return handleRefresh(handleBase.getNewData(), handleBase.getNewHeader(), handleBase.getNewFooter(), handleBase.getType(), handleBase.getRefreshType());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DiffUtil.DiffResult>() {
                    @Override
                    public void accept(@NonNull DiffUtil.DiffResult diffResult) throws Exception {
                        handleResult(diffResult);
                    }
                });
    }
}
