package com.crazysunj.multityperecyclerviewadapter.apt;

import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.crazysunj.multitypeadapter.annotation.AdapterHelper;
import com.crazysunj.multitypeadapter.annotation.BindDefaultLevel;
import com.crazysunj.multityperecyclerviewadapter.R;
import com.crazysunj.multityperecyclerviewadapter.data.FirstItem;
import com.crazysunj.multityperecyclerviewadapter.helper.ShimmerViewHolder;

/**
 * description
 * <p>
 * Created by sunjian on 2017/5/4.
 */

@AdapterHelper(entity = "com.crazysunj.multityperecyclerviewadapter.data.FirstItem",
        adapter = "com.crazysunj.multityperecyclerviewadapter.apt.AptDefaultHelperAdapter")
public class AptDefaultHelperAdapter extends BaseQuickAdapter<FirstItem, ShimmerViewHolder> {

    @BindDefaultLevel()
    final int LAYOUT_RES_ID = R.layout.item_first;

    private AptDefaultHelperAdapterHelper mHelper;

    public AptDefaultHelperAdapter(AptDefaultHelperAdapterHelper helper) {

        super(helper.getData());
        helper.bindAdapter(this);
        mHelper = helper;
    }

    @Override
    protected ShimmerViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        return createBaseViewHolder(parent, mHelper.getLayoutId(viewType));
    }

    @Override
    protected int getDefItemViewType(int position) {
        return mHelper.getItemViewType(position);
    }

    @Override
    protected void convert(ShimmerViewHolder helper, FirstItem item) {

        renderFirst(helper, item);
    }

    @Override
    public void onViewAttachedToWindow(ShimmerViewHolder holder) {
        holder.startAnim();
    }

    @Override
    public void onViewDetachedFromWindow(ShimmerViewHolder holder) {
        holder.stopAnim();
    }

    private void renderFirst(BaseViewHolder helper, FirstItem item) {
        helper.setText(R.id.item_name, item.getName());
        helper.setImageResource(R.id.item_img, item.getImg());
    }
}
