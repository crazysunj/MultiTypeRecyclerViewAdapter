package com.crazysunj.multityperecyclerviewadapter.data;

import com.crazysunj.multityperecyclerviewadapter.R;
import com.crazysunj.multityperecyclerviewadapter.SampleAdapter;
import com.crazysunj.multityperecyclerviewadapter.sticky.FourthStickyItem;

/**
 * Created by sunjian on 2017/3/28.
 */

public class FourthItem implements FourthStickyItem {

    private String name;

    private int img = R.mipmap.ic_launcher;

    private long id;
    private String stickyName = "第四条粘性";

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
    public long getId() {
        return id;
    }

    @Override
    public long getHeaderId() {
        return 4;
    }

    @Override
    public int getItemType() {
        return SampleAdapter.TYPE_TWO;
    }

    @Override
    public String getStickyName() {
        return stickyName;
    }
}
