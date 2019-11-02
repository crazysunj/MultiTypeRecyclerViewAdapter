package com.crazysunj.multityperecyclerviewadapter.component;

import androidx.annotation.NonNull;

import java.util.HashSet;
import java.util.Set;

/**
 * @author sunjian
 * @date 2019-10-22 14:18
 */
public class LoadMoreHelper {
    private Set<LoadMoreEvent> loadMoreEvents;
    private boolean loadMoreEnable = false;
    private OnLoadMoreRequestListener mOnLoadMoreRequestListener;
    private int loadMoreState;

    public void setLoadMoreEnable(boolean loadMoreEnable) {
        if (this.loadMoreEnable == loadMoreEnable) {
            return;
        }
        this.loadMoreEnable = loadMoreEnable;
        if (loadMoreEnable) {
            loadMoreDefault();
            return;
        }
        loadMoreGone();
    }

    public int loadMoreState() {
        return loadMoreState;
    }

    public void loadMoreGone() {
        loadMoreState = LoadMoreViewHolder.STATE_GONE;
        if (loadMoreEvents == null) {
            return;
        }
        for (LoadMoreEvent event : loadMoreEvents) {
            event.onGone();
        }
    }

    private void loadMoreLoading() {
        loadMoreState = LoadMoreViewHolder.STATE_LOADING;
        if (loadMoreEvents == null) {
            return;
        }
        for (LoadMoreEvent event : loadMoreEvents) {
            event.onLoading();
        }
    }

    public void loadMoreCompleted() {
        loadMoreState = LoadMoreViewHolder.STATE_COMPLETED;
        if (loadMoreEvents == null) {
            return;
        }
        for (LoadMoreEvent event : loadMoreEvents) {
            event.onCompleted();
        }
    }

    public void loadMoreFailed() {
        loadMoreState = LoadMoreViewHolder.STATE_FAILED;
        if (loadMoreEvents == null) {
            return;
        }
        for (LoadMoreEvent event : loadMoreEvents) {
            event.onFailed();
        }
    }

    public void loadMoreEnd() {
        loadMoreState = LoadMoreViewHolder.STATE_END;
        if (loadMoreEvents == null) {
            return;
        }
        for (LoadMoreEvent event : loadMoreEvents) {
            event.onEnd();
        }
    }

    public void loadMoreDefault() {
        loadMoreState = LoadMoreViewHolder.STATE_DEFAULT;
        if (loadMoreEvents == null) {
            return;
        }
        for (LoadMoreEvent event : loadMoreEvents) {
            event.onDefault();
        }
    }

    public void handleLoadMoreEvent(@NonNull LoadMore loadMore, @NonNull LoadMoreEvent event) {
        if (loadMore.state == LoadMore.STATE_INIT) {
            if (loadMoreEvents == null) {
                loadMoreEvents = new HashSet<>();
            }
            loadMoreEvents.add(event);
            return;
        }
        if (loadMore.state == LoadMore.STATE_LOADING) {
            if (mOnLoadMoreRequestListener != null) {
                mOnLoadMoreRequestListener.onLoadMore();
            }
        }
    }

    public void setOnLoadMoreRequestListener(OnLoadMoreRequestListener listener) {
        mOnLoadMoreRequestListener = listener;
    }

    public interface OnLoadMoreRequestListener {
        void onLoadMore();
    }
}
