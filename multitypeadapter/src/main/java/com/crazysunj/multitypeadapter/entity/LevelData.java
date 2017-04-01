package com.crazysunj.multitypeadapter.entity;

import java.util.List;

/**
 * 一开始的时候就这样设计了，懒得改了 - -！,分别获取个人感觉更人性化
 * Created by sunjian on 2017/3/27.
 */

public class LevelData<T> {

    private List<T> data;
    private T header;

    public LevelData(List<T> data, T header) {
        this.data = data;
        this.header = header;
    }

    public List<T> getData() {
        return data;
    }

    public T getHeader() {
        return header;
    }
}
