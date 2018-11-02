package com.crazysunj.multityperecyclerviewadapter.helper;

import androidx.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.crazysunj.multitypeadapter.adapter.LoadingEntityAdapter;
import com.crazysunj.multitypeadapter.helper.LoadingConfig;
import com.crazysunj.multitypeadapter.sticky.StickyHeaderAdapter;
import com.crazysunj.multityperecyclerviewadapter.R;
import com.crazysunj.multityperecyclerviewadapter.apt.RxAptHelperAdapterHelper;
import com.crazysunj.multityperecyclerviewadapter.data.ErrorAndEmptyEmptyEntity;
import com.crazysunj.multityperecyclerviewadapter.data.ErrorAndEmptyErrorEntity;
import com.crazysunj.multityperecyclerviewadapter.data.ErrorAndEmptyLoadingEntity;
import com.crazysunj.multityperecyclerviewadapter.data.FirstItem;
import com.crazysunj.multityperecyclerviewadapter.data.FourthItem;
import com.crazysunj.multityperecyclerviewadapter.data.MyErrorAndEmptyEmptyEntity;
import com.crazysunj.multityperecyclerviewadapter.data.MyErrorAndEmptyErrorEntity;
import com.crazysunj.multityperecyclerviewadapter.data.SecondItem;
import com.crazysunj.multityperecyclerviewadapter.data.ThirdItem;
import com.crazysunj.multityperecyclerviewadapter.header.HeaderFirstItem;
import com.crazysunj.multityperecyclerviewadapter.header.HeaderFourthItem;
import com.crazysunj.multityperecyclerviewadapter.header.HeaderSecondItem;
import com.crazysunj.multityperecyclerviewadapter.header.HeaderThirdItem;
import com.crazysunj.multityperecyclerviewadapter.sticky.FirstStickyItem;
import com.crazysunj.multityperecyclerviewadapter.sticky.FourthStickyItem;
import com.crazysunj.multityperecyclerviewadapter.sticky.SecondStickyItem;
import com.crazysunj.multityperecyclerviewadapter.sticky.StickyItem;
import com.crazysunj.multityperecyclerviewadapter.sticky.ThirdStickyItem;

/**
 * description
 * <p>
 * Created by sunjian on 2017/5/4.
 */

public class ErrorAndrEmptyHelperAdapter extends BaseHelperAdapter<StickyItem, ShimmerViewHolder, ErrorAndEmptyAdapterHelper>
        implements StickyHeaderAdapter<ShimmerViewHolder> {

    private OnErrorCallback callback;

    public ErrorAndrEmptyHelperAdapter(ErrorAndEmptyAdapterHelper helper) {
        super(helper);
        helper.setEmptyAdapter((type, level) -> new ErrorAndEmptyEmptyEntity(type));
        helper.setErrorAdapter((type, level) -> new ErrorAndEmptyErrorEntity(type));
        helper.setLoadingAdapter(new LoadingEntityAdapter<StickyItem>() {
            @Override
            public StickyItem createLoadingEntity(int type, int level) {
                return new ErrorAndEmptyLoadingEntity(type);
            }

            @Override
            public StickyItem createLoadingHeaderEntity(int type, int level) {
                return new ErrorAndEmptyLoadingEntity(type);
            }

            @Override
            public void bindLoadingEntity(StickyItem loadingEntity, int position) {
                int itemType = loadingEntity.getItemType();
                int type = position < 0 ? itemType + RxAptHelperAdapterHelper.LOADING_HEADER_TYPE_DIFFER : itemType + RxAptHelperAdapterHelper.LOADING_DATA_TYPE_DIFFER;
                switch (type) {
                    case SimpleHelper.TYPE_ONE:
                        loadingEntity.setHeaderId(1);
                        loadingEntity.setStickyName("第一条粘性");
                        break;
                    case SimpleHelper.TYPE_TWO:
                        loadingEntity.setHeaderId(4);
                        loadingEntity.setStickyName("第四条粘性");
                        break;
                    case SimpleHelper.TYPE_THREE:
                        loadingEntity.setHeaderId(2);
                        loadingEntity.setStickyName("第二条粘性");
                        break;
                    case SimpleHelper.TYPE_FOUR:
                        loadingEntity.setHeaderId(3);
                        loadingEntity.setStickyName("第三条粘性");
                        break;
                    default:
                        break;
                }
            }
        });
        helper.initGlobalLoadingConfig(new LoadingConfig.Builder()
                .setLoading(SimpleHelper.LEVEL_FIRST, 3, true)
                .setLoading(SimpleHelper.LEVEL_THIRD, 2)
                .setLoading(SimpleHelper.LEVEL_FOURTH, true)
                .setLoading(SimpleHelper.LEVEL_SENCOND, 4, true)
                .build());
    }

    @Override
    protected void convert(ShimmerViewHolder holder, StickyItem item) {
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
        } else if (item instanceof ErrorAndEmptyErrorEntity) {
            if (item.getItemType() == SimpleHelper.LEVEL_SENCOND - SimpleHelper.ERROR_TYPE_DIFFER) {
                renderErrorSecond(holder, SimpleHelper.LEVEL_SENCOND);
            }

            if (item instanceof MyErrorAndEmptyErrorEntity) {
                MyErrorAndEmptyErrorEntity errorEntity = (MyErrorAndEmptyErrorEntity) item;
                if (errorEntity.getItemType() == SimpleHelper.LEVEL_FOURTH - SimpleHelper.ERROR_TYPE_DIFFER) {
                    renderErrorFourth(holder, errorEntity, SimpleHelper.LEVEL_FOURTH);
                }
            }
        } else if (item instanceof MyErrorAndEmptyEmptyEntity) {
            renderEmptyThird(holder, (MyErrorAndEmptyEmptyEntity) item, SimpleHelper.LEVEL_THIRD);
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

    private void renderEmptyThird(ShimmerViewHolder holder, MyErrorAndEmptyEmptyEntity item, int type) {
        holder.setText(R.id.empty_title, item.getTitle());
    }

    private void renderErrorFourth(ShimmerViewHolder helper, MyErrorAndEmptyErrorEntity item, int type) {
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
        StickyItem stickyEntity = mData.get(position);

        if (stickyEntity instanceof FirstStickyItem) {
            helper.setText(R.id.item_header, stickyEntity.getStickyName());
        } else if (stickyEntity instanceof SecondStickyItem) {
            helper.setText(R.id.item_header, stickyEntity.getStickyName());
        } else if (stickyEntity instanceof ThirdStickyItem) {
            helper.setText(R.id.item_header, stickyEntity.getStickyName());
        } else if (stickyEntity instanceof FourthStickyItem) {
            helper.setText(R.id.item_header, stickyEntity.getStickyName());
        } else {
            helper.setText(R.id.item_header, stickyEntity.getStickyName());
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
