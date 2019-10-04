package com.crazysunj.multityperecyclerviewadapter.data;

import androidx.annotation.Nullable;

import com.crazysunj.multityperecyclerviewadapter.R;
import com.crazysunj.multityperecyclerviewadapter.helper.SimpleHelper;
import com.crazysunj.multityperecyclerviewadapter.sticky.FourthStickyItem;

import java.util.UUID;

/**
 * Created by sunjian on 2017/3/28.
 */

public class FourthItem implements FourthStickyItem {

    private String name;

    private int img = R.mipmap.ic_launcher;

    private long id;
    private String stickyName = "第四条粘性";

    public FourthItem(String name) {
        this.name = name;
        id = UUID.nameUUIDFromBytes((name + stickyName).getBytes()).hashCode();
    }

    public FourthItem(String name, long id) {
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
        if (!(obj instanceof FourthItem)) {
            return false;
        }
        return id == ((FourthItem) obj).id;
    }

    @Override
    public long getHeaderId() {
        return 4;
    }

    @Override
    public int getItemType() {
        return SimpleHelper.TYPE_TWO;
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
