/*
  Copyright 2017 Sun Jian
  <p>
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  <p>
  http://www.apache.org/licenses/LICENSE-2.0
  <p>
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.crazysunj.multitypeadapter.helper;

import android.util.SparseArray;

import androidx.annotation.IntRange;

/**
 * @author: sunjian
 * created on: 2017/6/9
 * description: 全局loading配置项
 * 如果没有用到这个，可以采用
 * {@link RecyclerViewAdapterHelper#notifyLoadingHeaderChanged(int)}
 * {@link RecyclerViewAdapterHelper#notifyLoadingDataAndHeaderChanged(int, int)}
 * {@link RecyclerViewAdapterHelper#notifyLoadingDataChanged(int, int)}
 */
public class LoadingConfig {

    private SparseArray<LoadingConfigEntity> mConfigs;

    public LoadingConfig() {
        if (mConfigs == null) {
            mConfigs = new SparseArray<>();
        }
    }

    private LoadingConfig(SparseArray<LoadingConfigEntity> configs) {
        mConfigs = configs;
    }

    public void setLoading(int level, @IntRange(from = 1) int count, boolean isHaveHeader) {
        mConfigs.put(level, new LoadingConfigEntity(count, isHaveHeader));
    }

    public void setLoading(int level, @IntRange(from = 1) int count) {
        mConfigs.put(level, new LoadingConfigEntity(count));
    }

    public void setLoading(int level, boolean isHaveHeader) {
        mConfigs.put(level, new LoadingConfigEntity(isHaveHeader));
    }

    public static class Builder {

        private SparseArray<LoadingConfigEntity> mBuilderConfigs;

        public Builder() {
            if (mBuilderConfigs == null) {
                mBuilderConfigs = new SparseArray<>();
            }
        }

        public Builder setLoading(int level, @IntRange(from = 1) int count, boolean isHaveHeader) {
            mBuilderConfigs.put(level, new LoadingConfigEntity(count, isHaveHeader));
            return this;
        }

        public Builder setLoading(int level, @IntRange(from = 1) int count) {
            mBuilderConfigs.put(level, new LoadingConfigEntity(count));
            return this;
        }

        public Builder setLoading(int level, boolean isHaveHeader) {
            mBuilderConfigs.put(level, new LoadingConfigEntity(isHaveHeader));
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
