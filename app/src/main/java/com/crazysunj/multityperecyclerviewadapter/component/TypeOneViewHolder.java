package com.crazysunj.multityperecyclerviewadapter.component;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.crazysunj.multitypeadapter.entity.MultiTypeEntity;
import com.crazysunj.multityperecyclerviewadapter.R;
import com.crazysunj.recycler.BindLayout;

/**
 * @author sunjian
 * @date 2019-10-18 19:13
 */
@BindLayout(layoutId = R.layout.item_type_one)
public class TypeOneViewHolder extends BaseViewHolder<MultiTypeEntity> {

    public TypeOneViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    protected void showData(MultiTypeEntity data, int position) {
        super.showData(data, position);
        ((TextView) itemView).setText("type:" + data.getItemType() + " isVisible:" + mSubject.isVisible());
    }

    @Override
    protected void onViewAttachedToWindow() {
        super.onViewAttachedToWindow();
        Log.d("TypeOneViewHolder", "onViewAttachedToWindow");
    }

    @Override
    protected void onViewDetachedFromWindow() {
        super.onViewDetachedFromWindow();
        Log.d("TypeOneViewHolder", "onViewDetachedFromWindow");
    }

    @Override
    protected void onViewRecycled() {
        super.onViewRecycled();
        Log.d("TypeOneViewHolder", "onViewRecycled");
    }
}
