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

import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.util.ListUpdateCallback;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.LruCache;
import android.util.SparseArray;

import com.crazysunj.multitypeadapter.entity.ErrorEntity;
import com.crazysunj.multitypeadapter.entity.HandleBase;
import com.crazysunj.multitypeadapter.entity.LevelData;
import com.crazysunj.multitypeadapter.entity.MultiHeaderEntity;
import com.crazysunj.multitypeadapter.sticky.StickyHeaderDecoration;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * description
 * 如果您发现哪里性能比较差或者说设计不合理，希望您能反馈给我
 * email:twsunj@gmail.com
 * type 取值范围
 * 数据类型 [0,1000)
 * 头类型 [-1000,0)
 * shimmer数据类型 [-2000,-1000)
 * shimmer头类型 [-3000,-2000)
 * error类型 [-4000,-3000)
 * Created by sunjian on 2017/5/4.
 */

public abstract class RecyclerViewAdapterHelper<T extends MultiHeaderEntity> {

    //标准刷新，相应的type集合在一起
    public static final int MODE_STANDARD = 0;
    //随机模式，操作一般的数据
    public static final int MODE_MIXED = 1;

    @IntDef({MODE_STANDARD, MODE_MIXED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface RefreshMode {
    }

    //默认数量为0
    private static final int PRE_DATA_COUNT = 0;

    //头类型差值
    public static final int HEADER_TYPE_DIFFER = 1000;
    //shimmer数据类型差值
    public static final int SHIMMER_DATA_TYPE_DIFFER = 2000;
    //shimmer头类型差值
    public static final int SHIMMER_HEADER_TYPE_DIFFER = 3000;
    //错误类型差值
    public static final int ERROR_TYPE_DIFFER = 4000;

    //默认viewType
    private static final int DEFAULT_VIEW_TYPE = -1;
    //默认头的level，处理数据只跟type有关系
    static final int DEFAULT_HEADER_LEVEL = -1;
    //只刷新头
    private static final int REFRESH_HEADER = 0;
    //只刷新数据
    private static final int REFRESH_DATA = 1;
    //同时刷新数据和头
    private static final int REFRESH_HEADER_DATA = 2;
    //刷新全部
    private static final int REFRESH_ALL = 3;

    //控制麒麟臂用户
    private boolean mIsCanRefresh = true;
    //随机id的默认最大值
    private long mMaxRandomId = -1;
    //随机id的默认最小值
    private long mMinRandomId = Long.MIN_VALUE;
    //loadingview关于数据方面的最大缓存值
    private int mMaxDataCacheCount = 12;
    //loadingview关于头方面的最大缓存值
    private int mMaxHeaderCacheCount = 6;

    //根据level存储的数据
    private SparseArray<LevelData<T>> mLevelOldData;
    //数据的缓存
    private LruCache<Integer, List<T>> mDataCache;
    //头的缓存
    private LruCache<Integer, T> mHeaderCache;
    //老数据
    protected List<T> mNewData;
    //当前数据
    protected List<T> mData;
    //绑定Adapter
    private RecyclerView.Adapter mAdapter;
    //资源管理
    private ResourcesManager mResourcesManager;
    //刷新队列，支持高频率刷新
    private Queue<HandleBase<T>> mRefreshQueue;

    //当前模式
    private int mCurrentMode;

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
    public void bindAdapter(RecyclerView.Adapter adapter) {

        mAdapter = adapter;
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
     * @param type                     数据类型
     * @param level                    数据级别
     * @param layoutResId              正常类型布局id
     * @param headerResId              头类型布局id
     * @param shimmerLayoutResId       loading 数据类型布局id
     * @param shimmerHeaderLayoutResId loading 头类型布局id
     */
    @Deprecated
    public void registerMoudleWithShimmer(@IntRange(from = 0, to = 999) int type, @IntRange(from = 0) int level,
                                          @LayoutRes int layoutResId, @LayoutRes int headerResId,
                                          @LayoutRes int shimmerLayoutResId, @LayoutRes int shimmerHeaderLayoutResId) {

        registerMoudle(type, level, layoutResId, headerResId);

        int shimmerType = type - SHIMMER_DATA_TYPE_DIFFER;
        mResourcesManager.putLevel(shimmerType, level);
        mResourcesManager.putLayoutId(shimmerType, shimmerLayoutResId);

        int shimmerHeaderType = type - SHIMMER_HEADER_TYPE_DIFFER;
        mResourcesManager.putLevel(shimmerHeaderType, DEFAULT_HEADER_LEVEL);
        mResourcesManager.putLayoutId(shimmerHeaderType, shimmerHeaderLayoutResId);
    }

    @Deprecated
    public void registerMoudleWithShimmer(@IntRange(from = 0, to = 999) int type, @IntRange(from = 0) int level,
                                          @LayoutRes int layoutResId, @LayoutRes int headerResId, @LayoutRes int shimmerHeaderLayoutResId) {

        registerMoudle(type, level, layoutResId, headerResId);

        int shimmerHeaderType = type - SHIMMER_HEADER_TYPE_DIFFER;
        mResourcesManager.putLevel(shimmerHeaderType, DEFAULT_HEADER_LEVEL);
        mResourcesManager.putLayoutId(shimmerHeaderType, shimmerHeaderLayoutResId);
    }

    @Deprecated
    public void registerMoudleWithShimmer(@IntRange(from = 0, to = 999) int type, @IntRange(from = 0) int level,
                                          @LayoutRes int layoutResId, @LayoutRes int shimmerLayoutResId) {

        registerMoudle(type, level, layoutResId);

        int shimmerType = type - SHIMMER_DATA_TYPE_DIFFER;
        mResourcesManager.putLevel(shimmerType, level);
        mResourcesManager.putLayoutId(shimmerType, shimmerLayoutResId);
    }

    @Deprecated
    public void registerMoudle(@IntRange(from = 0, to = 999) int type, @IntRange(from = 0) int level,
                               @LayoutRes int layoutResId, @LayoutRes int headerResId) {

        registerMoudle(type, level, layoutResId);

        int headerType = type - HEADER_TYPE_DIFFER;
        mResourcesManager.putLevel(headerType, DEFAULT_HEADER_LEVEL);
        mResourcesManager.putLayoutId(headerType, headerResId);
    }

    @Deprecated
    public void registerMoudle(@IntRange(from = 0, to = 999) int type, @IntRange(from = 0) int level,
                               @LayoutRes int layoutResId) {

        mResourcesManager.putLevel(type, level);
        mResourcesManager.putLayoutId(type, layoutResId);
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
     * 提供粘性头headerId
     *
     * @param position 索引
     * @return headerId
     */
    public long getHeaderId(int position) {

        return mData.get(position - getPreDataCount()).getHeaderId();
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
     * 务必在调用之前确定缓存最大值，可调用setMaxHeaderCacheCount和setMaxHeaderCacheCount
     * 对应notifyMoudleDataChanged
     *
     * @param type      数据类型
     * @param dataCount 刷新条目数
     */
    public void notifyShimmerDataChanged(int type, @IntRange(from = 1) int dataCount) {

        List<T> datas = createShimmerDatas(type, dataCount);

        notifyMoudleChanged(datas, null, type, REFRESH_DATA);
    }

    /**
     * 只刷新数据
     *
     * @param data 数据
     * @param type 数据类型
     */
    @SuppressWarnings("unchecked")
    public void notifyMoudleDataChanged(List<? extends T> data, int type) {

        notifyMoudleChanged((List<T>) data, null, type, REFRESH_DATA);
    }

    public void notifyMoudleDataChanged(T data, int type) {

        notifyMoudleChanged(Collections.singletonList(data), null, type, REFRESH_DATA);
    }

    /**
     * 务必在调用之前确定缓存最大值，可调用setMaxHeaderCacheCount和setMaxHeaderCacheCount
     * 对应notifyMoudleHeaderChanged
     *
     * @param type 数据类型
     */
    public void notifyShimmerHeaderChanged(int type) {

        T header = createShimmerHeader(type);

        notifyMoudleChanged(null, header, type, REFRESH_HEADER);
    }

    /**
     * 只刷新头
     *
     * @param header 头
     * @param type   数据类型
     */
    public void notifyMoudleHeaderChanged(T header, int type) {

        notifyMoudleChanged(null, header, type, REFRESH_HEADER);
    }

    /**
     * 务必在调用之前确定缓存最大值，可调用setMaxHeaderCacheCount和setMaxHeaderCacheCount
     * 对应notifyMoudleDataAndHeaderChanged
     *
     * @param type      数据类型
     * @param dataCount 刷新条目数
     */
    public void notifyShimmerDataAndHeaderChanged(int type, @IntRange(from = 1) int dataCount) {

        T header = createShimmerHeader(type);
        List<T> datas = createShimmerDatas(type, dataCount);

        notifyMoudleChanged(datas, header, type, REFRESH_HEADER_DATA);
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

        notifyMoudleChanged((List<T>) data, header, type, REFRESH_HEADER_DATA);
    }

    /**
     * 同时刷新头和数据
     *
     * @param data   数据
     * @param header 头
     * @param type   数据类型
     */
    public void notifyMoudleDataAndHeaderChanged(T data, T header, int type) {

        notifyMoudleChanged(Collections.singletonList(data), header, type, REFRESH_HEADER_DATA);
    }

    /**
     * 刷新相应类型错误界面
     * 如果刷新没有跟数据属性关联，那么调用notifyMoudleErrorChanged(int type)
     *
     * @param errorData 错误数据
     * @param type      错误数据类型
     */
    @SuppressWarnings("unchecked")
    public void notifyMoudleErrorChanged(ErrorEntity errorData, int type) {

        notifyMoudleChanged(Collections.singletonList((T) errorData), null, type, REFRESH_HEADER_DATA);
    }

    public void notifyMoudleErrorChanged(int type) {

        notifyMoudleErrorChanged(new ErrorEntity(getRandomId(), type), type);
    }

    /**
     * 刷新全局数据
     *
     * @param newData     新数据集合
     * @param refreshMode 刷新模式
     */
    @SuppressWarnings("unchecked")
    public void notifyDataSetChanged(List<? extends T> newData, @RefreshMode int refreshMode) {

        checkAdapterBind();

        mData.clear();

        if (refreshMode == MODE_STANDARD) {
            mData.addAll(initStandardNewData((List<T>) newData));
        } else {
            mData.addAll(newData);
        }
        mAdapter.notifyDataSetChanged();
    }


    /**
     * 默认标准刷新
     *
     * @param newData 新数据集合
     */
    public void notifyDataSetChanged(List<? extends T> newData) {

        notifyDataSetChanged(newData, MODE_STANDARD);
    }


    /**
     * 数据比较后刷新，可支持异步，与Type刷新用法一样
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

        notifyMoudleChanged(mNewData, null, DEFAULT_VIEW_TYPE, REFRESH_ALL);

    }

    public void notifyDataByDiff(List<? extends T> newData) {

        notifyDataByDiff(newData, MODE_STANDARD);
    }


    /**
     * 清除单个type数据
     *
     * @param type 数据类型
     */
    public void clearMoudle(int type) {

        checkStandardMode();

        LevelData<T> data = getDataWithType(type);
        if (data == null || data.getData() == null) {
            return;
        }

        notifyMoudleChanged(null, null, type, REFRESH_HEADER_DATA);
    }

    /**
     * 清除所有数据
     */
    public void clear() {

        if (mData.isEmpty()) {
            return;
        }

        checkAdapterBind();

        mData.clear();

        mAdapter.notifyDataSetChanged();
    }

    /**
     * 在position位置添加数据
     *
     * @param position 添加的位置
     */
    public void addData(int position, T data) {

        checkMixedMode();

        checkAdapterBind();

        mData.add(position, data);
        mAdapter.notifyItemInserted(position + getPreDataCount());
        compatibilityDataSizeChanged(1);
    }

    /**
     * 添加数据
     *
     * @param data 添加的数据
     */
    public void addData(T data) {

        checkMixedMode();

        checkAdapterBind();

        mData.add(data);
        mAdapter.notifyItemInserted(mData.size() + getPreDataCount());
        compatibilityDataSizeChanged(1);
    }

    /**
     * 移除position位置的数据
     *
     * @param position 移除位置
     */
    public T removeData(int position) {

        checkAdapterBind();

        T removeData = mData.remove(position);
        int internalPosition = position + getPreDataCount();
        mAdapter.notifyItemRemoved(internalPosition);
        compatibilityDataSizeChanged(0);
        mAdapter.notifyItemRangeChanged(internalPosition, mData.size() - internalPosition);

        if (mCurrentMode == MODE_STANDARD) {

            int itemType = removeData.getItemType();
            boolean isHeader = itemType >= -1000 && itemType < 0;
            if (isHeader) {
                itemType += HEADER_TYPE_DIFFER;
            }
            LevelData<T> levelData = getDataWithType(itemType);
            if (levelData == null) {
                return removeData;
            }

            if (isHeader) {

                levelData.removeHeader();
            } else {

                List<T> list = levelData.getData();
                if (list == null || list.isEmpty()) {
                    return removeData;
                }
                list.remove(removeData);
            }

        }

        return removeData;
    }

    /**
     * 修改position位置数据
     *
     * @param position 修改位置
     * @param data     修改数据
     */
    public void setData(int position, @NonNull T data) {

        checkAdapterBind();

        if (mCurrentMode == MODE_STANDARD) {
            T currentData = mData.get(position);

            if (currentData == null) {
                throw new DataException("please your data !");
            }

            int itemType = data.getItemType();
            if (currentData.getItemType() != itemType) {
                throw new DataException("please check your updated data's type !");
            }

            T oldData = mData.set(position, data);
            mAdapter.notifyItemChanged(position + getPreDataCount());
            boolean isHeader = itemType >= -1000 && itemType < 0;
            LevelData<T> levelData = getDataWithType(isHeader ? itemType + HEADER_TYPE_DIFFER : itemType);
            if (levelData == null) {
                return;
            }

            if (isHeader) {
                
                levelData.setHeader(data);
            } else {

                List<T> list = levelData.getData();
                if (list == null || list.isEmpty()) {
                    return;
                }
                int oldIndex = list.indexOf(oldData);
                list.set(oldIndex, data);
            }
            return;
        }

        mData.set(position, data);
        mAdapter.notifyItemChanged(position + getPreDataCount());
    }

    /**
     * 在position位置插入数据集合
     *
     * @param position 插入位置
     */
    public void addData(int position, List<? extends T> data) {

        checkMixedMode();

        checkAdapterBind();

        mData.addAll(position, data);
        mAdapter.notifyItemRangeInserted(position + getPreDataCount(), data.size());
        compatibilityDataSizeChanged(data.size());
    }

    /**
     * 添加数据集合
     *
     * @param newData 添加的新数据集合
     */
    public void addData(List<? extends T> newData) {

        checkMixedMode();

        checkAdapterBind();

        mData.addAll(newData);
        mAdapter.notifyItemRangeInserted(mData.size() - newData.size() + getPreDataCount(), newData.size());
        compatibilityDataSizeChanged(newData.size());
    }

    /**
     * 切换模式
     *
     * @param mode 模式
     */
    public void switchMode(@RefreshMode int mode) {

        mCurrentMode = mode;
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
    public void setMaxHeaderCacheCount(int maxHeaderCacheCount) {

        mMaxHeaderCacheCount = maxHeaderCacheCount;
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
    protected void notifyMoudleChanged(List<T> newData, T newHeader, int type, int refreshType) {

        boolean offer = mRefreshQueue.offer(new HandleBase<T>(newData, newHeader, type, refreshType));

        if (!mIsCanRefresh || !offer) {
            return;
        }
        mIsCanRefresh = false;

        HandleBase<T> pollData = mRefreshQueue.poll();
        if (pollData != null) {
            startRefresh(pollData);
        }
    }

    /**
     * 如果需要，清除队列
     */
    public void clearQueue() {

        mRefreshQueue.clear();
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
            mIsCanRefresh = true;
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
     * @param type        数据类型
     * @param refreshType 刷新类型
     * @return DiffResult
     */
    protected final DiffUtil.DiffResult handleRefresh(List<T> newData, T newHeader, int type, int refreshType) {

        if (refreshType == REFRESH_ALL) {

            return DiffUtil.calculateDiff(getDiffCallBack(mData, newData), isDetectMoves());
        }

        checkStandardMode();

        mNewData.clear();
        mNewData.addAll(mData);

        int level = getLevel(type);
        int sum = 0;

        for (int i = 0; i < level; i++) {

            LevelData<T> data = mLevelOldData.get(i);

            if (data == null) {
                continue;
            }

            if (data.getHeader() != null) {
                sum++;
            }

            List<T> list = data.getData();
            if (list == null || list.isEmpty()) {
                continue;
            }
            int size = list.size();
            sum += size;
        }

        LevelData<T> oldLevelData = mLevelOldData.get(level);
        List<T> oldData = null;
        T oldHeader = null;

        if (oldLevelData != null) {

            List<T> oldItemData = oldLevelData.getData();
            if (oldItemData != null && !oldItemData.isEmpty()) {
                if (refreshType == REFRESH_HEADER) {
                    oldData = oldItemData;
                } else {
                    mNewData.removeAll(oldItemData);
                }
            }

            T oldItemHeader = oldLevelData.getHeader();
            if (oldItemHeader != null) {
                if (refreshType == REFRESH_DATA) {
                    oldHeader = oldItemHeader;
                } else {
                    mNewData.remove(oldItemHeader);
                }
            }
        }

        int positionStart = sum;
        boolean isDataEmpty = newData == null || newData.isEmpty();
        boolean isHeaderEmpty = newHeader == null;

        if (!isDataEmpty && refreshType != REFRESH_HEADER) {
            mNewData.addAll(oldHeader == null ? positionStart : positionStart + 1, newData);
        }
        if (!isHeaderEmpty && refreshType != REFRESH_DATA) {
            mNewData.add(positionStart, newHeader);
        }

        DiffUtil.DiffResult result = DiffUtil.calculateDiff(getDiffCallBack(mData, mNewData), isDetectMoves());
        mLevelOldData.put(level, new LevelData<T>(refreshType != REFRESH_HEADER ? newData : oldData, refreshType != REFRESH_DATA ? newHeader : oldHeader));

        return result;
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
            throw new DataException("boy , are you sure register this data type (not include header type) ?");
        }

        return level;
    }

    /**
     * @param type      数据类型
     * @param dataCount 显示条目数
     * @return 根据type产生loading的数据集合
     */
    @SuppressWarnings("unchecked")
    private List<T> createShimmerDatas(int type, @IntRange(from = 1) int dataCount) {

        if (mDataCache == null) {
            mDataCache = new LruCache<Integer, List<T>>(mMaxDataCacheCount);
        }

        List<T> datas = mDataCache.get(type);

        if (datas == null) {
            datas = new ArrayList<T>();
            mDataCache.put(type, datas);
        }

        int size = datas.size();
        int diffSize = dataCount - size;
        if (diffSize > 0) {

            for (int i = 0; i < diffSize; i++) {
                datas.add((T) new ShimmerEntity());
            }
        } else if (diffSize < 0) {
            datas = datas.subList(0, dataCount);
        }
        long headerId = StickyHeaderDecoration.NO_HEADER_ID;
        LevelData<T> levelData = getDataWithType(type);
        if (levelData != null) {
            List<T> data = levelData.getData();
            if (data != null && !data.isEmpty()) {
                headerId = data.get(0).getHeaderId();
            }
        }
        for (int i = 0; i < dataCount; i++) {

            T entity = datas.get(i);
            if (entity instanceof ShimmerEntity) {
                ShimmerEntity head = (ShimmerEntity) entity;
                head.setId(getRandomId());
                head.setHeaderId(headerId);
                head.setType(type - SHIMMER_DATA_TYPE_DIFFER);
            }
        }

        return datas;
    }

    /**
     * @param type 数据类型
     * @return 根据type产生loading的头
     */
    @SuppressWarnings("unchecked")
    private T createShimmerHeader(int type) {

        if (mHeaderCache == null) {
            mHeaderCache = new LruCache<Integer, T>(mMaxHeaderCacheCount);
        }

        T header = mHeaderCache.get(type);

        if (header == null) {
            header = (T) new ShimmerEntity();
            mHeaderCache.put(type, header);
        }

        if (header instanceof ShimmerEntity) {
            long headerId = StickyHeaderDecoration.NO_HEADER_ID;
            LevelData<T> levelData = getDataWithType(type);
            if (levelData != null) {
                List<T> data = levelData.getData();
                if (data != null && !data.isEmpty()) {
                    headerId = data.get(0).getHeaderId();
                }
            }
            ShimmerEntity head = (ShimmerEntity) header;
            head.setId(getRandomId());
            head.setHeaderId(headerId);
            head.setType(type - SHIMMER_HEADER_TYPE_DIFFER);
        }

        return header;
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
            mIsCanRefresh = true;
            mRefreshQueue.clear();
            throw new RefreshException("current refresh mode can't support random refresh mode !");
        }
    }

    /**
     * 检查随机模式刷新
     */
    private void checkMixedMode() {

        if (mCurrentMode == MODE_STANDARD) {
            throw new RefreshException("current refresh mode can't support standard refresh mode !");
        }
    }

    /**
     * 检查Adapter是否为绑定
     */
    private void checkAdapterBind() {

        if (mAdapter == null) {
            throw new AdapterBindException("are you sure bind the adapter ?");
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
        for (T data : newData) {

            int itemType = data.getItemType();
            if (itemType >= 0 && itemType < 1000) {
                int level = getLevel(itemType);
                List<T> dataList = temDatas.get(level);
                if (dataList == null) {
                    dataList = new ArrayList<T>();
                    temDatas.put(level, dataList);
                }
                dataList.add(data);

            } else if (itemType >= -1000 && itemType < 0) {
                temHeaders.put(getLevel(itemType + HEADER_TYPE_DIFFER), data);
            }
        }

        List<T> addData = new ArrayList<T>();
        int size = temDatas.size();
        int[] levels = new int[size];
        for (int i = 0; i < size; i++) {
            levels[i] = temDatas.keyAt(i);
        }

        Arrays.sort(levels);

        for (int i = 0; i < size; i++) {
            int level = levels[i];

            List<T> data = temDatas.get(level);
            T header = temHeaders.get(level);
            mLevelOldData.put(level, new LevelData<T>(data, header));
            if (header != null) {
                addData.add(header);
            }
            addData.addAll(data);
        }

        return addData;
    }

    /**
     * 处理关键字高亮
     * 如果需要检索算法库，请联系我
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
