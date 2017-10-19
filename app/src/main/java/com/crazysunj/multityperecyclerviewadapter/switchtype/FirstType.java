package com.crazysunj.multityperecyclerviewadapter.switchtype;

import com.crazysunj.multitypeadapter.entity.MultiTypeEntity;

/**
 * @author: sunjian
 * created on: 2017/10/19 上午11:16
 * description:
 */

public class FirstType implements MultiTypeEntity {

    public static final int TYPE_1 = 0;

    private long id;
    private String title;

    public FirstType(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int getItemType() {
        return TYPE_1;
    }

    @Override
    public long getId() {
        return id;
    }
}
