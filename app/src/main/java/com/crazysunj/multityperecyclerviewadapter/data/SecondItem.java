package com.crazysunj.multityperecyclerviewadapter.data;

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
