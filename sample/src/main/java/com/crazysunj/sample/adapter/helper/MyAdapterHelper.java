package com.crazysunj.sample.adapter.helper;

import com.crazysunj.multitypeadapter.helper.AsynAdapterHelper;
import com.crazysunj.sample.R;
import com.crazysunj.sample.base.MutiTypeTitleEntity;
import com.crazysunj.sample.entity.ItemEntity1;
import com.crazysunj.sample.entity.ItemEntity2;
import com.crazysunj.sample.entity.ItemEntity3;
import com.crazysunj.sample.entity.ItemEntity4;

import java.util.List;

import androidx.recyclerview.widget.DiffUtil;

/**
 * author: sunjian
 * created on: 2017/8/3 下午5:38
 * description:
 */

public class MyAdapterHelper extends AsynAdapterHelper<MutiTypeTitleEntity> {

    public static final int LEVEL_HEAD = 0;
    public static final int LEVEL_1 = 1;
    public static final int LEVEL_2 = 2;
    public static final int LEVEL_3 = 3;
    public static final int LEVEL_4 = 4;
    public static final int LEVEL_FOOT = 5;
    public static final int TYPE_HEAD = 4;
    public static final int TYPE_FOOT = 5;

    public MyAdapterHelper() {
        super(null);
    }

    @Override
    protected void registerModule() {

        registerModule(LEVEL_HEAD)
                .type(TYPE_HEAD)
                .layoutResId(R.layout.head_home)
                .loading()
                .loadingLayoutResId(R.layout.layout_loading)
                .register();

        registerModule(LEVEL_1)
                .type(ItemEntity1.TYPE_1)
                .layoutResId(R.layout.item_1)
                .headerResId(R.layout.header_common)
                .footerResId(R.layout.item_footer)
                .isFolded(true)
                .minSize(2)
                .loading()
                .loadingHeaderResId(R.layout.layout_loading_header)
                .loadingLayoutResId(R.layout.layout_loading)
                .register();

        registerModule(LEVEL_2)
                .type(ItemEntity2.TYPE_2)
                .layoutResId(R.layout.item_2)
                .headerResId(R.layout.header_common)
                .loading()
                .loadingHeaderResId(R.layout.layout_loading_header)
                .loadingLayoutResId(R.layout.layout_loading)
                .register();

        registerModule(LEVEL_3)
                .type(ItemEntity3.TYPE_3)
                .layoutResId(R.layout.item_3)
                .loading()
                .loadingLayoutResId(R.layout.layout_loading)
                .register();

        registerModule(LEVEL_4)
                .type(ItemEntity4.TYPE_4)
                .layoutResId(R.layout.item_4)
                .headerResId(R.layout.header_common)
                .loading()
                .loadingHeaderResId(R.layout.layout_loading_header)
                .loadingLayoutResId(R.layout.layout_loading)
                .register();

        registerModule(LEVEL_FOOT)
                .type(TYPE_FOOT)
                .layoutResId(R.layout.footer_home)
                .loading()
                .loadingLayoutResId(R.layout.layout_loading)
                .register();
    }

    @Override
    protected DiffUtil.Callback getDiffCallBack(List<MutiTypeTitleEntity> oldData, List<MutiTypeTitleEntity> newData) {
        return new TitleDiffCallBack(oldData, newData);
    }

    private static class TitleDiffCallBack extends DiffUtil.Callback {

        private List<MutiTypeTitleEntity> mOldDatas;
        private List<MutiTypeTitleEntity> mNewDatas;

        TitleDiffCallBack(List<MutiTypeTitleEntity> mOldDatas, List<MutiTypeTitleEntity> mNewDatas) {
            this.mOldDatas = mOldDatas;
            this.mNewDatas = mNewDatas;
        }

        @Override
        public int getOldListSize() {
            return mOldDatas == null ? 0 : mOldDatas.size();
        }

        @Override
        public int getNewListSize() {
            return mNewDatas == null ? 0 : mNewDatas.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {

            MutiTypeTitleEntity oldItem = mOldDatas.get(oldItemPosition);
            MutiTypeTitleEntity newItem = mNewDatas.get(newItemPosition);
            return !(oldItem == null || newItem == null) && oldItem.getItemType() == newItem.getItemType();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            MutiTypeTitleEntity oldItem = mOldDatas.get(oldItemPosition);
            MutiTypeTitleEntity newItem = mNewDatas.get(newItemPosition);
            return oldItem.getId() == newItem.getId() && oldItem.getTitle().equals(newItem.getTitle());
        }
    }
}
