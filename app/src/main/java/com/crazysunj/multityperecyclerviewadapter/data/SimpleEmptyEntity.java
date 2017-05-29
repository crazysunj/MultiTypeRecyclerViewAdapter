package com.crazysunj.multityperecyclerviewadapter.data;

import com.crazysunj.multitypeadapter.entity.EmptyEntity;

/**
 * description
 * <p>
 * Created by sunjian on 2017/5/16.
 */

public class SimpleEmptyEntity extends EmptyEntity {

    private String title;

    public SimpleEmptyEntity(String title, int type) {
        super(System.currentTimeMillis(), type);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

}
