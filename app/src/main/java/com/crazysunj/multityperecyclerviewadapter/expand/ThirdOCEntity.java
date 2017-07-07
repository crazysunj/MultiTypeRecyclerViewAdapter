package com.crazysunj.multityperecyclerviewadapter.expand;

import com.crazysunj.multitypeadapter.sticky.StickyHeaderDecoration;

import java.util.UUID;

/**
 * description
 * <p>
 * Created by sunjian on 2017/7/5.
 */

public class ThirdOCEntity implements OpenCloseItem {

    public static final int OC_THIRD_TYPE = 9;
    private long id;

    private String title;
    private final int FLAG = 3;

    public ThirdOCEntity(String title) {
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
        return OC_THIRD_TYPE;
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
