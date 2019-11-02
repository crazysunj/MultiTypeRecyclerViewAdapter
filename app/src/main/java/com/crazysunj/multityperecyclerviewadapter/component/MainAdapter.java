package com.crazysunj.multityperecyclerviewadapter.component;

import com.crazysunj.multitypeadapter.entity.MultiTypeEntity;
import com.crazysunj.multitypeadapter.helper.AdapterHelper;
import com.crazysunj.multitypeadapter.helper.AsynAdapterHelper;
import com.crazysunj.multitypeadapter.helper.RecyclerViewAdapterHelper;

import java.util.List;

/**
 * @author sunjian
 * @date 2019-10-18 19:12
 */
public class MainAdapter extends BaseAdapter<MultiTypeEntity> {

    public MainAdapter() {
        super(new AsynAdapterHelper<MultiTypeEntity>(RecyclerViewAdapterHelper.MODE_MIXED) {
            @Override
            protected boolean isDetectMoves() {
                return false;
            }
        });
    }

    @Override
    protected void init() {
        super.init();
        register(TypeOne.TYPE, TypeOneViewHolder.class);
        register(TypeTwo.TYPE, TypeTwoViewHolder.class);
        register(TypeThree.TYPE, TypeThreeViewHolder.class);
    }

    public void refresh(List<MultiTypeEntity> data) {
        AdapterHelper.action()
                .all(data)
                .into(mHelper);
    }

    public void refreshDiff(List<MultiTypeEntity> data) {
        AdapterHelper.action()
                .allDiff(data)
                .into(mHelper);
    }

    public void loadMore(List<MultiTypeEntity> data) {
        AdapterHelper.action()
                .add(data)
                .into(mHelper);
    }
}
