package com.crazysunj.sample.entity;

import android.text.TextUtils;

import com.crazysunj.sample.base.MutiTypeTitleEntity;
import com.crazysunj.sample.util.MD5Util;

import java.util.ArrayList;
import java.util.List;

/**
 * author: sunjian
 * created on: 2017/8/8 上午9:42
 * description:
 */

public class ItemEntity2 implements MutiTypeTitleEntity {

    private int img;
    private String title;
    private String content;
    private String price;
    private List<String> tags;

    public static final int TYPE_2 = 1;
    public static final String HEADER_TITLE = "订餐就餐";
    private long id;


    public ItemEntity2(int img, String title, String content, String price) {
        this.img = img;
        this.title = title;
        this.content = content;
        this.price = price;
        List<String> tags = new ArrayList<String>();
        tags.add("低脂肪");
        tags.add("低胆固醇");
        tags.add("低盐");
        this.tags = tags;
        this.id = TextUtils.isEmpty(title) ? System.currentTimeMillis() : MD5Util.stringToMD5(title).hashCode();
    }

    public int getImg() {
        return img;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getPrice() {
        return price;
    }

    public List<String> getTags() {
        return tags;
    }

    @Override
    public int getItemType() {
        return TYPE_2;
    }

    @Override
    public long getId() {
        return id;
    }
}
