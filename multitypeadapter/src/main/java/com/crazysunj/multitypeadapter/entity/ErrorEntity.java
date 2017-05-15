package com.crazysunj.multitypeadapter.entity;

import com.crazysunj.multitypeadapter.sticky.StickyHeaderDecoration;

/**
 * description
 * <p>
 * 错误实体类
 * 可根据业务自定义错误实体类
 * <p>
 * Created by sunjian on 2017/5/15.
 */
public final class ErrorEntity implements MultiHeaderEntity {

    private long id;
    private int type;

    public ErrorEntity(long id, int type) {
        this.id = id;
        this.type = type;
    }

    @Override
    public int getItemType() {
        return type - 4000;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public long getHeaderId() {
        return StickyHeaderDecoration.NO_HEADER_ID;
    }
}
