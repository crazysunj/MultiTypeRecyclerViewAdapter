package com.crazysunj.multityperecyclerviewadapter.data;

/**
 * description
 * <p>
 * Created by sunjian on 2017/5/16.
 */

public class MyEmptyEntity extends SimpleEmptyEntity {

    private String title;

    public MyEmptyEntity(int type, String title) {

        super(type);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

}
