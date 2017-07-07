/**
 * Copyright 2017 Sun Jian
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.crazysunj.multitypeadapter.helper;

import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.util.SparseArray;
import android.util.SparseIntArray;

import static com.crazysunj.multitypeadapter.helper.RecyclerViewAdapterHelper.DEFAULT_HEADER_LEVEL;

/**
 * description
 * <p>
 * 资源管理
 * Created by sunjian on 2017/5/15.
 */

final class ResourcesManager {

    //根据type存储layoutId
    private SparseIntArray mLayouts;
    //根据type存储等级
    private SparseIntArray mLevels;
    //根据level存储属性
    private SparseArray<AttrsEntity> mAttrs;

    private TypesManager mTypeManager;
    private LoadingsManager mLoadingManager;
    private ErrorsManager mErrorManager;
    private EmptysManager mEmptysManager;

    ResourcesManager() {

        if (mLevels == null) {
            mLevels = new SparseIntArray();
        }

        if (mLayouts == null) {
            mLayouts = new SparseIntArray();
        }

        if (mAttrs == null) {
            mAttrs = new SparseArray<AttrsEntity>();
        }
    }

    TypesManager type(@IntRange(from = 0, to = 999) int type) {
        return mTypeManager = new TypesManager(type);
    }

    private LoadingsManager loading(@IntRange(from = 0, to = 999) int type) {
        return mLoadingManager = new LoadingsManager(type);
    }

    private ErrorsManager error(@IntRange(from = 0, to = 999) int type) {
        return mErrorManager = new ErrorsManager(type);
    }

    private EmptysManager empty(@IntRange(from = 0, to = 999) int type) {
        return mEmptysManager = new EmptysManager(type);
    }

    private void register() {

        int type = mTypeManager.type;

        int level = mTypeManager.level;
        if (level < 0) {
            throw new RuntimeException("are you sure register the level ?");
        }

        int layoutResId = mTypeManager.layoutResId;
        if (layoutResId == 0) {
            throw new RuntimeException("are you sure register the layoutResId ?");
        }

        mLevels.put(type, level);
        mLayouts.put(type, layoutResId);
        mAttrs.put(level, new AttrsEntity(mTypeManager.minSize, mTypeManager.isFolded));

        int headerResId = mTypeManager.headerResId;
        if (headerResId != 0) {
            int headerType = type - RecyclerViewAdapterHelper.HEADER_TYPE_DIFFER;
            mLevels.put(headerType, DEFAULT_HEADER_LEVEL);
            mLayouts.put(headerType, headerResId);
        }

        int footerResId = mTypeManager.footerResId;
        if (footerResId != 0) {
            int footerType = type - RecyclerViewAdapterHelper.FOOTER_TYPE_DIFFER;
            mLevels.put(footerType, DEFAULT_HEADER_LEVEL);
            mLayouts.put(footerType, footerResId);
        }

        if (mLoadingManager != null) {
            int loadingLayoutResId = mLoadingManager.loadingLayoutResId;
            if (loadingLayoutResId != 0) {
                int loadingLayoutType = type - RecyclerViewAdapterHelper.LOADING_DATA_TYPE_DIFFER;
                mLevels.put(loadingLayoutType, level);
                mLayouts.put(loadingLayoutType, loadingLayoutResId);
            }

            int loadingHeaderResId = mLoadingManager.loadingHeaderResId;
            if (loadingHeaderResId != 0) {
                int loadingHeaderType = type - RecyclerViewAdapterHelper.LOADING_HEADER_TYPE_DIFFER;
                mLevels.put(loadingHeaderType, DEFAULT_HEADER_LEVEL);
                mLayouts.put(loadingHeaderType, loadingHeaderResId);
            }
        }

        if (mErrorManager != null) {
            int errorLayoutResId = mErrorManager.errorLayoutResId;
            if (errorLayoutResId != 0) {
                int errorType = type - RecyclerViewAdapterHelper.ERROR_TYPE_DIFFER;
                mLevels.put(errorType, level);
                mLayouts.put(errorType, errorLayoutResId);
            }
        }

        if (mEmptysManager != null) {
            int emptyLayoutResId = mEmptysManager.emptyLayoutResId;
            if (emptyLayoutResId != 0) {
                int emptyType = type - RecyclerViewAdapterHelper.EMPTY_TYPE_DIFFER;
                mLevels.put(emptyType, level);
                mLayouts.put(emptyType, emptyLayoutResId);
            }
        }

    }

    void putLayoutId(int type, int layoutId) {
        mLayouts.put(type, layoutId);
    }

    int getLayoutId(int type) {
        return mLayouts.get(type);
    }

    void putLevel(int type, int level) {
        mLevels.put(type, level);
    }

    int getLevel(int type) {
        return mLevels.get(type, DEFAULT_HEADER_LEVEL);
    }

    AttrsEntity getAttrsEntity(int level) {
        return mAttrs.get(level);
    }

    /**
     * Type管理
     */
    public class TypesManager {

        private int type;
        private int level = -1;
        private int layoutResId;
        private int headerResId;
        private int footerResId;
        private int minSize = 3;
        private boolean isFolded = false;

        TypesManager(int type) {
            this.type = type;
        }

        public TypesManager level(@IntRange(from = 0) int level) {
            this.level = level;
            return this;
        }

        public TypesManager layoutResId(@LayoutRes int layoutResId) {
            this.layoutResId = layoutResId;
            return this;
        }

        public TypesManager headerResId(@LayoutRes int headerResId) {
            this.headerResId = headerResId;
            return this;
        }

        public TypesManager footerResId(@LayoutRes int footerResId) {
            this.footerResId = footerResId;
            return this;
        }

        public TypesManager minSize(int minSize) {
            this.minSize = minSize;
            return this;
        }

        public TypesManager isFolded(boolean isFolded) {
            this.isFolded = isFolded;
            return this;
        }

        public LoadingsManager loading() {
            return ResourcesManager.this.loading(type);
        }

        public ErrorsManager error() {
            return ResourcesManager.this.error(type);
        }

        public EmptysManager empty() {
            return ResourcesManager.this.empty(type);
        }

        public void register() {
            ResourcesManager.this.register();
        }

    }

    /**
     * loading管理
     */
    public class LoadingsManager {

        private int type;
        private int loadingLayoutResId;
        private int loadingHeaderResId;

        LoadingsManager(int type) {
            this.type = type;
        }

        public LoadingsManager loadingLayoutResId(@LayoutRes int loadingLayoutResId) {
            this.loadingLayoutResId = loadingLayoutResId;
            return this;
        }

        public LoadingsManager loadingHeaderResId(@LayoutRes int loadingHeaderResId) {
            this.loadingHeaderResId = loadingHeaderResId;
            return this;
        }

        public ErrorsManager error() {
            return ResourcesManager.this.error(type);
        }

        public EmptysManager empty() {
            return ResourcesManager.this.empty(type);
        }

        public void register() {
            ResourcesManager.this.register();
        }

    }

    /**
     * error管理
     */
    public class ErrorsManager {

        private int type;
        private int errorLayoutResId;

        ErrorsManager(int type) {
            this.type = type;
        }

        public ErrorsManager errorLayoutResId(@LayoutRes int errorLayoutResId) {
            this.errorLayoutResId = errorLayoutResId;
            return this;
        }

        public EmptysManager empty() {
            return ResourcesManager.this.empty(type);
        }

        public void register() {
            ResourcesManager.this.register();
        }
    }

    /**
     * empty管理
     */
    public class EmptysManager {

        private int type;
        private int emptyLayoutResId;

        EmptysManager(int type) {
            this.type = type;
        }

        public EmptysManager emptyLayoutResId(@LayoutRes int emptyLayoutResId) {
            this.emptyLayoutResId = emptyLayoutResId;
            return this;
        }

        public void register() {
            ResourcesManager.this.register();
        }
    }

    static class AttrsEntity {
        int minSize;
        boolean isFolded;

        public AttrsEntity(int minSize, boolean isFolded) {
            this.minSize = minSize;
            this.isFolded = isFolded;
        }
    }
}
