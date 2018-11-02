package com.crazysunj.multityperecyclerviewadapter.apt;

import com.crazysunj.multitypeadapter.annotation.AdapterHelper;
import com.crazysunj.multitypeadapter.annotation.BindDefaultLevel;
import com.crazysunj.multityperecyclerviewadapter.R;
import com.crazysunj.multityperecyclerviewadapter.data.FirstItem;
import com.crazysunj.multityperecyclerviewadapter.helper.BaseHelperAdapter;
import com.crazysunj.multityperecyclerviewadapter.helper.BaseViewHolder;
import com.crazysunj.multityperecyclerviewadapter.helper.ShimmerViewHolder;

import androidx.annotation.NonNull;

/**
 * description
 * <p>
 * Created by sunjian on 2017/5/4.
 */

@AdapterHelper(entity = "com.crazysunj.multityperecyclerviewadapter.data.FirstItem")
public class AptDefaultHelperAdapter extends BaseHelperAdapter<FirstItem, ShimmerViewHolder, AptDefaultHelperAdapterHelper> {

    @BindDefaultLevel()
    final int LAYOUT_RES_ID = R.layout.item_first;

    public AptDefaultHelperAdapter(AptDefaultHelperAdapterHelper helper) {
        super(helper);
        helper.bindAdapter(this);
        mHelper = helper;
    }

    @Override
    protected void convert(ShimmerViewHolder holder, FirstItem item) {
        renderFirst(holder, item);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ShimmerViewHolder holder) {
        holder.startAnim();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ShimmerViewHolder holder) {
        holder.stopAnim();
    }

    private void renderFirst(BaseViewHolder helper, FirstItem item) {
        helper.setText(R.id.item_name, item.getName());
        helper.setImageResource(R.id.item_img, item.getImg());
    }
}
