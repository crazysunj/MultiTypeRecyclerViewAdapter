package com.crazysunj.multityperecyclerviewadapter.testlevel;

import androidx.annotation.Nullable;

/**
 * author: sunjian
 * created on: 2018/4/3 上午11:11
 * description:
 */

public class TypeTwoItem implements MultiTypeTitleEntity {

    public static final int TYPE_TWO = 2;
    private long id;
    private String msg;

    public TypeTwoItem(long id, String msg) {
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
        return TYPE_TWO;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof TypeTwoItem)) {
            return false;
        }
        if (id != ((TypeTwoItem) obj).id) {
            return false;
        }
        if (msg == null) {
            return false;
        }
        return msg.equals(((TypeTwoItem) obj).msg);
    }
}
