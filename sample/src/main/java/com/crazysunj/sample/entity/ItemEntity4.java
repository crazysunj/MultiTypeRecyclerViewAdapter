package com.crazysunj.sample.entity;

import android.text.TextUtils;

import com.crazysunj.sample.base.MutiTypeTitleEntity;
import com.crazysunj.sample.util.MD5Util;

/**
 * author: sunjian
 * created on: 2017/8/11 下午4:53
 * description:
 */

public class ItemEntity4 implements MutiTypeTitleEntity {
    public static final int TYPE_4 = 3;
    public static final String HEADER_TITLE = "名医推荐";

    private long id;

    private int img;
    private String title;
    private String name;
    private String des;
    private String tag;
    private String people;
    private String price;

    public ItemEntity4(int img, String title, String name, String des, String tag, String people, String price) {
        this.id = TextUtils.isEmpty(title) ? System.currentTimeMillis() : MD5Util.stringToMD5(title).hashCode();
        this.img = img;
        this.title = title;
        this.name = name;
        this.des = des;
        this.tag = tag;
        this.people = people;
        this.price = price;
    }

    public int getImg() {
        return img;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public String getName() {
        return name;
    }

    public String getDes() {
        return des;
    }

    public String getTag() {
        return tag;
    }

    public String getPeople() {
        return people;
    }

    public String getPrice() {
        return price;
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
