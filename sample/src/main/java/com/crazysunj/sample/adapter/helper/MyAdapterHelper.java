package com.crazysunj.sample.adapter.helper;

import android.support.v7.util.DiffUtil;

import com.crazysunj.multitypeadapter.helper.AsynAdapterHelper;
import com.crazysunj.sample.R;
import com.crazysunj.sample.base.BaseAdapter;
import com.crazysunj.sample.base.MutiTypeTitleEntity;
import com.crazysunj.sample.entity.ItemEntity1;
import com.crazysunj.sample.entity.ItemEntity2;
import com.crazysunj.sample.entity.ItemEntity3;
import com.crazysunj.sample.entity.ItemEntity4;

import java.util.List;

/**
 * author: sunjian
 * created on: 2017/8/3 下午5:38
 * description:
 */

public class MyAdapterHelper extends AsynAdapterHelper<MutiTypeTitleEntity, BaseAdapter> {


    public MyAdapterHelper() {
        super(null);
    }

    @Override
    protected void registerMoudle() {

        registerMoudle(ItemEntity1.TYPE_1)
                .level(0)
                .headerResId(R.layout.header_common)
                .layoutResId(R.layout.item_1)
                .footerResId(R.layout.item_footer)
                .isFolded(true)
                .minSize(2)
                .loading()
                .loadingHeaderResId(R.layout.layout_loading_header)
                .loadingLayoutResId(R.layout.layout_loading)
                .register();

        registerMoudle(ItemEntity2.TYPE_2)
                .level(1)
                .headerResId(R.layout.header_common)
                .layoutResId(R.layout.item_2)
                .loading()
                .loadingHeaderResId(R.layout.layout_loading_header)
                .loadingLayoutResId(R.layout.layout_loading)
                .register();

        registerMoudle(ItemEntity3.TYPE_3)
                .level(2)
                .layoutResId(R.layout.item_3)
                .loading()
                .loadingLayoutResId(R.layout.layout_loading)
                .register();

        registerMoudle(ItemEntity4.TYPE_4)
                .level(3)
                .headerResId(R.layout.header_common)
                .layoutResId(R.layout.item_4)
                .loading()
                .loadingHeaderResId(R.layout.layout_loading_header)
                .loadingLayoutResId(R.layout.layout_loading)
                .register();
    }

    @Override
    protected int getPreDataCount() {
        return mAdapter.getHeaderLayoutCount();
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
