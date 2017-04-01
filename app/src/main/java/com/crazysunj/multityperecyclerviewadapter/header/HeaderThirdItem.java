package com.crazysunj.multityperecyclerviewadapter.header;

import com.crazysunj.multityperecyclerviewadapter.SampleAdapter;
import com.crazysunj.multityperecyclerviewadapter.sticky.ThirdStickyItem;

/**
 * Created by sunjian on 2017/3/28.
 */

public class HeaderThirdItem implements ThirdStickyItem {

    private String name;
    private long id;
    private String stickyName = "第三条粘性";


    public HeaderThirdItem(String name, long id) {
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
        return 3;
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
