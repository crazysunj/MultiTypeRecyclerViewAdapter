package com.crazysunj.multityperecyclerviewadapter.header;

import androidx.annotation.Nullable;

import com.crazysunj.multityperecyclerviewadapter.helper.SimpleHelper;
import com.crazysunj.multityperecyclerviewadapter.sticky.FirstStickyItem;

import java.util.UUID;

/**
 * Created by sunjian on 2017/3/28.
 */

public class HeaderFirstItem implements FirstStickyItem {

    private String name;
    private long id;
    private String stickyName = "第一条粘性";

    public HeaderFirstItem(String name) {
        this.name = name;
        id = UUID.nameUUIDFromBytes((name + stickyName).getBytes()).hashCode();
    }

    public HeaderFirstItem(String name, long id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof HeaderFirstItem)) {
            return false;
        }
        return id == ((HeaderFirstItem) obj).id;
    }

    @Override
    public long getHeaderId() {
        return 1;
    }

    @Override
    public int getItemType() {
        return SimpleHelper.LEVEL_FIRST - 1000;
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
