package com.crazysunj.multityperecyclerviewadapter.expand;

import com.crazysunj.multitypeadapter.helper.AsynAdapterHelper;
import com.crazysunj.multityperecyclerviewadapter.R;

/**
 * description
 * <p>
 * Created by sunjian on 2017/7/5.
 */

public class OpenCloseAdapterHelper extends AsynAdapterHelper<OpenCloseItem> {

    public static final int LEVEL_FIRST = 0;
    public static final int LEVEL_SECOND = 1;
    public static final int LEVEL_THIRD = 2;

    public OpenCloseAdapterHelper() {
        super();
        registerModule();
    }

    private void registerModule() {

        registerModule(LEVEL_FIRST)
                .type(FirstOCEntity.OC_FIRST_TYPE)
                .layoutResId(R.layout.item_first)
                .headerResId(R.layout.item_header)
                .footerResId(R.layout.item_footer)
                .isFolded(true)
                .minSize(3)
                .loading()
                .loadingHeaderResId(R.layout.layout_default_shimmer_header_view)
                .loadingLayoutResId(R.layout.layout_default_shimmer_view)
                .error()
                .errorLayoutResId(R.layout.layout_error_two)
                .empty()
                .emptyLayoutResId(R.layout.layout_empty)
                .register();

        registerModule(LEVEL_SECOND)
                .type(SecondOCEntity.OC_SECOND_TYPE)
                .layoutResId(R.layout.item_second)
                .headerResId(R.layout.item_header)
                .footerResId(R.layout.item_footer)
                .minSize(2)
                .register();

        registerModule(LEVEL_THIRD)
                .type(ThirdOCEntity.OC_THIRD_TYPE)
                .layoutResId(R.layout.item_third)
                .headerResId(R.layout.item_header)
                .footerResId(R.layout.item_footer)
                .minSize(4)
                .register();
    }
}
