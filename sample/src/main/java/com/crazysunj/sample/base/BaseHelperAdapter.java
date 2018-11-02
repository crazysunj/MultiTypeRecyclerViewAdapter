package com.crazysunj.sample.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crazysunj.multitypeadapter.entity.MultiTypeEntity;
import com.crazysunj.multitypeadapter.helper.RecyclerViewAdapterHelper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * description
 * <p>
 * Created by sunjian on 2017/7/5.
 */
public abstract class BaseHelperAdapter<T extends MultiTypeEntity, VH extends BaseViewHolder, H extends RecyclerViewAdapterHelper<T>> extends RecyclerView.Adapter<VH> {

    protected Context mContext;
    protected LayoutInflater mLayoutInflater;
    protected List<T> mData;
    protected H mHelper;

    public BaseHelperAdapter(H helper) {
        mData = helper.getData();
        helper.bindAdapter(this);
        mHelper = helper;
    }

    @Override
    public int getItemViewType(int position) {
        return mHelper.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        convert(holder, mData.get(position));
    }

    protected abstract void convert(VH holder, T item);

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return createBaseViewHolder(parent, mHelper.getLayoutId(viewType));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    protected VH createBaseViewHolder(ViewGroup parent, int layoutResId) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        if (mLayoutInflater == null) {
            mLayoutInflater = LayoutInflater.from(mContext);
        }
        return createBaseViewHolder(mLayoutInflater.inflate(layoutResId, parent, false));
    }

    @SuppressWarnings("unchecked")
    protected VH createBaseViewHolder(View view) {
        Class temp = getClass();
        Class z = null;
        while (z == null && null != temp) {
            z = getInstancedGenericKClass(temp);
            temp = temp.getSuperclass();
        }
        VH vh = createGenericKInstance(z, view);
        return null != vh ? vh : (VH) new BaseViewHolder(view);
    }

    @SuppressWarnings("unchecked")
    private VH createGenericKInstance(Class z, View view) {
        try {
            Constructor constructor;
            String buffer = Modifier.toString(z.getModifiers());
            String className = z.getName();
            if (className.contains("$") && !buffer.contains("static")) {
                constructor = z.getDeclaredConstructor(getClass(), View.class);
                return (VH) constructor.newInstance(this, view);
            } else {
                constructor = z.getDeclaredConstructor(View.class);
                return (VH) constructor.newInstance(view);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

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
