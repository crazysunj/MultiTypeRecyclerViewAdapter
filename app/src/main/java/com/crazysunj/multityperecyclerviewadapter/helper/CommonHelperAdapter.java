package com.crazysunj.multityperecyclerviewadapter.helper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseViewHolder;
import com.crazysunj.multitypeadapter.entity.MultiHeaderEntity;
import com.crazysunj.multitypeadapter.helper.RecyclerViewAdapterHelper;
import com.crazysunj.multitypeadapter.helper.SynAdapterHelper;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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

public abstract class CommonHelperAdapter<T extends MultiHeaderEntity, K extends CommonShimmerVH> extends RecyclerView.Adapter<K> {

    protected Context mContext;
    protected LayoutInflater mLayoutInflater;
    protected List<T> mData;
    protected RecyclerViewAdapterHelper<T> mHelper;

    public CommonHelperAdapter(RecyclerViewAdapterHelper<T> helper) {

        mData = helper.getData();
        mHelper = helper == null ? new SynAdapterHelper<T>(null) : helper;
    }

    @Override
    public K onCreateViewHolder(ViewGroup parent, int viewType) {
        return createCommonShimmerVH(parent, mHelper.getLayoutId(viewType));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    protected K createCommonShimmerVH(ViewGroup parent, int layoutResId) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        if (mLayoutInflater == null) {
            mLayoutInflater = LayoutInflater.from(mContext);
        }
        return createCommonShimmerVH(mLayoutInflater.inflate(layoutResId, parent, false));
    }

    protected K createCommonShimmerVH(View view) {
        Class temp = getClass();
        Class z = null;
        while (z == null && null != temp) {
            z = getInstancedGenericKClass(temp);
            temp = temp.getSuperclass();
        }
        K k = createGenericKInstance(z, view);
        return null != k ? k : (K) new CommonShimmerVH(view);
    }

    /**
     * try to create Generic K instance
     *
     * @param z
     * @param view
     * @return
     */
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
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
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
                    if (BaseViewHolder.class.isAssignableFrom(tempClass)) {
                        return tempClass;
                    }
                }
            }
        }
        return null;
    }

}
