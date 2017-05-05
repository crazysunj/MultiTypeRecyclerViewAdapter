package com.crazysunj.multityperecyclerviewadapter.helper;

import android.view.View;

import com.crazysunj.multitypeadapter.holder.CommonViewHolder;
import com.facebook.shimmer.ShimmerFrameLayout;

public class CommonShimmerVH extends CommonViewHolder {


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
