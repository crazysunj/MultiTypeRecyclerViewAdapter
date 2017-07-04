package com.crazysunj.multitypeadapter.helper;

import android.support.annotation.IntRange;
import android.util.SparseArray;

/**
 * description
 * <p>loading配置项
 * Created by sunjian on 2017/6/9.
 */

public class LoadingConfig {

    private SparseArray<LoadingConfigEntity> mConfigs;

    public LoadingConfig() {
        if (mConfigs == null) {
            mConfigs = new SparseArray<LoadingConfigEntity>();
        }
    }

    private LoadingConfig(SparseArray<LoadingConfigEntity> configs) {
        mConfigs = configs;
    }

    public void setLoading(int type, @IntRange(from = 1) int count, boolean isHaveHeader) {
        mConfigs.put(type, new LoadingConfigEntity(count, isHaveHeader));
    }

    public void setLoading(int type, @IntRange(from = 1) int count) {
        mConfigs.put(type, new LoadingConfigEntity(count));
    }

    public void setLoading(int type, boolean isHaveHeader) {
        mConfigs.put(type, new LoadingConfigEntity(isHaveHeader));
    }

    public static class Builder {

        private SparseArray<LoadingConfigEntity> mBuilderConfigs;

        public Builder() {
            if (mBuilderConfigs == null) {
                mBuilderConfigs = new SparseArray<LoadingConfigEntity>();
            }
        }

        public Builder setLoading(int type, @IntRange(from = 1) int count, boolean isHaveHeader) {
            mBuilderConfigs.put(type, new LoadingConfigEntity(count, isHaveHeader));
            return this;
        }

        public Builder setLoading(int type, @IntRange(from = 1) int count) {
            mBuilderConfigs.put(type, new LoadingConfigEntity(count));
            return this;
        }

        public Builder setLoading(int type, boolean isHaveHeader) {
            mBuilderConfigs.put(type, new LoadingConfigEntity(isHaveHeader));
            return this;
        }

        public LoadingConfig build() {

            return new LoadingConfig(mBuilderConfigs);
        }
    }

    SparseArray<LoadingConfigEntity> getConfigs() {
        return mConfigs;
    }
}
