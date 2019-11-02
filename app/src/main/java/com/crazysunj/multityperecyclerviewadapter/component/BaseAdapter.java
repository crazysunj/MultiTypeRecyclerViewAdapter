package com.crazysunj.multityperecyclerviewadapter.component;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.crazysunj.multitypeadapter.entity.MultiTypeEntity;
import com.crazysunj.multitypeadapter.helper.RecyclerViewAdapterHelper;
import com.crazysunj.recycler.CrazyAdapter;
import com.crazysunj.recycler.CrazyViewHolder;

/**
 * @author sunjian
 * @date 2019-10-18 20:02
 */
public class BaseAdapter<T extends MultiTypeEntity> extends CrazyAdapter<T> implements ComponentSubject {

    private OnEventCallback mEventCallback;
    private boolean isVisible;
    private LoadMoreHelper mLoadMoreHelper;

    public BaseAdapter(@NonNull RecyclerViewAdapterHelper<T> helper) {
        super(helper);
    }

    @Override
    protected void init() {
        super.init();
        // MTRVA会接管资源，如果是标准模式（采用level的）想绕过，只能写一个空注册，否则可能会有异常
//        mHelper.registerModule(Integer.MIN_VALUE).register();
        mLoadMoreHelper = new LoadMoreHelper();
        register(LoadMoreViewHolder.ITEM_TYPE_LOAD_MORE, LoadMoreViewHolder.class);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mData.size()) {
            return LoadMoreViewHolder.ITEM_TYPE_LOAD_MORE;
        }
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(@NonNull CrazyViewHolder holder, int position) {
        if (position == mData.size()) {
            return;
        }
        super.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    @Override
    protected void onViewHolderCreated(@NonNull CrazyViewHolder holder) {
        super.onViewHolderCreated(holder);
        if (holder instanceof BaseViewHolder) {
            ((BaseViewHolder) holder).subscribe(this);
        }
    }

    /**
     * 在主容器可见的时候调用
     */
    public void onVisible() {
        isVisible = true;
    }

    /**
     * 在主容器不可见的时候调用
     */
    public void onInVisible() {
        isVisible = false;
    }

    @Override
    public int loadMoreState() {
        return mLoadMoreHelper.loadMoreState();
    }

    @Override
    public boolean isVisible() {
        return isVisible;
    }

    public LoadMoreHelper getLoadMoreHelper() {
        return mLoadMoreHelper;
    }

    /**
     * 主容器注册子组件的事件，自己需要单独处理的直接在notifyEvent方法中使用
     *
     * @param callback 事件回调
     */
    public void registerEvent(@NonNull OnEventCallback callback) {
        mEventCallback = callback;
    }

    @Override
    public void notifyEvent(@NonNull Object event, @Nullable IEvent iEvent) {
        if (mEventCallback != null) {
            mEventCallback.onEvent(event, iEvent);
        }
        if (event instanceof LoadMore && iEvent instanceof LoadMoreEvent) {
            mLoadMoreHelper.handleLoadMoreEvent((LoadMore) event, (LoadMoreEvent) iEvent);
        }
    }

    public void setOnLoadMoreRequestListener(LoadMoreHelper.OnLoadMoreRequestListener listener) {
        mLoadMoreHelper.setOnLoadMoreRequestListener(listener);
    }

    public interface OnEventCallback {
        void onEvent(@NonNull Object event, @Nullable IEvent iEvent);
    }
}
