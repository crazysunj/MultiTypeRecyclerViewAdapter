package com.crazysunj.multityperecyclerviewadapter.helper;

import android.annotation.SuppressLint;
import android.util.Log;

import com.crazysunj.multitypeadapter.entity.HandleBase;
import com.crazysunj.multitypeadapter.helper.RecyclerViewAdapterHelper;
import com.crazysunj.multityperecyclerviewadapter.R;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.crazysunj.multityperecyclerviewadapter.helper.SimpleHelper.LEVEL_FIRST;
import static com.crazysunj.multityperecyclerviewadapter.helper.SimpleHelper.LEVEL_FOURTH;
import static com.crazysunj.multityperecyclerviewadapter.helper.SimpleHelper.LEVEL_SENCOND;
import static com.crazysunj.multityperecyclerviewadapter.helper.SimpleHelper.LEVEL_THIRD;
import static com.crazysunj.multityperecyclerviewadapter.helper.SimpleHelper.TYPE_FOUR;
import static com.crazysunj.multityperecyclerviewadapter.helper.SimpleHelper.TYPE_ONE;
import static com.crazysunj.multityperecyclerviewadapter.helper.SimpleHelper.TYPE_THREE;
import static com.crazysunj.multityperecyclerviewadapter.helper.SimpleHelper.TYPE_TWO;

/**
 * description
 * <p>
 * Created by sunjian on 2017/5/6.
 */

public class RxAdapterHelper extends RecyclerViewAdapterHelper<MultiHeaderEntity> {

    public RxAdapterHelper() {
        this(null);
    }

    public RxAdapterHelper(List<MultiHeaderEntity> data, @RefreshMode int mode) {
        super(data, mode);
    }

    public RxAdapterHelper(List<MultiHeaderEntity> data) {
        super(data);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void startRefresh(HandleBase<MultiHeaderEntity> refreshData) {
        Flowable.just(refreshData)
                .onBackpressureDrop()
                .observeOn(Schedulers.io())
                .map(handleBase -> handleRefresh(handleBase.getNewData(), handleBase.getNewHeader(), handleBase.getNewFooter(), handleBase.getLevel(), handleBase.getRefreshType()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResult);
    }

    public long getHeaderId(int position) {
        int preDataCount = getPreDataCount();
        if (position < preDataCount) {
            return -1;
        }
        MultiHeaderEntity data = mData.get(position - preDataCount);
        return data.getHeaderId();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "刷新开始---当前Type:" + getCurrentRefreshLevel());
    }

    @Override
    protected void onEnd() {
        super.onEnd();
        Log.d(TAG, "刷新结束");
    }

    @Override
    protected void registerModule() {
        registerModule(LEVEL_FIRST)
                .type(TYPE_ONE)
                .layoutResId(R.layout.item_first)
                .headerResId(R.layout.item_header)
                .loading()
                .loadingLayoutResId(R.layout.layout_default_shimmer_view)
                .loadingHeaderResId(R.layout.layout_default_shimmer_header_view)
                .register();

        registerModule(LEVEL_THIRD)
                .type(TYPE_FOUR)
                .layoutResId(R.layout.item_third)
                .headerResId(R.layout.item_header_img)
                .loading()
                .loadingLayoutResId(R.layout.layout_default_shimmer_view)
                .loadingHeaderResId(R.layout.layout_default_shimmer_header_view)
                .empty()
                .emptyLayoutResId(R.layout.layout_empty)
                .register();

        registerModule(LEVEL_FOURTH)
                .type(TYPE_TWO)
                .layoutResId(R.layout.item_fourth)
                .headerResId(R.layout.item_header_img)
                .loading()
                .loadingLayoutResId(R.layout.layout_default_shimmer_view)
                .loadingHeaderResId(R.layout.layout_default_shimmer_header_view)
                .error()
                .errorLayoutResId(R.layout.layout_error_two)
                .register();

        registerModule(LEVEL_SENCOND)
                .type(TYPE_THREE)
                .layoutResId(R.layout.item_second)
                .headerResId(R.layout.item_header_img)
                .loading()
                .loadingLayoutResId(R.layout.layout_default_shimmer_view)
                .loadingHeaderResId(R.layout.layout_default_shimmer_header_view)
                .error()
                .errorLayoutResId(R.layout.layout_error)
                .register();

    }

//    @Override
//    protected DiffUtil.Callback getDiffCallBack(List<MultiHeaderEntity> oldData, List<MultiHeaderEntity> newData) {
//        return new SimpleDiffCallBack(oldData, newData);
//    }
}
