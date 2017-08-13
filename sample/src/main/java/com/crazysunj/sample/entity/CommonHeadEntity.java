package com.crazysunj.sample.entity;

import com.crazysunj.multitypeadapter.helper.RecyclerViewAdapterHelper;
import com.crazysunj.sample.base.MutiTypeTitleEntity;

/**
 * author: sunjian
 * created on: 2017/8/6 下午5:08
 * description:
 */

public class CommonHeadEntity implements MutiTypeTitleEntity {

    private String title;
    private int type;
    private long id;

    public CommonHeadEntity(String title, int type) {
        this.title = title;
        this.type = type - RecyclerViewAdapterHelper.HEADER_TYPE_DIFFER;
        this.id = System.currentTimeMillis();
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public int getItemType() {
        return type;
    }

    @Override
    public long getId() {
        return id;
    }
}
