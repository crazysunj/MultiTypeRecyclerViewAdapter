package com.crazysunj.multityperecyclerviewadapter.expand;

import com.crazysunj.multitypeadapter.sticky.StickyHeaderDecoration;

import java.util.UUID;

/**
 * description
 * <p>
 * Created by sunjian on 2017/7/5.
 */

public class ErrorOCEntity implements OpenCloseItem {

    private long id;

    private String title;
    private int type;
    private final int FLAG = -4;

    public ErrorOCEntity(int type, String title) {
        this.type = type;
        this.title = title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
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
        if (id == 0) {
            return id = UUID.nameUUIDFromBytes((FLAG + title).getBytes()).hashCode();
        }
        return id;
    }

    @Override
    public long getHeaderId() {
        return StickyHeaderDecoration.NO_HEADER_ID;
    }
}
