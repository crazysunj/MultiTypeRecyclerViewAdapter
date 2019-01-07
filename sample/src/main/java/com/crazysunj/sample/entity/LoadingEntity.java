package com.crazysunj.sample.entity;

import com.crazysunj.sample.base.MutiTypeTitleEntity;

/**
 * author: sunjian
 * created on: 2017/8/13 下午6:13
 * description:
 */

public class LoadingEntity implements MutiTypeTitleEntity {

    private int type;
    private long id;
    private String msg;

    public LoadingEntity(int type, long id) {
        this.type = type;
        this.id = id;
        this.msg = String.valueOf(type) + String.valueOf(id);
    }

    @Override
    public String getTitle() {
        return msg;
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
