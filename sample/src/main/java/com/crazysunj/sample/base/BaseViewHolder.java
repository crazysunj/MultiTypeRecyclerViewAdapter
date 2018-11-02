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
package com.crazysunj.sample.base;

import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @author sunjian
 * 常用holder，有具体需求的可以自定义
 * 这里暂提供TextView和ImageView，其他控件可使用{@link #getView(int, Class)}
 * date 2017/5/5
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> mViews;

    public BaseViewHolder(View view) {
        super(view);
        if (mViews == null) {
            mViews = new SparseArray<>();
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId, Class<T> clazz) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public void setText(int viewId, String text) {
        getTextView(viewId).setText(text);
    }

    public void setImageResource(int viewId, int resId) {
        getImageView(viewId).setImageResource(resId);
    }

    public TextView getTextView(int viewId) {
        return getView(viewId, TextView.class);
    }

    public ImageView getImageView(int viewId) {
        return getView(viewId, ImageView.class);
    }

}
