
package com.crazysunj.multitypeadapter.sticky;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * 粘性头部适配器接口
 */
public interface StickyHeaderAdapter<T extends RecyclerView.ViewHolder> {

    long getHeaderId(int position);

    T onCreateHeaderViewHolder(ViewGroup parent);

    void onBindHeaderViewHolder(T holder, int position) throws Exception;
}
