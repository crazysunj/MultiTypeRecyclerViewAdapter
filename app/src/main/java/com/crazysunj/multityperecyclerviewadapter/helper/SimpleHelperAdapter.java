package com.crazysunj.multityperecyclerviewadapter.helper;

import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.crazysunj.multitypeadapter.adapter.EmptyEntityAdapter;
import com.crazysunj.multitypeadapter.adapter.ErrorEntityAdapter;
import com.crazysunj.multitypeadapter.adapter.LoadingEntityAdapter;
import com.crazysunj.multitypeadapter.entity.MultiHeaderEntity;
import com.crazysunj.multitypeadapter.sticky.StickyHeaderAdapter;
import com.crazysunj.multityperecyclerviewadapter.R;
import com.crazysunj.multityperecyclerviewadapter.apt.RxAptHelperAdapterHelper;
import com.crazysunj.multityperecyclerviewadapter.data.FirstItem;
import com.crazysunj.multityperecyclerviewadapter.data.FourthItem;
import com.crazysunj.multityperecyclerviewadapter.data.LoadingEntity;
import com.crazysunj.multityperecyclerviewadapter.data.SecondItem;
import com.crazysunj.multityperecyclerviewadapter.data.SimpleEmptyEntity;
import com.crazysunj.multityperecyclerviewadapter.data.SimpleErrorEntity;
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

public class SimpleHelperAdapter extends BaseQuickAdapter<MultiHeaderEntity, ShimmerViewHolder>
        implements StickyHeaderAdapter<ShimmerViewHolder> {

    private SimpleHelper mHelper;

    public SimpleHelperAdapter(SimpleHelper helper) {

        super(helper.getData());
        helper.bindAdapter(this);
        helper.setEmptyAdapter(new EmptyEntityAdapter<MultiHeaderEntity>() {
            @Override
            public MultiHeaderEntity createEmptyEntity(int type) {
                return new SimpleEmptyEntity(type);
            }
        });
        helper.setErrorAdapter(new ErrorEntityAdapter<MultiHeaderEntity>() {
            @Override
            public MultiHeaderEntity createErrorEntity(int type) {
                return new SimpleErrorEntity(type);
            }
        });
        helper.setLoadingAdapter(new LoadingEntityAdapter<MultiHeaderEntity>() {
            @Override
            public MultiHeaderEntity createLoadingEntity(int type) {
                return new LoadingEntity(type - RxAptHelperAdapterHelper.SHIMMER_DATA_TYPE_DIFFER);
            }

            @Override
            public MultiHeaderEntity createLoadingHeaderEntity(int type) {
                return new LoadingEntity(type - RxAptHelperAdapterHelper.SHIMMER_HEADER_TYPE_DIFFER);
            }

            @Override
            public void bindLoadingEntity(MultiHeaderEntity loadingEntity, int position) {
//                int itemType = loadingEntity.getItemType();
//                int type = position < 0 ? itemType + RxAptHelperAdapterHelper.SHIMMER_HEADER_TYPE_DIFFER : itemType + RxAptHelperAdapterHelper.SHIMMER_DATA_TYPE_DIFFER;
//                switch (type) {
//                    case TYPE_ONE:
//                        break;
//                    case TYPE_TWO:
//                        break;
//                    case TYPE_THREE:
//                        break;
//                    case TYPE_FOUR:
//                        break;
//                    default:
//                        break;
//                }
            }
        });
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
        }
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

    @Override
    public void onViewAttachedToWindow(ShimmerViewHolder holder) {
        holder.startAnim();
    }

    @Override
    public void onViewDetachedFromWindow(ShimmerViewHolder holder) {
        holder.stopAnim();
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
