package com.crazysunj.multityperecyclerviewadapter.helper;

import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.crazysunj.multitypeadapter.entity.MultiHeaderEntity;
import com.crazysunj.multitypeadapter.helper.RecyclerViewAdapterHelper;

/**
 * description
 * <p>
 * Created by sunjian on 2017/7/5.
 */

public abstract class BaseHelperAdapter<T extends MultiHeaderEntity, K extends RecyclerViewAdapterHelper> extends BaseQuickAdapter<T, BaseViewHolder> {

    protected K mHelper;

    public BaseHelperAdapter(K helper) {
        super(helper.getData());
        helper.bindAdapter(this);
        mHelper = helper;
    }

    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        return createBaseViewHolder(parent, mHelper.getLayoutId(viewType));
    }

    @Override
    protected int getDefItemViewType(int position) {
        return mHelper.getItemViewType(position);
    }
}
