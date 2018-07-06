package com.crazysunj.multityperecyclerviewadapter.testlevel;


import com.crazysunj.multityperecyclerviewadapter.helper.BaseViewHolder;

/**
 * author: sunjian
 * created on: 2018/4/3 下午1:32
 * description:
 */

public interface ItemConvert<T, K extends BaseViewHolder> {
    void convert(K helper, T item);
}
