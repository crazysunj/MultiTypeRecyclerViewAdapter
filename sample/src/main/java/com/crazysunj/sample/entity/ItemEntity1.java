package com.crazysunj.sample.entity;

import android.text.TextUtils;
import android.text.format.DateFormat;

import com.crazysunj.sample.R;
import com.crazysunj.sample.base.MutiTypeTitleEntity;
import com.crazysunj.sample.util.MD5Util;

import java.util.Calendar;
import java.util.Random;

/**
 * author: sunjian
 * created on: 2017/8/3 下午5:40
 * description:
 */

public class ItemEntity1 implements MutiTypeTitleEntity {

    private String title;
    private String content;
    private int img;
    private String time;
    private String timeFlag;
    private long id;
    public static final int TYPE_1 = 0;
    public static final String HEADER_TITLE = "今日提醒";

    public ItemEntity1(String title, String content, int flag) {
        this.title = title;
        this.content = content;
        this.img = getImg(flag);
        long currentTimeMillis = System.currentTimeMillis();
        long timeMillis = currentTimeMillis - new Random().nextInt(1000 * 60 * 60 * 24 * 5);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMillis);
        int time_flag = calendar.get(Calendar.AM_PM);
        this.time = (String) DateFormat.format("HH:mm", calendar);
        this.timeFlag = time_flag == Calendar.AM ? "AM" : "PM";
        this.id = TextUtils.isEmpty(title) ? currentTimeMillis : MD5Util.stringToMD5(title).hashCode();
    }

    public String getTimeFlag() {
        return timeFlag;
    }

    public String getContent() {
        return content;
    }

    public int getImg() {
        return img;
    }

    public String getTime() {
        return time;
    }

    private int getImg(int flag) {
        if (flag == 0) {
            return R.drawable.shape_circle_5c93f7;
        } else if (flag == 1) {
            return R.drawable.shape_circle_f9ba43;
        } else {
            return R.drawable.shape_circle_f7657d;
        }
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public int getItemType() {
        return TYPE_1;
    }

    @Override
    public long getId() {
        return id;
    }
}
