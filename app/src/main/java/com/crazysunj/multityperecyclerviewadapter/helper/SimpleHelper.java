package com.crazysunj.multityperecyclerviewadapter.helper;

import com.crazysunj.multitypeadapter.entity.MultiHeaderEntity;
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
        registerMoudleWithShimmer(TYPE_ONE, LEVEL_FIRST, R.layout.item_first,
                R.layout.item_header, R.layout.layout_default_shimmer_view, R.layout.layout_default_shimmer_header_view);
        registerMoudleWithShimmer(TYPE_TWO, LEVEL_FOURTH, R.layout.item_fourth, R.layout.item_header_img,
                R.layout.layout_default_shimmer_view, R.layout.layout_default_shimmer_header_view);
        registerMoudleWithShimmer(TYPE_THREE, LEVEL_SENCOND, R.layout.item_second, R.layout.item_header_img,
                R.layout.layout_default_shimmer_view, R.layout.layout_default_shimmer_header_view);
        registerMoudleWithShimmer(TYPE_FOUR, LEVEL_THIRD, R.layout.item_third,
                R.layout.item_header_img, R.layout.layout_default_shimmer_view, R.layout.layout_default_shimmer_header_view);
    }
}
