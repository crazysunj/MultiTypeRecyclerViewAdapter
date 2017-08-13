package com.crazysunj.sample.entity;

import com.crazysunj.sample.base.MutiTypeTitleEntity;
import com.crazysunj.sample.constant.Constants;

/**
 * author: sunjian
 * created on: 2017/8/13 下午6:13
 * description:
 */

public class LoadingEntity implements MutiTypeTitleEntity {

    private int type;
    private long id;

    public LoadingEntity(int type, long id) {
        this.type = type;
        this.id = id;
    }

    @Override
    public String getTitle() {
        return Constants.EMPTY;
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
