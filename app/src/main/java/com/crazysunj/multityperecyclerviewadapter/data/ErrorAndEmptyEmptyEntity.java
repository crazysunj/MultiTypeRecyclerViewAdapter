package com.crazysunj.multityperecyclerviewadapter.data;

import androidx.annotation.Nullable;

import java.util.UUID;

/**
 * description
 * <p>
 * Created by sunjian on 2017/5/16.
 */

public class ErrorAndEmptyEmptyEntity extends NoStickyEntity {

    private long id;

    private int type;

    public ErrorAndEmptyEmptyEntity(int type) {
        this.id = UUID.nameUUIDFromBytes(("empty_" + type).getBytes()).hashCode();
        this.type = type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public int getItemType() {
        return type;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof ErrorAndEmptyEmptyEntity)) {
            return false;
        }
        return id == ((ErrorAndEmptyEmptyEntity) obj).id;
    }
}
