package com.crazysunj.sample.normal;

import com.crazysunj.sample.R;
import com.crazysunj.sample.base.BaseAdapter;
import com.crazysunj.sample.base.BaseViewHolder;
import com.crazysunj.sample.entity.ItemEntity1;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * author: sunjian
 * created on: 2017/8/22 下午2:23
 * description:
 */

public class Type1Adapter extends BaseAdapter<ItemEntity1, BaseViewHolder> {

    public Type1Adapter(@Nullable List<ItemEntity1> data) {
        super(data, R.layout.item_1);
    }

    @Override
    protected void convert(BaseViewHolder helper, ItemEntity1 item) {
        helper.setImageResource(R.id.item_1_img, item.getImg());
        helper.setText(R.id.item_1_title, item.getTitle());
        helper.setText(R.id.item_1_content, item.getContent());
        helper.setText(R.id.item_1_time, item.getTime());
        helper.setText(R.id.item_1_time_flag, item.getTimeFlag());
    }
}
