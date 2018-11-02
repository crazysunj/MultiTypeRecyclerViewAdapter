package com.crazysunj.multityperecyclerviewadapter.testlevel;

import java.util.List;

import androidx.recyclerview.widget.DiffUtil;

/**
 * author: sunjian
 * created on: 2018/4/3 下午4:32
 * description:
 */
public class TestLevelDiffCallback extends DiffUtil.Callback {

    private List<MultiTypeTitleEntity> mOldData;
    private List<MultiTypeTitleEntity> mNewData;

    public TestLevelDiffCallback(List<MultiTypeTitleEntity> oldData, List<MultiTypeTitleEntity> newData) {
        this.mOldData = oldData;
        this.mNewData = newData;
    }

    @Override
    public int getOldListSize() {
        return mOldData == null ? 0 : mOldData.size();
    }

    @Override
    public int getNewListSize() {
        return mNewData == null ? 0 : mNewData.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        MultiTypeTitleEntity oldItem = mOldData.get(oldItemPosition);
        MultiTypeTitleEntity newItem = mNewData.get(newItemPosition);
        return !(oldItem == null || newItem == null) && oldItem.getItemType() == newItem.getItemType();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        MultiTypeTitleEntity oldItem = mOldData.get(oldItemPosition);
        MultiTypeTitleEntity newItem = mNewData.get(newItemPosition);
        if (oldItem.getId() == newItem.getId()) {
            String oldMsg = oldItem.getMsg();
            String newMsg = newItem.getMsg();
            return !(oldMsg == null || newMsg == null) && oldMsg.equals(newMsg);
        }
        return false;
    }
}
