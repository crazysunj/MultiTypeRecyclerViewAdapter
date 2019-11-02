package com.crazysunj.multityperecyclerviewadapter.component;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.crazysunj.multityperecyclerviewadapter.R;
import com.crazysunj.recycler.BindLayout;

/**
 * @author sunjian
 * @date 2019-10-21 19:41
 */
@BindLayout(layoutId = R.layout.item_type_load_more)
public class LoadMoreViewHolder extends BaseViewHolder implements LoadMoreEvent {

    static final int ITEM_TYPE_LOAD_MORE = -0x333;

    static final int STATE_GONE = 0x100;
    static final int STATE_LOADING = 0x101;
    static final int STATE_COMPLETED = 0x102;
    static final int STATE_FAILED = 0x103;
    static final int STATE_DEFAULT = 0x104;
    static final int STATE_END = 0x105;

    private int state;
    private LoadMore loadMore;
    private TextView mNotice;
    private ProgressBar mProgress;

    public LoadMoreViewHolder(@NonNull View itemView) {
        super(itemView);
        state = STATE_DEFAULT;
        loadMore = new LoadMore();
        loadMore.state = LoadMore.STATE_INIT;
        mNotice = itemView.findViewById(R.id.load_more_notice);
        mProgress = itemView.findViewById(R.id.load_more_progress);
    }

    @Override
    protected void onViewAttachedToWindow() {
        super.onViewAttachedToWindow();
        if (!(state == STATE_GONE || state == STATE_END || state == STATE_LOADING)) {
            loadMore.state = LoadMore.STATE_LOADING;
            onLoading();
            mSubject.notifyEvent(loadMore, this);
        }
    }

    @Override
    protected void subscribe(@NonNull ComponentSubject subject) {
        super.subscribe(subject);
        state = subject.loadMoreState();
        handleLoadMore();
        subject.notifyEvent(loadMore, this);
    }

    private void handleLoadMore() {
        if (state == STATE_GONE) {
            itemView.setVisibility(View.GONE);
            return;
        }
        if (state == STATE_LOADING) {
            mNotice.setVisibility(View.GONE);
            mProgress.setVisibility(View.VISIBLE);
            return;
        }
        if (state == STATE_COMPLETED) {
            mNotice.setVisibility(View.VISIBLE);
            mNotice.setText("刷新结束");
            mProgress.setVisibility(View.GONE);
            return;
        }
        if (state == STATE_FAILED) {
            mNotice.setVisibility(View.VISIBLE);
            mNotice.setText("刷新失败");
            mProgress.setVisibility(View.GONE);
            return;
        }
        if (state == STATE_END) {
            mNotice.setVisibility(View.VISIBLE);
            mNotice.setText("没有更多了");
            mProgress.setVisibility(View.GONE);
            return;
        }
        mNotice.setVisibility(View.VISIBLE);
        mNotice.setText("上拉加载");
        mProgress.setVisibility(View.GONE);
    }

    @Override
    public void onDefault() {
        state = STATE_DEFAULT;
        handleLoadMore();
    }

    @Override
    public void onGone() {
        state = STATE_GONE;
        handleLoadMore();
    }

    @Override
    public void onLoading() {
        state = STATE_LOADING;
        handleLoadMore();
    }

    @Override
    public void onFailed() {
        state = STATE_FAILED;
        handleLoadMore();
    }

    @Override
    public void onCompleted() {
        state = STATE_COMPLETED;
        handleLoadMore();
    }

    @Override
    public void onEnd() {
        state = STATE_END;
        handleLoadMore();
    }
}
