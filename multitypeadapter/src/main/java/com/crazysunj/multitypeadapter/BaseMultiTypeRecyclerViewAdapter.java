package com.crazysunj.multitypeadapter;

import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.LruCache;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.crazysunj.multitypeadapter.entity.LevelData;
import com.crazysunj.multitypeadapter.entity.MultiHeaderEntity;
import com.crazysunj.multitypeadapter.entity.ShimmerEntity;
import com.crazysunj.multitypeadapter.sticky.StickyHeaderAdapter;
import com.crazysunj.multitypeadapter.sticky.StickyHeaderDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * 使用粘性头部，请调用bindToRecyclerView,否则自己添加decoration
 * type 取值范围
 * 数据类型 [0,1000)
 * 头类型 [-1000,0]
 * shimmer数据类型 [-2000,-1000)
 * shimmer头类型 [-3000,-2000)
 * Created by sunjian on 2017/3/27.
 */

public abstract class BaseMultiTypeRecyclerViewAdapter<T extends MultiHeaderEntity, K extends BaseViewHolder>
        extends BaseQuickAdapter<T, K> implements StickyHeaderAdapter<K> {

    private static final int DEFAULT_VIEW_TYPE = -0xff;
    public static final int DEFAULT_HEADER_LEVEL = -1;
    public static final int REFRESH_HEADER = 0;
    public static final int REFRESH_DATA = 1;
    public static final int REFRESH_HEADER_DATA = 2;

    private boolean isCanRefresh = true;
    private long mMaxHeaderId = -1;
    private long mMinHeaderId = Long.MIN_VALUE;
    private boolean mIsUseStickyHeader = false;

    private SparseIntArray mLayouts;
    private SparseIntArray mLevels;
    private SparseArray<LevelData<T>> mLevelOldData;
    protected List<T> mOldData;

    public BaseMultiTypeRecyclerViewAdapter() {
        this(false);
    }

    public BaseMultiTypeRecyclerViewAdapter(boolean isUseStickyHeader) {

        super(null);

        this.mIsUseStickyHeader = isUseStickyHeader;

        if (mOldData == null) {
            mOldData = new ArrayList<T>();
        }

        if (mLevelOldData == null) {
            mLevelOldData = new SparseArray<LevelData<T>>();
        }

        if (mLevels == null) {
            mLevels = new SparseIntArray();
        }

        if (mLayouts == null) {
            mLayouts = new SparseIntArray();
        }
    }

    /**
     * 这里会产生recyclerview传说中的BUG
     *
     * @param position
     * @return
     */
    @Override
    public long getHeaderId(int position) {
        try {
            return mData.get(position).getHeaderId();
        } catch (Exception e) {
            e.printStackTrace();
            return StickyHeaderDecoration.NO_HEADER_ID;
        }
    }

    @Override
    public K onCreateHeaderViewHolder(ViewGroup parent) {
        return createBaseViewHolder(parent, R.layout.item_header);
    }

    @Override
    public void onBindHeaderViewHolder(K helper, int position) throws Exception {

    }

    @Override
    protected void convert(K helper, T item) {
        convert(helper, item, helper.getLayoutPosition() - getHeaderLayoutCount());
    }

    @Override
    protected int getDefItemViewType(int position) {
        T item = mData.get(position);
        if (item != null) {
            return item.getItemType();
        }
        return DEFAULT_VIEW_TYPE;
    }

    @Override
    protected K onCreateDefViewHolder(ViewGroup parent, int viewType) {
        return createBaseViewHolder(parent, getLayoutId(viewType));
    }

    @Override
    public void bindToRecyclerView(RecyclerView recyclerView) {
        if (mIsUseStickyHeader) {
            recyclerView.addItemDecoration(new StickyHeaderDecoration(this));
        }
        super.bindToRecyclerView(recyclerView);
    }

    /**
     * @param type                     数据类型
     * @param level                    数据级别
     * @param layoutResId              正常类型布局id
     * @param headerResId              头类型布局id
     * @param shimmerLayoutResId       loading 数据类型布局id
     * @param shimmerHeaderLayoutResId loading 头类型布局id
     */
    public void registerMoudleWithShimmer(@IntRange(from = 0, to = 999) int type, @IntRange(from = 0) int level,
                                          @LayoutRes int layoutResId, @LayoutRes int headerResId,
                                          @LayoutRes int shimmerLayoutResId, @LayoutRes int shimmerHeaderLayoutResId) {
        registerMoudle(type, level, layoutResId, headerResId);

        int shimmerType = type - 2000;
        mLevels.put(shimmerType, level);
        mLayouts.put(shimmerType, shimmerLayoutResId);

        int shimmerHeaderType = type - 3000;
        mLevels.put(shimmerHeaderType, level);
        mLayouts.put(shimmerHeaderType, shimmerHeaderLayoutResId);
    }

    public void registerMoudleWithShimmer(@IntRange(from = 0, to = 999) int type, @IntRange(from = 0) int level,
                                          @LayoutRes int layoutResId, @LayoutRes int headerResId, @LayoutRes int shimmerHeaderLayoutResId) {
        registerMoudle(type, level, layoutResId, headerResId);
        int shimmerHeaderType = type - 3000;
        mLevels.put(shimmerHeaderType, level);
        mLayouts.put(shimmerHeaderType, shimmerHeaderLayoutResId);
    }

    public void registerMoudleWithShimmer(@IntRange(from = 0, to = 999) int type, @IntRange(from = 0) int level,
                                          @LayoutRes int layoutResId, @LayoutRes int shimmerLayoutResId) {
        registerMoudle(type, level, layoutResId);
        int shimmerType = type - 2000;
        mLevels.put(shimmerType, level);
        mLayouts.put(shimmerType, shimmerLayoutResId);
    }

    public void registerMoudle(@IntRange(from = 0, to = 999) int type, @IntRange(from = 0) int level,
                               @LayoutRes int layoutResId, @LayoutRes int headerResId) {
        registerMoudle(type, level, layoutResId);
        int headerType = type - 1000;
        mLevels.put(headerType, level);
        mLayouts.put(headerType, headerResId);
    }

    public void registerMoudle(@IntRange(from = 0, to = 999) int type, @IntRange(from = 0) int level,
                               @LayoutRes int layoutResId) {
        mLevels.put(type, level);
        mLayouts.put(type, layoutResId);
    }

    /**
     * 传头部的type是拿不到数据的
     *
     * @param type
     * @return
     */
    public LevelData<T> getDataWithType(int type) {
        return mLevelOldData.get(getLevel(type));
    }

    public void setMaxHeaderId(long maxHeaderId) {
        mMaxHeaderId = maxHeaderId;
    }

    public void setMinHeaderId(long minHeaderId) {
        mMinHeaderId = minHeaderId;
    }

    /**
     * 不断向下取值，如果抛异常，你可以重新设置最大值，但切记不能重复啊，报错我可不负责啊，这么多的值都用完了，是在下输了
     * 有特殊要求的同学可以自己设计
     *
     * @return
     */
    public long getRefreshHeaderId() {
        if (mMaxHeaderId <= mMinHeaderId) {
            throw new RuntimeException("boy,you win !");
        }
        return mMaxHeaderId--;
    }

    /**
     * 务必在调用之前确定缓存数量值，可调用setMaxHeaderCacheCount和setMaxHeaderCacheCount
     *
     * @param type
     * @param dataCount
     */
    public void notifyShimmerDataChanged(int type, @IntRange(from = 1) int dataCount) {
        List<T> datas = getShimmerDatas(type, dataCount);
        notifyMoudleChanged(datas, null, type, REFRESH_DATA);
    }

    public void notifyMoudleDataChanged(List<T> data, int type) {
        notifyMoudleChanged(data, null, type, REFRESH_DATA);
    }

    /**
     * 务必在调用之前确定缓存数量值，可调用setMaxHeaderCacheCount和setMaxHeaderCacheCount
     *
     * @param type
     */
    public void notifyShimmerHeaderChanged(int type) {
        T header = getShimmerHeader(type);
        notifyMoudleChanged(null, header, type, REFRESH_HEADER);
    }

    public void notifyMoudleHeaderChanged(T header, int type) {
        notifyMoudleChanged(null, header, type, REFRESH_HEADER);
    }

    private LruCache<Integer, List<T>> mDataCache;
    private LruCache<Integer, T> mHeaderCache;
    private int mMaxDataCacheCount = 12;
    private int mMaxHeaderCacheCount = 6;

    public void setMaxDataCacheCount(int mMaxDataCacheCount) {
        this.mMaxDataCacheCount = mMaxDataCacheCount;
    }

    public void setMaxHeaderCacheCount(int mMaxHeaderCacheCount) {
        this.mMaxHeaderCacheCount = mMaxHeaderCacheCount;
    }

    /**
     * 务必在调用之前确定缓存数量值，可调用setMaxHeaderCacheCount和setMaxHeaderCacheCount
     *
     * @param type
     * @param dataCount
     */
    public void notifyShimmerDataAndHeaderChanged(int type, @IntRange(from = 1) int dataCount) {

        T header = getShimmerHeader(type);
        List<T> datas = getShimmerDatas(type, dataCount);
        notifyMoudleChanged(datas, header, type, REFRESH_HEADER_DATA);

    }

    private List<T> getShimmerDatas(int type, @IntRange(from = 1) int dataCount) {
        if (mDataCache == null) {
            mDataCache = new LruCache<Integer, List<T>>(mMaxDataCacheCount);
        }
        List<T> datas = mDataCache.get(type);
        if (datas == null) {
            datas = new ArrayList<T>();
            mDataCache.put(type, datas);
        }
        int size = datas.size();
        if (size < dataCount) {
            for (int i = 0, n = dataCount - size; i < n; i++) {
                datas.add((T) new ShimmerEntity());
            }
        }
        for (int i = 0; i < dataCount; i++) {
            T entity = datas.get(i);
            entity.setId(getRefreshHeaderId());
            entity.setType(type - 3000);
        }

        return datas;
    }

    private T getShimmerHeader(int type) {

        if (mHeaderCache == null) {
            mHeaderCache = new LruCache<Integer, T>(mMaxHeaderCacheCount);
        }
        T header = mHeaderCache.get(type);
        if (header == null) {
            header = (T) new ShimmerEntity();
            mHeaderCache.put(type, header);
        }
        header.setId(getRefreshHeaderId());
        header.setType(type - 3000);

        return header;
    }

    public void notifyMoudleDataAndHeaderChanged(List<T> data, T header, int type) {
        notifyMoudleChanged(data, header, type, REFRESH_HEADER_DATA);
    }

    //返回比较的callback对象，提供新老数据
    protected DiffUtil.Callback getDiffCallBack(List<T> oldData, List<T> newData) {
        return new DiffCallBack<T>(oldData, newData);
    }

    //是否要移动
    protected boolean isDetectMoves() {
        return true;
    }


    protected void convert(K helper, T item, int position) {

    }

    /**
     * @param newData     刷新的数据
     * @param newHeader   刷新数据顶部的头,如果不需要传空就行了
     * @param type        刷新数据的类型,切忌,传头部类型是报错的,只要关心数据类型就行了
     * @param refreshType 刷新类型
     */
    protected void notifyMoudleChanged(List<T> newData, T newHeader, int type, int refreshType) {

        if (!isCanRefresh) {
            return;
        }
        isCanRefresh = false;

        startRefresh(newData, newHeader, type, refreshType);
    }

    protected abstract void startRefresh(List<T> newData, T newHeader, int type, int refreshType);

    protected void handleResult(DiffUtil.DiffResult diffResult) {
        diffResult.dispatchUpdatesTo(this);
        isCanRefresh = true;
    }

    protected final DiffUtil.DiffResult handleRefresh(List<T> newData, T newHeader, int type, int refreshType) {
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
                    mData.removeAll(oldItemData);
                }
            }

            T oldItemHeader = oldLevelData.getHeader();
            if (oldItemHeader != null) {
                if (refreshType == REFRESH_DATA) {
                    oldHeader = oldItemHeader;
                } else {
                    mData.remove(oldItemHeader);
                }
            }
        }
        int positionStart = sum + getHeaderLayoutCount();
        boolean isDataEmpty = newData == null || newData.isEmpty();
        boolean isHeaderEmpty = newHeader == null;

        if (!isDataEmpty && refreshType != REFRESH_HEADER) {
            mData.addAll(positionStart, newData);
        }
        if (!isHeaderEmpty && refreshType != REFRESH_DATA) {
            mData.add(positionStart, newHeader);
        }

        DiffUtil.DiffResult result = DiffUtil.calculateDiff(getDiffCallBack(mOldData, mData), isDetectMoves());
        mLevelOldData.put(level, new LevelData<T>(refreshType != REFRESH_HEADER ? newData : oldData, refreshType != REFRESH_DATA ? newHeader : oldHeader));
        mOldData.clear();
        mOldData.addAll(mData);
        return result;
    }

    private int getLayoutId(int viewType) {
        return mLayouts.get(viewType);
    }

    private int getLevel(int type) {
        int level = mLevels.get(type, DEFAULT_HEADER_LEVEL);
        if (level <= DEFAULT_HEADER_LEVEL) {
            throw new RuntimeException("boy , are you sure register this data type (not include header type) ?");
        }
        return level;
    }
}
