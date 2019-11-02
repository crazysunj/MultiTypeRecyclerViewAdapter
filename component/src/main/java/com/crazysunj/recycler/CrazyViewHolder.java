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

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crazysunj.multitypeadapter.entity.MultiTypeEntity;

/**
 * 基础封装ViewHolder，直接采用ViewHolder有个比较大的缺点是不能直接进到对应layout的xml文件
 *
 * @author sunjian
 * @date 2019-10-18 14:38
 */
public class CrazyViewHolder<T extends MultiTypeEntity> extends RecyclerView.ViewHolder {

    public CrazyViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    /**
     * 子组件UI处理
     *
     * @param data     数据
     * @param position 下标
     */
    protected void showData(T data, int position) {
    }

    /**
     * {@link androidx.recyclerview.widget.RecyclerView.Adapter#onViewAttachedToWindow(RecyclerView.ViewHolder)}
     */
    protected void onViewAttachedToWindow() {
    }

    /**
     * {@link androidx.recyclerview.widget.RecyclerView.Adapter#onViewDetachedFromWindow(RecyclerView.ViewHolder)}
     */
    protected void onViewDetachedFromWindow() {
    }

    /**
     * {@link androidx.recyclerview.widget.RecyclerView.Adapter#onViewRecycled(RecyclerView.ViewHolder)}
     */
    protected void onViewRecycled() {
    }
}
