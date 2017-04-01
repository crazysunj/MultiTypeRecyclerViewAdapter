package com.crazysunj.multitypeadapter;

import android.app.Dialog;
import android.support.annotation.LayoutRes;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.crazysunj.multitypeadapter.entity.LevelData;
import com.crazysunj.multitypeadapter.entity.MultiHeaderEntity;
import com.crazysunj.multitypeadapter.sticky.StickyHeaderAdapter;
import com.crazysunj.multitypeadapter.sticky.StickyHeaderDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * 使用粘性头部，请调用bindToRecyclerView,否则自己添加decoration
 * Created by sunjian on 2017/3/27.
 */

public abstract class BaseMultiTypeRecyclerViewAdapter<T extends MultiHeaderEntity, K extends BaseViewHolder>
        extends BaseQuickAdapter<T, K> implements StickyHeaderAdapter<K> {

    private static final int DEFAULT_VIEW_TYPE = -0xff;
    private static final int DEFAULT_VIEW_LEVEL = 0;
    public static final int DEFAULT_HEADER_LEVEL = -1;
    public static final int DEFAULT_HEADER_TYPE = -1;
    public static final int REFRESH_HEADER = 0;
    public static final int REFRESH_DATA = 1;
    public static final int REFRESH_HEADER_DATA = 2;

    private boolean isCanRefresh = true;
    private long mMaxHeaderId = -1;
    private long mMinHeaderId = Long.MIN_VALUE;
    private boolean mIsUseStickyHeader = false;

    private Dialog mDialog;

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
     * @param type
     * @param level       如果是头部，瞎JB传个负数就行了，但类型得分清楚
     * @param layoutResId
     */
    public void registerMoudle(int type, int level, @LayoutRes int layoutResId) {
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

    public void handleMoudleWithProgress(Dialog dialog) {
        mDialog = dialog;
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

    public void notifyMoudleDataChanged(List<T> data, int type) {
        notifyMoudleChanged(data, null, type, REFRESH_DATA);
    }

    public void notifyMoudleHeaderChanged(T header, int type) {
        notifyMoudleChanged(null, header, type, REFRESH_HEADER);
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

    protected void setDefaultViewTypeLayout(@LayoutRes int layoutResId) {
        registerMoudle(DEFAULT_VIEW_TYPE, DEFAULT_VIEW_LEVEL, layoutResId);
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
        dismissDialog();
        isCanRefresh = true;
    }

    protected void showDialog() {
        if (mDialog != null) {
            mDialog.show();
        }
    }

    protected void dismissDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
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
