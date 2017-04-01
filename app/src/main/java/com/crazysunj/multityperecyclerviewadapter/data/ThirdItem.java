package com.crazysunj.multityperecyclerviewadapter.data;

import com.crazysunj.multityperecyclerviewadapter.R;
import com.crazysunj.multityperecyclerviewadapter.SampleAdapter;
import com.crazysunj.multityperecyclerviewadapter.sticky.ThirdStickyItem;

/**
 * Created by sunjian on 2017/3/28.
 */

public class ThirdItem implements ThirdStickyItem {

    private String name;
    private long id;
    private int img = R.mipmap.ic_launcher;
    private String stickyName = "第三条粘性";

    public ThirdItem(String name, long id) {
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
        return id;
    }

    @Override
    public long getHeaderId() {
        return 3;
    }

    @Override
    public int getItemType() {
        return SampleAdapter.TYPE_FOUR;
    }

    @Override
    public String getStickyName() {
        return stickyName;
    }
}
