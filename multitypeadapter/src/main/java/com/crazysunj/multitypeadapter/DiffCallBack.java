package com.crazysunj.multitypeadapter;

import android.support.v7.util.DiffUtil;

import com.crazysunj.multitypeadapter.entity.MultiHeaderEntity;

import java.util.List;

/**
 * 关于新老数据比较的callback，暂时不提供出去，大可自己实现一个，关于接口已经提供
 * Created by sunjian on 2017/3/28.
 */

final class DiffCallBack<T extends MultiHeaderEntity> extends DiffUtil.Callback {

    private List<T> mOldDatas;
    private List<T> mNewDatas;

    public DiffCallBack(List<T> mOldDatas, List<T> mNewDatas) {
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
        T oldItem = mOldDatas.get(oldItemPosition);
        T newItem = mNewDatas.get(newItemPosition);
        if (oldItem == null || newItem == null) {
            return false;
        }
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        T oldItem = mOldDatas.get(oldItemPosition);
        T newItem = mNewDatas.get(newItemPosition);
        if (oldItem == null || newItem == null) {
            return false;
        }
        return oldItem.getId() == newItem.getId();
    }
}
