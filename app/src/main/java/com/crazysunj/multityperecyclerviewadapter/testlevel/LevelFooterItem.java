package com.crazysunj.multityperecyclerviewadapter.testlevel;

import androidx.annotation.Nullable;

import java.util.UUID;

/**
 * author: sunjian
 * created on: 2018/4/3 上午11:11
 * description:
 */

public class LevelFooterItem implements MultiTypeTitleEntity {

    private long id;
    private int type;
    private String msg;

    public LevelFooterItem(int type, String msg) {
        this.id = UUID.nameUUIDFromBytes(msg.getBytes()).hashCode();
        this.type = type;
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
        return type;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof LevelFooterItem)) {
            return false;
        }
        if (msg == null) {
            return false;
        }
        return msg.equals(((LevelFooterItem) obj).msg);
    }
}
