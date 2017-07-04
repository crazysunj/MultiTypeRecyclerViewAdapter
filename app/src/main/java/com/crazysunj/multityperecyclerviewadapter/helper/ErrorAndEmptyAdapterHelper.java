package com.crazysunj.multityperecyclerviewadapter.helper;

import android.support.v7.util.DiffUtil;
import android.util.Log;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.crazysunj.multitypeadapter.entity.HandleBase;
import com.crazysunj.multitypeadapter.helper.RecyclerViewAdapterHelper;
import com.crazysunj.multityperecyclerviewadapter.R;
import com.crazysunj.multityperecyclerviewadapter.sticky.StickyItem;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
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

public class ErrorAndEmptyAdapterHelper extends RecyclerViewAdapterHelper<StickyItem, BaseQuickAdapter> {

    public ErrorAndEmptyAdapterHelper() {
        this(null);
    }

    public ErrorAndEmptyAdapterHelper(List<StickyItem> data, @RefreshMode int mode) {
        super(data, mode);
    }

    public ErrorAndEmptyAdapterHelper(List<StickyItem> data) {
        super(data);
    }

    @Override
    protected void startRefresh(HandleBase<StickyItem> refreshData) {
        Flowable.just(refreshData)
                .onBackpressureDrop()
                .observeOn(Schedulers.computation())
                .map(new Function<HandleBase<StickyItem>, DiffUtil.DiffResult>() {
                    @Override
                    public DiffUtil.DiffResult apply(@NonNull HandleBase<StickyItem> handleBase) throws Exception {
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

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "刷新开始---当前Type:" + getCurrentRefreshType());
    }

    @Override
    protected void onEnd() {
        super.onEnd();
        Log.d(TAG, "刷新结束");
    }

    @Override
    protected void registerMoudle() {
        registerMoudle(TYPE_ONE)
                .level(LEVEL_FIRST)
                .layoutResId(R.layout.item_first)
                .headerResId(R.layout.item_header)
                .loading()
                .loadingLayoutResId(R.layout.layout_default_shimmer_view)
                .loadingHeaderResId(R.layout.layout_default_shimmer_header_view)
                .register();

        registerMoudle(TYPE_FOUR)
                .level(LEVEL_THIRD)
                .layoutResId(R.layout.item_third)
                .headerResId(R.layout.item_header_img)
                .loading()
                .loadingLayoutResId(R.layout.layout_default_shimmer_view)
                .loadingHeaderResId(R.layout.layout_default_shimmer_header_view)
                .empty()
                .emptyLayoutResId(R.layout.layout_empty)
                .register();

        registerMoudle(TYPE_TWO)
                .level(LEVEL_FOURTH)
                .layoutResId(R.layout.item_fourth)
                .headerResId(R.layout.item_header_img)
                .loading()
                .loadingLayoutResId(R.layout.layout_default_shimmer_view)
                .loadingHeaderResId(R.layout.layout_default_shimmer_header_view)
                .error()
                .errorLayoutResId(R.layout.layout_error_two)
                .register();

        registerMoudle(TYPE_THREE)
                .level(LEVEL_SENCOND)
                .layoutResId(R.layout.item_second)
                .headerResId(R.layout.item_header_img)
                .loading()
                .loadingLayoutResId(R.layout.layout_default_shimmer_view)
                .loadingHeaderResId(R.layout.layout_default_shimmer_header_view)
                .error()
                .errorLayoutResId(R.layout.layout_error)
                .register();

    }

    @Override
    protected int getPreDataCount() {
        return mAdapter.getHeaderLayoutCount();
    }
}
