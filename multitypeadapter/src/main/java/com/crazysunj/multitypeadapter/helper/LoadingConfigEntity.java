package com.crazysunj.multitypeadapter.helper;

/**
 * description
 * <p>loading的全局刷新配置实体
 * Created by sunjian on 2017/6/9.
 */
class LoadingConfigEntity {

    int count;
    boolean isHaveHeader;

    LoadingConfigEntity(boolean isHaveHeader) {
        this(0, isHaveHeader);
    }

    LoadingConfigEntity(int count) {
        this(count, false);
    }

    LoadingConfigEntity(int count, boolean isHaveHeader) {
        this.count = count;
        this.isHaveHeader = isHaveHeader;
    }
}
