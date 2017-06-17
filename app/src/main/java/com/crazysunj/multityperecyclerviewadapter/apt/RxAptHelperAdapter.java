package com.crazysunj.multityperecyclerviewadapter.apt;

import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.crazysunj.multitypeadapter.annotation.AdapterHelper;
import com.crazysunj.multitypeadapter.annotation.BindAllType;
import com.crazysunj.multitypeadapter.annotation.BindHeaderRes;
import com.crazysunj.multitypeadapter.annotation.BindLoadingHeaderRes;
import com.crazysunj.multitypeadapter.annotation.BindLoadingLayoutRes;
import com.crazysunj.multitypeadapter.annotation.PreDataCount;
import com.crazysunj.multitypeadapter.entity.ErrorEntity;
import com.crazysunj.multitypeadapter.entity.MultiHeaderEntity;
import com.crazysunj.multitypeadapter.sticky.StickyHeaderAdapter;
import com.crazysunj.multityperecyclerviewadapter.R;
import com.crazysunj.multityperecyclerviewadapter.data.FirstItem;
import com.crazysunj.multityperecyclerviewadapter.data.FourthItem;
import com.crazysunj.multityperecyclerviewadapter.data.SecondItem;
import com.crazysunj.multityperecyclerviewadapter.data.SimpleEmptyEntity;
import com.crazysunj.multityperecyclerviewadapter.data.SimpleErrorEntity;
import com.crazysunj.multityperecyclerviewadapter.data.ThirdItem;
import com.crazysunj.multityperecyclerviewadapter.header.HeaderFirstItem;
import com.crazysunj.multityperecyclerviewadapter.header.HeaderFourthItem;
import com.crazysunj.multityperecyclerviewadapter.header.HeaderSecondItem;
import com.crazysunj.multityperecyclerviewadapter.header.HeaderThirdItem;
import com.crazysunj.multityperecyclerviewadapter.helper.ShimmerViewHolder;
import com.crazysunj.multityperecyclerviewadapter.helper.SimpleHelper;
import com.crazysunj.multityperecyclerviewadapter.sticky.FirstStickyItem;
import com.crazysunj.multityperecyclerviewadapter.sticky.FourthStickyItem;
import com.crazysunj.multityperecyclerviewadapter.sticky.SecondStickyItem;
import com.crazysunj.multityperecyclerviewadapter.sticky.ThirdStickyItem;

/**
 * description
 * <p>
 * Created by sunjian on 2017/5/4.
 */

@AdapterHelper(superObj = "com.crazysunj.multityperecyclerviewadapter.helper.SimpleRxAdapterHelper",
        adapter = "com.crazysunj.multityperecyclerviewadapter.apt.RxAptHelperAdapter")
public class RxAptHelperAdapter extends BaseQuickAdapter<MultiHeaderEntity, ShimmerViewHolder>
        implements StickyHeaderAdapter<ShimmerViewHolder> {

    @BindAllType(level = 0, layoutResId = R.layout.item_first, headerResId = R.layout.item_header)
    public static final int TYPE_ONE = 0;

    @BindAllType(level = 3, layoutResId = R.layout.item_fourth, errorLayoutResId = R.layout.layout_error_two)
    public static final int TYPE_TWO = 1;

    @BindAllType(level = 1, layoutResId = R.layout.item_second, errorLayoutResId = R.layout.layout_error)
    public static final int TYPE_THREE = 2;

    @BindAllType(level = 2, layoutResId = R.layout.item_third, emptyLayoutResId = R.layout.layout_empty)
    public static final int TYPE_FOUR = 3;

    @BindHeaderRes(type = {TYPE_TWO, TYPE_THREE, TYPE_FOUR})
    final int HEADER_LAYOUT_RES_ID = R.layout.item_header_img;

    @BindLoadingHeaderRes(type = {TYPE_ONE, TYPE_TWO, TYPE_THREE, TYPE_FOUR})
    final int LOADING_HEADER_RES_ID = R.layout.layout_default_shimmer_header_view;

    @BindLoadingLayoutRes(type = {TYPE_ONE, TYPE_TWO, TYPE_THREE, TYPE_FOUR})
    final int LOADING_LAYOUT_RES_ID = R.layout.layout_default_shimmer_view;


    private RxAptHelperAdapterHelper mHelper;
    private OnErrorCallback callback;

    public RxAptHelperAdapter(RxAptHelperAdapterHelper helper) {

        super(helper.getData());
        helper.bindAdapter(this);
        mHelper = helper;
    }

    @PreDataCount()
    @Override
    public int getHeaderLayoutCount() {
        return super.getHeaderLayoutCount();
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
    protected void convert(ShimmerViewHolder helper, MultiHeaderEntity item) {

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
        } else if (item instanceof ErrorEntity) {
            if (((ErrorEntity) item).getType() == SimpleHelper.TYPE_THREE) {
                renderErrorSecond(helper, SimpleHelper.TYPE_THREE);
            }

            if (item instanceof SimpleErrorEntity) {
                SimpleErrorEntity errorEntity = (SimpleErrorEntity) item;
                if (errorEntity.getType() == SimpleHelper.TYPE_TWO) {
                    renderErrorFourth(helper, errorEntity, SimpleHelper.TYPE_TWO);
                }
            }
        } else if (item instanceof SimpleEmptyEntity) {
            renderEmptyThird(helper, (SimpleEmptyEntity) item, SimpleHelper.TYPE_FOUR);
        }
    }

    private void renderEmptyThird(ShimmerViewHolder helper, SimpleEmptyEntity item, int type) {
        helper.setText(R.id.empty_title, item.getTitle());
    }

    private void renderErrorFourth(ShimmerViewHolder helper, SimpleErrorEntity item, int type) {
        helper.setText(R.id.title, item.getTitle());
        helper.setText(R.id.message, item.getMessage());
        helper.getView(R.id.retry).setOnClickListener(new SimpleListener(type));
    }

    private void renderErrorSecond(final ShimmerViewHolder helper, int type) {

        helper.getView(R.id.retry).setOnClickListener(new SimpleListener(type));
    }

    @Override
    public void onViewAttachedToWindow(ShimmerViewHolder holder) {
        holder.startAnim();
    }

    @Override
    public void onViewDetachedFromWindow(ShimmerViewHolder holder) {
        holder.stopAnim();
    }


    private class SimpleListener implements View.OnClickListener {

        private int type;

        public SimpleListener(int type) {
            this.type = type;
        }

        @Override
        public void onClick(View v) {
            if (callback != null) {
                callback.onClick(v, type);
            }
        }
    }

    public void setOnErrorCallback(OnErrorCallback callback) {
        this.callback = callback;
    }

    public interface OnErrorCallback {
        void onClick(View v, int type);
    }

    @Override
    public long getHeaderId(int position) {
        return mHelper.getHeaderId(position);
    }

    @Override
    public ShimmerViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return createBaseViewHolder(parent, R.layout.item_header);
    }

    @Override
    public void onBindHeaderViewHolder(ShimmerViewHolder helper, int position) {
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

    private void renderHeaderFourth(BaseViewHolder helper, HeaderFourthItem item) {
        helper.setText(R.id.item_header_name, item.getName());
        helper.setImageResource(R.id.item_header_img, R.mipmap.ic_launcher);
    }

    private void renderHeaderThird(BaseViewHolder helper, HeaderThirdItem item) {
        helper.setText(R.id.item_header_name, item.getName());
        helper.setImageResource(R.id.item_header_img, R.mipmap.ic_launcher);

    }

    private void renderHeaderSecond(BaseViewHolder helper, HeaderSecondItem item) {
        helper.setText(R.id.item_header_name, item.getName());
        helper.setImageResource(R.id.item_header_img, R.mipmap.ic_launcher);

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
