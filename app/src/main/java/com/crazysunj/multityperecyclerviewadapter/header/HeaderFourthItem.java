package com.crazysunj.multityperecyclerviewadapter.header;

import com.crazysunj.multityperecyclerviewadapter.helper.SimpleHelper;
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
        return SimpleHelper.TYPE_FOUR - 1000;
    }

    @Override
    public String getStickyName() {
        return stickyName;
    }
}
