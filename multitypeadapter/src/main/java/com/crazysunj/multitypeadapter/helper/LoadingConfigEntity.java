package com.crazysunj.multitypeadapter.helper;

/**
 * description
 * <p>loading的全局刷新配置实体
 * Created by sunjian on 2017/6/9.
 */
class LoadingConfigEntity {

    int count;
    boolean isHaveHeader;
    boolean isSupportSticky;

    LoadingConfigEntity(boolean isHaveHeader) {
        this(false, 0, isHaveHeader);
    }

    LoadingConfigEntity(boolean isSupportSticky, boolean isHaveHeader) {
        this(isSupportSticky, 0, isHaveHeader);
    }

    LoadingConfigEntity(int count) {
        this(false, count, false);
    }

    LoadingConfigEntity(boolean isSupportSticky, int count) {
        this(isSupportSticky, count, false);
    }

    LoadingConfigEntity(int count, boolean isHaveHeader) {
        this(false, count, isHaveHeader);
    }

    LoadingConfigEntity(boolean isSupportSticky, int count, boolean isHaveHeader) {
        this.isSupportSticky = isSupportSticky;
        this.count = count;
        this.isHaveHeader = isHaveHeader;
    }
}
