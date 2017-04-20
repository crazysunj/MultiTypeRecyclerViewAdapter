package com.crazysunj.multityperecyclerviewadapter.data;

import com.crazysunj.multityperecyclerviewadapter.R;
import com.crazysunj.multityperecyclerviewadapter.SampleAdapter;
import com.crazysunj.multityperecyclerviewadapter.sticky.SecondStickyItem;

/**
 * Created by sunjian on 2017/3/28.
 */

public class SecondItem implements SecondStickyItem {

    private String name;
    private long id;
    private int img = R.mipmap.ic_launcher;

    private String stickyName = "第二条粘性";

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
        return id;
    }

    @Override
    public long getHeaderId() {
        return 2;
    }

    @Override
    public void setId(long id) {

    }

    @Override
    public void setType(int type) {

    }

    @Override
    public int getItemType() {
        return SampleAdapter.TYPE_THREE;
    }

    @Override
    public String getStickyName() {
        return stickyName;
    }
}
