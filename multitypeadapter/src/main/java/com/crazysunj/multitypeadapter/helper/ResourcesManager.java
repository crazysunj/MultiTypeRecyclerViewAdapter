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

import java.util.Locale;

import androidx.annotation.IntRange;
import androidx.annotation.LayoutRes;

import static com.crazysunj.multitypeadapter.helper.RecyclerViewAdapterHelper.DEFAULT_HEADER_LEVEL;

/**
 * @author: sunjian
 * created on: 2017/5/15
 * description: 资源管理
 */
final class ResourcesManager {

    /**
     * 根据type存储layoutId
     */
    private SparseIntArray mLayouts;
    /**
     * 根据type存储等级
     */
    private SparseIntArray mLevels;
    /**
     * 根据level存储属性
     */
    private SparseArray<AttrsEntity> mAttrs;

    private SparseArray<LevelsManager> mLevelsManagers;
    private SparseArray<LoadingsManager> mLoadingsManagers;
    private SparseArray<ErrorsManager> mErrorsManagers;
    private SparseArray<EmptysManager> mEmptysManagers;

    ResourcesManager() {
        mLevels = new SparseIntArray();
        mLayouts = new SparseIntArray();
        mAttrs = new SparseArray<>();
    }

    LevelsManager level(int level) {
        if (mLevelsManagers == null) {
            mLevelsManagers = new SparseArray<>();
        }
        LevelsManager typesManager = mLevelsManagers.get(level);
        if (typesManager == null) {
            typesManager = new LevelsManager(this, level);
            mLevelsManagers.put(level, typesManager);
        }
        return typesManager;
    }

    private LoadingsManager loading(int level) {
        if (mLoadingsManagers == null) {
            mLoadingsManagers = new SparseArray<>();
        }
        LoadingsManager loadingsManager = mLoadingsManagers.get(level);
        if (loadingsManager == null) {
            loadingsManager = new LoadingsManager(this, level);
            mLoadingsManagers.put(level, loadingsManager);
        }
        return loadingsManager;
    }

    private ErrorsManager error(int level) {
        if (mErrorsManagers == null) {
            mErrorsManagers = new SparseArray<>();
        }
        ErrorsManager errorsManager = mErrorsManagers.get(level);
        if (errorsManager == null) {
            errorsManager = new ErrorsManager(this, level);
            mErrorsManagers.put(level, errorsManager);
        }
        return errorsManager;
    }

    private EmptysManager empty(int level) {
        if (mEmptysManagers == null) {
            mEmptysManagers = new SparseArray<>();
        }
        EmptysManager emptysManager = mEmptysManagers.get(level);
        if (emptysManager == null) {
            emptysManager = new EmptysManager(this, level);
            mEmptysManagers.put(level, emptysManager);
        }
        return emptysManager;
    }

    private void registerLevel(int level) {
        LevelsManager typesManager = mLevelsManagers.get(level);
        final SparseIntArray typeRes = typesManager.typeRes;
        final int size = typeRes.size();
        for (int i = 0; i < size; i++) {
            final int type = typeRes.keyAt(i);
            final int layoutResId = typeRes.valueAt(i);
            if (layoutResId == 0) {
                throw new RuntimeException(String.format(Locale.getDefault(), "are you sure register the layoutResId of level = %d?", type));
            }
            mLevels.put(type, level);
            mLayouts.put(type, layoutResId);
        }
        mAttrs.put(level, new AttrsEntity(typesManager.minSize, typesManager.isFolded));
        final int headerResId = typesManager.headerResId;
        if (headerResId != 0) {
            final int headerType = level - RecyclerViewAdapterHelper.HEADER_TYPE_DIFFER;
            mLevels.put(headerType, level);
            mLayouts.put(headerType, headerResId);
        }
        final int footerResId = typesManager.footerResId;
        if (footerResId != 0) {
            final int footerType = level - RecyclerViewAdapterHelper.FOOTER_TYPE_DIFFER;
            mLevels.put(footerType, level);
            mLayouts.put(footerType, footerResId);
        }
    }

    private void registerLoading(int level) {
        LoadingsManager loadingsManager = mLoadingsManagers.get(level);
        final int loadingLayoutResId = loadingsManager.loadingLayoutResId;
        if (loadingLayoutResId != 0) {
            final int loadingLayoutType = level - RecyclerViewAdapterHelper.LOADING_DATA_TYPE_DIFFER;
            mLevels.put(loadingLayoutType, level);
            mLayouts.put(loadingLayoutType, loadingLayoutResId);
        }
        final int loadingHeaderResId = loadingsManager.loadingHeaderResId;
        if (loadingHeaderResId != 0) {
            final int loadingHeaderType = level - RecyclerViewAdapterHelper.LOADING_HEADER_TYPE_DIFFER;
            mLevels.put(loadingHeaderType, level);
            mLayouts.put(loadingHeaderType, loadingHeaderResId);
        }
    }

    private void registerError(int level) {
        ErrorsManager errorsManager = mErrorsManagers.get(level);
        final int errorLayoutResId = errorsManager.errorLayoutResId;
        if (errorLayoutResId != 0) {
            final int errorType = level - RecyclerViewAdapterHelper.ERROR_TYPE_DIFFER;
            mLevels.put(errorType, level);
            mLayouts.put(errorType, errorLayoutResId);
        }
    }

    private void registerEmpty(int level) {
        EmptysManager emptysManager = mEmptysManagers.get(level);
        final int emptyLayoutResId = emptysManager.emptyLayoutResId;
        if (emptyLayoutResId != 0) {
            final int emptyType = level - RecyclerViewAdapterHelper.EMPTY_TYPE_DIFFER;
            mLevels.put(emptyType, level);
            mLayouts.put(emptyType, emptyLayoutResId);
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

    void release() {
        mLevels.clear();
        mLevels = null;
        mLayouts.clear();
        mLayouts = null;
        mAttrs.clear();
        mAttrs = null;
        mLevelsManagers.clear();
        mLevelsManagers = null;
        mLoadingsManagers.clear();
        mLoadingsManagers = null;
        mErrorsManagers.clear();
        mErrorsManagers = null;
        mEmptysManagers.clear();
        mEmptysManagers = null;
    }

    AttrsEntity getAttrsEntity(int level) {
        return mAttrs.get(level);
    }

    /**
     * Type管理
     */
    public static class LevelsManager {

        private ResourcesManager resourcesManager;
        private int level;
        private int headerResId;
        private int footerResId;
        private int minSize = 3;
        private boolean isFolded = false;
        private SparseIntArray typeRes;

        LevelsManager(ResourcesManager resourcesManager, int level) {
            this.resourcesManager = resourcesManager;
            this.level = level;
            typeRes = new SparseIntArray();
        }

        public TypesManager type(@IntRange(from = 0, to = 999) int type) {
            return new TypesManager(this, type);
        }

        public LevelsManager headerResId(@LayoutRes int headerResId) {
            this.headerResId = headerResId;
            return this;
        }

        public LevelsManager footerResId(@LayoutRes int footerResId) {
            this.footerResId = footerResId;
            return this;
        }

        public LevelsManager minSize(int minSize) {
            this.minSize = minSize;
            return this;
        }

        public LevelsManager isFolded(boolean isFolded) {
            this.isFolded = isFolded;
            return this;
        }

        public LoadingsManager loading() {
            register();
            return resourcesManager.loading(level);
        }

        public ErrorsManager error() {
            register();
            return resourcesManager.error(level);
        }

        public EmptysManager empty() {
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
            private LevelsManager levelsManager;
            private int type;

            TypesManager(LevelsManager levelsManager, int type) {
                this.levelsManager = levelsManager;
                this.type = type;
            }

            public LevelsManager layoutResId(@LayoutRes int layoutResId) {
                levelsManager.addType(type, layoutResId);
                return levelsManager;
            }
        }

    }

    /**
     * loading管理
     */
    public static class LoadingsManager {

        private int level;
        private int loadingLayoutResId;
        private int loadingHeaderResId;
        private ResourcesManager resourcesManager;

        LoadingsManager(ResourcesManager resourcesManager, int level) {
            this.resourcesManager = resourcesManager;
            this.level = level;
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
            register();
            return resourcesManager.error(level);
        }

        public EmptysManager empty() {
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
    public static class ErrorsManager {

        private int level;
        private int errorLayoutResId;
        private ResourcesManager resourcesManager;

        ErrorsManager(ResourcesManager resourcesManager, int level) {
            this.resourcesManager = resourcesManager;
            this.level = level;
        }

        public ErrorsManager errorLayoutResId(@LayoutRes int errorLayoutResId) {
            this.errorLayoutResId = errorLayoutResId;
            return this;
        }

        public EmptysManager empty() {
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
    public static class EmptysManager {

        private int level;
        private int emptyLayoutResId;
        private ResourcesManager resourcesManager;

        EmptysManager(ResourcesManager resourcesManager, int level) {
            this.resourcesManager = resourcesManager;
            this.level = level;
        }

        public EmptysManager emptyLayoutResId(@LayoutRes int emptyLayoutResId) {
            this.emptyLayoutResId = emptyLayoutResId;
            return this;
        }

        public void register() {
            resourcesManager.registerEmpty(level);
        }
    }

    static class AttrsEntity {
        int minSize;
        boolean isFolded;
        boolean initState;

        AttrsEntity(int minSize, boolean isFolded) {
            this.minSize = minSize;
            this.initState = isFolded;
            this.isFolded = isFolded;
        }
    }
}
