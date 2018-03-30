package com.crazysunj.multityperecyclerviewadapter.header;

import com.crazysunj.multityperecyclerviewadapter.helper.SimpleHelper;
import com.crazysunj.multityperecyclerviewadapter.sticky.ThirdStickyItem;

import java.util.UUID;

/**
 * Created by sunjian on 2017/3/28.
 */

public class HeaderThirdItem implements ThirdStickyItem {

    private String name;
    private long id;
    private String stickyName = "第三条粘性";

    public HeaderThirdItem(String name) {
        this.name = name;
    }

    public HeaderThirdItem(String name, long id) {
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
        return 3;
    }

    @Override
    public int getItemType() {
        return SimpleHelper.LEVEL_THIRD - 1000;
    }

    @Override
    public void setHeaderId(long headerId) {
        
    }

    @Override
    public void setStickyName(String stickyName) {

    }

    @Override
    public String getStickyName() {
        return stickyName;
    }
}
