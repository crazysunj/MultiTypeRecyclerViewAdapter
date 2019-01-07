package com.crazysunj.multityperecyclerviewadapter.testlevel;

/**
 * author: sunjian
 * created on: 2018/4/3 上午11:11
 * description:
 */

public class TypeEmptyAllItem implements MultiTypeTitleEntity {

    public static final int TYPE_EMPTY = 6;
    private String msg;

    public TypeEmptyAllItem( String msg) {
        this.msg = msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public int getItemType() {
        return TYPE_EMPTY;
    }

    @Override
    public long getId() {
        return 0;
    }
}
