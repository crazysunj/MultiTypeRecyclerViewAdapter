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
import android.util.SparseIntArray;

import androidx.annotation.IntRange;
import androidx.annotation.LayoutRes;

import java.util.Locale;

import static com.crazysunj.multitypeadapter.helper.RecyclerViewAdapterHelper.DEFAULT_HEADER_LEVEL;

/**
 * @author: sunjian
 * created on: 2017/5/15
 * description: 资源管理
 */
public final class ResourcesManager {

    /**
     * 根据type存储layoutId
     */
    private SparseIntArray mLayoutMap;
    /**
     * 根据type存储等级
     */
    private SparseIntArray mLevelMap;
    /**
     * 根据level存储属性
     */
    private SparseArray<AttrEntity> mAttrMap;

    private SparseArray<LevelManager> mLevelManagerMap;
    private SparseArray<LoadingManager> mLoadingManagerMap;
    private SparseArray<ErrorManager> mErrorManagerMap;
    private SparseArray<EmptyManager> mEmptyManagerMap;

    ResourcesManager() {
        mLevelMap = new SparseIntArray();
        mLayoutMap = new SparseIntArray();
        mAttrMap = new SparseArray<>();
    }

    LevelManager level(int level) {
        if (mLevelManagerMap == null) {
            mLevelManagerMap = new SparseArray<>();
        }
        LevelManager levelManager = mLevelManagerMap.get(level);
        if (levelManager == null) {
            levelManager = new LevelManager(this, level);
            mLevelManagerMap.put(level, levelManager);
        }
        return levelManager;
    }

    private LoadingManager loading(int level) {
        if (mLoadingManagerMap == null) {
            mLoadingManagerMap = new SparseArray<>();
        }
        LoadingManager loadingManager = mLoadingManagerMap.get(level);
        if (loadingManager == null) {
            loadingManager = new LoadingManager(this, level);
            mLoadingManagerMap.put(level, loadingManager);
        }
        return loadingManager;
    }

    private ErrorManager error(int level) {
        if (mErrorManagerMap == null) {
            mErrorManagerMap = new SparseArray<>();
        }
        ErrorManager errorManager = mErrorManagerMap.get(level);
        if (errorManager == null) {
            errorManager = new ErrorManager(this, level);
            mErrorManagerMap.put(level, errorManager);
        }
        return errorManager;
    }

    private EmptyManager empty(int level) {
        if (mEmptyManagerMap == null) {
            mEmptyManagerMap = new SparseArray<>();
        }
        EmptyManager emptyManager = mEmptyManagerMap.get(level);
        if (emptyManager == null) {
            emptyManager = new EmptyManager(this, level);
            mEmptyManagerMap.put(level, emptyManager);
        }
        return emptyManager;
    }

    private void registerLevel(int level) {
        LevelManager typesManager = mLevelManagerMap.get(level);
        final SparseIntArray typeRes = typesManager.typeRes;
        final int size = typeRes.size();
        for (int i = 0; i < size; i++) {
            final int type = typeRes.keyAt(i);
            final int layoutResId = typeRes.valueAt(i);
            if (layoutResId == 0) {
                throw new RuntimeException(String.format(Locale.getDefault(), "are you sure register the layoutResId of level = %d?", type));
            }
            mLevelMap.put(type, level);
            mLayoutMap.put(type, layoutResId);
        }
        mAttrMap.put(level, new AttrEntity(typesManager.minSize, typesManager.isFolded));
        final int headerResId = typesManager.headerResId;
        if (headerResId != 0) {
            final int headerType = level - RecyclerViewAdapterHelper.HEADER_TYPE_DIFFER;
            mLevelMap.put(headerType, level);
            mLayoutMap.put(headerType, headerResId);
        }
        final int footerResId = typesManager.footerResId;
        if (footerResId != 0) {
            final int footerType = level - RecyclerViewAdapterHelper.FOOTER_TYPE_DIFFER;
            mLevelMap.put(footerType, level);
            mLayoutMap.put(footerType, footerResId);
        }
    }

    private void registerLoading(int level) {
        LoadingManager loadingManager = mLoadingManagerMap.get(level);
        final int loadingLayoutResId = loadingManager.loadingLayoutResId;
        if (loadingLayoutResId != 0) {
            final int loadingLayoutType = level - RecyclerViewAdapterHelper.LOADING_DATA_TYPE_DIFFER;
            mLevelMap.put(loadingLayoutType, level);
            mLayoutMap.put(loadingLayoutType, loadingLayoutResId);
        }
        final int loadingHeaderResId = loadingManager.loadingHeaderResId;
        if (loadingHeaderResId != 0) {
            final int loadingHeaderType = level - RecyclerViewAdapterHelper.LOADING_HEADER_TYPE_DIFFER;
            mLevelMap.put(loadingHeaderType, level);
            mLayoutMap.put(loadingHeaderType, loadingHeaderResId);
        }
    }

    private void registerError(int level) {
        ErrorManager errorManager = mErrorManagerMap.get(level);
        final int errorLayoutResId = errorManager.errorLayoutResId;
        if (errorLayoutResId != 0) {
            final int errorType = level - RecyclerViewAdapterHelper.ERROR_TYPE_DIFFER;
            mLevelMap.put(errorType, level);
            mLayoutMap.put(errorType, errorLayoutResId);
        }
    }

    private void registerEmpty(int level) {
        EmptyManager emptyManager = mEmptyManagerMap.get(level);
        final int emptyLayoutResId = emptyManager.emptyLayoutResId;
        if (emptyLayoutResId != 0) {
            final int emptyType = level - RecyclerViewAdapterHelper.EMPTY_TYPE_DIFFER;
            mLevelMap.put(emptyType, level);
            mLayoutMap.put(emptyType, emptyLayoutResId);
        }
    }

    void putLayoutId(int type, int layoutId) {
        mLayoutMap.put(type, layoutId);
    }

    int getLayoutId(int type) {
        return mLayoutMap.get(type);
    }

    void putLevel(int type, int level) {
        mLevelMap.put(type, level);
    }

    int getLevel(int type) {
        return mLevelMap.get(type, DEFAULT_HEADER_LEVEL);
    }

    void release() {
        mLevelMap.clear();
        mLayoutMap.clear();
        mAttrMap.clear();
        mLevelManagerMap.clear();
        mLevelManagerMap = null;
        mLoadingManagerMap.clear();
        mLoadingManagerMap = null;
        mErrorManagerMap.clear();
        mErrorManagerMap = null;
        mEmptyManagerMap.clear();
        mEmptyManagerMap = null;
    }

    AttrEntity getAttrsEntity(int level) {
        return mAttrMap.get(level);
    }

    /**
     * Type管理
     */
    public static class LevelManager {

        private ResourcesManager resourcesManager;
        private int level;
        private int headerResId;
        private int footerResId;
        private int minSize = 3;
        private boolean isFolded = false;
        private SparseIntArray typeRes;

        LevelManager(ResourcesManager resourcesManager, int level) {
            this.resourcesManager = resourcesManager;
            this.level = level;
            typeRes = new SparseIntArray();
        }

        public TypesManager type(@IntRange(from = 0, to = 999) int type) {
            return new TypesManager(this, type);
        }

        public LevelManager headerResId(@LayoutRes int headerResId) {
            this.headerResId = headerResId;
            return this;
        }

        public LevelManager footerResId(@LayoutRes int footerResId) {
            this.footerResId = footerResId;
            return this;
        }

        public LevelManager minSize(int minSize) {
            this.minSize = minSize;
            return this;
        }

        public LevelManager isFolded(boolean isFolded) {
            this.isFolded = isFolded;
            return this;
        }

        public LoadingManager loading() {
            register();
            return resourcesManager.loading(level);
        }

        public ErrorManager error() {
            register();
            return resourcesManager.error(level);
        }

        public EmptyManager empty() {
            register();
            return resourcesManager.empty(level);
        }

        public void register() {
            resourcesManager.registerLevel(level);
        }

        void addType(int type, int layoutResId) {
            typeRes.put(type, layoutResId);
        }

        /**
         * 每个level的type管理
         */
        public static class TypesManager {
            private LevelManager levelManager;
            private int type;

            TypesManager(LevelManager levelManager, int type) {
                this.levelManager = levelManager;
                this.type = type;
            }

            public LevelManager layoutResId(@LayoutRes int layoutResId) {
                levelManager.addType(type, layoutResId);
                return levelManager;
            }
        }

    }

    /**
     * loading管理
     */
    public static class LoadingManager {

        private int level;
        private int loadingLayoutResId;
        private int loadingHeaderResId;
        private ResourcesManager resourcesManager;

        LoadingManager(ResourcesManager resourcesManager, int level) {
            this.resourcesManager = resourcesManager;
            this.level = level;
        }

        public LoadingManager loadingLayoutResId(@LayoutRes int loadingLayoutResId) {
            this.loadingLayoutResId = loadingLayoutResId;
            return this;
        }

        public LoadingManager loadingHeaderResId(@LayoutRes int loadingHeaderResId) {
            this.loadingHeaderResId = loadingHeaderResId;
            return this;
        }

        public ErrorManager error() {
            register();
            return resourcesManager.error(level);
        }

        public EmptyManager empty() {
            register();
            return resourcesManager.empty(level);
        }

        public void register() {
            resourcesManager.registerLoading(level);
        }

    }

    /**
     * error管理
     */
    public static class ErrorManager {

        private int level;
        private int errorLayoutResId;
        private ResourcesManager resourcesManager;

        ErrorManager(ResourcesManager resourcesManager, int level) {
            this.resourcesManager = resourcesManager;
            this.level = level;
        }

        public ErrorManager errorLayoutResId(@LayoutRes int errorLayoutResId) {
            this.errorLayoutResId = errorLayoutResId;
            return this;
        }

        public EmptyManager empty() {
            register();
            return resourcesManager.empty(level);
        }

        public void register() {
            resourcesManager.registerError(level);
        }
    }

    /**
     * empty管理
     */
    public static class EmptyManager {

        private int level;
        private int emptyLayoutResId;
        private ResourcesManager resourcesManager;

        EmptyManager(ResourcesManager resourcesManager, int level) {
            this.resourcesManager = resourcesManager;
            this.level = level;
        }

        public EmptyManager emptyLayoutResId(@LayoutRes int emptyLayoutResId) {
            this.emptyLayoutResId = emptyLayoutResId;
            return this;
        }

        public void register() {
            resourcesManager.registerEmpty(level);
        }
    }

    static class AttrEntity {
        /**
         * 闭合时最低展示多少个
         */
        int minSize;
        /**
         * 是否是闭合状态
         */
        boolean isFolded;
        /**
         * 初始化是否闭合
         */
        boolean initState;

        AttrEntity(int minSize, boolean isFolded) {
            this.minSize = minSize;
            this.initState = isFolded;
            this.isFolded = isFolded;
        }
    }
}
