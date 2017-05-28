package com.crazysunj.multityperecyclerviewadapter.header;

import com.crazysunj.multityperecyclerviewadapter.helper.SimpleHelper;
import com.crazysunj.multityperecyclerviewadapter.sticky.SecondStickyItem;

import java.util.UUID;

/**
 * Created by sunjian on 2017/3/28.
 */

public class HeaderSecondItem implements SecondStickyItem {

    private String name;
    private long id;
    private String stickyName = "第二条粘性";

    public HeaderSecondItem(String name) {
        this.name = name;
    }

    public HeaderSecondItem(String name, long id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @Override
    public long getId() {
        if (id == 0) {
            return id = UUID.nameUUIDFromBytes((name + stickyName).getBytes()).hashCode();
        }
        return id;
    }

    @Override
    public long getHeaderId() {
        return 2;
    }

    @Override
    public int getItemType() {
        return SimpleHelper.TYPE_THREE - 1000;
    }

    @Override
    public String getStickyName() {
        return stickyName;
    }
}
