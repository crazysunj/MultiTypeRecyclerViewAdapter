package com.crazysunj.multityperecyclerviewadapter.component;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 主容器主题，子组件和主容器之间交互桥梁
 *
 * @author sunjian
 * @date 2019-10-18 20:07
 */
public interface ComponentSubject {

    /**
     * @return 主容器上拉加载状态
     */
    int loadMoreState();

    /**
     * @return 当前主容器是否可见
     */
    boolean isVisible();

    /**
     * 事件交互，通过callback可达到循环依赖
     *
     * @param event    事件对象
     * @param callback 事件回调
     */
    void notifyEvent(@NonNull Object event, @Nullable IEvent callback);
}
