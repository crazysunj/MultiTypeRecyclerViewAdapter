package com.crazysunj.multityperecyclerviewadapter.helper;

import android.view.View;

import com.facebook.shimmer.ShimmerFrameLayout;

public class ShimmerViewHolder extends BaseViewHolder {

    public ShimmerViewHolder(View view) {
        super(view);
        if (view instanceof ShimmerFrameLayout) {
            final ShimmerFrameLayout shimmerView = (ShimmerFrameLayout) view;
            shimmerView.setAutoStart(false);
        }
    }

    public void startAnim() {

        if (itemView instanceof ShimmerFrameLayout) {
            final ShimmerFrameLayout shimmerView = (ShimmerFrameLayout) itemView;
            if (!shimmerView.isAnimationStarted()) {
                shimmerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        shimmerView.startShimmerAnimation();
                    }
                }, 100);

            }
        }
    }

    public void stopAnim() {

        if (itemView instanceof ShimmerFrameLayout) {
            final ShimmerFrameLayout shimmerView = (ShimmerFrameLayout) itemView;
            if (shimmerView.isAnimationStarted()) {
                shimmerView.setAutoStart(false);
            }
        }
    }
}
