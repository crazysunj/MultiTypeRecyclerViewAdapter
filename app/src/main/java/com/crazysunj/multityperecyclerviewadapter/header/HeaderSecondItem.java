package com.crazysunj.multityperecyclerviewadapter.header;

import androidx.annotation.Nullable;

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
        id = UUID.nameUUIDFromBytes((name + stickyName).getBytes()).hashCode();
    }

    public HeaderSecondItem(String name, long id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof HeaderSecondItem)) {
            return false;
        }
        return id == ((HeaderSecondItem) obj).id;
    }

    @Override
    public long getHeaderId() {
        return 2;
    }

    @Override
    public int getItemType() {
        return SimpleHelper.LEVEL_SENCOND - 1000;
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
