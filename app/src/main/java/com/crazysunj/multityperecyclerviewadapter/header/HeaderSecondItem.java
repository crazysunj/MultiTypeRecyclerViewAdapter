package com.crazysunj.multityperecyclerviewadapter.header;

import com.crazysunj.multityperecyclerviewadapter.SampleAdapter;
import com.crazysunj.multityperecyclerviewadapter.sticky.SecondStickyItem;

/**
 * Created by sunjian on 2017/3/28.
 */

public class HeaderSecondItem implements SecondStickyItem {

    private String name;
    private long id;
    private String stickyName = "第二条粘性";

    public HeaderSecondItem(String name, long id) {
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
        return 2;
    }

    @Override
    public int getItemType() {
        return SampleAdapter.DEFAULT_HEADER_TYPE;
    }

    @Override
    public String getStickyName() {
        return stickyName;
    }
}
