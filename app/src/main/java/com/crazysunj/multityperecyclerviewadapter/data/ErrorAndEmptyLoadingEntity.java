package com.crazysunj.multityperecyclerviewadapter.data;

import com.crazysunj.multityperecyclerviewadapter.sticky.StickyItem;

import java.util.UUID;

/**
 * description
 * <p>
 * Created by sunjian on 2017/7/3.
 */

public class ErrorAndEmptyLoadingEntity implements StickyItem {

    private long id;

    private int type;

    private long headerId;

    private String stickyName;

    public ErrorAndEmptyLoadingEntity(int type) {
        this.id = UUID.nameUUIDFromBytes(("loading_" + type).getBytes()).hashCode();
        this.type = type;
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
        return headerId;
    }

    @Override
    public void setHeaderId(long headerId) {
        this.headerId = headerId;
    }

    @Override
    public void setStickyName(String stickyName) {
        this.stickyName = stickyName;
    }

    @Override
    public String getStickyName() {
        return stickyName;
    }
}
