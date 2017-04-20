package com.crazysunj.multitypeadapter.entity;

import com.crazysunj.multitypeadapter.sticky.StickyHeaderDecoration;

/**
 * description
 * <p>
 * Created by sunjian on 2017/4/20.
 */

public class ShimmerEntity implements MultiHeaderEntity {

    private long id;
    private int type;

    public ShimmerEntity() {
    }

    public ShimmerEntity(long id, int type) {
        this.id = id;
        this.type = type;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public void setType(int type) {
        this.type = type;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public long getHeaderId() {
        return StickyHeaderDecoration.NO_HEADER_ID;
    }

    @Override
    public int getItemType() {
        return type;
    }
}
