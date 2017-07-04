package com.crazysunj.multityperecyclerviewadapter.data;

import com.crazysunj.multitypeadapter.sticky.StickyHeaderDecoration;
import com.crazysunj.multityperecyclerviewadapter.sticky.StickyItem;

/**
 * description
 * <p>
 * Created by sunjian on 2017/7/3.
 */

public abstract class NoStickyEntity implements StickyItem {

    @Override
    public void setHeaderId(long headerId) {
        
    }

    @Override
    public void setStickyName(String stickyName) {

    }

    @Override
    public String getStickyName() {
        return null;
    }

    @Override
    public long getHeaderId() {
        return StickyHeaderDecoration.NO_HEADER_ID;
    }
}
