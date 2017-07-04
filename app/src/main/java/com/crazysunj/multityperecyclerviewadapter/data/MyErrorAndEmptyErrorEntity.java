package com.crazysunj.multityperecyclerviewadapter.data;

/**
 * description
 * <p>
 * Created by sunjian on 2017/5/16.
 */

public class MyErrorAndEmptyErrorEntity extends ErrorAndEmptyErrorEntity {

    private String title;
    private String message;

    public MyErrorAndEmptyErrorEntity(int type, String title, String message) {
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
