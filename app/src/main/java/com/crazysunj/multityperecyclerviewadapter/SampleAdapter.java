package com.crazysunj.multityperecyclerviewadapter;

import com.chad.library.adapter.base.BaseViewHolder;
import com.crazysunj.multitypeadapter.AsynMultiTypeAdapter;
import com.crazysunj.multitypeadapter.SynMultiTypeAdapter;
import com.crazysunj.multitypeadapter.entity.MultiHeaderEntity;
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
 * Created by sunjian on 2017/3/28.
 */

public class SampleAdapter extends SynMultiTypeAdapter<MultiHeaderEntity, BaseViewHolder> {


    public static final int TYPE_HEADER_IMG = -2;
    public static final int TYPE_ONE = 0;
    public static final int TYPE_TWO = 1;
    public static final int TYPE_THREE = 2;
    public static final int TYPE_FOUR = 3;

    public static final int LEVEL_FIRST = 0;
    public static final int LEVEL_SENCOND = 1;
    public static final int LEVEL_THIRD = 2;
    public static final int LEVEL_FOURTH = 3;

    public SampleAdapter() {
        super(true);
        registerMoudle(TYPE_ONE, LEVEL_FIRST, R.layout.item_first);
        registerMoudle(TYPE_TWO, LEVEL_FOURTH, R.layout.item_fourth);
        registerMoudle(TYPE_THREE, LEVEL_SENCOND, R.layout.item_second);
        registerMoudle(TYPE_FOUR, LEVEL_THIRD, R.layout.item_third);
        registerMoudle(DEFAULT_HEADER_TYPE, DEFAULT_HEADER_LEVEL, R.layout.item_header);
        registerMoudle(TYPE_HEADER_IMG, DEFAULT_HEADER_LEVEL, R.layout.item_header_img);
    }

    @Override
    public void onBindHeaderViewHolder(BaseViewHolder helper, int position) {
        MultiHeaderEntity stickyEntity = mData.get(position);

        if (stickyEntity instanceof FirstStickyItem) {
            helper.setText(R.id.item_header, ((FirstStickyItem) stickyEntity).getStickyName());
        } else if (stickyEntity instanceof SecondStickyItem) {
            helper.setText(R.id.item_header, ((SecondStickyItem) stickyEntity).getStickyName());
        } else if (stickyEntity instanceof ThirdStickyItem) {
            helper.setText(R.id.item_header, ((ThirdStickyItem) stickyEntity).getStickyName());
        } else if (stickyEntity instanceof FourthStickyItem) {
            helper.setText(R.id.item_header, ((FourthStickyItem) stickyEntity).getStickyName());
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiHeaderEntity item, int position) {
        if (item instanceof FirstItem) {
            renderFirst(helper, (FirstItem) item);
        } else if (item instanceof SecondItem) {
            renderSecond(helper, (SecondItem) item);
        } else if (item instanceof ThirdItem) {
            renderThird(helper, (ThirdItem) item);
        } else if (item instanceof FourthItem) {
            renderFourth(helper, (FourthItem) item);
        } else if (item instanceof HeaderFirstItem) {
            renderHeaderFirst(helper, (HeaderFirstItem) item);
        } else if (item instanceof HeaderSecondItem) {
            renderHeaderSecond(helper, (HeaderSecondItem) item);
        } else if (item instanceof HeaderThirdItem) {
            renderHeaderThird(helper, (HeaderThirdItem) item);
        } else if (item instanceof HeaderFourthItem) {
            renderHeaderFourth(helper, (HeaderFourthItem) item);
        }
    }

    private void renderHeaderFourth(BaseViewHolder helper, HeaderFourthItem item) {
        helper.setText(R.id.item_header_name, item.getName());
        helper.setImageResource(R.id.item_header_img, R.mipmap.ic_launcher);
    }

    private void renderHeaderThird(BaseViewHolder helper, HeaderThirdItem item) {
        helper.setText(R.id.item_header, item.getName());
    }

    private void renderHeaderSecond(BaseViewHolder helper, HeaderSecondItem item) {
        helper.setText(R.id.item_header, item.getName());
    }

    private void renderHeaderFirst(BaseViewHolder helper, HeaderFirstItem item) {
        helper.setText(R.id.item_header, item.getName());
    }

    private void renderFourth(BaseViewHolder helper, FourthItem item) {
        helper.setText(R.id.item_name, item.getName());
        helper.setImageResource(R.id.item_img, item.getImg());
    }

    private void renderThird(BaseViewHolder helper, ThirdItem item) {
        helper.setText(R.id.item_name, item.getName());
        helper.setImageResource(R.id.item_img, item.getImg());
    }

    private void renderSecond(BaseViewHolder helper, SecondItem item) {
        helper.setText(R.id.item_name, item.getName());
        helper.setImageResource(R.id.item_img, item.getImg());
    }

    private void renderFirst(BaseViewHolder helper, FirstItem item) {
        helper.setText(R.id.item_name, item.getName());
        helper.setImageResource(R.id.item_img, item.getImg());
    }
}
