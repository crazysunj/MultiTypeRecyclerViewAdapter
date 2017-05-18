package com.crazysunj.multitypeadapter.entity;

import com.crazysunj.multitypeadapter.helper.RecyclerViewAdapterHelper;
import com.crazysunj.multitypeadapter.sticky.StickyHeaderDecoration;

/**
 * description
 * <p>
 * 错误实体类
 * 可根据业务自定义错误实体类
 * <p>
 * Created by sunjian on 2017/5/15.
 */
public class ErrorEntity implements MultiHeaderEntity {

    protected long id;
    protected int type;

    public ErrorEntity(long id, int type) {
        this.id = id;
        this.type = type;
    }

    public final int getType() {
        return type;
    }

    @Override
    public final int getItemType() {
        return type - RecyclerViewAdapterHelper.ERROR_TYPE_DIFFER;
    }

    @Override
    public final long getId() {
        return id;
    }

    @Override
    public long getHeaderId() {
        return StickyHeaderDecoration.NO_HEADER_ID;
    }
}
