package com.crazysunj.multitypeadapter.entity;

import java.util.List;

/**
 *
 * Created by sunjian on 2017/3/31.
 */

public final class HandleBase<T> {
    private List<T> newData;
    private T newHeader;
    private int refreshType;
    private int type;

    public HandleBase(List<T> newData, T newHeader, int type, int refreshType) {
        this.newData = newData;
        this.newHeader = newHeader;
        this.type = type;
        this.refreshType = refreshType;
    }

    public int getType() {
        return type;
    }

    public List<T> getNewData() {
        return newData;
    }

    public T getNewHeader() {
        return newHeader;
    }

    public int getRefreshType() {
        return refreshType;
    }
}
