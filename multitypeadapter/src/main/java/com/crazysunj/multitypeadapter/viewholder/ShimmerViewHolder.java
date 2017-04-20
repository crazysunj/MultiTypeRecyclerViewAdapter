/**
 * Copyright 2017 Harish Sridharan
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.crazysunj.multitypeadapter.viewholder;

import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.shimmer.ShimmerFrameLayout;

/**
 * Created by sharish on 22/11/16.
 */

public class ShimmerViewHolder extends BaseViewHolder {


    public ShimmerViewHolder(View view) {
        super(view);
        if (view instanceof ShimmerFrameLayout) {
            ((ShimmerFrameLayout) view).setAutoStart(false);
        }
    }

    public void startAnim() {

        if (itemView instanceof ShimmerFrameLayout) {
            ((ShimmerFrameLayout) itemView).startShimmerAnimation();
        }
    }
}
