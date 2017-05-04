package com.crazysunj.multityperecyclerviewadapter.helper;

import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.shimmer.ShimmerFrameLayout;

public class ShimmerViewHolder extends BaseViewHolder {


    public ShimmerViewHolder(View view) {
        super(view);
        if (view instanceof ShimmerFrameLayout) {
            ((ShimmerFrameLayout) view).setAutoStart(true);
        }
    }

    public void startAnim() {

        if (itemView instanceof ShimmerFrameLayout) {
            ((ShimmerFrameLayout) itemView).startShimmerAnimation();
        }
    }
}
