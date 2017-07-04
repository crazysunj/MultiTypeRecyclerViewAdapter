package com.crazysunj.multityperecyclerviewadapter.data;

/**
 * description
 * <p>
 * Created by sunjian on 2017/5/16.
 */

public class MyErrorEntity extends SimpleErrorEntity {

    private String title;
    private String message;

    public MyErrorEntity(int type, String title, String message) {
        super(type);
        this.title = title;
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }
}
