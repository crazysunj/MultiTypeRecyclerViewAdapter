package com.crazysunj.multityperecyclerviewadapter.testlevel;

import android.util.Log;

import com.crazysunj.multitypeadapter.helper.AsynAdapterHelper;
import com.crazysunj.multityperecyclerviewadapter.R;

import java.util.List;

import androidx.recyclerview.widget.DiffUtil;

/**
 * author: sunjian
 * created on: 2018/4/3 上午11:19
 * description:
 */

public class TestLevelAdapterHelper extends AsynAdapterHelper<MultiTypeTitleEntity> {

    public static final int LEVEL_FIRST = 0;
    public static final int LEVEL_SECOND = 1;
    public static final int LEVEL_THIRD = 2;
    public static final int LEVEL_EMPTY_ALL = 3;

    public TestLevelAdapterHelper() {
        super(null);
    }

    public TestLevelAdapterHelper(List<MultiTypeTitleEntity> data) {
        super(data);
    }

    @Override
    protected void registerModule() {
        registerModule(LEVEL_FIRST)
                .type(TypeOneItem.TYPE_ONE)
                .layoutResId(R.layout.item_first)
                .type(TypeTwoItem.TYPE_TWO)
                .layoutResId(R.layout.item_second)
                .headerResId(R.layout.item_header)
                .footerResId(R.layout.item_footer)
                .loading()
                .loadingHeaderResId(R.layout.layout_default_shimmer_header_view)
                .loadingLayoutResId(R.layout.layout_default_shimmer_view)
                .empty()
                .emptyLayoutResId(R.layout.layout_empty)
                .register();

        registerModule(LEVEL_SECOND)
                .type(TypeThreeItem.TYPE_THREE)
                .layoutResId(R.layout.item_third)
                .headerResId(R.layout.item_header)
                .loading()
                .loadingHeaderResId(R.layout.layout_default_shimmer_header_view)
                .loadingLayoutResId(R.layout.layout_default_shimmer_view)
                .error()
                .errorLayoutResId(R.layout.layout_error)
                .register();

        registerModule(LEVEL_THIRD)
                .type(TypeFourItem.TYPE_FOUR)
                .layoutResId(R.layout.item_fourth)
                .type(TypeFiveItem.TYPE_FIVE)
                .layoutResId(R.layout.item_fifth)
                .headerResId(R.layout.item_header)
                .footerResId(R.layout.item_footer)
                .loading()
                .loadingHeaderResId(R.layout.layout_default_shimmer_header_view)
                .loadingLayoutResId(R.layout.layout_default_shimmer_view)
                .register();

        registerModule(LEVEL_EMPTY_ALL)
                .type(TypeEmptyAllItem.TYPE_EMPTY)
                .layoutResId(R.layout.layout_empty_all)
                .register();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "刷新开始---当前Level:" + getCurrentRefreshLevel());
    }

    @Override
    protected void onEnd() {
        super.onEnd();
        Log.d(TAG, "刷新结束");
    }

    @Override
    protected DiffUtil.Callback getDiffCallBack(List<MultiTypeTitleEntity> oldData, List<MultiTypeTitleEntity> newData) {
        return new TestLevelDiffCallback(oldData, newData);
    }
}
