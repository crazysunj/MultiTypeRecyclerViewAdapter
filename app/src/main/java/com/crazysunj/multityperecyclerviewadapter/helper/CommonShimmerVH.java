package com.crazysunj.multityperecyclerviewadapter.helper;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.facebook.shimmer.ShimmerFrameLayout;

public class CommonShimmerVH extends RecyclerView.ViewHolder {


    public CommonShimmerVH(View view) {
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
