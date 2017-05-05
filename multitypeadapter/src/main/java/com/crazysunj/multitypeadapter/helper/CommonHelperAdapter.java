/**
 * Copyright 2017 Sun Jian
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.crazysunj.multitypeadapter.helper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crazysunj.multitypeadapter.entity.MultiHeaderEntity;
import com.crazysunj.multitypeadapter.holder.CommonViewHolder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * description
 * <p>
 * 一般RecyclerViewAdapter结合helper使用
 * Created by sunjian on 2017/5/4.
 */

public abstract class CommonHelperAdapter<T extends MultiHeaderEntity, K extends CommonViewHolder> extends RecyclerView.Adapter<K> {

    protected Context mContext;
    protected LayoutInflater mLayoutInflater;
    protected List<T> mData;
    protected RecyclerViewAdapterHelper<T> mHelper;

    public CommonHelperAdapter(RecyclerViewAdapterHelper<T> helper) {

        mData = helper.getData();
        helper.bindAdapter(this);
        mHelper = helper;
    }

    @Override
    public int getItemViewType(int position) {
        return mHelper.getItemViewType(position);
    }

    @Override
    public K onCreateViewHolder(ViewGroup parent, int viewType) {
        return createCommonViewHolder(parent, mHelper.getLayoutId(viewType));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    protected K createCommonViewHolder(ViewGroup parent, int layoutResId) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        if (mLayoutInflater == null) {
            mLayoutInflater = LayoutInflater.from(mContext);
        }
        return createCommonViewHolder(mLayoutInflater.inflate(layoutResId, parent, false));
    }

    @SuppressWarnings("unchecked")
    protected K createCommonViewHolder(View view) {
        Class temp = getClass();
        Class z = null;
        while (z == null && null != temp) {
            z = getInstancedGenericKClass(temp);
            temp = temp.getSuperclass();
        }
        K k = createGenericKInstance(z, view);
        return null != k ? k : (K) new CommonViewHolder(view);
    }

    /**
     * try to create Generic K instance
     *
     * @param z
     * @param view
     * @return
     */
    @SuppressWarnings("unchecked")
    private K createGenericKInstance(Class z, View view) {
        try {
            Constructor constructor;
            String buffer = Modifier.toString(z.getModifiers());
            String className = z.getName();
            // inner and unstatic class
            if (className.contains("$") && !buffer.contains("static")) {
                constructor = z.getDeclaredConstructor(getClass(), View.class);
                return (K) constructor.newInstance(this, view);
            } else {
                constructor = z.getDeclaredConstructor(View.class);
                return (K) constructor.newInstance(view);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * get generic parameter K
     *
     * @param z
     * @return
     */
    private Class getInstancedGenericKClass(Class z) {
        Type type = z.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
            for (Type temp : types) {
                if (temp instanceof Class) {
                    Class tempClass = (Class) temp;
                    if (CommonViewHolder.class.isAssignableFrom(tempClass)) {
                        return tempClass;
                    }
                }
            }
        }
        return null;
    }

}
