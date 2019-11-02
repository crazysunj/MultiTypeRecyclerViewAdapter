package com.crazysunj.multityperecyclerviewadapter.component;

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
@BindLayout(layoutId = R.layout.item_type_three)
public class TypeThreeViewHolder extends BaseViewHolder<MultiTypeEntity> {

    public TypeThreeViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    protected void showData(MultiTypeEntity data, int position) {
        super.showData(data, position);
        ((TextView) itemView).setText("type:" + data.getItemType());
    }
}
