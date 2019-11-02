package com.crazysunj.multityperecyclerviewadapter.component;

import com.crazysunj.multitypeadapter.entity.MultiTypeEntity;

/**
 * @author sunjian
 * @date 2019-10-18 19:15
 */
public class TypeThree implements MultiTypeEntity {

    public static final int TYPE = 2;

    @Override
    public int getItemType() {
        return TYPE;
    }
}
