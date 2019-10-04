package com.crazysunj.multityperecyclerviewadapter.data;

import androidx.annotation.Nullable;

import java.util.UUID;

/**
 * description
 * <p>
 * Created by sunjian on 2017/5/16.
 */

public class ErrorAndEmptyErrorEntity extends NoStickyEntity {

    private long id;
    private int type;

    public ErrorAndEmptyErrorEntity(int type) {
        this.id = UUID.nameUUIDFromBytes(("error_" + type).getBytes()).hashCode();
        this.type = type;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int getItemType() {
        return type;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof ErrorAndEmptyErrorEntity)) {
            return false;
        }
        return id == ((ErrorAndEmptyErrorEntity) obj).id;
    }

}
