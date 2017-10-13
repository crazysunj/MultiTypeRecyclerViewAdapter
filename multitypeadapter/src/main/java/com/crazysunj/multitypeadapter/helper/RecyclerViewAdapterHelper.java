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

import android.support.annotation.CallSuper;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.util.ListUpdateCallback;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.LruCache;
import android.util.SparseArray;
import android.util.SparseIntArray;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * description
 * 如果您发现哪里性能比较差或者说设计不合理，希望您能反馈给我
 * email:twsunj@gmail.com
 * type 取值范围
 * data类型 [0,1000)
 * header类型 [-1000,0)
 * loading-data类型 [-2000,-1000)
 * loading-header类型 [-3000,-2000)
 * error类型 [-4000,-3000)
 * empty类型 [-5000,-4000)
 * footer类型 [-6000,-5000)
 * Created by sunjian on 2017/5/4.
 */

public abstract class RecyclerViewAdapterHelper<T extends MultiTypeEntity, A extends RecyclerView.Adapter> {

    protected static final String TAG = RecyclerViewAdapterHelper.class.getSimpleName();

    //标准刷新，相应的type集合在一起
    public static final int MODE_STANDARD = 0;
    //随机模式，操作一般的数据
    public static final int MODE_MIXED = 1;

    @IntDef({MODE_STANDARD, MODE_MIXED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface RefreshMode {
    }

    //闲置状态
    public static final int REFRESH_TYPE_IDLE = -1;
    //刷新数据全部
    public static final int REFRESH_TYPE_DATA_ALL = -2;
    //刷新loading全部
    public static final int REFRESH_TYPE_LOAD_ALL = -3;
    //清除操作
    public static final int REFRESH_TYPE_CLEAR = -4;
    //其他
    public static final int REFRESH_TYPE_OTHER = -5;

    //默认数量为0
    private static final int PRE_DATA_COUNT = 0;

    //header类型差值
    public static final int HEADER_TYPE_DIFFER = 1000;
    //loading-data类型差值
    public static final int LOADING_DATA_TYPE_DIFFER = 2000;
    //loading-header类型差值
    public static final int LOADING_HEADER_TYPE_DIFFER = 3000;
    //error类型差值
    public static final int ERROR_TYPE_DIFFER = 4000;
    //empty类型差值
    public static final int EMPTY_TYPE_DIFFER = 5000;
    //footer类型差值
    public static final int FOOTER_TYPE_DIFFER = 6000;

    //默认viewType
    public static final int DEFAULT_VIEW_TYPE = 0;
    //默认头的level，处理数据只跟type有关系
    static final int DEFAULT_HEADER_LEVEL = -1;
    //只刷新头 001
    private static final int REFRESH_HEADER = 1;
    //只刷新底 010
    private static final int REFRESH_FOOTER = 2;
    //同时刷新头和底 011
    private static final int REFRESH_HEADER_FOOTER = 3;
    //只刷新数据 100
    private static final int REFRESH_DATA = 4;
    //同时刷新数据和头 101
    private static final int REFRESH_HEADER_DATA = 5;
    //同时刷新数据和底 110
    private static final int REFRESH_FOOTER_DATA = 6;
    //同时刷新头，底，数据 111
    private static final int REFRESH_HEADER_FOOTER_DATA = 7;
    //刷新全部
    private static final int REFRESH_ALL = 8;

    //控制麒麟臂用户
    private boolean mIsCanRefresh = true;
    //随机id的默认最大值
    private long mMaxRandomId = -1;
    //随机id的默认最小值
    private long mMinRandomId = Long.MIN_VALUE;
    //loadingview关于数据方面的最大缓存值
    private int mMaxDataCacheCount = 12;
    //loadingview关于头方面的最大缓存值
    private int mMaxSingleCacheCount = 6;

    //根据level存储的数据
    private SparseArray<LevelData<T>> mLevelOldData;
    //数据的缓存
    private LruCache<String, List<T>> mDataCache;
    //头的缓存
    private LruCache<String, T> mSingleCache;
    //老数据
    protected List<T> mNewData;
    //当前数据
    protected List<T> mData;
    //绑定Adapter
    protected A mAdapter;
    //资源管理
    private ResourcesManager mResourcesManager;
    //刷新队列，支持高频率刷新
    private Queue<HandleBase<T>> mRefreshQueue;
    //全局loading刷新数据
    private ArrayList<T> mGlobalLoadingEntitys;

    //当前模式
    private int mCurrentMode;
    //当前刷新Type
    private int mCurrentType;

    public RecyclerViewAdapterHelper(List<T> data) {
        this(data, MODE_STANDARD);
    }

    public RecyclerViewAdapterHelper(List<T> data, @RefreshMode int mode) {

        mData = data == null ? new ArrayList<T>() : data;
        mCurrentMode = mode;

        if (mNewData == null) {
            mNewData = new ArrayList<T>();
        }

        if (mLevelOldData == null) {
            mLevelOldData = new SparseArray<LevelData<T>>();
        }

        if (mResourcesManager == null) {
            mResourcesManager = new ResourcesManager();
        }

        if (mRefreshQueue == null) {
            mRefreshQueue = new LinkedList<HandleBase<T>>();
        }

        registerMoudle();

        if (mCurrentMode == MODE_STANDARD && !mData.isEmpty()) {
            List<T> newData = initStandardNewData(mData);
            mData.clear();
            mData.addAll(newData);
        }
    }

    /**
     * 绑定adapter
     *
     * @param adapter 绑定adapter
     */
    public void bindAdapter(A adapter) {
        mAdapter = adapter;
    }

    /**
     * 返回绑定adapter
     *
     * @return RecyclerView.Adapter
     */
    public A getBindAdapter() {
        return mAdapter;
    }

    private LoadingEntityAdapter<T> mLoadingEntityAdapter;//加载实体类适配器

    /**
     * 设置加载实体类适配器
     *
     * @param adapter 适配器
     */
    public void setLoadingAdapter(LoadingEntityAdapter<T> adapter) {
        mLoadingEntityAdapter = adapter;
    }

    private EmptyEntityAdapter<T> mEmptyEntityAdapter;//空实体类适配器

    /**
     * 设置空实体类适配器
     *
     * @param adapter 适配器
     */
    public void setEmptyAdapter(EmptyEntityAdapter<T> adapter) {
        mEmptyEntityAdapter = adapter;
    }

    private ErrorEntityAdapter<T> mErrorEntityAdapter;//错误实体类适配器

    /**
     * 设置错误实体类适配器
     *
     * @param adapter 适配器
     */
    public void setErrorAdapter(ErrorEntityAdapter<T> adapter) {
        mErrorEntityAdapter = adapter;
    }

    /**
     * 根据索引返回type
     *
     * @param position 索引
     * @return type
     */
    public int getItemViewType(int position) {

        T item = mData.get(position);
        if (item != null) {
            return item.getItemType();
        }
        return DEFAULT_VIEW_TYPE;
    }

    /**
     * 链式注册moudle
     *
     * @param type 数据类型
     * @return TypesManager
     */
    public ResourcesManager.TypesManager registerMoudle(@IntRange(from = 0, to = 999) int type) {
        return mResourcesManager.type(type);
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
     * 传头部的type是拿不到数据的
     *
     * @param type 数据类型
     * @return 获取LevelData
     */
    public LevelData<T> getDataWithType(int type) {
        return mLevelOldData.get(getLevel(type));
    }

    /**
     * 设置随机id的最大值
     *
     * @param maxRandomId 随机id的最大值
     */
    public void setMaxRandomId(long maxRandomId) {
        mMaxRandomId = maxRandomId;
    }

    /**
     * 设置随机id的最小值
     *
     * @param minRandomId 随机id的最小值
     */
    public void setMinRandomId(long minRandomId) {
        mMinRandomId = minRandomId;
    }

    /**
     * 不断向下取值，如果抛异常，你可以重新设置最大值，但切记不能重复啊，报错我可不负责啊，这么多的值都用完了，是在下输了
     * 有特殊要求的同学可以自己设计
     * 是不是随机有待商量
     * 注意并发问题
     *
     * @return 返回不重复Id
     */
    public long getRandomId() {
        if (mMaxRandomId <= mMinRandomId) {
            throw new DataException("boy,you win !");
        }
        return mMaxRandomId--;
    }

    /**
     * 全局初始化的loading集合进行刷新
     */
    public void notifyLoadingChanged() {

        if (mGlobalLoadingEntitys == null) return;

        mNewData.clear();
        mLevelOldData.clear();
        for (T entity : mGlobalLoadingEntitys) {
            final int itemType = entity.getItemType();
            int level;
            if (itemType >= -LOADING_DATA_TYPE_DIFFER && itemType < -HEADER_TYPE_DIFFER) {
                level = getLevel(itemType + LOADING_DATA_TYPE_DIFFER);
                LevelData<T> levelData = mLevelOldData.get(level);
                List<T> data;
                if (levelData == null) {
                    data = new ArrayList<T>();
                    levelData = new LevelData<T>(data, null, null);
                    mLevelOldData.put(level, levelData);
                } else {
                    data = levelData.getData();
                    if (data == null) {
                        data = new ArrayList<T>();
                        levelData.setData(data);
                    }
                }
                data.add(entity);
            } else if (itemType >= -LOADING_HEADER_TYPE_DIFFER && itemType < -LOADING_DATA_TYPE_DIFFER) {
                level = getLevel(itemType + LOADING_HEADER_TYPE_DIFFER);
                LevelData<T> levelData = mLevelOldData.get(level);
                if (levelData == null) {
                    levelData = new LevelData<T>(null, entity, null);
                    mLevelOldData.put(level, levelData);
                } else {
                    levelData.setHeader(entity);
                }
            } else {
                throw new DataException("are you sure the ture type ?");
            }
        }
        mNewData.addAll(mGlobalLoadingEntitys);

        notifyMoudleChanged(mNewData, null, null, REFRESH_TYPE_LOAD_ALL, REFRESH_ALL);
    }

    /**
     * 全局初始化的loading集合根据type进行刷新
     *
     * @param type 数据类型
     */
    public void notifyLoadingChanged(int type) {

        if (mGlobalLoadingEntitys == null) return;

        final int loadingType = type - LOADING_DATA_TYPE_DIFFER;
        final int loadingHeaderType = type - LOADING_HEADER_TYPE_DIFFER;
        List<T> loadingEntitys = new ArrayList<T>();
        boolean isHaveHeader = false;
        for (T entity : mGlobalLoadingEntitys) {
            int itemType = entity.getItemType();

            if (itemType == loadingType) {
                loadingEntitys.add(entity);
                continue;
            }

            if (itemType == loadingHeaderType) {
                isHaveHeader = true;
                loadingEntitys.add(entity);
            }
        }
        T header = isHaveHeader ? loadingEntitys.remove(0) : null;
        notifyMoudleChanged(loadingEntitys, header, null, type, REFRESH_HEADER_FOOTER_DATA);
    }

    /**
     * 务必在调用之前确定缓存最大值，可调用setMaxHeaderCacheCount和setMaxHeaderCacheCount
     * 对应notifyMoudleDataChanged
     *
     * @param type      数据类型
     * @param dataCount 刷新条目数
     */
    public void notifyLoadingDataChanged(int type, @IntRange(from = 1) int dataCount) {

        List<T> datas = createLoadingData(type, dataCount);

        notifyMoudleChanged(datas, null, null, type, REFRESH_FOOTER_DATA);
    }

    public void notifyMoudleDataInserted(T data, int type) {
        notifyMoudleDataInserted(Collections.singletonList(data), type);
    }

    /**
     * 在原来基础上添加一个数据集合
     *
     * @param data 添加数据集
     * @param type 添加数据类型
     */
    public void notifyMoudleDataInserted(List<? extends T> data, int type) {
        final int level = getLevel(type);
        LevelData<T> levelData = mLevelOldData.get(level);
        if (levelData == null) {
            levelData = new LevelData<T>(new ArrayList<T>(), null, null);
            mLevelOldData.put(level, levelData);
        }
        List<T> list = levelData.getData();
        if (list == null) {
            list = new ArrayList<T>();
            levelData.setData(list);
        }
        list.addAll(data);
        notifyMoudleChanged(list, levelData.getHeader(), levelData.getFooter(), type, REFRESH_HEADER_FOOTER_DATA);
    }

    /**
     * 只刷新数据
     *
     * @param data 数据
     * @param type 数据类型
     */
    @SuppressWarnings("unchecked")
    public void notifyMoudleDataChanged(List<? extends T> data, int type) {
        notifyMoudleChanged((List<T>) data, null, null, type, REFRESH_DATA);
    }

    public void notifyMoudleDataChanged(T data, int type) {
        notifyMoudleChanged(Collections.singletonList(data), null, null, type, REFRESH_DATA);
    }

    /**
     * 务必在调用之前确定缓存最大值，可调用setMaxHeaderCacheCount和setMaxHeaderCacheCount
     * 对应notifyMoudleHeaderChanged
     *
     * @param type 数据类型
     */
    public void notifyLoadingHeaderChanged(int type) {

        T header = createLoadingHeader(type);

        notifyMoudleChanged(null, header, null, type, REFRESH_HEADER_FOOTER);
    }

    /**
     * 只刷新头
     *
     * @param header 头
     * @param type   数据类型
     */
    public void notifyMoudleHeaderChanged(T header, int type) {
        notifyMoudleChanged(null, header, null, type, REFRESH_HEADER);
    }

    /**
     * 只刷新底
     *
     * @param footer 底
     * @param type   数据类型
     */
    public void notifyMoudleFooterChanged(T footer, int type) {
        notifyMoudleChanged(null, null, footer, type, REFRESH_FOOTER);
    }

    /**
     * 同时刷新头和底
     *
     * @param header 头
     * @param footer 底
     * @param type   数据类型
     */
    public void notifyMoudleHeaderAndFooterChanged(T header, T footer, int type) {
        notifyMoudleChanged(null, header, footer, type, REFRESH_HEADER_FOOTER);
    }

    /**
     * 务必在调用之前确定缓存最大值，可调用setMaxHeaderCacheCount和setMaxHeaderCacheCount
     * 对应notifyMoudleDataAndHeaderChanged
     *
     * @param type      数据类型
     * @param dataCount 刷新条目数
     */
    public void notifyLoadingDataAndHeaderChanged(int type, @IntRange(from = 1) int dataCount) {

        T header = createLoadingHeader(type);
        List<T> datas = createLoadingData(type, dataCount);

        notifyMoudleChanged(datas, header, null, type, REFRESH_HEADER_FOOTER_DATA);
    }


    /**
     * 同时刷新头和数据
     *
     * @param data   数据
     * @param header 头
     * @param type   数据类型
     */
    @SuppressWarnings("unchecked")
    public void notifyMoudleDataAndHeaderChanged(List<? extends T> data, T header, int type) {
        notifyMoudleChanged((List<T>) data, header, null, type, REFRESH_HEADER_DATA);
    }

    /**
     * 同时刷新底和数据
     *
     * @param data   数据
     * @param footer 底
     * @param type   数据类型
     */
    @SuppressWarnings("unchecked")
    public void notifyMoudleDataAndFooterChanged(List<? extends T> data, T footer, int type) {
        notifyMoudleChanged((List<T>) data, null, footer, type, REFRESH_FOOTER_DATA);
    }

    /**
     * 同时刷新头和数据
     *
     * @param data   数据
     * @param header 头
     * @param type   数据类型
     */
    public void notifyMoudleDataAndHeaderChanged(T data, T header, int type) {
        notifyMoudleChanged(Collections.singletonList(data), header, null, type, REFRESH_HEADER_DATA);
    }

    /**
     * 同时刷新底和数据
     *
     * @param data   数据
     * @param footer 底
     * @param type   数据类型
     */
    public void notifyMoudleDataAndFooterChanged(T data, T footer, int type) {
        notifyMoudleChanged(Collections.singletonList(data), null, footer, type, REFRESH_FOOTER_DATA);
    }

    /**
     * 同时刷新头和底和数据
     *
     * @param header 头
     * @param data   数据
     * @param footer 底
     * @param type   数据类型
     */
    @SuppressWarnings("unchecked")
    public void notifyMoudleDataAndHeaderAndFooterChanged(T header, List<? extends T> data, T footer, int type) {
        notifyMoudleChanged((List<T>) data, header, footer, type, REFRESH_HEADER_FOOTER_DATA);
    }

    /**
     * 同时刷新头和底和数据
     *
     * @param header 头
     * @param data   数据
     * @param footer 底
     * @param type   数据类型
     */
    public void notifyMoudleDataAndHeaderAndFooterChanged(T header, T data, T footer, int type) {
        notifyMoudleChanged(Collections.singletonList(data), header, footer, type, REFRESH_HEADER_FOOTER_DATA);
    }

    /**
     * 刷新相应类型空界面
     * 如果刷新没有跟数据属性关联，notifyMoudleEmptyChanged(int type)
     *
     * @param emptyData 空数据
     * @param type      空数据类型
     */
    public void notifyMoudleEmptyChanged(T emptyData, int type) {

        if (emptyData.getItemType() == type - EMPTY_TYPE_DIFFER) {
            notifyMoudleChanged(Collections.singletonList(emptyData), null, null, type, REFRESH_HEADER_FOOTER_DATA);
        } else {
            throw new DataException("please set correct itemType!");
        }
    }

    public void notifyMoudleEmptyChanged(int type) {

        if (mSingleCache == null) {
            mSingleCache = new LruCache<String, T>(mMaxSingleCacheCount);
        }

        T emptyEntity = mSingleCache.get(getEmptyKey(type));

        if (emptyEntity == null) {
            checkEmptyAdapterBind();
            emptyEntity = mEmptyEntityAdapter.createEmptyEntity(type);
        }
        notifyMoudleEmptyChanged(emptyEntity, type);
    }

    /**
     * 刷新相应类型错误界面
     * 如果刷新没有跟数据属性关联，那么调用notifyMoudleErrorChanged(int type)
     *
     * @param errorData 错误数据
     * @param type      错误数据类型
     */
    public void notifyMoudleErrorChanged(T errorData, int type) {

        if (errorData.getItemType() == type - ERROR_TYPE_DIFFER) {
            notifyMoudleChanged(Collections.singletonList(errorData), null, null, type, REFRESH_HEADER_FOOTER_DATA);
        } else {
            throw new DataException("please set correct itemType!");
        }
    }

    public void notifyMoudleErrorChanged(int type) {

        if (mSingleCache == null) {
            mSingleCache = new LruCache<String, T>(mMaxSingleCacheCount);
        }

        T errorEntity = mSingleCache.get(getErrorKey(type));

        if (errorEntity == null) {
            checkErrorAdapterBind();
            errorEntity = mErrorEntityAdapter.createErrorEntity(type);
        }
        notifyMoudleErrorChanged(errorEntity, type);
    }

    /**
     * 刷新全局数据并切换刷新模式
     * <p>
     * 默认为当前刷新模式
     *
     * @param newData     新数据集合
     * @param refreshMode 刷新模式
     */
    @SuppressWarnings("unchecked")
    public void notifyDataSetChanged(List<? extends T> newData, @RefreshMode int refreshMode) {

        checkAdapterBind();

        if (!mIsCanRefresh) {
            return;
        }
        mCurrentType = REFRESH_TYPE_DATA_ALL;
        onStart();
        mData.clear();

        if (refreshMode == MODE_STANDARD) {
            mData.addAll(initStandardNewData((List<T>) newData));
        } else {
            mData.addAll(newData);
        }
        mAdapter.notifyDataSetChanged();

        mCurrentMode = refreshMode;
        onEnd();
    }


    /**
     * 默认标准刷新
     *
     * @param newData 新数据集合
     */
    public void notifyDataSetChanged(List<? extends T> newData) {
        notifyDataSetChanged(newData, mCurrentMode);
    }

    /**
     * 全局刷新
     */
    public void notifyDataSetChanged() {

        checkAdapterBind();
        if (!mIsCanRefresh) {
            return;
        }
        mCurrentType = REFRESH_TYPE_DATA_ALL;
        onStart();
        mAdapter.notifyDataSetChanged();
        onEnd();
    }


    /**
     * 数据比较后刷新，可支持异步，与Type刷新用法一样
     * 同notifyDataSetChanged可切换模式
     * <p>
     * 默认为当前刷新模式
     *
     * @param newData     刷新数据集合
     * @param refreshMode 刷新模式
     */
    @SuppressWarnings("unchecked")
    public void notifyDataByDiff(List<? extends T> newData, @RefreshMode int refreshMode) {

        mNewData.clear();

        if (refreshMode == MODE_STANDARD) {
            mNewData.addAll(initStandardNewData((List<T>) newData));
        } else {
            mNewData.addAll(newData);
        }

        notifyMoudleChanged(mNewData, null, null, REFRESH_TYPE_DATA_ALL, REFRESH_ALL);

        mCurrentMode = refreshMode;

    }

    public void notifyDataByDiff(List<? extends T> newData) {
        notifyDataByDiff(newData, mCurrentMode);
    }


    /**
     * 清除type数组中的数据
     *
     * @param type 数据类型
     */
    public void clearMoudle(int... type) {

        checkStandardMode();

        if (type == null || type.length == 0) {
            return;
        }

        for (int i : type) {
            int level = getLevel(i);
            mLevelOldData.remove(level);
        }

        mNewData.clear();
        for (int i = 0, size = mLevelOldData.size(); i < size; i++) {
            LevelData<T> levelData = mLevelOldData.valueAt(i);
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

        notifyMoudleChanged(mNewData, null, null, REFRESH_TYPE_DATA_ALL, REFRESH_ALL);
    }

    /**
     * 只剩下type数组中的数据
     *
     * @param type 数据类型
     */
    public void remainMoudle(int... type) {

        checkStandardMode();

        if (type == null || type.length == 0) {
            return;
        }

        SparseArray<LevelData<T>> temData = new SparseArray<LevelData<T>>();
        for (int i : type) {
            int level = getLevel(i);
            LevelData<T> levelData = mLevelOldData.get(level);
            if (levelData == null) {
                continue;
            }
            temData.put(level, levelData);
        }

        mNewData.clear();
        mLevelOldData.clear();
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
            mLevelOldData.put(level, levelData);
        }

        notifyMoudleChanged(mNewData, null, null, REFRESH_TYPE_DATA_ALL, REFRESH_ALL);
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
        mCurrentType = REFRESH_TYPE_CLEAR;
        onStart();

        mData.clear();

        mLevelOldData.clear();

        mAdapter.notifyDataSetChanged();

        onEnd();
    }

    /**
     * 在position位置添加数据
     * 标准模式只支持data的添加，如果你要添加其它，完全可以调标准模式的方法
     *
     * @param position 添加的位置
     */
    public void addData(int position, @NonNull T data) {

        checkAdapterBind();

        if (!mIsCanRefresh) {
            return;
        }
        mCurrentType = REFRESH_TYPE_OTHER;
        onStart();

        if (mCurrentMode == MODE_STANDARD) {
            T t = mData.get(position);
            int itemType = t.getItemType();
            int pos;
            if (itemType >= -HEADER_TYPE_DIFFER && itemType < 0) {
                pos = position + 1;
                itemType = itemType + HEADER_TYPE_DIFFER;
                if (itemType != data.getItemType()) {
                    onEnd();
                    throw new DataException("please check add data");
                }
                final int level = getLevel(itemType);
                LevelData<T> levelData = mLevelOldData.get(level);
                if (levelData == null) {
                    //当前有header，因此不可能为空
                    levelData = new LevelData<T>(new ArrayList<T>(), null, null);
                    mLevelOldData.put(level, levelData);
                }
                List<T> list = levelData.getData();
                if (list == null) {
                    list = new ArrayList<T>();
                    levelData.setData(list);
                }
                list.add(0, data);
            } else if (itemType >= -FOOTER_TYPE_DIFFER && itemType < -EMPTY_TYPE_DIFFER) {
                pos = position;
                itemType = itemType + FOOTER_TYPE_DIFFER;
                if (itemType != data.getItemType()) {
                    onEnd();
                    throw new DataException("please check add data");
                }
                final int level = getLevel(itemType);
                LevelData<T> levelData = mLevelOldData.get(level);
                if (levelData == null) {
                    //当前有footer，因此不可能为空
                    levelData = new LevelData<T>(new ArrayList<T>(), null, null);
                    mLevelOldData.put(level, levelData);
                }
                List<T> list = levelData.getData();
                if (list == null) {
                    list = new ArrayList<T>();
                    levelData.setData(list);
                }
                list.add(data);
            } else if (itemType >= 0) {
                pos = position;
                if (itemType != data.getItemType()) {
                    onEnd();
                    throw new DataException("please check add data");
                }
                final int level = getLevel(itemType);
                int positionStart = getPositionStart(level);
                LevelData<T> levelData = mLevelOldData.get(level);
                if (levelData == null) {
                    //当前有data，因此不可能为空
                    levelData = new LevelData<T>(new ArrayList<T>(), null, null);
                    mLevelOldData.put(level, levelData);
                }
                List<T> list = levelData.getData();
                if (list == null) {
                    list = new ArrayList<T>();
                    levelData.setData(list);
                }

                T header = levelData.getHeader();
                int addPosition = header == null ? pos - positionStart : pos - positionStart - 1;
                list.add(addPosition, data);
            } else {
                onEnd();
                throw new DataException("please check the current showing data");
            }

            position = pos;
        }
        mData.add(position, data);
        mAdapter.notifyItemInserted(position + getPreDataCount());
        compatibilityDataSizeChanged(1);
        onEnd();
    }

    /**
     * 添加数据
     * 标准模式只支持data的添加，如果你要添加其它，完全可以调标准模式的方法
     *
     * @param data 添加的数据
     */
    public void addData(T data) {

        checkAdapterBind();

        if (!mIsCanRefresh) {
            return;
        }
        mCurrentType = REFRESH_TYPE_OTHER;
        onStart();

        if (mCurrentMode == MODE_STANDARD) {
            int position = mData.size() - 1;
            T t = mData.get(position);
            int itemType = t.getItemType();
            int pos;
            if (itemType >= -HEADER_TYPE_DIFFER && itemType < 0) {
                pos = position + 1;
                itemType = itemType + HEADER_TYPE_DIFFER;
            } else if (itemType >= -FOOTER_TYPE_DIFFER && itemType < -EMPTY_TYPE_DIFFER) {
                pos = position;
                itemType = itemType + FOOTER_TYPE_DIFFER;
            } else if (itemType >= 0) {
                pos = position;
            } else {
                onEnd();
                throw new DataException("please check the current showing data");
            }

            if (itemType != data.getItemType()) {
                onEnd();
                throw new DataException("please check add data");
            }
            position = pos + 1;
            final int level = getLevel(itemType);
            LevelData<T> levelData = mLevelOldData.get(level);
            if (levelData == null) {
                levelData = new LevelData<T>(new ArrayList<T>(), null, null);
                mLevelOldData.put(level, levelData);
            }
            List<T> list = levelData.getData();
            if (list == null) {
                list = new ArrayList<T>();
                levelData.setData(list);
            }
            list.add(data);
            mData.add(position, data);
            mAdapter.notifyItemInserted(position + getPreDataCount());
            compatibilityDataSizeChanged(1);
        } else {
            mData.add(data);
            mAdapter.notifyItemInserted(mData.size() + getPreDataCount());
            compatibilityDataSizeChanged(1);
        }
        onEnd();
    }

    /**
     * 移除position位置的数据
     *
     * @param position 移除位置
     */
    public T removeData(int position) {

        checkAdapterBind();

        if (!mIsCanRefresh) {
            return null;
        }
        mCurrentType = REFRESH_TYPE_OTHER;
        onStart();

        T removeData = mData.remove(position);
        int internalPosition = position + getPreDataCount();
        mAdapter.notifyItemRemoved(internalPosition);
        compatibilityDataSizeChanged(0);
        mAdapter.notifyItemRangeChanged(internalPosition, mData.size() - internalPosition);

        if (mCurrentMode == MODE_STANDARD) {

            int itemType = removeData.getItemType();
            boolean isHeader = itemType >= -HEADER_TYPE_DIFFER && itemType < 0;
            boolean isFooter = itemType >= -FOOTER_TYPE_DIFFER && itemType < -EMPTY_TYPE_DIFFER;
            if (isHeader) {
                itemType += HEADER_TYPE_DIFFER;
            }
            if (isFooter) {
                itemType += FOOTER_TYPE_DIFFER;
            }
            LevelData<T> levelData = getDataWithType(itemType);
            if (levelData == null) {
                onEnd();
                return removeData;
            }

            if (isHeader) {
                levelData.removeHeader();
            } else if (isFooter) {
                levelData.removeFooter();
            } else {
                List<T> list = levelData.getData();
                if (list == null || list.isEmpty()) {
                    onEnd();
                    return removeData;
                }
                list.remove(removeData);
            }

        }
        onEnd();
        return removeData;
    }

    /**
     * 修改position位置数据
     *
     * @param position 修改位置
     * @param data     修改数据，数据不可为空，不然会引起不必要的麻烦
     */
    public void setData(int position, @NonNull T data) {

        checkAdapterBind();

        if (!mIsCanRefresh) {
            return;
        }
        mCurrentType = REFRESH_TYPE_OTHER;
        onStart();

        if (mCurrentMode == MODE_STANDARD) {
            T currentData = mData.get(position);

            if (currentData == null) {
                onEnd();
                throw new DataException("please check your data !");
            }

            int itemType = data.getItemType();
            if (currentData.getItemType() != itemType) {
                onEnd();
                throw new DataException("please check your updated data's type !");
            }

            T oldData = mData.set(position, data);
            mAdapter.notifyItemChanged(position + getPreDataCount());
            boolean isHeader = itemType >= -HEADER_TYPE_DIFFER && itemType < 0;
            boolean isFooter = itemType >= -FOOTER_TYPE_DIFFER && itemType < -EMPTY_TYPE_DIFFER;
            if (isHeader) {
                itemType += HEADER_TYPE_DIFFER;
            }
            if (isFooter) {
                itemType += FOOTER_TYPE_DIFFER;
            }
            LevelData<T> levelData = getDataWithType(itemType);
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
     * 在position位置插入数据集合
     * 不考虑data包含下一level或者下下个level的数据，只支持当前level
     *
     * @param position 插入位置
     */
    public void addData(int position, List<? extends T> data) {

        checkAdapterBind();

        if (!mIsCanRefresh) {
            return;
        }
        mCurrentType = REFRESH_TYPE_OTHER;
        onStart();

        if (mCurrentMode == MODE_STANDARD) {
            if (data == null || data.isEmpty()) {
                onEnd();
                return;
            }

            T t = mData.get(position);
            int itemType = t.getItemType();
            int pos;
            if (itemType >= -HEADER_TYPE_DIFFER && itemType < 0) {
                pos = position + 1;
                itemType = itemType + HEADER_TYPE_DIFFER;
                checkList(itemType, data);
                final int level = getLevel(itemType);
                LevelData<T> levelData = mLevelOldData.get(level);
                if (levelData == null) {
                    //当前有header，因此不可能为空
                    levelData = new LevelData<T>(new ArrayList<T>(), null, null);
                    mLevelOldData.put(level, levelData);
                }
                List<T> list = levelData.getData();
                if (list == null) {
                    list = new ArrayList<T>();
                    levelData.setData(list);
                }
                list.addAll(0, data);
            } else if (itemType >= -FOOTER_TYPE_DIFFER && itemType < -EMPTY_TYPE_DIFFER) {
                pos = position;
                itemType = itemType + FOOTER_TYPE_DIFFER;
                checkList(itemType, data);
                final int level = getLevel(itemType);
                LevelData<T> levelData = mLevelOldData.get(level);
                if (levelData == null) {
                    //当前有footer，因此不可能为空
                    levelData = new LevelData<T>(new ArrayList<T>(), null, null);
                    mLevelOldData.put(level, levelData);
                }
                List<T> list = levelData.getData();
                if (list == null) {
                    list = new ArrayList<T>();
                    levelData.setData(list);
                }
                list.addAll(data);
            } else if (itemType >= 0) {
                pos = position;
                checkList(itemType, data);
                final int level = getLevel(itemType);
                int positionStart = getPositionStart(level);
                LevelData<T> levelData = mLevelOldData.get(level);
                if (levelData == null) {
                    //当前有data，因此不可能为空
                    levelData = new LevelData<T>(new ArrayList<T>(), null, null);
                    mLevelOldData.put(level, levelData);
                }
                List<T> list = levelData.getData();
                if (list == null) {
                    list = new ArrayList<T>();
                    levelData.setData(list);
                }

                T header = levelData.getHeader();
                int addPosition = header == null ? pos - positionStart : pos - positionStart - 1;
                list.addAll(addPosition, data);
            } else {
                onEnd();
                throw new DataException("please check the current showing data");
            }

            position = pos;
        }

        mData.addAll(position, data);
        mAdapter.notifyItemRangeInserted(position + getPreDataCount(), data.size());
        compatibilityDataSizeChanged(data.size());
        onEnd();
    }

    /**
     * 添加数据集合
     * 不考虑data包含下一level或者下下个level的数据，只支持当前level
     *
     * @param newData 添加的新数据集合
     */
    public void addData(List<? extends T> newData) {

        checkAdapterBind();

        if (!mIsCanRefresh) {
            return;
        }
        mCurrentType = REFRESH_TYPE_OTHER;
        onStart();
        if (mCurrentMode == MODE_STANDARD) {
            int position = mData.size() - 1;
            T t = mData.get(position);
            int itemType = t.getItemType();
            int pos;
            if (itemType >= -HEADER_TYPE_DIFFER && itemType < 0) {
                pos = position + 1;
                itemType = itemType + HEADER_TYPE_DIFFER;
            } else if (itemType >= -FOOTER_TYPE_DIFFER && itemType < -EMPTY_TYPE_DIFFER) {
                pos = position;
                itemType = itemType + FOOTER_TYPE_DIFFER;
            } else if (itemType >= 0) {
                pos = position;
            } else {
                onEnd();
                throw new DataException("please check the current showing data");
            }

            checkList(itemType, newData);
            position = pos + 1;
            final int level = getLevel(itemType);
            LevelData<T> levelData = mLevelOldData.get(level);
            if (levelData == null) {
                levelData = new LevelData<T>(new ArrayList<T>(), null, null);
                mLevelOldData.put(level, levelData);
            }
            List<T> list = levelData.getData();
            if (list == null) {
                list = new ArrayList<T>();
                levelData.setData(list);
            }
            list.addAll(newData);
            mData.addAll(position, newData);
            mAdapter.notifyItemRangeInserted(position + getPreDataCount(), newData.size());
            compatibilityDataSizeChanged(newData.size());
        } else {
            mData.addAll(newData);
            mAdapter.notifyItemRangeInserted(mData.size() - newData.size() + getPreDataCount(), newData.size());
            compatibilityDataSizeChanged(newData.size());
        }
        onEnd();
    }

    /**
     * 判断数据是否处于合拢状态
     *
     * @param type 数据类型
     * @return boolean ture为已合拢状态，false为展开状态
     */
    public boolean isDataFolded(int type) {

        LevelData<T> levelData = getDataWithType(type);
        if (levelData == null) {
            return false;
        }

        List<T> data = levelData.getData();
        if (data == null || data.isEmpty()) {
            return false;
        }

        int size = data.size();
        ResourcesManager.AttrsEntity attrsEntity = mResourcesManager.getAttrsEntity(getLevel(type));
        return !(!attrsEntity.isFolded || size <= attrsEntity.minSize);
    }

    /**
     * 展开合拢type数据
     *
     * @param type   数据类型
     * @param isFold 是否合拢
     */
    public void foldType(int type, boolean isFold) {

        LevelData<T> levelData = getDataWithType(type);
        if (levelData == null) {
            return;
        }

        List<T> data = levelData.getData();
        if (data == null || data.isEmpty()) {
            return;
        }
        int size = data.size();

        int level = getLevel(type);
        ResourcesManager.AttrsEntity attrsEntity = mResourcesManager.getAttrsEntity(level);

        if ((!attrsEntity.isFolded || size <= attrsEntity.minSize) && !isFold) {
            return;
        }

        if ((attrsEntity.isFolded || size <= attrsEntity.minSize) && isFold) {
            return;
        }

        checkAdapterBind();
        if (!mIsCanRefresh) {
            return;
        }
        mCurrentType = type;
        onStart();
        int headerCount = levelData.getHeader() == null ? 0 : 1;
        int positionStart = getPositionStart(level) + headerCount + attrsEntity.minSize;
        List<T> handleData = data.subList(attrsEntity.minSize, size);

        if (isFold) {
            mData.removeAll(handleData);
            mAdapter.notifyItemRangeRemoved(positionStart + getPreDataCount(), handleData.size());
            compatibilityDataSizeChanged(handleData.size());
            attrsEntity.isFolded = true;
        } else {
            mData.addAll(positionStart, handleData);
            mAdapter.notifyItemRangeInserted(positionStart + getPreDataCount(), handleData.size());
            compatibilityDataSizeChanged(handleData.size());
            attrsEntity.isFolded = false;
        }
        onEnd();
    }

    /**
     * 切换模式，只切换模式以及对数据整理，并不会刷新，如果需要切换模式并刷新数据，请调用
     * notifyDataSetChanged
     *
     * @param mode   模式
     * @param isSort 是否整理数据，建议整理否则容易出现数据混乱，崩溃等问题
     */
    public void switchMode(@RefreshMode int mode, boolean isSort) {

        if (!mIsCanRefresh) {
            return;
        }
        mCurrentType = REFRESH_TYPE_DATA_ALL;
        onStart();

        mCurrentMode = mode;

        if (isSort && mCurrentMode == MODE_STANDARD) {
            mNewData.clear();
            mNewData.addAll(mData);
            mData.clear();
            mData.addAll(initStandardNewData(mNewData));
        }

        onEnd();
    }

    public void switchMode(@RefreshMode int mode) {
        switchMode(mode, true);
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
     * 返回当前刷新Type
     *
     * @return RefreshMode
     */
    public int getCurrentRefreshType() {
        return mCurrentType;
    }

    /**
     * 设置loadview的数据集合缓存最大值
     *
     * @param maxDataCacheCount 缓存最大值
     */
    public void setMaxDataCacheCount(int maxDataCacheCount) {
        mMaxDataCacheCount = maxDataCacheCount;
    }

    /**
     * 设置loadview的头集合缓存最大值
     *
     * @param maxHeaderCacheCount 缓存最大值
     */
    public void setMaxSingleCacheCount(int maxHeaderCacheCount) {
        mMaxSingleCacheCount = maxHeaderCacheCount;
    }

    /**
     * 如果需要，清除队列
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
        return new DiffCallBack<T>(oldData, newData);
    }

    /**
     * @return 是否要移动
     */
    protected boolean isDetectMoves() {
        return true;
    }

    /**
     * 采用队列形式防止异步数据混乱
     *
     * @param newData     刷新的数据
     * @param newHeader   刷新数据顶部的头,如果不需要传空就行了
     * @param type        刷新数据的类型,切忌,传头部类型是报错的,只要关心数据类型就行了
     * @param refreshType 刷新类型
     */
    protected void notifyMoudleChanged(List<T> newData, T newHeader, T newFooter, int type, int refreshType) {

        boolean offer = mRefreshQueue.offer(new HandleBase<T>(newData, newHeader, newFooter, type, refreshType));

        if (!mIsCanRefresh || !offer) {
            return;
        }
        mCurrentType = type;
        onStart();

        HandleBase<T> pollData = mRefreshQueue.poll();
        if (pollData != null) {
            startRefresh(pollData);
        }
    }

    /**
     * 重写此方法，实现同步或异步刷新
     * 同步稳定，但是可能会卡顿
     * 异步效果好，但可能会异常
     * 根据实际情况选择相应的刷新机制
     *
     * @param refreshData 刷新数据合
     */
    protected abstract void startRefresh(HandleBase<T> refreshData);

    /**
     * 防止未注册的情况发生以及更好地利用注册资源，提供抽象方法
     */
    protected abstract void registerMoudle();

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
        mCurrentType = REFRESH_TYPE_IDLE;
    }

    /**
     * 开始刷新
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
    protected ListUpdateCallback getListUpdateCallback(final A adapter) {

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
     * @param type        数据类型
     * @param refreshType 刷新类型
     * @return DiffResult
     */
    protected final DiffUtil.DiffResult handleRefresh(List<T> newData, T newHeader, T newFooter, int type, int refreshType) {

        if (refreshType == REFRESH_ALL) {
            return DiffUtil.calculateDiff(getDiffCallBack(mData, newData), isDetectMoves());
        }

        checkStandardMode();

        mNewData.clear();
        mNewData.addAll(mData);

        int level = getLevel(type);

        LevelData<T> oldLevelData = mLevelOldData.get(level);
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
            ResourcesManager.AttrsEntity attrsEntity = mResourcesManager.getAttrsEntity(level);
            if (!attrsEntity.initState || data.size() <= attrsEntity.minSize) {
                mNewData.addAll(header == null ? positionStart : positionStart + 1, data);
                dataSize = data.size();
            } else {
                mNewData.addAll(header == null ? positionStart : positionStart + 1, data.subList(0, attrsEntity.minSize));
                dataSize = attrsEntity.minSize;
            }
            attrsEntity.isFolded = attrsEntity.initState;
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
        mLevelOldData.put(level, new LevelData<T>(data, header, footer));

        return result;
    }

    private int getPositionStart(int level) {

        int sum = 0;
        for (int i = 0; i < level; i++) {

            LevelData<T> data = mLevelOldData.get(i);
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

            ResourcesManager.AttrsEntity attrsEntity = mResourcesManager.getAttrsEntity(i);
            int size = list.size();
            if (!attrsEntity.isFolded || size <= attrsEntity.minSize) {
                sum += size;
            } else {
                sum += attrsEntity.minSize;
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
     * @param type 数据类型
     * @return 数据类型等级
     */
    private int getLevel(int type) {

        int level = mResourcesManager.getLevel(type);
        if (level <= DEFAULT_HEADER_LEVEL) {
            onEnd();
            throw new DataException("boy , are you sure register this data type (not include header type) ?");
        }

        return level;
    }

    /**
     * @param type      数据类型
     * @param dataCount 显示条目数
     * @return 根据type产生loading的数据集合
     */
    private List<T> createLoadingData(int type, @IntRange(from = 1) int dataCount) {

        checkLoadingAdapterBind();

        if (mDataCache == null) {
            mDataCache = new LruCache<String, List<T>>(mMaxDataCacheCount);
        }

        String dataKey = getDataKey(type);
        List<T> datas = mDataCache.get(dataKey);

        if (datas == null) {
            datas = new ArrayList<T>();
            mDataCache.put(dataKey, datas);
        }

        int size = datas.size();
        int diffSize = dataCount - size;
        if (diffSize > 0) {

            for (int i = 0; i < diffSize; i++) {
                datas.add(mLoadingEntityAdapter.createLoadingEntity(type));
            }
        } else if (diffSize < 0) {
            datas = datas.subList(0, dataCount);
        }

        for (int i = 0; i < dataCount; i++) {

            T entity = datas.get(i);
            mLoadingEntityAdapter.bindLoadingEntity(entity, i);
        }

        return datas;
    }

    /**
     * @param type 数据类型
     * @return 根据type产生loading的头
     */
    private T createLoadingHeader(int type) {

        checkLoadingAdapterBind();

        if (mSingleCache == null) {
            mSingleCache = new LruCache<String, T>(mMaxSingleCacheCount);
        }

        String headerKey = getHeaderKey(type);
        T header = mSingleCache.get(headerKey);

        if (header == null) {
            header = mLoadingEntityAdapter.createLoadingHeaderEntity(type);
            mSingleCache.put(headerKey, header);
        }

        mLoadingEntityAdapter.bindLoadingEntity(header, -1);
        return header;
    }

    /**
     * @param loadingConfig 配置项
     */
    public void initGlobalLoadingConfig(LoadingConfig loadingConfig) {

        SparseArray<LoadingConfigEntity> configs = loadingConfig.getConfigs();
        int size = configs.size();
        if (size == 0) {
            return;
        }

        SparseArray<LoadingConfigEntity> temConfigs = new SparseArray<LoadingConfigEntity>();
        SparseIntArray types = new SparseIntArray();
        for (int i = 0; i < size; i++) {
            int type = configs.keyAt(i);
            int level = getLevel(type);
            temConfigs.put(level, configs.valueAt(i));
            types.put(level, type);
        }

        if (mGlobalLoadingEntitys == null) {
            mGlobalLoadingEntitys = new ArrayList<T>();
        } else {
            mGlobalLoadingEntitys.clear();
        }

        for (int i = 0; i < size; i++) {
            LoadingConfigEntity entity = temConfigs.valueAt(i);

            int level = temConfigs.keyAt(i);
            int type = types.get(level);

            if (entity.isHaveHeader) {
                mGlobalLoadingEntitys.add(createLoadingHeader(type));
            }

            if (entity.count > 0) {
                mGlobalLoadingEntitys.addAll(createLoadingData(type, entity.count));
            }
        }

    }

    /**
     * 兼容其他库postion与实际position不一致的情况
     *
     * @param size 修改数据数量
     */
    private void compatibilityDataSizeChanged(int size) {

        final int dataSize = mData == null ? 0 : mData.size();
        if (dataSize == size) {
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 检查标准模式刷新
     */
    private void checkStandardMode() {

        if (mCurrentMode == MODE_MIXED) {
            onEnd();
            mRefreshQueue.clear();
            throw new RefreshException("current refresh mode can't support random refresh mode !");
        }
    }

    /**
     * 检查随机模式刷新
     */
    private void checkMixedMode() {

        if (mCurrentMode == MODE_STANDARD) {
            onEnd();
            throw new RefreshException("current refresh mode can't support standard refresh mode !");
        }
    }

    /**
     * 检查Adapter是否为绑定
     */
    private void checkAdapterBind() {

        if (mAdapter == null) {
            onEnd();
            throw new AdapterBindException("are you sure bind the adapter ?");
        }
    }

    /**
     * 检查LoadingAdapter是否为绑定
     */
    private void checkLoadingAdapterBind() {

        if (mLoadingEntityAdapter == null) {
            onEnd();
            throw new AdapterBindException("are you sure bind the loading adapter ?");
        }
    }

    /**
     * 检查EmptyAdapter是否为绑定
     */
    private void checkEmptyAdapterBind() {

        if (mEmptyEntityAdapter == null) {
            onEnd();
            throw new AdapterBindException("are you sure bind the empty adapter ?");
        }
    }

    /**
     * 检查ErrorAdapter是否为绑定
     */
    private void checkErrorAdapterBind() {

        if (mErrorEntityAdapter == null) {
            onEnd();
            throw new AdapterBindException("are you sure bind the error adapter ?");
        }
    }

    private void checkList(int type, List<? extends T> data) {
        for (T t : data) {
            if (type != t.getItemType()) {
                onEnd();
                throw new DataException("please check data type!");
            }
        }
    }

    /**
     * 初始化标准模式数据
     *
     * @param newData 新数据
     */
    private List<T> initStandardNewData(List<T> newData) {

        mLevelOldData.clear();
        SparseArray<List<T>> temDatas = new SparseArray<List<T>>();
        SparseArray<T> temHeaders = new SparseArray<T>();
        SparseArray<T> temFooters = new SparseArray<T>();
        for (T data : newData) {

            int itemType = data.getItemType();
            if (itemType >= 0 && itemType < HEADER_TYPE_DIFFER) {
                int level = getLevel(itemType);
                List<T> dataList = temDatas.get(level);
                if (dataList == null) {
                    dataList = new ArrayList<T>();
                    temDatas.put(level, dataList);
                }
                dataList.add(data);

            } else if (itemType >= -HEADER_TYPE_DIFFER && itemType < 0) {
                temHeaders.put(getLevel(itemType + HEADER_TYPE_DIFFER), data);
            } else if (itemType >= -FOOTER_TYPE_DIFFER && itemType < -EMPTY_TYPE_DIFFER) {
                temFooters.put(getLevel(itemType + FOOTER_TYPE_DIFFER), data);
            }
        }

        List<T> addData = new ArrayList<T>();
        int size = temDatas.size();
        for (int i = 0; i < size; i++) {
            int level = temDatas.keyAt(i);
            List<T> data = temDatas.get(level);
            T header = temHeaders.get(level);
            T footer = temFooters.get(level);
            mLevelOldData.put(level, new LevelData<T>(data, header, footer));
            if (header != null) {
                addData.add(header);
            }
            ResourcesManager.AttrsEntity attrsEntity = mResourcesManager.getAttrsEntity(level);
            if (!attrsEntity.initState || data.size() <= attrsEntity.minSize) {
                addData.addAll(data);
            } else {
                addData.addAll(data.subList(0, attrsEntity.minSize));
            }
            attrsEntity.isFolded = attrsEntity.initState;
            if (footer != null) {
                addData.add(footer);
            }
        }

        return addData;
    }

    private String getDataKey(int type) {
        return String.format(Locale.getDefault(), "data_%d", type);
    }

    private String getHeaderKey(int type) {
        return String.format(Locale.getDefault(), "header_%d", type);
    }

    private String getEmptyKey(int type) {
        return String.format(Locale.getDefault(), "empty_%d", type);
    }

    private String getErrorKey(int type) {
        return String.format(Locale.getDefault(), "error_%d", type);
    }

    /**
     * 释放资源，谨慎使用，可能引起不必要的崩溃
     */
    public void release() {
        mRefreshQueue.clear();
        mRefreshQueue = null;
        mNewData.clear();
        mNewData = null;
        mData.clear();
        mData = null;
        mGlobalLoadingEntitys.clear();
        mGlobalLoadingEntitys = null;
        mSingleCache.evictAll();
        mSingleCache = null;
        mDataCache.evictAll();
        mDataCache = null;
        mLevelOldData.clear();
        mLevelOldData = null;
        mResourcesManager.release();
        mResourcesManager = null;
    }

    /**
     * 处理关键字高亮
     *
     * @param originStr       被处理字符串
     * @param keyWord         关键字
     * @param hightLightColor 高亮颜色
     * @return CharSequence
     */
    public static CharSequence handleKeyWordHighLight
    (String originStr, String keyWord, @ColorInt int hightLightColor) {

        SpannableString ss = new SpannableString(originStr);
        Pattern p = Pattern.compile(keyWord);
        Matcher m = p.matcher(ss);

        while (m.find()) {
            ss.setSpan(new ForegroundColorSpan(hightLightColor), m.start(), m.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return ss;
    }

}
