package com.crazysunj.multityperecyclerviewadapter.helper;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.crazysunj.multitypeadapter.entity.MultiHeaderEntity;
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
        implements StickyHeaderAdapter<SimpleCommonHelperAdapter.StickyHeaderVH> {

    public SimpleCommonHelperAdapter(RecyclerViewAdapterHelper<MultiHeaderEntity> helper) {
        super(helper);
    }

    @Override
    public void onBindViewHolder(CommonShimmerVH holder, int position) {
        MultiHeaderEntity item = mData.get(position);
        if (item instanceof FirstItem) {
            renderFirst((FirstVH) holder, (FirstItem) item);
        } else if (item instanceof SecondItem) {
            renderSecond((FirstVH) holder, (SecondItem) item);
        } else if (item instanceof ThirdItem) {
            renderThird((FirstVH) holder, (ThirdItem) item);
        } else if (item instanceof FourthItem) {
            renderFourth((FirstVH) holder, (FourthItem) item);
        } else if (item instanceof HeaderFirstItem) {
            renderHeaderFirst((SecondVH) holder, (HeaderFirstItem) item);
        } else if (item instanceof HeaderSecondItem) {
            renderHeaderSecond((FirstVH) holder, (HeaderSecondItem) item);
        } else if (item instanceof HeaderThirdItem) {
            renderHeaderThird((FirstVH) holder, (HeaderThirdItem) item);
        } else if (item instanceof HeaderFourthItem) {
            renderHeaderFourth((FirstVH) holder, (HeaderFourthItem) item);
        }
    }

    @Override
    public long getHeaderId(int position) {
        return mHelper.getHeaderId(position);
    }

    @Override
    public StickyHeaderVH onCreateHeaderViewHolder(ViewGroup parent) {
        return new StickyHeaderVH(mLayoutInflater.inflate(R.layout.item_header, parent, false));
    }

    @Override
    public void onBindHeaderViewHolder(StickyHeaderVH helper, int position) throws Exception {
        MultiHeaderEntity stickyEntity = mData.get(position);

        if (stickyEntity instanceof FirstStickyItem) {
            helper.mStickyHeader.setText(((FirstStickyItem) stickyEntity).getStickyName());
        } else if (stickyEntity instanceof SecondStickyItem) {
            helper.mStickyHeader.setText(((SecondStickyItem) stickyEntity).getStickyName());
        } else if (stickyEntity instanceof ThirdStickyItem) {
            helper.mStickyHeader.setText(((ThirdStickyItem) stickyEntity).getStickyName());
        } else if (stickyEntity instanceof FourthStickyItem) {
            helper.mStickyHeader.setText(((FourthStickyItem) stickyEntity).getStickyName());
        }
    }

    static class StickyHeaderVH extends CommonShimmerVH {

        private TextView mStickyHeader;

        StickyHeaderVH(View view) {
            super(view);
            mStickyHeader = (TextView) view.findViewById(R.id.item_header);
        }
    }

    static class FirstVH extends CommonShimmerVH {

        private TextView mName;
        private ImageView mImg;

        FirstVH(View view) {
            super(view);
            mName = (TextView) view.findViewById(R.id.item_header_name);
            mImg = (ImageView) view.findViewById(R.id.item_header_img);
        }
    }

    static class SecondVH extends CommonShimmerVH {

        private TextView mName;

        SecondVH(View view) {
            super(view);
            mName = (TextView) view.findViewById(R.id.item_header);
        }
    }

    private void renderHeaderFourth(FirstVH holder, HeaderFourthItem item) {
        holder.mName.setText(item.getName());
        holder.mImg.setImageResource(R.mipmap.ic_launcher);
    }

    private void renderHeaderThird(FirstVH holder, HeaderThirdItem item) {
        holder.mName.setText(item.getName());
        holder.mImg.setImageResource(R.mipmap.ic_launcher);
    }

    private void renderHeaderSecond(FirstVH holder, HeaderSecondItem item) {
        holder.mName.setText(item.getName());
        holder.mImg.setImageResource(R.mipmap.ic_launcher);
    }

    private void renderHeaderFirst(SecondVH holder, HeaderFirstItem item) {
        holder.mName.setText(item.getName());
    }

    private void renderFourth(FirstVH holder, FourthItem item) {
        holder.mName.setText(item.getName());
        holder.mImg.setImageResource(item.getImg());
    }

    private void renderThird(FirstVH holder, ThirdItem item) {
        holder.mName.setText(item.getName());
        holder.mImg.setImageResource(item.getImg());
    }

    private void renderSecond(FirstVH holder, SecondItem item) {
        holder.mName.setText(item.getName());
        holder.mImg.setImageResource(item.getImg());
    }

    private void renderFirst(FirstVH holder, FirstItem item) {
        holder.mName.setText(item.getName());
        holder.mImg.setImageResource(item.getImg());
    }
}
