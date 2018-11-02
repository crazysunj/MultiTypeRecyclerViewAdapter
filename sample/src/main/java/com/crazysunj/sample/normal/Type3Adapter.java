package com.crazysunj.sample.normal;

import com.crazysunj.sample.R;
import com.crazysunj.sample.base.BaseAdapter;
import com.crazysunj.sample.base.BaseViewHolder;
import com.crazysunj.sample.entity.ItemEntity4;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * author: sunjian
 * created on: 2017/8/22 下午2:23
 * description:
 */

public class Type3Adapter extends BaseAdapter<ItemEntity4, BaseViewHolder> {

    public Type3Adapter(@Nullable List<ItemEntity4> data) {
        super(data, R.layout.item_4);
    }

    @Override
    protected void convert(BaseViewHolder helper, ItemEntity4 item) {
        helper.setImageResource(R.id.item_4_icon, item.getImg());
        helper.setText(R.id.item_4_title, item.getTitle());
        helper.setText(R.id.item_4_name, item.getName());
        helper.setText(R.id.item_4_des, item.getDes());
        helper.setText(R.id.item_4_tag, item.getTag());
        helper.setText(R.id.item_4_people, item.getPeople());
        helper.setText(R.id.item_4_price, item.getPrice());
    }
}
