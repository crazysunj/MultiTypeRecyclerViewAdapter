package com.crazysunj.multityperecyclerviewadapter.testlevel;

/**
 * author: sunjian
 * created on: 2018/4/3 上午11:11
 * description:
 */

public class TypeFiveItem implements MultiTypeTitleEntity {

    public static final int TYPE_FIVE = 5;
    private long id;
    private String msg;

    public TypeFiveItem(long id, String msg) {
        this.id = id;
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
        return TYPE_FIVE;
    }

    @Override
    public long getId() {
        return id;
    }
}
