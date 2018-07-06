package com.crazysunj.sample.base;

import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.crazysunj.multitypeadapter.helper.RecyclerViewAdapterHelper;

/**
 * author: sunjian
 * created on: 2017/8/3 下午5:18
 * description:
 */

public abstract class BaseAdapter<T extends MutiTypeTitleEntity, K extends BaseViewHolder, H extends RecyclerViewAdapterHelper<T>> extends BaseQuickAdapter<T, K> {

    protected H mHelper;

    public BaseAdapter(H helper) {
        super(helper.getData());
        mHelper = helper;
        mHelper.bindAdapter(this);
    }

    @Override
    protected K onCreateDefViewHolder(ViewGroup parent, int viewType) {
        return createBaseViewHolder(parent, mHelper.getLayoutId(viewType));
    }

    @Override
    protected int getDefItemViewType(int position) {
        return mHelper.getItemViewType(position);
    }

    public H getHelper() {
        return mHelper;
    }
}
