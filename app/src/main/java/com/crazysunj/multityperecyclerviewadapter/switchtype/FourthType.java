package com.crazysunj.multityperecyclerviewadapter.switchtype;

import com.crazysunj.multitypeadapter.entity.MultiTypeEntity;

import static com.crazysunj.multityperecyclerviewadapter.switchtype.ThirdType.TYPE_3;


/**
 * @author: sunjian
 * created on: 2017/10/19 上午11:16
 * description:
 */

public class FourthType implements MultiTypeEntity {

    public static final int TYPE_4 = 3;

    private long id;
    private String title;

    public FourthType(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int getItemType() {
        return TYPE_4;
    }

    @Override
    public long getId() {
        return id;
    }
}
