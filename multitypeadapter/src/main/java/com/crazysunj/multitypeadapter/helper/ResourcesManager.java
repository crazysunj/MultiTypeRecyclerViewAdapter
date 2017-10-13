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

    private SparseArray<TypesManager> mTypeManagers;
    private SparseArray<LoadingsManager> mLoadingManagers;
    private SparseArray<ErrorsManager> mErrorManagers;
    private SparseArray<EmptysManager> mEmptysManagers;

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

        if (mTypeManagers == null) {
            mTypeManagers = new SparseArray<TypesManager>();
        }

        TypesManager typesManager = mTypeManagers.get(type);
        if (typesManager == null) {
            typesManager = new TypesManager(this, type);
            mTypeManagers.put(type, typesManager);
        }
        return typesManager;
    }

    private LoadingsManager loading(@IntRange(from = 0, to = 999) int type) {

        if (mLoadingManagers == null) {
            mLoadingManagers = new SparseArray<LoadingsManager>();
        }

        LoadingsManager loadingsManager = mLoadingManagers.get(type);
        if (loadingsManager == null) {
            loadingsManager = new LoadingsManager(this, type);
            mLoadingManagers.put(type, loadingsManager);
        }
        return loadingsManager;
    }

    private ErrorsManager error(@IntRange(from = 0, to = 999) int type) {

        if (mErrorManagers == null) {
            mErrorManagers = new SparseArray<ErrorsManager>();
        }

        ErrorsManager errorsManager = mErrorManagers.get(type);
        if (errorsManager == null) {
            errorsManager = new ErrorsManager(this, type);
            mErrorManagers.put(type, errorsManager);
        }
        return errorsManager;
    }

    private EmptysManager empty(@IntRange(from = 0, to = 999) int type) {

        if (mEmptysManagers == null) {
            mEmptysManagers = new SparseArray<EmptysManager>();
        }

        EmptysManager emptysManager = mEmptysManagers.get(type);
        if (emptysManager == null) {
            emptysManager = new EmptysManager(this, type);
            mEmptysManagers.put(type, emptysManager);
        }
        return emptysManager;
    }

    private int registerType(int type) {
        TypesManager typesManager = mTypeManagers.get(type);
        final int level = typesManager.level;
        if (level < 0) {
            throw new RuntimeException("are you sure register the level ?");
        }

        final int layoutResId = typesManager.layoutResId;
        if (layoutResId == 0) {
            throw new RuntimeException("are you sure register the layoutResId ?");
        }

        mLevels.put(type, level);
        mLayouts.put(type, layoutResId);
        mAttrs.put(level, new AttrsEntity(typesManager.minSize, typesManager.isFolded));

        final int headerResId = typesManager.headerResId;
        if (headerResId != 0) {
            final int headerType = type - RecyclerViewAdapterHelper.HEADER_TYPE_DIFFER;
            mLevels.put(headerType, DEFAULT_HEADER_LEVEL);
            mLayouts.put(headerType, headerResId);
        }

        final int footerResId = typesManager.footerResId;
        if (footerResId != 0) {
            final int footerType = type - RecyclerViewAdapterHelper.FOOTER_TYPE_DIFFER;
            mLevels.put(footerType, DEFAULT_HEADER_LEVEL);
            mLayouts.put(footerType, footerResId);
        }

        return level;
    }

    private void registerLoading(int type, int level) {
        LoadingsManager loadingsManager = mLoadingManagers.get(type);
        final int loadingLayoutResId = loadingsManager.loadingLayoutResId;
        if (loadingLayoutResId != 0) {
            final int loadingLayoutType = type - RecyclerViewAdapterHelper.LOADING_DATA_TYPE_DIFFER;
            mLevels.put(loadingLayoutType, level);
            mLayouts.put(loadingLayoutType, loadingLayoutResId);
        }

        final int loadingHeaderResId = loadingsManager.loadingHeaderResId;
        if (loadingHeaderResId != 0) {
            final int loadingHeaderType = type - RecyclerViewAdapterHelper.LOADING_HEADER_TYPE_DIFFER;
            mLevels.put(loadingHeaderType, DEFAULT_HEADER_LEVEL);
            mLayouts.put(loadingHeaderType, loadingHeaderResId);
        }
    }

    private void registerError(int type, int level) {
        ErrorsManager errorsManager = mErrorManagers.get(type);
        final int errorLayoutResId = errorsManager.errorLayoutResId;
        if (errorLayoutResId != 0) {
            final int errorType = type - RecyclerViewAdapterHelper.ERROR_TYPE_DIFFER;
            mLevels.put(errorType, level);
            mLayouts.put(errorType, errorLayoutResId);
        }
    }

    private void registerEmpty(int type, int level) {
        EmptysManager emptysManager = mEmptysManagers.get(type);
        final int emptyLayoutResId = emptysManager.emptyLayoutResId;
        if (emptyLayoutResId != 0) {
            final int emptyType = type - RecyclerViewAdapterHelper.EMPTY_TYPE_DIFFER;
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
        mTypeManagers.clear();
        mTypeManagers = null;
        mLoadingManagers.clear();
        mLoadingManagers = null;
        mErrorManagers.clear();
        mErrorManagers = null;
        mEmptysManagers.clear();
        mEmptysManagers = null;
    }

    AttrsEntity getAttrsEntity(int level) {
        return mAttrs.get(level);
    }

    /**
     * Type管理
     */
    public static class TypesManager {

        private ResourcesManager resourcesManager;
        private int type;
        private int level = -1;
        private int layoutResId;
        private int headerResId;
        private int footerResId;
        private int minSize = 3;
        private boolean isFolded = false;

        TypesManager(ResourcesManager resourcesManager, int type) {
            this.resourcesManager = resourcesManager;
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
            register();
            return resourcesManager.loading(type);
        }

        public ErrorsManager error() {
            register();
            return resourcesManager.error(type);
        }

        public EmptysManager empty() {
            register();
            return resourcesManager.empty(type);
        }

        public void register() {
            resourcesManager.registerType(type);
        }

    }

    /**
     * loading管理
     */
    public static class LoadingsManager {

        private int type;
        private int loadingLayoutResId;
        private int loadingHeaderResId;
        private ResourcesManager resourcesManager;

        LoadingsManager(ResourcesManager resourcesManager, int type) {
            this.resourcesManager = resourcesManager;
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
            register();
            return resourcesManager.error(type);
        }

        public EmptysManager empty() {
            register();
            return resourcesManager.empty(type);
        }

        public void register() {
            final int level = resourcesManager.getLevel(type);
            resourcesManager.registerLoading(type, level);
        }

    }

    /**
     * error管理
     */
    public static class ErrorsManager {

        private int type;
        private int errorLayoutResId;
        private ResourcesManager resourcesManager;

        ErrorsManager(ResourcesManager resourcesManager, int type) {
            this.resourcesManager = resourcesManager;
            this.type = type;
        }

        public ErrorsManager errorLayoutResId(@LayoutRes int errorLayoutResId) {
            this.errorLayoutResId = errorLayoutResId;
            return this;
        }

        public EmptysManager empty() {
            register();
            return resourcesManager.empty(type);
        }

        public void register() {
            final int level = resourcesManager.getLevel(type);
            resourcesManager.registerError(type, level);
        }
    }

    /**
     * empty管理
     */
    public static class EmptysManager {

        private int type;
        private int emptyLayoutResId;
        private ResourcesManager resourcesManager;

        EmptysManager(ResourcesManager resourcesManager, int type) {
            this.resourcesManager = resourcesManager;
            this.type = type;
        }

        public EmptysManager emptyLayoutResId(@LayoutRes int emptyLayoutResId) {
            this.emptyLayoutResId = emptyLayoutResId;
            return this;
        }

        public void register() {
            final int level = resourcesManager.getLevel(type);
            resourcesManager.registerEmpty(type, level);
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
