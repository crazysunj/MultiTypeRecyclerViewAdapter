package com.crazysunj.multityperecyclerviewadapter.expand;

import com.crazysunj.multitypeadapter.sticky.StickyHeaderDecoration;

/**
 * description
 * <p>
 * Created by sunjian on 2017/7/5.
 */

public class LoadingOCEntity implements OpenCloseItem {

    private long id;

    private int type;
    private final int FLAG = -5;

    public LoadingOCEntity(int type, long id) {
        this.type = type;
        this.id = id;
    }

    @Override
    public String getTitle() {
        return "";
    }

    @Override
    public int getFlag() {
        return FLAG;
    }

    @Override
    public int getItemType() {
        return type;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public long getHeaderId() {
        return StickyHeaderDecoration.NO_HEADER_ID;
    }
}
