package com.crazysunj.multityperecyclerviewadapter.switchtype;

import androidx.annotation.Nullable;

import com.crazysunj.multitypeadapter.entity.MultiTypeEntity;

/**
 * @author: sunjian
 * created on: 2017/10/19 上午11:16
 * description:
 */

public class SwtichType implements MultiTypeEntity {

    public static final int TYPE_A = 4;
    public static final int TYPE_B = 5;
    public static final int TYPE_C = 6;
    public static final int TYPE_D = 7;

    private int type;
    private String title;

    public SwtichType(int type, String title) {
        this.type = type;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int getItemType() {
        return type;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof SwtichType)) {
            return false;
        }
        return type == ((SwtichType) obj).type;
    }
}
