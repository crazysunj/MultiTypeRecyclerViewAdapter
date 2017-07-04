package com.crazysunj.multityperecyclerviewadapter.data;

/**
 * description
 * <p>
 * Created by sunjian on 2017/5/16.
 */

public class MyErrorAndEmptyEmptyEntity extends ErrorAndEmptyEmptyEntity {

    private String title;

    public MyErrorAndEmptyEmptyEntity(int type, String title) {

        super(type);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

}
