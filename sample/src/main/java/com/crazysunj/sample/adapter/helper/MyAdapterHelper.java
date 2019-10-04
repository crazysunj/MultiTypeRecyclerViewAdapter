package com.crazysunj.sample.adapter.helper;

import androidx.recyclerview.widget.DiffUtil;

import com.crazysunj.multitypeadapter.helper.AsynAdapterHelper;
import com.crazysunj.sample.R;
import com.crazysunj.sample.base.MutiTypeTitleEntity;

import java.util.List;

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
    private DataChangeCallback callback;

    public MyAdapterHelper(DataChangeCallback callback) {
        super();
        this.callback = callback;
        registerModule();
    }

    private void registerModule() {

        registerModule(LEVEL_HEAD)
                .loading()
                .loadingLayoutResId(R.layout.layout_loading)
                .register();

        registerModule(MyAdapterHelper.LEVEL_1)
                .loading()
                .loadingHeaderResId(R.layout.layout_loading_header)
                .loadingLayoutResId(R.layout.layout_loading)
                .register();

        registerModule(LEVEL_2)
                .loading()
                .loadingHeaderResId(R.layout.layout_loading_header)
                .loadingLayoutResId(R.layout.layout_loading)
                .register();

        registerModule(LEVEL_3)
                .loading()
                .loadingLayoutResId(R.layout.layout_loading)
                .register();

        registerModule(LEVEL_4)
                .loading()
                .loadingHeaderResId(R.layout.layout_loading_header)
                .loadingLayoutResId(R.layout.layout_loading)
                .register();

        registerModule(LEVEL_FOOT)
                .loading()
                .loadingLayoutResId(R.layout.layout_loading)
                .register();
    }

    @Override
    protected void onEnd() {
        if (callback != null) {
            callback.onEnd(getCurrentRefreshLevel());
        }
        super.onEnd();
    }

    public interface DataChangeCallback {
        /**
         * 这里只提供结束时的回调，开始的同理
         *
         * @param level level
         */
        void onEnd(int level);
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
            return oldItem.getTitle().equals(newItem.getTitle());
        }
    }
}
