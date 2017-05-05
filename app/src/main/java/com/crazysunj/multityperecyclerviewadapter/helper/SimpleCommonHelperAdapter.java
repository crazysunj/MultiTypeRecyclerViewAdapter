package com.crazysunj.multityperecyclerviewadapter.helper;

import android.view.ViewGroup;

import com.crazysunj.multitypeadapter.entity.MultiHeaderEntity;
import com.crazysunj.multitypeadapter.helper.CommonHelperAdapter;
import com.crazysunj.multitypeadapter.helper.RecyclerViewAdapterHelper;
import com.crazysunj.multitypeadapter.sticky.StickyHeaderAdapter;
import com.crazysunj.multityperecyclerviewadapter.R;
import com.crazysunj.multityperecyclerviewadapter.data.FirstItem;
import com.crazysunj.multityperecyclerviewadapter.data.FourthItem;
import com.crazysunj.multityperecyclerviewadapter.data.SecondItem;
import com.crazysunj.multityperecyclerviewadapter.data.ThirdItem;
import com.crazysunj.multityperecyclerviewadapter.header.HeaderFirstItem;
import com.crazysunj.multityperecyclerviewadapter.header.HeaderFourthItem;
import com.crazysunj.multityperecyclerviewadapter.header.HeaderSecondItem;
import com.crazysunj.multityperecyclerviewadapter.header.HeaderThirdItem;
import com.crazysunj.multityperecyclerviewadapter.sticky.FirstStickyItem;
import com.crazysunj.multityperecyclerviewadapter.sticky.FourthStickyItem;
import com.crazysunj.multityperecyclerviewadapter.sticky.SecondStickyItem;
import com.crazysunj.multityperecyclerviewadapter.sticky.ThirdStickyItem;

/**
 * description
 * <p>
 * Created by sunjian on 2017/5/4.
 */

public class SimpleCommonHelperAdapter extends CommonHelperAdapter<MultiHeaderEntity, CommonShimmerVH>
        implements StickyHeaderAdapter<CommonShimmerVH> {

    public SimpleCommonHelperAdapter(RecyclerViewAdapterHelper<MultiHeaderEntity> helper) {
        super(helper);
    }

    @Override
    public void onBindViewHolder(CommonShimmerVH holder, int position) {

        MultiHeaderEntity item = mData.get(position);
        if (item instanceof FirstItem) {
            renderFirst(holder, (FirstItem) item);
        } else if (item instanceof SecondItem) {
            renderSecond(holder, (SecondItem) item);
        } else if (item instanceof ThirdItem) {
            renderThird(holder, (ThirdItem) item);
        } else if (item instanceof FourthItem) {
            renderFourth(holder, (FourthItem) item);
        } else if (item instanceof HeaderFirstItem) {
            renderHeaderFirst(holder, (HeaderFirstItem) item);
        } else if (item instanceof HeaderSecondItem) {
            renderHeaderSecond(holder, (HeaderSecondItem) item);
        } else if (item instanceof HeaderThirdItem) {
            renderHeaderThird(holder, (HeaderThirdItem) item);
        } else if (item instanceof HeaderFourthItem) {
            renderHeaderFourth(holder, (HeaderFourthItem) item);
        }
    }

    @Override
    public long getHeaderId(int position) {
        return mHelper.getHeaderId(position);
    }

    @Override
    public CommonShimmerVH onCreateHeaderViewHolder(ViewGroup parent) {
        return createCommonViewHolder(parent, R.layout.item_header);
    }

    @Override
    public void onBindHeaderViewHolder(CommonShimmerVH holder, int position) throws Exception {
        MultiHeaderEntity stickyEntity = mData.get(position);

        if (stickyEntity instanceof FirstStickyItem) {
            holder.getTextView(R.id.item_header).setText(((FirstStickyItem) stickyEntity).getStickyName());
        } else if (stickyEntity instanceof SecondStickyItem) {
            holder.getTextView(R.id.item_header).setText(((SecondStickyItem) stickyEntity).getStickyName());
        } else if (stickyEntity instanceof ThirdStickyItem) {
            holder.getTextView(R.id.item_header).setText(((ThirdStickyItem) stickyEntity).getStickyName());
        } else if (stickyEntity instanceof FourthStickyItem) {
            holder.getTextView(R.id.item_header).setText(((FourthStickyItem) stickyEntity).getStickyName());
        }
    }

    private void renderHeaderFourth(CommonShimmerVH holder, HeaderFourthItem item) {
        holder.getTextView(R.id.item_header_name).setText(item.getName());
        holder.getImageView(R.id.item_header_img).setImageResource(R.mipmap.ic_launcher);
    }

    private void renderHeaderThird(CommonShimmerVH holder, HeaderThirdItem item) {
        holder.getTextView(R.id.item_header_name).setText(item.getName());
        holder.getImageView(R.id.item_header_img).setImageResource(R.mipmap.ic_launcher);
    }

    private void renderHeaderSecond(CommonShimmerVH holder, HeaderSecondItem item) {
        holder.getTextView(R.id.item_header_name).setText(item.getName());
        holder.getImageView(R.id.item_header_img).setImageResource(R.mipmap.ic_launcher);
    }

    private void renderHeaderFirst(CommonShimmerVH holder, HeaderFirstItem item) {
        holder.getTextView(R.id.item_header).setText(item.getName());
    }

    private void renderFourth(CommonShimmerVH holder, FourthItem item) {
        holder.getTextView(R.id.item_name).setText(item.getName());
        holder.getImageView(R.id.item_img).setImageResource(R.mipmap.ic_launcher);
    }

    private void renderThird(CommonShimmerVH holder, ThirdItem item) {
        holder.getTextView(R.id.item_name).setText(item.getName());
        holder.getImageView(R.id.item_img).setImageResource(R.mipmap.ic_launcher);
    }

    private void renderSecond(CommonShimmerVH holder, SecondItem item) {
        holder.getTextView(R.id.item_name).setText(item.getName());
        holder.getImageView(R.id.item_img).setImageResource(R.mipmap.ic_launcher);
    }

    private void renderFirst(CommonShimmerVH holder, FirstItem item) {
        holder.getTextView(R.id.item_name).setText(item.getName());
        holder.getImageView(R.id.item_img).setImageResource(R.mipmap.ic_launcher);
    }
}
