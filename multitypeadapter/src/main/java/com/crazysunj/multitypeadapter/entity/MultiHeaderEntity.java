package com.crazysunj.multitypeadapter.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * 多类型实体类接口,id标志不同数据项(包括头,唯一标识),headerId标志粘性头部的类型(拥有相同headerId的数据项公用同一个粘性头部)
 * Created by sunjian on 2017/3/28.
 */

public interface MultiHeaderEntity extends MultiItemEntity {

    long getId();

    long getHeaderId();

    void setId(long id);

    void setType(int type);
}
