package com.crazysunj.multitypeadapter;

import com.crazysunj.multitypeadapter.entity.MultiHeaderEntity;
import com.crazysunj.multitypeadapter.viewholder.ShimmerViewHolder;

import java.util.List;

/**
 * 同步适配器，数据量过大可能会卡顿，甚至出现anr，但是1500左右的数据还是没问题，相对来说比较稳定
 * Created by sunjian on 2017/4/1.
 */

public class SynMultiTypeAdapter<T extends MultiHeaderEntity, K extends ShimmerViewHolder> extends BaseMultiTypeRecyclerViewAdapter<T, K> {

    public SynMultiTypeAdapter(boolean isUseStickyHeader) {
        super(isUseStickyHeader);
    }

    @Override
    protected void startRefresh(List<T> newData, T newHeader, int type, int refreshType) {
        handleResult(handleRefresh(newData, newHeader, type, refreshType));
    }

}
