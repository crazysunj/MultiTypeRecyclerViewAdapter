package com.crazysunj.multityperecyclerviewadapter.component;

/**
 * @author sunjian
 * @date 2019-10-21 20:01
 */
public interface LoadMoreEvent extends IEvent {
    void onDefault();

    void onGone();

    void onLoading();

    void onFailed();

    void onCompleted();

    void onEnd();
}
