package com.crazysunj.multityperecyclerviewadapter.data;

import com.crazysunj.multityperecyclerviewadapter.helper.DefaultMultiHeaderEntity;

import java.util.UUID;

/**
 * description
 * <p>
 * Created by sunjian on 2017/7/3.
 */

public class LoadingEntity extends DefaultMultiHeaderEntity {

    private long id;

    private int type;

    public LoadingEntity(int type) {
        this.id = UUID.nameUUIDFromBytes(("loading_" + type).getBytes()).hashCode();
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

}
