package com.crazysunj.multityperecyclerviewadapter.expand;

import android.support.v7.util.DiffUtil;

import com.crazysunj.multitypeadapter.helper.AsynAdapterHelper;
import com.crazysunj.multityperecyclerviewadapter.R;

import java.util.List;

/**
 * description
 * <p>
 * Created by sunjian on 2017/7/5.
 */

public class OpenCloseAdapterHelper extends AsynAdapterHelper<OpenCloseItem, OpenCloseAdapter> {

    public OpenCloseAdapterHelper() {
        super(null);
    }

    @Override
    protected void registerMoudle() {

        registerMoudle(FirstOCEntity.OC_FIRST_TYPE)
                .level(0)
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

        registerMoudle(SecondOCEntity.OC_SECOND_TYPE)
                .level(1)
                .layoutResId(R.layout.item_second)
                .headerResId(R.layout.item_header)
                .footerResId(R.layout.item_footer)
                .minSize(2)
                .register();

        registerMoudle(ThirdOCEntity.OC_THIRD_TYPE)
                .level(2)
                .layoutResId(R.layout.item_third)
                .headerResId(R.layout.item_header)
                .footerResId(R.layout.item_footer)
                .minSize(4)
                .register();
    }

    @Override
    protected int getPreDataCount() {
        return mAdapter.getHeaderLayoutCount();
    }

    @Override
    protected DiffUtil.Callback getDiffCallBack(List<OpenCloseItem> oldData, List<OpenCloseItem> newData) {
        return new OpenCloseDiffCallBack(oldData, newData);
    }
}
