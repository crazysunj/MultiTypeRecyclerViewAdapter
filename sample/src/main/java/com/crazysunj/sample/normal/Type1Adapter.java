package com.crazysunj.sample.normal;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.crazysunj.sample.R;
import com.crazysunj.sample.entity.ItemEntity1;

import java.util.List;

/**
 * author: sunjian
 * created on: 2017/8/22 下午2:23
 * description:
 */

public class Type1Adapter extends BaseQuickAdapter<ItemEntity1, BaseViewHolder> {

    public Type1Adapter(@Nullable List<ItemEntity1> data) {
        super(R.layout.item_1, data);
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
