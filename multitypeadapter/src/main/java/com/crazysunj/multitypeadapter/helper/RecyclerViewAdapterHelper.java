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

import android.util.LruCache;
import android.util.SparseArray;

import androidx.annotation.CallSuper;
import androidx.annotation.IntDef;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListUpdateCallback;
import androidx.recyclerview.widget.RecyclerView;

import com.crazysunj.multitypeadapter.adapter.EmptyEntityAdapter;
import com.crazysunj.multitypeadapter.adapter.ErrorEntityAdapter;
import com.crazysunj.multitypeadapter.adapter.LoadingEntityAdapter;
import com.crazysunj.multitypeadapter.entity.HandleBase;
import com.crazysunj.multitypeadapter.entity.LevelData;
import com.crazysunj.multitypeadapter.entity.MultiTypeEntity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Queue;

/**
 * @author: sunjian
 * created on: 2017/5/4
 * description:
 * 如果您发现哪里性能比较差或者说设计不合理，希望您能反馈给我
 * email:twsunj@gmail.com
 * 博客有联系方式{@link <a href="http://crazysunj.com/">博客</a>}
 * type 取值范围
 * data类型 [0,1000)
 * header类型 [-1000,0)
 * loading-data类型 [-2000,-1000)
 * loading-header类型 [-3000,-2000)
 * error类型 [-4000,-3000)
 * empty类型 [-5000,-4000)
 * footer类型 [-6000,-5000)
 */
public abstract class RecyclerViewAdapterHelper<T extends MultiTypeEntity> {

    protected static final String TAG = RecyclerViewAdapterHelper.class.getSimpleName();

    /**
     * 标准模式，根据level顺序摆放
     */
    public static final int MODE_STANDARD = 0;
    /**
     * 随机模式，日常集合
     */
    public static final int MODE_MIXED = 1;

    @IntDef({MODE_STANDARD, MODE_MIXED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface RefreshMode {
    }

    /**
     * 闲置状态
     */
    public static final int REFRESH_TYPE_IDLE = -1;
    /**
     * 刷新数据全部
     */
    public static final int REFRESH_TYPE_DATA_ALL = -2;
    /**
     * 刷新loading全部
     */
    public static final int REFRESH_TYPE_LOAD_ALL = -3;
    /**
     * 清除操作
     */
    public static final int REFRESH_TYPE_CLEAR = -4;
    /**
     * 其他
     */
    public static final int REFRESH_TYPE_OTHER = -5;

    /**
     * 默认数量为0
     */
    private static final int PRE_DATA_COUNT = 0;

    /**
     * header类型差值
     */
    public static final int HEADER_TYPE_DIFFER = 1000;
    /**
     * loading-data类型差值
     */
    public static final int LOADING_DATA_TYPE_DIFFER = 2000;
    /**
     * loading-header类型差值
     */
    public static final int LOADING_HEADER_TYPE_DIFFER = 3000;
    /**
     * error类型差值
     */
    public static final int ERROR_TYPE_DIFFER = 4000;
    /**
     * empty类型差值
     */
    public static final int EMPTY_TYPE_DIFFER = 5000;
    /**
     * footer类型差值
     */
    public static final int FOOTER_TYPE_DIFFER = 6000;

    /**
     * 默认viewType
     */
    public static final int DEFAULT_VIEW_TYPE = 0;
    /**
     * 默认header的level
     */
    static final int DEFAULT_HEADER_LEVEL = -1;
    static final int REFRESH_NONE = 0x00;
    /**
     * 只刷新header 001
     */
    static final int REFRESH_HEADER = 0x01;
    /**
     * 只刷新fooer 010
     */
    static final int REFRESH_FOOTER = 0x02;
    /**
     * 同时刷新header和footer 011
     */
    private static final int REFRESH_HEADER_FOOTER = 0x03;
    /**
     * 只刷新data 100
     */
    static final int REFRESH_DATA = 0x04;
    /**
     * 同时刷新data和header 101
     */
    private static final int REFRESH_HEADER_DATA = 0x05;
    /**
     * 同时刷新data和footer 110
     */
    private static final int REFRESH_FOOTER_DATA = 0x06;
    /**
     * 同时刷新header,data,footer 111
     */
    private static final int REFRESH_HEADER_FOOTER_DATA = 0x07;
    /**
     * 刷新全部
     */
    private static final int REFRESH_ALL = 0x08;

    /**
     * 拦截刷新，主要防止手快
     */
    private boolean mIsCanRefresh = true;
    /**
     * loadingview关于data的最大缓存值
     */
    private int mMaxDataCacheCount = 12;
    /**
     * loadingview关于header的最大缓存值
     */
    private int mMaxSingleCacheCount = 6;

    /**
     * 根据level存储的数据
     */
    private SparseArray<LevelData<T>> mLevelData;
    /**
     * loading的data缓存
     */
    private LruCache<String, List<T>> mDataCache;
    /**
     * loading的header缓存
     */
    private LruCache<String, T> mSingleCache;
    /**
     * 老数据
     */
    protected List<T> mNewData;
    /**
     * 当前数据
     */
    protected List<T> mData;
    /**
     * 绑定Adapter
     */
    protected RecyclerView.Adapter mAdapter;
    /**
     * 资源管理
     */
    private ResourcesManager mResourcesManager;
    /**
     * 刷新队列，支持高频率刷新
     */
    private Queue<HandleBase<T>> mRefreshQueue;
    /**
     * 全局loading刷新数据
     */
    private SparseArray<LevelData<T>> mLoadingLevelData;

    /**
     * 当前模式
     */
    private int mCurrentMode;
    /**
     * 当前刷新Level
     */
    private int mCurrentLevel;

    public RecyclerViewAdapterHelper() {
        this(MODE_STANDARD);
    }

    public RecyclerViewAdapterHelper(@RefreshMode int mode) {
        mData = new ArrayList<>();
        mCurrentMode = mode;
        mNewData = new ArrayList<>();
        mLevelData = new SparseArray<>();
        mResourcesManager = new ResourcesManager();
        mRefreshQueue = new LinkedList<>();
    }

    /**
     * 绑定adapter
     *
     * @param adapter adapter
     */
    public void bindAdapter(RecyclerView.Adapter adapter) {
        mAdapter = adapter;
    }

    /**
     * 返回绑定adapter
     *
     * @return RecyclerView.Adapter
     */
    @SuppressWarnings("unchecked")
    public <A extends RecyclerView.Adapter> A getBindAdapter() {
        return (A) mAdapter;
    }

    /**
     * 加载实体类适配器
     */
    private LoadingEntityAdapter<T> mLoadingEntityAdapter;

    /**
     * 设置加载实体类适配器
     *
     * @param adapter adapter
     */
    public void setLoadingAdapter(LoadingEntityAdapter<T> adapter) {
        mLoadingEntityAdapter = adapter;
    }

    /**
     * 空实体类适配器
     */
    private EmptyEntityAdapter<T> mEmptyEntityAdapter;

    /**
     * 设置空实体类适配器
     *
     * @param adapter adapter
     */
    public void setEmptyAdapter(EmptyEntityAdapter<T> adapter) {
        mEmptyEntityAdapter = adapter;
    }

    /**
     * 错误实体类适配器
     */
    private ErrorEntityAdapter<T> mErrorEntityAdapter;

    /**
     * 设置错误实体类适配器
     *
     * @param adapter adapter
     */
    public void setErrorAdapter(ErrorEntityAdapter<T> adapter) {
        mErrorEntityAdapter = adapter;
    }

    /**
     * 根据索引返回type
     *
     * @param position 索引
     * @return level
     */
    public int getItemViewType(int position) {
        T item = mData.get(position);
        if (item != null) {
            return item.getItemType();
        }
        return DEFAULT_VIEW_TYPE;
    }

    /**
     * 根据索引返回item
     *
     * @param position 索引
     * @return 返回的item
     */
    public T getItem(int position) {
        return mData.get(position);
    }

    /**
     * 链式注册Module
     *
     * @param level 数据类型等级
     * @return LevelManager
     */
    public ResourcesManager.LevelManager registerModule(@IntRange(from = 0, to = 999) int level) {
        return mResourcesManager.level(level);
    }

    /**
     * 获取依赖的数据集合
     *
     * @return 数据集合
     */
    public List<T> getData() {
        return mData;
    }

    /**
     * @param level 数据类型等级
     * @return 获取LevelData
     */
    public LevelData<T> getDataWithLevel(int level) {
        return mLevelData.get(level);
    }

    /**
     * 刷新loading页
     * 调用前请初始化loading全局数据{@link #initGlobalLoadingConfig(LoadingConfig)}
     */
    public void notifyLoadingChanged() {
        if (mLoadingLevelData == null) {
            return;
        }

        mNewData.clear();
        mLevelData.clear();

        final int size = mLoadingLevelData.size();
        List<T> data = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            int level = mLoadingLevelData.keyAt(i);
            LevelData<T> levelData = mLoadingLevelData.valueAt(i);
            mLevelData.put(level, levelData);
            T header = levelData.getHeader();
            List<T> itemData = levelData.getData();
            T footer = levelData.getFooter();
            if (header != null) {
                data.add(header);
            }
            if (itemData != null) {
                data.addAll(itemData);
            }
            if (footer != null) {
                data.add(footer);
            }
        }
        mNewData.addAll(data);
        notifyModuleChanged(mNewData, null, null, REFRESH_TYPE_LOAD_ALL, REFRESH_ALL);
    }

    /**
     * 刷新level的loading页
     * 调用前请初始化loading全局数据{@link #initGlobalLoadingConfig(LoadingConfig)}
     *
     * @param level level
     */
    public void notifyLoadingChanged(int level) {
        if (mLoadingLevelData == null) {
            return;
        }

        T header = null;
        List<T> data = null;
        LevelData<T> levelData = mLoadingLevelData.get(level);
        if (levelData != null) {
            header = levelData.getHeader();
            data = levelData.getData();
        }
        notifyModuleChanged(data, header, null, level, REFRESH_HEADER_FOOTER_DATA);
    }

    /**
     * 刷新loading的data
     *
     * @param level     level
     * @param dataCount 刷新条目数
     */
    public void notifyLoadingDataChanged(int level, @IntRange(from = 1) int dataCount) {
        List<T> datas = createLoadingData(level, dataCount);
        notifyModuleChanged(datas, null, null, level, REFRESH_FOOTER_DATA);
    }

    /**
     * 刷新loading的header
     *
     * @param level level
     */
    public void notifyLoadingHeaderChanged(int level) {
        T header = createLoadingHeader(level);
        notifyModuleChanged(null, header, null, level, REFRESH_HEADER_FOOTER);
    }

    /**
     * 刷新loading的data和header
     *
     * @param level     level
     * @param dataCount 刷新条目数
     */
    public void notifyLoadingDataAndHeaderChanged(int level, @IntRange(from = 1) int dataCount) {
        T header = createLoadingHeader(level);
        List<T> datas = createLoadingData(level, dataCount);
        notifyModuleChanged(datas, header, null, level, REFRESH_HEADER_FOOTER_DATA);
    }

    /**
     * 只刷新data
     *
     * @param data  data
     * @param level level
     */
    @SuppressWarnings("unchecked")
    public void notifyModuleDataChanged(@NonNull List<? extends T> data, int level) {
        notifyModuleChanged((List<T>) data, null, null, level, REFRESH_DATA);
    }

    public void notifyModuleDataChanged(@NonNull T data, int level) {
        notifyModuleChanged(Collections.singletonList(data), null, null, level, REFRESH_DATA);
    }

    /**
     * 只刷新header
     *
     * @param header header
     * @param level  level
     */
    public void notifyModuleHeaderChanged(@NonNull T header, int level) {
        notifyModuleChanged(null, header, null, level, REFRESH_HEADER);
    }

    /**
     * 只刷新footer
     *
     * @param footer fooer
     * @param level  level
     */
    public void notifyModuleFooterChanged(@NonNull T footer, int level) {
        notifyModuleChanged(null, null, footer, level, REFRESH_FOOTER);
    }

    /**
     * 同时刷新header和fooer
     *
     * @param header header
     * @param footer footer
     * @param level  level
     */
    public void notifyModuleHeaderAndFooterChanged(@NonNull T header, @NonNull T footer, int level) {
        notifyModuleChanged(null, header, footer, level, REFRESH_HEADER_FOOTER);
    }

    /**
     * 同时刷新data和header
     *
     * @param data   data
     * @param header header
     * @param level  level
     */
    @SuppressWarnings("unchecked")
    public void notifyModuleDataAndHeaderChanged(@NonNull List<? extends T> data, @NonNull T header, int level) {
        notifyModuleChanged((List<T>) data, header, null, level, REFRESH_HEADER_DATA);
    }

    /**
     * 同时刷新data和header
     *
     * @param data   data
     * @param header header
     * @param level  level
     */
    public void notifyModuleDataAndHeaderChanged(@NonNull T data, @NonNull T header, int level) {
        notifyModuleChanged(Collections.singletonList(data), header, null, level, REFRESH_HEADER_DATA);
    }

    /**
     * 同时刷新data和footer
     *
     * @param data   data
     * @param footer fooer
     * @param level  level
     */
    @SuppressWarnings("unchecked")
    public void notifyModuleDataAndFooterChanged(@NonNull List<? extends T> data, @NonNull T footer, int level) {
        notifyModuleChanged((List<T>) data, null, footer, level, REFRESH_FOOTER_DATA);
    }

    /**
     * 同时刷新data和fooer
     *
     * @param data   data
     * @param footer fooer
     * @param level  level
     */
    public void notifyModuleDataAndFooterChanged(@NonNull T data, @NonNull T footer, int level) {
        notifyModuleChanged(Collections.singletonList(data), null, footer, level, REFRESH_FOOTER_DATA);
    }

    /**
     * 同时刷新header、data和fooer
     *
     * @param header header
     * @param data   data
     * @param footer fooer
     * @param level  level
     */
    @SuppressWarnings("unchecked")
    public void notifyModuleDataAndHeaderAndFooterChanged(@NonNull List<? extends T> data, @NonNull T header, @NonNull T footer, int level) {
        notifyModuleChanged((List<T>) data, header, footer, level, REFRESH_HEADER_FOOTER_DATA);
    }

    /**
     * 同时刷新header、data和fooer
     * 2.1.0版本调整data参数靠前，2.1.0之前header靠前，多人反馈参数不和谐问题
     * 这里对2.1.0版本之前的做了兼容，但希望大家能调整过来
     *
     * @param data   data
     * @param header header
     * @param footer fooer
     * @param level  level
     */
    public void notifyModuleDataAndHeaderAndFooterChanged(@NonNull T data, @NonNull T header, @NonNull T footer, int level) {
        if (data.getItemType() < 0) {
            T temp = data;
            data = header;
            header = temp;
        }
        notifyModuleChanged(Collections.singletonList(data), header, footer, level, REFRESH_HEADER_FOOTER_DATA);
    }

    /**
     * 刷新空数据页面
     * 如果调用了{@link #setEmptyAdapter(EmptyEntityAdapter)}可直接调用{@link #notifyModuleEmptyChanged(int)}
     *
     * @param emptyData 空数据
     * @param level     level
     */
    public void notifyModuleEmptyChanged(@NonNull T emptyData, int level) {
        if (emptyData.getItemType() == level - EMPTY_TYPE_DIFFER) {
            notifyModuleChanged(Collections.singletonList(emptyData), null, null, level, REFRESH_HEADER_FOOTER_DATA);
        } else {
            throw new DataException("Please set correct itemType ! level = " + level);
        }
    }

    /**
     * 需要调用{@link #setEmptyAdapter(EmptyEntityAdapter)}
     * 如果不想设置{@link EmptyEntityAdapter}，请调用{@link #notifyModuleEmptyChanged(MultiTypeEntity, int)}
     *
     * @param level level
     */
    public void notifyModuleEmptyChanged(int level) {
        if (mSingleCache == null) {
            mSingleCache = new LruCache<>(mMaxSingleCacheCount);
        }

        T emptyEntity = mSingleCache.get(getEmptyKey(level));
        if (emptyEntity == null) {
            checkEmptyAdapterBind();
            emptyEntity = mEmptyEntityAdapter.createEmptyEntity(level - EMPTY_TYPE_DIFFER, level);
        }
        notifyModuleEmptyChanged(emptyEntity, level);
    }

    /**
     * 刷新错误页面
     * 如果调用了{@link #setErrorAdapter(ErrorEntityAdapter)}可直接调用{@link #notifyModuleErrorChanged(int)}
     *
     * @param errorData 错误数据
     * @param level     level
     */
    public void notifyModuleErrorChanged(@NonNull T errorData, int level) {
        if (errorData.getItemType() == level - ERROR_TYPE_DIFFER) {
            notifyModuleChanged(Collections.singletonList(errorData), null, null, level, REFRESH_HEADER_FOOTER_DATA);
        } else {
            throw new DataException("Please set correct itemType ! level = " + level);
        }
    }

    /**
     * 需要调用{@link #setErrorAdapter(ErrorEntityAdapter)}
     * 如果不想设置{@link ErrorEntityAdapter}，请调用{@link #notifyModuleErrorChanged(MultiTypeEntity, int)}
     *
     * @param level level
     */
    public void notifyModuleErrorChanged(int level) {
        if (mSingleCache == null) {
            mSingleCache = new LruCache<>(mMaxSingleCacheCount);
        }

        T errorEntity = mSingleCache.get(getErrorKey(level));
        if (errorEntity == null) {
            checkErrorAdapterBind();
            errorEntity = mErrorEntityAdapter.createErrorEntity(level - ERROR_TYPE_DIFFER, level);
        }
        notifyModuleErrorChanged(errorEntity, level);
    }

    /**
     * 全局刷新
     * 标记为过时，请直接调用{@link RecyclerView.Adapter#notifyDataSetChanged()}
     */
    @Deprecated
    public void notifyDataSetChanged() {
        checkAdapterBind();
        if (!mIsCanRefresh) {
            return;
        }
        mCurrentLevel = REFRESH_TYPE_DATA_ALL;
        onStart();
        mAdapter.notifyDataSetChanged();
        onEnd();
    }

    /**
     * 直接操作单个level后的刷新方法
     * 该方法只支持item的改变，同{@link #setData(int, MultiTypeEntity)}
     *
     * @param level level
     */
    public void notifyDataSetChanged(int level) {
        LevelData<T> levelData = getDataWithLevel(level);
        if (levelData == null) {
            return;
        }
        checkAdapterBind();
        if (!mIsCanRefresh) {
            return;
        }
        mCurrentLevel = level;
        onStart();

        int positionStart = getPositionStart(level);
        int count = 0;
        if (levelData.getHeader() != null) {
            count++;
        }
        if (levelData.getFooter() != null) {
            count++;
        }
        List<T> list = levelData.getData();
        if (list != null && !list.isEmpty()) {
            ResourcesManager.AttrEntity attrEntity = mResourcesManager.getAttrsEntity(level);
            int size = list.size();
            if (!attrEntity.isFolded || size <= attrEntity.minSize) {
                count += size;
            } else {
                count += attrEntity.minSize;
            }
        }
        mAdapter.notifyItemRangeChanged(positionStart + getPreDataCount(), count);
        onEnd();
    }

    /**
     * 专门用于混合模式的全局刷新 {@link RecyclerViewAdapterHelper#MODE_MIXED}
     *
     * @param list List
     */
    public void notifyDataSetChanged(@NonNull List<? extends T> list) {
        checkAdapterBind();
        if (!mIsCanRefresh) {
            return;
        }
        mCurrentMode = MODE_MIXED;
        mCurrentLevel = REFRESH_TYPE_DATA_ALL;
        onStart();
        mNewData.clear();
        mNewData.addAll(list);
        mData.clear();
        mData.addAll(mNewData);
        mAdapter.notifyDataSetChanged();
        onEnd();
    }


    /**
     * 专门用于标准模式的全局刷新 {@link RecyclerViewAdapterHelper#MODE_STANDARD}
     *
     * @param levelData SparseArray，需要对list2次封装
     */
    public void notifyDataSetChanged(@NonNull SparseArray<LevelData<T>> levelData) {
        mCurrentMode = MODE_STANDARD;
        final int size = levelData.size();
        mNewData.clear();
        mLevelData.clear();
        for (int i = 0; i < size; i++) {
            LevelData<T> itemData = levelData.valueAt(i);
            if (itemData == null) {
                continue;
            }
            int level = levelData.keyAt(i);
            mLevelData.put(level, itemData);
            T header = itemData.getHeader();
            if (header != null) {
                mNewData.add(header);
            }
            List<T> data = itemData.getData();
            if (data != null) {
                ResourcesManager.AttrEntity attrEntity = mResourcesManager.getAttrsEntity(level);
                int itemSize = data.size();
                if (!attrEntity.isFolded || itemSize <= attrEntity.minSize) {
                    mNewData.addAll(data);
                } else {
                    mNewData.addAll(data.subList(0, attrEntity.minSize));
                }
            }
            T footer = itemData.getFooter();
            if (footer != null) {
                mNewData.add(footer);
            }
        }
        notifyModuleChanged(mNewData, null, null, REFRESH_TYPE_DATA_ALL, REFRESH_ALL);
    }

    /**
     * 清除level数组中的数据
     *
     * @param levels 数据类型等级
     */
    public void clearModule(@NonNull int... levels) {
        checkStandardMode();
        if (levels.length == 0) {
            return;
        }
        for (int level : levels) {
            mLevelData.remove(level);
        }
        mNewData.clear();
        for (int i = 0, size = mLevelData.size(); i < size; i++) {
            LevelData<T> levelData = mLevelData.valueAt(i);
            T header = levelData.getHeader();
            List<T> data = levelData.getData();
            T footer = levelData.getFooter();
            if (header != null) {
                mNewData.add(header);
            }
            if (data != null) {
                mNewData.addAll(data);
            }
            if (footer != null) {
                mNewData.add(footer);
            }
        }
        notifyModuleChanged(mNewData, null, null, REFRESH_TYPE_DATA_ALL, REFRESH_ALL);
    }

    /**
     * 只剩下level数组中的数据
     *
     * @param levels 数据类型集合
     */
    public void remainModule(@NonNull int... levels) {
        checkStandardMode();

        if (levels.length == 0) {
            return;
        }

        SparseArray<LevelData<T>> temData = new SparseArray<>();
        for (int level : levels) {
            LevelData<T> levelData = mLevelData.get(level);
            if (levelData == null) {
                continue;
            }
            temData.put(level, levelData);
        }

        mNewData.clear();
        mLevelData.clear();
        for (int i = 0, size = temData.size(); i < size; i++) {
            int level = temData.keyAt(i);
            LevelData<T> levelData = temData.valueAt(i);
            T header = levelData.getHeader();
            List<T> data = levelData.getData();
            T footer = levelData.getFooter();
            if (header != null) {
                mNewData.add(header);
            }
            if (data != null) {
                mNewData.addAll(data);
            }
            if (footer != null) {
                mNewData.add(footer);
            }
            mLevelData.put(level, levelData);
        }
        notifyModuleChanged(mNewData, null, null, REFRESH_TYPE_DATA_ALL, REFRESH_ALL);
    }

    /**
     * 清除所有数据
     */
    public void clear() {
        if (mData.isEmpty()) {
            return;
        }

        checkAdapterBind();

        if (!mIsCanRefresh) {
            return;
        }
        mCurrentLevel = REFRESH_TYPE_CLEAR;
        onStart();
        mData.clear();
        mLevelData.clear();
        mAdapter.notifyDataSetChanged();
        onEnd();
    }

    /**
     * 添加数据，仅用于混合模式{@link RecyclerViewAdapterHelper#MODE_MIXED}
     *
     * @param position 添加的位置
     */
    public void addData(int position, @NonNull T data) {
        addData(position, data, DEFAULT_HEADER_LEVEL);
    }

    /**
     * 添加数据，仅用于标准模式{@link RecyclerViewAdapterHelper#MODE_STANDARD}
     *
     * @param position 添加的位置
     * @param level    level，必须添加level，不然无法确认添加的是什么level，存放在哪里
     */
    public void addData(int position, @NonNull T data, int level) {

        if (position >= mData.size()) {
            addData(data);
            return;
        }

        checkAdapterBind();
        if (!mIsCanRefresh) {
            return;
        }
        mCurrentLevel = REFRESH_TYPE_OTHER;
        onStart();

        if (mCurrentMode == MODE_STANDARD) {
            final int itemType = data.getItemType();
            final int currentLevel = getLevelByPosition(position);
            if (level > currentLevel) {
                onEnd();
                throw new DataException("the data's level can't be add, the level bigger");
            }

            if (position - 1 >= 0) {
                final int preLevel = getLevelByPosition(position - 1);
                if (level == preLevel) {
                    final int preItemType = mData.get(position - 1).getItemType();
                    if (itemType >= -HEADER_TYPE_DIFFER && itemType < 0 && preItemType == itemType) {
                        // 插入的位置前一个是head类型且与它同一type，冲突
                        onEnd();
                        throw new DataException("the data's level can't be add, one level has one header");
                    }

                    if (itemType >= -FOOTER_TYPE_DIFFER && itemType < -EMPTY_TYPE_DIFFER && preItemType == itemType) {
                        // 插入的位置前一个是foot类型且与它同一type，冲突
                        onEnd();
                        throw new DataException("the data's level can't be add, one level has one footer");
                    }
                }
            }

            if (level == currentLevel) {
                final int currentItemType = mData.get(position).getItemType();
                if (itemType >= -HEADER_TYPE_DIFFER && itemType < 0 && currentItemType == itemType) {
                    // 插入的位置是head类型且与它同一type，冲突
                    onEnd();
                    throw new DataException("the data's level can't be add, one level has one header");
                }

                if (itemType >= -FOOTER_TYPE_DIFFER && itemType < -EMPTY_TYPE_DIFFER && currentItemType == itemType) {
                    // 插入的位置前一个是foot类型且与它同一type，冲突
                    onEnd();
                    throw new DataException("the data's level can't be add, one level has one footer");
                }
            }

            LevelData<T> levelData = mLevelData.get(level);
            if (levelData == null) {
                levelData = new LevelData<>(new ArrayList<T>(), null, null);
                mLevelData.put(level, levelData);
            }
            List<T> list = levelData.getData();
            if (list == null) {
                levelData.setData(new ArrayList<T>());
                list = levelData.getData();
            }
            if (itemType >= -HEADER_TYPE_DIFFER && itemType < 0) {
                levelData.setHeader(data);
            } else if (itemType >= -FOOTER_TYPE_DIFFER && itemType < -EMPTY_TYPE_DIFFER) {
                levelData.setFooter(data);
            } else if (itemType >= 0) {
                int positionStart = getPositionStart(level);
                T header = levelData.getHeader();
                int addPosition = header == null ? position - positionStart : position - positionStart - 1;
                list.add(addPosition, data);
            } else {
                onEnd();
                throw new DataException("The data's type can't be add");
            }
        }
        mData.add(position, data);
        mAdapter.notifyItemInserted(position + getPreDataCount());
        onEnd();
    }

    /**
     * 添加数据，{@link RecyclerViewAdapterHelper#MODE_MIXED}
     *
     * @param data 数据
     */
    public void addData(@NonNull T data) {
        addData(data, DEFAULT_HEADER_LEVEL);
    }

    /**
     * 添加数据，{@link RecyclerViewAdapterHelper#MODE_STANDARD}
     *
     * @param data  数据
     * @param level level，标准模式需要提供level，除了校验数据外，可能新增的数据从未加载过，无法确认level
     */
    public void addData(@NonNull T data, int level) {
        checkAdapterBind();

        if (!mIsCanRefresh) {
            return;
        }
        mCurrentLevel = REFRESH_TYPE_OTHER;
        onStart();

        if (mCurrentMode == MODE_STANDARD) {
            final int size = mData.size();
            int itemType = data.getItemType();
            if (size > 0) {
                // 前面有数据
                final int lastLevel = getLevelByPosition(size - 1);
                if (level < lastLevel) {
                    onEnd();
                    throw new DataException("the data's level can't be add, the level smaller");
                }
                if (level == lastLevel) {
                    int lastItemType = mData.get(size - 1).getItemType();
                    if (itemType >= -HEADER_TYPE_DIFFER && itemType < 0 && lastItemType == itemType) {
                        // 插入的位置是head类型且与它同一type，冲突
                        onEnd();
                        throw new DataException("the data's level can't be add, one level has one header");
                    }

                    if (itemType >= -FOOTER_TYPE_DIFFER && itemType < -EMPTY_TYPE_DIFFER && lastItemType == itemType) {
                        // 插入的位置前一个是foot类型且与它同一type，冲突
                        onEnd();
                        throw new DataException("the data's level can't be add, one level has one footer");
                    }
                }
            }

            LevelData<T> levelData = mLevelData.get(level);
            if (levelData == null) {
                levelData = new LevelData<>(new ArrayList<T>(), null, null);
                mLevelData.put(level, levelData);
            }
            List<T> list = levelData.getData();
            if (list == null) {
                levelData.setData(new ArrayList<T>());
                list = levelData.getData();
            }

            if (itemType >= -HEADER_TYPE_DIFFER && itemType < 0) {
                levelData.setHeader(data);
            } else if (itemType >= -FOOTER_TYPE_DIFFER && itemType < -EMPTY_TYPE_DIFFER) {
                levelData.setFooter(data);
            } else if (itemType >= 0) {
                list.add(data);
            } else {
                onEnd();
                throw new DataException("The data's type can't be add");
            }
        }
        mData.add(data);
        mAdapter.notifyItemInserted(mData.size() + getPreDataCount());
        onEnd();
    }

    /**
     * 移除数据
     *
     * @param position 位置
     */
    public T removeData(int position) {
        checkAdapterBind();

        if (!mIsCanRefresh) {
            return null;
        }
        mCurrentLevel = REFRESH_TYPE_OTHER;
        onStart();

        if (position >= mData.size()) {
            onEnd();
            return null;
        }

        T removeData = mData.remove(position);
        int internalPosition = position + getPreDataCount();
        mAdapter.notifyItemRemoved(internalPosition);
        mAdapter.notifyItemRangeChanged(internalPosition, mData.size() - internalPosition);

        if (mCurrentMode == MODE_STANDARD) {
            final int size = mLevelData.size();
            int index = -1;
            for (int i = 0; i < size; i++) {
                LevelData<T> levelData = mLevelData.valueAt(i);
                if (levelData == null) {
                    continue;
                }
                T header = levelData.getHeader();
                if (header != null) {
                    index++;
                    if (index == position) {
                        levelData.removeHeader();
                        break;
                    }
                }
                List<T> data = levelData.getData();
                if (data != null) {
                    index += data.size();
                    if (position <= index) {
                        data.remove(removeData);
                        break;
                    }
                }
                T footer = levelData.getFooter();
                if (footer != null) {
                    index++;
                    if (index == position) {
                        levelData.removeFooter();
                        break;
                    }
                }
            }
        }
        onEnd();
        return removeData;
    }

    /**
     * 移除数据
     * 标准模式只支持当前level
     * 可以根据返回的集合配合{@link #addData(int, List)}实现展开闭合效果
     *
     * @param positionStart 位置
     * @param itemCount     移除长度
     * @return 返回被移除的集合
     */
    public List<T> removeData(int positionStart, @IntRange(from = 1) int itemCount) {
        if (itemCount == 1) {
            return Collections.singletonList(removeData(positionStart));
        }
        checkAdapterBind();
        if (!mIsCanRefresh) {
            return null;
        }
        mCurrentLevel = REFRESH_TYPE_OTHER;
        onStart();

        final int initSize = mData.size();
        if (positionStart >= initSize) {
            onEnd();
            return null;
        }

        List<T> removeData = new ArrayList<>();
        final int size = positionStart + itemCount >= initSize ? initSize : positionStart + itemCount;
        for (int i = positionStart; i < size; i++) {
            T remove = mData.remove(positionStart);
            removeData.add(remove);
        }
        final int removePosition = positionStart + getPreDataCount();
        mAdapter.notifyItemRangeRemoved(removePosition, itemCount);
        mAdapter.notifyItemRangeChanged(removePosition, mData.size() - removePosition);


        if (mCurrentMode == MODE_STANDARD) {
            final int levelSize = mLevelData.size();
            final int removeSize = removeData.size();
            int index = -1;
            for (int i = 0; i < levelSize; i++) {
                LevelData<T> levelData = mLevelData.valueAt(i);
                if (levelData == null) {
                    continue;
                }
                T header = levelData.getHeader();
                if (header != null) {
                    index++;
                    if (index >= positionStart && index < positionStart + removeSize) {
                        levelData.removeHeader();
                        if (index == positionStart + removeSize - 1) {
                            break;
                        }
                    }
                }
                List<T> data = levelData.getData();
                if (data != null) {
                    final int dataSize = data.size();
                    if (index >= positionStart && index < positionStart + removeSize) {
                        index += dataSize;
                        if (index < positionStart + removeSize) {
                            data.clear();
                            if (index == positionStart + removeSize - 1) {
                                break;
                            }
                        } else {
                            final int diff = index - (positionStart + removeSize - 1);
                            for (int j = 0, count = dataSize - diff; j < count; j++) {
                                data.remove(0);
                            }
                            break;
                        }
                    }
                    final int lastIndex = index + dataSize - 1;
                    if (lastIndex >= positionStart + removeSize - 1 && index <= positionStart + removeSize - 1) {
                        final int start = dataSize - (lastIndex - positionStart + 1);
                        for (int j = start, count = start + removeSize; j < count; j++) {
                            data.remove(start);
                        }
                        break;
                    } else if ((lastIndex < positionStart + removeSize - 1) && index <= positionStart + removeSize - 1) {
                        final int needRemoveSize = lastIndex - positionStart + 1;
                        final int start = dataSize - needRemoveSize;
                        for (int j = start, count = start + needRemoveSize; j < count; j++) {
                            data.remove(start);
                        }
                    }
                    index += dataSize;
                }
                T footer = levelData.getFooter();
                if (footer != null) {
                    index++;
                    if (index >= positionStart && index < positionStart + removeSize) {
                        levelData.removeFooter();
                        if (index == positionStart + removeSize - 1) {
                            break;
                        }
                    }
                }
            }
        }
        onEnd();
        return removeData;
    }

    /**
     * 修改数据
     *
     * @param position 位置
     * @param data     数据
     */
    public void setData(int position, @NonNull T data) {
        checkAdapterBind();

        if (!mIsCanRefresh) {
            return;
        }
        mCurrentLevel = REFRESH_TYPE_OTHER;
        onStart();

        if (mCurrentMode == MODE_STANDARD) {
            T currentData = mData.get(position);
            if (currentData == null) {
                onEnd();
                throw new DataException("The change data == null ?");
            }
            final int itemType = data.getItemType();
            if (currentData.getItemType() != itemType) {
                onEnd();
                throw new DataException("The data's itemType can't be set");
            }

            T oldData = mData.set(position, data);
            mAdapter.notifyItemChanged(position + getPreDataCount());
            boolean isHeader = itemType >= -HEADER_TYPE_DIFFER && itemType < 0;
            boolean isFooter = itemType >= -FOOTER_TYPE_DIFFER && itemType < -EMPTY_TYPE_DIFFER;
            LevelData<T> levelData = mLevelData.get(getLevelByPosition(position));
            if (levelData == null) {
                onEnd();
                return;
            }

            if (isHeader) {
                levelData.setHeader(data);
            } else if (isFooter) {
                levelData.setFooter(data);
            } else {
                List<T> list = levelData.getData();
                if (list == null || list.isEmpty()) {
                    onEnd();
                    return;
                }
                int oldIndex = list.indexOf(oldData);
                list.set(oldIndex, data);
            }
            onEnd();
            return;
        }
        mData.set(position, data);
        mAdapter.notifyItemChanged(position + getPreDataCount());
        onEnd();
    }

    /**
     * 插入数据 {@link RecyclerViewAdapterHelper#MODE_MIXED}
     * 可以配合{@link #removeData(int, int)}实现展开闭合效果
     *
     * @param position 位置
     * @param data     数据
     */
    public void addData(int position, @NonNull List<? extends T> data) {
        addData(position, data, DEFAULT_HEADER_LEVEL);
    }

    /**
     * 插入数据 {@link RecyclerViewAdapterHelper#MODE_STANDARD}
     * 可以配合{@link #removeData(int, int)}实现展开闭合效果
     *
     * @param position 位置
     * @param data     数据
     * @param level    level，必须添加level，不然无法确认添加的是什么level，存放在哪里
     */
    public void addData(int position, @NonNull List<? extends T> data, int level) {
        if (data.isEmpty()) {
            return;
        }

        if (position >= mData.size()) {
            addData(data);
            return;
        }

        checkAdapterBind();
        if (!mIsCanRefresh) {
            return;
        }
        mCurrentLevel = REFRESH_TYPE_OTHER;
        onStart();

        if (mCurrentMode == MODE_STANDARD) {
            // 不再校验用户传入的数据
            LevelData<T> levelData = mLevelData.get(level);
            if (levelData == null) {
                levelData = new LevelData<>(new ArrayList<T>(), null, null);
                mLevelData.put(level, levelData);
            }
            List<T> list = levelData.getData();
            if (list == null) {
                levelData.setData(new ArrayList<T>());
                list = levelData.getData();
            }

            boolean isHaveHeader = data.get(0).getItemType() == level - HEADER_TYPE_DIFFER;
            boolean isHaveFooter = data.get(data.size() - 1).getItemType() == level - FOOTER_TYPE_DIFFER;

            int positionStart = getPositionStart(level);
            if (positionStart == position) {
                if (isHaveHeader && levelData.getHeader() != null) {
                    onEnd();
                    throw new DataException("Please check the current showing data");
                }
            }

            if (position > positionStart) {
                if (isHaveHeader) {
                    onEnd();
                    throw new DataException("Please check the current showing data");
                }
            }

            if (isHaveFooter) {
                if (levelData.getFooter() != null) {
                    onEnd();
                    throw new DataException("Please check the current showing data");
                }
                if ((levelData.getHeader() == null ? 0 : 1) + positionStart + list.size() > position) {
                    onEnd();
                    throw new DataException("Please check the current showing data");
                }
            }

            if (isHaveHeader) {
                levelData.setHeader(data.remove(0));
            }

            if (isHaveFooter) {
                levelData.setFooter(data.remove(data.size() - 1));
            }

            list.addAll(isHaveHeader ? position - positionStart - 1 : position - positionStart, data);
        }
        mData.addAll(position, data);
        mAdapter.notifyItemRangeInserted(position + getPreDataCount(), data.size());
        onEnd();
    }

    /**
     * 添加数据集合 {@link RecyclerViewAdapterHelper#MODE_MIXED}
     *
     * @param newData 数据
     */
    public void addData(@NonNull List<? extends T> newData) {
        addData(newData, DEFAULT_HEADER_LEVEL);
    }

    /**
     * 添加数据集合 {@link RecyclerViewAdapterHelper#MODE_STANDARD}
     *
     * @param newData 数据
     * @param level   level，必须添加level，不然无法确认添加的是什么level，存放在哪里
     */
    public void addData(@NonNull List<? extends T> newData, int level) {
        if (newData.isEmpty()) {
            return;
        }
        checkAdapterBind();

        if (!mIsCanRefresh) {
            return;
        }
        mCurrentLevel = REFRESH_TYPE_OTHER;
        onStart();
        if (mCurrentMode == MODE_STANDARD) {
            LevelData<T> levelData = mLevelData.get(level);
            if (levelData == null) {
                levelData = new LevelData<>(new ArrayList<T>(), null, null);
                mLevelData.put(level, levelData);
            }
            List<T> list = levelData.getData();
            if (list == null) {
                levelData.setData(new ArrayList<T>());
                list = levelData.getData();
            }

            boolean isHaveHeader = newData.get(0).getItemType() == level - HEADER_TYPE_DIFFER;

            if (isHaveHeader) {
                if (levelData.getHeader() != null || levelData.getFooter() != null || !list.isEmpty()) {
                    onEnd();
                    throw new DataException("Please check the current showing data");
                }
            }

            if (levelData.getFooter() != null) {
                onEnd();
                throw new DataException("Please check the current showing data");
            }

            boolean isHaveFooter = newData.get(newData.size() - 1).getItemType() == level - FOOTER_TYPE_DIFFER;
            if (isHaveFooter) {
                levelData.setFooter(newData.remove(newData.size() - 1));
            }
            list.addAll(newData);
        }
        mData.addAll(newData);
        mAdapter.notifyItemRangeInserted(mData.size() - newData.size() + getPreDataCount(), newData.size());
        onEnd();
    }

    /**
     * 判断数据是否处于闭合状态
     *
     * @param level level
     * @return boolean true为已闭合状态，false为展开状态
     */
    public boolean isDataFolded(int level) {
        ResourcesManager.AttrEntity attrEntity = mResourcesManager.getAttrsEntity(level);
        return attrEntity.isFolded;
    }

    /**
     * 展开闭合
     *
     * @param level  level
     * @param isFold 是否闭合
     */
    public void foldType(int level, boolean isFold) {
        LevelData<T> levelData = getDataWithLevel(level);
        if (levelData == null) {
            return;
        }

        List<T> data = levelData.getData();
        if (data == null || data.isEmpty()) {
            return;
        }
        final int size = data.size();
        ResourcesManager.AttrEntity attrEntity = mResourcesManager.getAttrsEntity(level);
        if ((!attrEntity.isFolded || size <= attrEntity.minSize) && !isFold) {
            return;
        }

        if ((attrEntity.isFolded || size <= attrEntity.minSize) && isFold) {
            return;
        }

        checkAdapterBind();
        if (!mIsCanRefresh) {
            return;
        }
        mCurrentLevel = level;
        onStart();
        int headerCount = levelData.getHeader() == null ? 0 : 1;
        int positionStart = getPositionStart(level) + headerCount + attrEntity.minSize;
        List<T> handleData = data.subList(attrEntity.minSize, size);

        if (isFold) {
            mData.removeAll(handleData);
            int removePosition = positionStart + getPreDataCount();
            mAdapter.notifyItemRangeRemoved(removePosition, handleData.size());
            mAdapter.notifyItemRangeChanged(removePosition, mData.size() - removePosition);
            attrEntity.isFolded = true;
        } else {
            mData.addAll(positionStart, handleData);
            mAdapter.notifyItemRangeInserted(positionStart + getPreDataCount(), handleData.size());
            attrEntity.isFolded = false;
        }
        onEnd();
    }

    /**
     * 返回当前刷新模式
     *
     * @return RefreshMode
     */
    public int getCurrentRefreshMode() {
        return mCurrentMode;
    }

    /**
     * 返回当前刷新level
     *
     * @return RefreshLevel
     */
    public int getCurrentRefreshLevel() {
        return mCurrentLevel;
    }

    /**
     * 设置loadview的data缓存最大值
     *
     * @param maxDataCacheCount 缓存最大值
     */
    public void setMaxDataCacheCount(int maxDataCacheCount) {
        mMaxDataCacheCount = maxDataCacheCount;
    }

    /**
     * 设置loadview的header缓存最大值
     *
     * @param maxHeaderCacheCount 缓存最大值
     */
    public void setMaxSingleCacheCount(int maxHeaderCacheCount) {
        mMaxSingleCacheCount = maxHeaderCacheCount;
    }

    /**
     * 清除队列
     */
    public void clearQueue() {
        mRefreshQueue.clear();
    }

    /**
     * 获取数据前条目数量，保持数据与adapter一致
     * 比如adapter的position=0添加的是headerview
     * 如果你的adapter有这样的条件，请重写该方法或者自己处理数据的时候注意，否则数据混乱，甚至崩溃都是有可能的
     *
     * @return int
     */
    protected int getPreDataCount() {
        return PRE_DATA_COUNT;
    }

    /**
     * 返回比较的callback对象，提供新老数据
     * 默认比较id和type,id设为long是为了比较效率,你可以使用字符串的hashCode，注意冲突，甚至你可以自定义Callback
     *
     * @param oldData 老数据
     * @param newData 新数据
     * @return 返回Callback
     */
    protected DiffUtil.Callback getDiffCallBack(List<T> oldData, List<T> newData) {
        return new DiffCallBack<>(oldData, newData);
    }

    /**
     * @return 是否要移动
     */
    protected boolean isDetectMoves() {
        return true;
    }

    /**
     * 队列管理
     *
     * @param newData     data
     * @param newHeader   header
     * @param newFooter   footer
     * @param level       数据的等级
     * @param refreshType 类型
     */
    protected void notifyModuleChanged(List<T> newData, T newHeader, T newFooter, int level, int refreshType) {
        if (refreshType == REFRESH_NONE) {
            return;
        }

        boolean offer = mRefreshQueue.offer(new HandleBase<>(newData, newHeader, newFooter, level, refreshType));

        if (!mIsCanRefresh || !offer) {
            return;
        }
        mCurrentLevel = level;
        onStart();

        HandleBase<T> pollData = mRefreshQueue.poll();
        if (pollData != null) {
            startRefresh(pollData);
        }
    }

    /**
     * 重写此方法，实现同步或异步刷新
     *
     * @param refreshData 刷新数据合
     */
    protected abstract void startRefresh(HandleBase<T> refreshData);

    /**
     * 刷新生命周期-开始
     */
    @CallSuper
    protected void onStart() {
        mIsCanRefresh = false;
    }

    /**
     * 刷新生命周期-结束
     */
    @CallSuper
    protected void onEnd() {
        mIsCanRefresh = true;
        mCurrentLevel = REFRESH_TYPE_IDLE;
    }

    /**
     * 处理数据，通知RV刷新
     *
     * @param diffResult 返回的diffResult
     */
    protected final void handleResult(DiffUtil.DiffResult diffResult) {
        checkAdapterBind();

        diffResult.dispatchUpdatesTo(getListUpdateCallback(mAdapter));

        mData.clear();
        mData.addAll(mNewData);

        HandleBase<T> pollData = mRefreshQueue.poll();
        if (pollData != null) {
            startRefresh(pollData);
        } else {
            onEnd();
        }
    }

    /**
     * 提供刷新规则,可自定义
     *
     * @return ListUpdateCallback
     */
    protected ListUpdateCallback getListUpdateCallback(final RecyclerView.Adapter adapter) {

        final int preDataCount = getPreDataCount();

        return new ListUpdateCallback() {
            @Override
            public void onInserted(int position, int count) {
                adapter.notifyItemRangeInserted(position + preDataCount, count);
            }

            @Override
            public void onRemoved(int position, int count) {
                adapter.notifyItemRangeRemoved(position + preDataCount, count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                adapter.notifyItemMoved(fromPosition + preDataCount, toPosition + preDataCount);
            }

            @Override
            public void onChanged(int position, int count, Object payload) {
                adapter.notifyItemRangeChanged(position + preDataCount, count, payload);
            }
        };
    }

    /**
     * 核心部分，新老数据比较
     *
     * @param newData     新数据
     * @param newHeader   新头
     * @param level       数据类型等级
     * @param refreshType 刷新类型
     * @return DiffResult
     */
    protected final DiffUtil.DiffResult handleRefresh(List<T> newData, T newHeader, T newFooter, int level, int refreshType) {
        if (refreshType == REFRESH_ALL) {
            return DiffUtil.calculateDiff(getDiffCallBack(mData, newData), isDetectMoves());
        }

        checkStandardMode();

        mNewData.clear();
        mNewData.addAll(mData);

        LevelData<T> oldLevelData = mLevelData.get(level);
        List<T> data = null;
        T header = null;
        T footer = null;
        boolean isRefreshData = (refreshType & REFRESH_DATA) == REFRESH_DATA;
        boolean isRefreshHeader = (refreshType & REFRESH_HEADER) == REFRESH_HEADER;
        boolean isRefreshFooter = (refreshType & REFRESH_FOOTER) == REFRESH_FOOTER;

        if (oldLevelData != null) {
            List<T> oldItemData = oldLevelData.getData();
            if (oldItemData != null && !oldItemData.isEmpty()) {
                if (isRefreshData) {
                    mNewData.removeAll(oldItemData);
                } else {
                    data = oldItemData;
                }
            }

            T oldItemHeader = oldLevelData.getHeader();
            if (oldItemHeader != null) {
                if (isRefreshHeader) {
                    mNewData.remove(oldItemHeader);
                } else {
                    header = oldItemHeader;
                }
            }

            T oldItemFooter = oldLevelData.getFooter();
            if (oldItemFooter != null) {
                if (isRefreshFooter) {
                    mNewData.remove(oldItemFooter);
                } else {
                    footer = oldItemFooter;
                }
            }
        }

        int positionStart = getPositionStart(level);
        int dataSize = data == null ? 0 : data.size();

        if (!(newData == null || newData.isEmpty()) && isRefreshData) {
            data = newData;
            ResourcesManager.AttrEntity attrEntity = mResourcesManager.getAttrsEntity(level);
            if (!attrEntity.initState || data.size() <= attrEntity.minSize) {
                mNewData.addAll(header == null ? positionStart : positionStart + 1, data);
                dataSize = data.size();
            } else {
                mNewData.addAll(header == null ? positionStart : positionStart + 1, data.subList(0, attrEntity.minSize));
                dataSize = attrEntity.minSize;
            }
            attrEntity.isFolded = attrEntity.initState;
        }

        if (newHeader != null && isRefreshHeader) {
            header = newHeader;
            mNewData.add(positionStart, newHeader);
        }

        if (newFooter != null && isRefreshFooter) {
            footer = newFooter;
            if (header != null) {
                positionStart++;
            }
            positionStart += dataSize;
            mNewData.add(positionStart, footer);
        }
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(getDiffCallBack(mData, mNewData), isDetectMoves());
        mLevelData.put(level, new LevelData<>(data, header, footer));
        return result;
    }

    /**
     * 获取level的data展示部分的长度，主要是折叠的存在
     *
     * @param level int
     * @return int
     */
    public int getDataSize(int level) {
        LevelData<T> data = mLevelData.get(level);
        if (data == null) {
            return 0;
        }
        List<T> list = data.getData();
        if (list == null || list.isEmpty()) {
            return 0;
        }

        ResourcesManager.AttrEntity attrEntity = mResourcesManager.getAttrsEntity(level);
        int size = list.size();
        if (!attrEntity.isFolded || size <= attrEntity.minSize) {
            return size;
        }
        return attrEntity.minSize;
    }

    /**
     * 根据level获取该level第一个item的position
     *
     * @param level level
     * @return int
     */
    public int getLevelPositionStart(int level) {
        return getPositionStart(level);
    }

    private int getPositionStart(int level) {
        int sum = 0;
        final int length = mLevelData.size();
        for (int i = 0; i < length; i++) {
            int l = mLevelData.keyAt(i);
            if (l >= level) {
                break;
            }
            LevelData<T> data = mLevelData.valueAt(i);
            if (data == null) {
                continue;
            }
            if (data.getHeader() != null) {
                sum++;
            }
            if (data.getFooter() != null) {
                sum++;
            }
            List<T> list = data.getData();
            if (list == null || list.isEmpty()) {
                continue;
            }

            ResourcesManager.AttrEntity attrEntity = mResourcesManager.getAttrsEntity(l);
            int size = list.size();
            if (!attrEntity.isFolded || size <= attrEntity.minSize) {
                sum += size;
            } else {
                sum += attrEntity.minSize;
            }
        }
        return sum;
    }

    /**
     * 获取layoutId
     *
     * @param viewType 数据类型
     * @return layoutId
     */
    public final int getLayoutId(int viewType) {
        return mResourcesManager.getLayoutId(viewType);
    }


    /**
     * 根据position获取对应level
     *
     * @param position position
     * @return level
     */
    public int getLevelByPosition(int position) {
        final int size = mLevelData.size();
        int index = -1;
        for (int i = 0; i < size; i++) {
            LevelData<T> levelData = mLevelData.valueAt(i);
            if (levelData == null) {
                continue;
            }
            T header = levelData.getHeader();
            final int level = mLevelData.keyAt(i);
            if (header != null) {
                index++;
                if (index == position) {
                    return level;
                }
            }
            List<T> data = levelData.getData();
            if (data != null) {
                index += data.size();
                if (position <= index) {
                    return level;
                }
            }
            T footer = levelData.getFooter();
            if (footer != null) {
                index++;
                if (index == position) {
                    return level;
                }
            }
        }
        return DEFAULT_HEADER_LEVEL;
    }

    /**
     * 根据内部item获取对应level
     *
     * @param item 内部item
     * @return level
     */
    public int getLevelByItem(@NonNull T item) {
        final int size = mLevelData.size();
        for (int i = 0; i < size; i++) {
            LevelData<T> levelData = mLevelData.valueAt(i);
            if (levelData == null) {
                continue;
            }
            T header = levelData.getHeader();
            final int level = mLevelData.keyAt(i);
            if (header != null) {
                if (header == item) {
                    return level;
                }
            }
            List<T> data = levelData.getData();
            if (data != null) {
                for (T d : data) {
                    if (d == item) {
                        return level;
                    }
                }
            }
            T footer = levelData.getFooter();
            if (footer != null) {
                if (footer == item) {
                    return level;
                }
            }
        }
        return DEFAULT_HEADER_LEVEL;
    }

    /**
     * 创建loading的data
     *
     * @param level     数据类型等级
     * @param dataCount 显示条目数
     * @return 根据type产生loading的数据集合
     */
    private List<T> createLoadingData(int level, @IntRange(from = 1) int dataCount) {
        checkLoadingAdapterBind();

        if (mDataCache == null) {
            mDataCache = new LruCache<>(mMaxDataCacheCount);
        }

        final String dataKey = getDataKey(level);
        List<T> datas = mDataCache.get(dataKey);

        if (datas == null) {
            datas = new ArrayList<>();
            mDataCache.put(dataKey, datas);
        }

        final int size = datas.size();
        int diffSize = dataCount - size;
        if (diffSize > 0) {
            for (int i = 0; i < diffSize; i++) {
                datas.add(mLoadingEntityAdapter.createLoadingEntity(level - LOADING_DATA_TYPE_DIFFER, level));
            }
        } else if (diffSize < 0) {
            datas = datas.subList(0, dataCount);
        }

        for (int i = 0; i < dataCount; i++) {
            mLoadingEntityAdapter.bindLoadingEntity(datas.get(i), i);
        }
        return datas;
    }

    /**
     * 创建loading的header
     *
     * @param level 数据类型等级
     * @return 根据type产生loading的头
     */
    private T createLoadingHeader(int level) {
        checkLoadingAdapterBind();

        if (mSingleCache == null) {
            mSingleCache = new LruCache<>(mMaxSingleCacheCount);
        }

        final String headerKey = getHeaderKey(level);
        T header = mSingleCache.get(headerKey);

        if (header == null) {
            header = mLoadingEntityAdapter.createLoadingHeaderEntity(level - LOADING_HEADER_TYPE_DIFFER, level);
            mSingleCache.put(headerKey, header);
        }

        mLoadingEntityAdapter.bindLoadingEntity(header, -1);
        return header;
    }

    /**
     * 初始化全局的loading数据
     *
     * @param loadingConfig 配置项
     */
    public void initGlobalLoadingConfig(@NonNull LoadingConfig loadingConfig) {
        SparseArray<LoadingConfigEntity> configs = loadingConfig.getConfigs();
        int size = configs.size();
        if (size == 0) {
            return;
        }

        if (mLoadingLevelData == null) {
            mLoadingLevelData = new SparseArray<>();
        } else {
            mLoadingLevelData.clear();
        }

        for (int i = 0; i < size; i++) {
            LoadingConfigEntity entity = configs.valueAt(i);
            int level = configs.keyAt(i);
            T header = null;
            List<T> data = null;
            if (entity.isHaveHeader) {
                header = createLoadingHeader(level);
            }

            if (entity.count > 0) {
                data = new ArrayList<>(createLoadingData(level, entity.count));
            }
            LevelData<T> levelData = new LevelData<>(data, header, null);
            mLoadingLevelData.put(level, levelData);
        }
    }

    /**
     * 检查是否为标准模式
     */
    private void checkStandardMode() {
        if (mCurrentMode == MODE_MIXED) {
            onEnd();
            mRefreshQueue.clear();
            throw new RefreshException("Current refresh mode can't support random refresh mode !");
        }
    }

    /**
     * 检查是否为随机模式
     */
    private void checkMixedMode() {
        if (mCurrentMode == MODE_STANDARD) {
            onEnd();
            throw new RefreshException("Current refresh mode can't support standard refresh mode !");
        }
    }

    /**
     * 检查{@link #mAdapter}是否为绑定
     * 如果未绑定，请调用{@link #bindAdapter(RecyclerView.Adapter)}
     */
    private void checkAdapterBind() {
        if (mAdapter == null) {
            onEnd();
            throw new AdapterBindException("Are you sure bind the adapter ?");
        }
    }

    /**
     * 检查{@link LoadingEntityAdapter}是否为绑定
     * 如果未绑定，请调用{@link #setLoadingAdapter(LoadingEntityAdapter)}
     */
    private void checkLoadingAdapterBind() {
        if (mLoadingEntityAdapter == null) {
            onEnd();
            throw new AdapterBindException("Are you sure bind the loading adapter ?");
        }
    }

    /**
     * 检查{@link EmptyEntityAdapter}是否为绑定
     * 如果未绑定，请调用{@link #setEmptyAdapter(EmptyEntityAdapter)}
     */
    private void checkEmptyAdapterBind() {
        if (mEmptyEntityAdapter == null) {
            onEnd();
            throw new AdapterBindException("Are you sure bind the empty adapter ?");
        }
    }

    /**
     * 检查{@link ErrorEntityAdapter}是否为绑定
     * 如果未绑定，请调用{@link #setErrorAdapter(ErrorEntityAdapter)}
     */
    private void checkErrorAdapterBind() {
        if (mErrorEntityAdapter == null) {
            onEnd();
            throw new AdapterBindException("Are you sure bind the error adapter ?");
        }
    }

    private String getDataKey(int level) {
        return String.format(Locale.getDefault(), "data_%d", level);
    }

    private String getHeaderKey(int level) {
        return String.format(Locale.getDefault(), "header_%d", level);
    }

    private String getEmptyKey(int level) {
        return String.format(Locale.getDefault(), "empty_%d", level);
    }

    private String getErrorKey(int level) {
        return String.format(Locale.getDefault(), "error_%d", level);
    }

    /**
     * 释放资源，谨慎使用，资源释放之后，需要重新注册，不然会引起异常，最好跟主容器绑定生命周期
     */
    public void release() {
        mRefreshQueue.clear();
        mNewData.clear();
        mData.clear();
        mLoadingLevelData.clear();
        mLoadingLevelData = null;
        mSingleCache.evictAll();
        mSingleCache = null;
        mDataCache.evictAll();
        mDataCache = null;
        mLevelData.clear();
        mResourcesManager.release();
    }
}
