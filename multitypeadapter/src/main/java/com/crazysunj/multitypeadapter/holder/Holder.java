package com.crazysunj.multitypeadapter.holder;

import androidx.annotation.Nullable;

/**
 * @author sunjian
 * @date 2019-09-11 16:08
 */
public interface Holder<T> {
    T apply(@Nullable T data);
}
