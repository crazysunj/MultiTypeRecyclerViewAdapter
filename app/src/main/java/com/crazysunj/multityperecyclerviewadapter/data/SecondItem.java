package com.crazysunj.multityperecyclerviewadapter.data;

import androidx.annotation.Nullable;

import com.crazysunj.multityperecyclerviewadapter.R;
import com.crazysunj.multityperecyclerviewadapter.helper.SimpleHelper;
import com.crazysunj.multityperecyclerviewadapter.sticky.SecondStickyItem;

import java.util.UUID;

/**
 * Created by sunjian on 2017/3/28.
 */

public class SecondItem implements SecondStickyItem {

    private String name;
    private long id;
    private int img = R.mipmap.ic_launcher;

    private String stickyName = "第二条粘性";

    public SecondItem(String name) {
        this.name = name;
        id = UUID.nameUUIDFromBytes((name + stickyName).getBytes()).hashCode();
    }

    public SecondItem(String name, long id) {
        this.name = name;
        this.id = id;
    }

    public int getImg() {
        return img;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof SecondItem)) {
            return false;
        }
        return id == ((SecondItem) obj).id;
    }

    @Override
    public long getHeaderId() {
        return 2;
    }

    @Override
    public int getItemType() {
        return SimpleHelper.TYPE_THREE;
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
