package com.crazysunj.multityperecyclerviewadapter.testlevel;

import java.util.UUID;

/**
 * author: sunjian
 * created on: 2018/4/3 上午11:11
 * description:
 */

public class LevelLoadingItem implements MultiTypeTitleEntity {

    private long id;
    private int type;

    public LevelLoadingItem(int type) {
        this.id = UUID.nameUUIDFromBytes(String.valueOf(type).getBytes()).hashCode();
        this.type = type;
    }
    
    @Override
    public int getItemType() {
        return type;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getMsg() {
        return null;
    }
}
