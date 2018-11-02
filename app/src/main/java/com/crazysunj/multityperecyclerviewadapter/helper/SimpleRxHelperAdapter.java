package com.crazysunj.multityperecyclerviewadapter.helper;

import android.view.View;
import android.view.ViewGroup;

import com.crazysunj.multitypeadapter.adapter.LoadingEntityAdapter;
import com.crazysunj.multitypeadapter.helper.LoadingConfig;
import com.crazysunj.multitypeadapter.sticky.StickyHeaderAdapter;
import com.crazysunj.multityperecyclerviewadapter.R;
import com.crazysunj.multityperecyclerviewadapter.data.FirstItem;
import com.crazysunj.multityperecyclerviewadapter.data.FourthItem;
import com.crazysunj.multityperecyclerviewadapter.data.LoadingEntity;
import com.crazysunj.multityperecyclerviewadapter.data.MyEmptyEntity;
import com.crazysunj.multityperecyclerviewadapter.data.MyErrorEntity;
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

import androidx.annotation.NonNull;

/**
 * description
 * <p>
 * Created by sunjian on 2017/5/4.
 */

public class SimpleRxHelperAdapter extends BaseHelperAdapter<MultiHeaderEntity, ShimmerViewHolder, RxAdapterHelper>
        implements StickyHeaderAdapter<ShimmerViewHolder> {

    private OnErrorCallback callback;

    public SimpleRxHelperAdapter(RxAdapterHelper helper) {
        super(helper);
        helper.setEmptyAdapter((type, level) -> new SimpleEmptyEntity(type));
        helper.setErrorAdapter((type, level) -> new SimpleErrorEntity(type));
        helper.setLoadingAdapter(new LoadingEntityAdapter<MultiHeaderEntity>() {
            @Override
            public MultiHeaderEntity createLoadingEntity(int type, int level) {
                return new LoadingEntity(type);
            }

            @Override
            public MultiHeaderEntity createLoadingHeaderEntity(int type, int level) {
                return new LoadingEntity(type);
            }

            @Override
            public void bindLoadingEntity(MultiHeaderEntity loadingEntity, int position) {
//                int itemType = loadingEntity.getItemType();
//                int type = position < 0 ? itemType + RxAptHelperAdapterHelper.LOADING_HEADER_TYPE_DIFFER : itemType + RxAptHelperAdapterHelper.LOADING_DATA_TYPE_DIFFER;
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
        helper.initGlobalLoadingConfig(new LoadingConfig.Builder()
                .setLoading(SimpleHelper.LEVEL_FIRST, 3, true)
                .setLoading(SimpleHelper.LEVEL_SENCOND, 2)
                .setLoading(SimpleHelper.LEVEL_THIRD, true)
                .setLoading(SimpleHelper.LEVEL_FOURTH, 4, true)
                .build());
    }

    @Override
    protected void convert(ShimmerViewHolder holder, MultiHeaderEntity item) {
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
        } else if (item instanceof SimpleErrorEntity) {
            if (item.getItemType() == SimpleHelper.TYPE_THREE - SimpleHelper.ERROR_TYPE_DIFFER) {
                renderErrorSecond(holder, SimpleHelper.TYPE_THREE);
            }
            if (item instanceof MyErrorEntity) {
                MyErrorEntity errorEntity = (MyErrorEntity) item;
                if (errorEntity.getItemType() == SimpleHelper.TYPE_TWO - SimpleHelper.ERROR_TYPE_DIFFER) {
                    renderErrorFourth(holder, errorEntity, SimpleHelper.TYPE_TWO);
                }
            }
        } else if (item instanceof MyEmptyEntity) {
            renderEmptyThird(holder, (MyEmptyEntity) item, SimpleHelper.TYPE_FOUR);
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ShimmerViewHolder holder) {
        holder.startAnim();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ShimmerViewHolder holder) {
        holder.stopAnim();
    }

    private void renderEmptyThird(ShimmerViewHolder helper, MyEmptyEntity item, int type) {
        helper.setText(R.id.empty_title, item.getTitle());
    }

    private void renderErrorFourth(ShimmerViewHolder helper, MyErrorEntity item, int type) {
        helper.setText(R.id.title, item.getTitle());
        helper.setText(R.id.message, item.getMessage());
        helper.getView(R.id.retry).setOnClickListener(new SimpleListener(type));
    }

    private void renderErrorSecond(final ShimmerViewHolder helper, int type) {

        helper.getView(R.id.retry).setOnClickListener(new SimpleListener(type));
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
