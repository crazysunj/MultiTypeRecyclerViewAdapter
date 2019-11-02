/*
  Copyright 2017 Sun Jian
  <p>
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  <p>
  http://www.apache.org/licenses/LICENSE-2.0
  <p>
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.crazysunj.recycler;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crazysunj.multitypeadapter.entity.MultiTypeEntity;
import com.crazysunj.multitypeadapter.helper.RecyclerViewAdapterHelper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.List;

/**
 * 组件化Adapter封装，配合[MTRVA](https://github.com/crazysunj/MultiTypeRecyclerViewAdapter)
 *
 * @author sunjian
 * @date 2019-10-18 14:35
 */
public class CrazyAdapter<T extends MultiTypeEntity> extends RecyclerView.Adapter<CrazyViewHolder> {

    private SparseArray<Class<? extends CrazyViewHolder>> mResourceManager;
    protected List<T> mData;
    protected RecyclerViewAdapterHelper<T> mHelper;
    protected Context mContext;

    public CrazyAdapter(@NonNull RecyclerViewAdapterHelper<T> helper) {
        this(helper, new SparseArray<Class<? extends CrazyViewHolder>>());
    }

    public CrazyAdapter(@NonNull RecyclerViewAdapterHelper<T> helper, @NonNull SparseArray<Class<? extends CrazyViewHolder>> manager) {
        mHelper = helper;
        mHelper.bindAdapter(this);
        mData = mHelper.getData();
        mResourceManager = manager;
        init();
    }

    /**
     * 如果需要初始化一些东西，可以在这写
     */
    protected void init() {
    }

    /**
     * 注册子组件，这里需要注意的是如果使用了level的header或者footer也需要注册对应子组件
     *
     * @param itemType type
     * @param vhClass  子组件
     */
    public final void register(int itemType, Class<? extends CrazyViewHolder> vhClass) {
        mResourceManager.put(itemType, vhClass);
    }

    public RecyclerViewAdapterHelper<T> getHelper() {
        return mHelper;
    }

    public List<T> getData() {
        return mData;
    }

    @Override
    public void onViewAttachedToWindow(@NonNull CrazyViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.onViewAttachedToWindow();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull CrazyViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.onViewDetachedFromWindow();
    }

    @Override
    public void onViewRecycled(@NonNull CrazyViewHolder holder) {
        super.onViewRecycled(holder);
        holder.onViewRecycled();
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public final CrazyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        Class<? extends CrazyViewHolder> vhClass = mResourceManager.get(viewType);
        BindLayout layoutAnnotation = vhClass.getAnnotation(BindLayout.class);
        int layoutId;
        if (layoutAnnotation == null) {
            layoutId = mHelper.getLayoutId(viewType);
        } else {
            layoutId = layoutAnnotation.layoutId();
        }
        if (layoutId == 0) {
            throw new RuntimeException("请在子组件中使用BindLayout注解绑定layoutId或者通过AdapterHelper注册layoutId");
        }
        View view = getItemView(inflater, parent, layoutId);
        CrazyViewHolder viewHolder = createCrazyViewHolder(vhClass, view);
        CrazyViewHolder vh = viewHolder == null ? new CrazyViewHolder(view) : viewHolder;
        onViewHolderCreated(vh);
        return vh;
    }

    protected void onViewHolderCreated(@NonNull CrazyViewHolder holder) {
    }

    @SuppressWarnings("unchecked")
    @Override
    @CallSuper
    public void onBindViewHolder(@NonNull CrazyViewHolder holder, int position) {
        holder.showData(mData.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mHelper.getItemViewType(position);
    }

    protected View getItemView(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, @LayoutRes int layoutId) {
        return inflater.inflate(layoutId, parent, false);
    }

    /**
     * 反射创建ViewHolder
     */
    @SuppressWarnings("unchecked")
    private CrazyViewHolder createCrazyViewHolder(Class z, View view) {
        try {
            Constructor constructor;
            if (z.isMemberClass() && !Modifier.isStatic(z.getModifiers())) {
                constructor = z.getDeclaredConstructor(getClass(), View.class);
                constructor.setAccessible(true);
                return (CrazyViewHolder) constructor.newInstance(this, view);
            } else {
                constructor = z.getDeclaredConstructor(View.class);
                constructor.setAccessible(true);
                return (CrazyViewHolder) constructor.newInstance(view);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
