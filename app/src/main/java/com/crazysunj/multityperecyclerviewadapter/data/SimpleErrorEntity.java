package com.crazysunj.multityperecyclerviewadapter.data;

import com.crazysunj.multitypeadapter.entity.ErrorEntity;

/**
 * description
 * <p>
 * Created by sunjian on 2017/5/16.
 */

public class SimpleErrorEntity extends ErrorEntity {

    private String title;
    private String message;

    public SimpleErrorEntity(String title, String message, long id, int type) {
        super(id, type);
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
