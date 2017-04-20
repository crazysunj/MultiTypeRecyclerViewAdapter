package com.crazysunj.multityperecyclerviewadapter.data;

import com.crazysunj.multityperecyclerviewadapter.R;
import com.crazysunj.multityperecyclerviewadapter.SampleAdapter;
import com.crazysunj.multityperecyclerviewadapter.sticky.FirstStickyItem;

/**
 * Created by sunjian on 2017/3/28.
 */

public class FirstItem implements FirstStickyItem {

    private String name;

    private String stickyName = "第一条粘性";

    private int img = R.mipmap.ic_launcher;

    private long id;

    public FirstItem(String name, long id) {
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
        return 1;
    }

    @Override
    public void setId(long id) {
    }

    @Override
    public void setType(int type) {

    }

    @Override
    public int getItemType() {
        return SampleAdapter.TYPE_ONE;
    }

    @Override
    public String getStickyName() {
        return stickyName;
    }
}
