package com.crazysunj.multityperecyclerviewadapter.helper;

import com.crazysunj.multitypeadapter.helper.AsynAdapterHelper;
import com.crazysunj.multityperecyclerviewadapter.R;

/**
 * description
 * <p>
 * Created by sunjian on 2017/5/4.
 */

public class SimpleHelper extends AsynAdapterHelper<MultiHeaderEntity> {

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
    protected void registerModule() {
        registerModule(LEVEL_FIRST)
                .type(TYPE_ONE)
                .layoutResId(R.layout.item_first)
                .headerResId(R.layout.item_header)
                .loading()
                .loadingLayoutResId(R.layout.layout_default_shimmer_view)
                .loadingHeaderResId(R.layout.layout_default_shimmer_header_view)
                .register();


        registerModule(LEVEL_FOURTH)
                .type(TYPE_TWO)
                .layoutResId(R.layout.item_fourth)
                .headerResId(R.layout.item_header_img)
                .loading()
                .loadingLayoutResId(R.layout.layout_default_shimmer_view)
                .loadingHeaderResId(R.layout.layout_default_shimmer_header_view)
                .register();

        registerModule(LEVEL_SENCOND)
                .type(TYPE_THREE)
                .layoutResId(R.layout.item_second)
                .headerResId(R.layout.item_header_img)
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
                .register();
    }

    public long getHeaderId(int position) {
        int preDataCount = getPreDataCount();
        if (position < preDataCount) {
            return -1;
        }
        MultiHeaderEntity data = mData.get(position - preDataCount);
        return data.getHeaderId();
    }
}
