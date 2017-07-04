package com.crazysunj.multityperecyclerviewadapter.helper;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.crazysunj.multitypeadapter.entity.MultiHeaderEntity;
import com.crazysunj.multitypeadapter.helper.AsynAdapterHelper;
import com.crazysunj.multityperecyclerviewadapter.R;

/**
 * description
 * <p>
 * Created by sunjian on 2017/5/4.
 */

public class SimpleHelper extends AsynAdapterHelper<MultiHeaderEntity, BaseQuickAdapter> {

    public static final int TYPE_ONE = 0;
    public static final int TYPE_TWO = 1;
    public static final int TYPE_THREE = 2;
    public static final int TYPE_FOUR = 3;

    public static final int LEVEL_FIRST = 0;
    public static final int LEVEL_SENCOND = 1;
    public static final int LEVEL_THIRD = 2;
    public static final int LEVEL_FOURTH = 3;

    public SimpleHelper() {
        super(null);
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


        registerMoudle(TYPE_TWO)
                .level(LEVEL_FOURTH)
                .layoutResId(R.layout.item_fourth)
                .headerResId(R.layout.item_header_img)
                .loading()
                .loadingLayoutResId(R.layout.layout_default_shimmer_view)
                .loadingHeaderResId(R.layout.layout_default_shimmer_header_view)
                .register();

        registerMoudle(TYPE_THREE)
                .level(LEVEL_SENCOND)
                .layoutResId(R.layout.item_second)
                .headerResId(R.layout.item_header_img)
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
                .register();
    }

    @Override
    protected int getPreDataCount() {
        return mAdapter.getHeaderLayoutCount();
    }
}
