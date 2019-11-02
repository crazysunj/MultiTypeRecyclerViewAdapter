package com.crazysunj.multityperecyclerviewadapter.component;

import android.view.View;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;

import com.crazysunj.multitypeadapter.entity.MultiTypeEntity;
import com.crazysunj.recycler.CrazyViewHolder;

/**
 * @author sunjian
 * @date 2019-10-18 20:03
 */
public class BaseViewHolder<T extends MultiTypeEntity> extends CrazyViewHolder<T> {

    protected ComponentSubject mSubject;

    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    @CallSuper
    protected void subscribe(@NonNull ComponentSubject subject) {
        mSubject = subject;
    }
}
