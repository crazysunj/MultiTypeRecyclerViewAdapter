package com.crazysunj.multityperecyclerviewadapter.header;

import com.crazysunj.multityperecyclerviewadapter.SampleAdapter;
import com.crazysunj.multityperecyclerviewadapter.sticky.FourthStickyItem;

/**
 * Created by sunjian on 2017/3/28.
 */

public class HeaderFourthItem implements FourthStickyItem {

    private String name;
    private long id;
    private String stickyName = "第四条粘性";

    public HeaderFourthItem(String name, long id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public long getHeaderId() {
        return 4;
    }

    @Override
    public int getItemType() {
        return SampleAdapter.TYPE_HEADER_IMG;
    }

    @Override
    public String getStickyName() {
        return stickyName;
    }
}
