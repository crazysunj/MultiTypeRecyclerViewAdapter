package com.crazysunj.multityperecyclerviewadapter.component;

import com.crazysunj.multitypeadapter.entity.MultiTypeEntity;

/**
 * @author sunjian
 * @date 2019-10-18 19:15
 */
public class TypeTwo implements MultiTypeEntity {

    public static final int TYPE = 1;

    @Override
    public int getItemType() {
        return TYPE;
    }
}
