package com.crazysunj.multityperecyclerviewadapter.expand;

import com.crazysunj.multitypeadapter.sticky.StickyHeaderDecoration;

import java.util.UUID;

/**
 * description
 * <p>
 * Created by sunjian on 2017/7/5.
 */

public class FirstOCEntity implements OpenCloseItem {

    public static final int OC_FIRST_TYPE = 3;
    private long id;

    private String title;
    private final int FLAG = 1;

    public FirstOCEntity(String title) {
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
        return OC_FIRST_TYPE;
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
