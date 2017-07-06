package com.crazysunj.multityperecyclerviewadapter.expand;


import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.crazysunj.multitypeadapter.adapter.EmptyEntityAdapter;
import com.crazysunj.multitypeadapter.adapter.ErrorEntityAdapter;
import com.crazysunj.multitypeadapter.helper.RecyclerViewAdapterHelper;
import com.crazysunj.multityperecyclerviewadapter.R;
import com.crazysunj.multityperecyclerviewadapter.helper.BaseHelperAdapter;

import java.util.List;

/**
 * description
 * <p>
 * Created by sunjian on 2017/7/5.
 */

public class OpenCloseAdapter extends BaseHelperAdapter<OpenCloseItem, OpenCloseAdapterHelper> {

    private OnErrorCallback mErrorCallback;
    private OnFooterClickListener mOnFooterClickListener;

    public OpenCloseAdapter() {
        super(new OpenCloseAdapterHelper());
        mHelper.setEmptyAdapter(new EmptyEntityAdapter<OpenCloseItem>() {
            @Override
            public OpenCloseItem createEmptyEntity(int type) {
                return new EmptyOCEntity(type, "肚子好饿" + type);
            }
        });
        mHelper.setErrorAdapter(new ErrorEntityAdapter<OpenCloseItem>() {
            @Override
            public OpenCloseItem createErrorEntity(int type) {
                return new ErrorOCEntity(type, "我错了" + type);
            }
        });
    }

    @Override
    protected void convert(BaseViewHolder helper, OpenCloseItem item) {
        int itemType = item.getItemType();
        switch (itemType) {
            case FirstOCEntity.OC_FIRST_TYPE:
                renderFirst(helper, (FirstOCEntity) item);
                break;
            case SecondOCEntity.OC_SECOND_TYPE:
                renderSecond(helper, (SecondOCEntity) item);
                break;
            case ThirdOCEntity.OC_THIRD_TYPE:
                renderThird(helper, (ThirdOCEntity) item);
                break;
            case FirstOCEntity.OC_FIRST_TYPE - RecyclerViewAdapterHelper.HEADER_TYPE_DIFFER:
            case SecondOCEntity.OC_SECOND_TYPE - RecyclerViewAdapterHelper.HEADER_TYPE_DIFFER:
            case ThirdOCEntity.OC_THIRD_TYPE - RecyclerViewAdapterHelper.HEADER_TYPE_DIFFER:
                renderHeader(helper, (TitleOCEntity) item);
                break;
            case FirstOCEntity.OC_FIRST_TYPE - RecyclerViewAdapterHelper.FOOTER_TYPE_DIFFER:
            case SecondOCEntity.OC_SECOND_TYPE - RecyclerViewAdapterHelper.FOOTER_TYPE_DIFFER:
            case ThirdOCEntity.OC_THIRD_TYPE - RecyclerViewAdapterHelper.FOOTER_TYPE_DIFFER:
                renderFooter(helper, (FooterOCEntity) item);
                break;
            case FirstOCEntity.OC_FIRST_TYPE - RecyclerViewAdapterHelper.EMPTY_TYPE_DIFFER:
                renderEmpty(helper, (EmptyOCEntity) item);
                break;
            case FirstOCEntity.OC_FIRST_TYPE - RecyclerViewAdapterHelper.ERROR_TYPE_DIFFER:
                renderError(helper, (ErrorOCEntity) item);
                break;
        }
    }

    public void open(int type, boolean isFold) {
        mHelper.foldType(type, isFold);
    }

    public void notifyAll(List<OpenCloseItem> data) {
        mHelper.notifyDataByDiff(data);
    }

    public void notifyFirstEmpty() {
        mHelper.notifyMoudleEmptyChanged(FirstOCEntity.OC_FIRST_TYPE);
    }

    public void notifyFirstError() {
        mHelper.notifyMoudleErrorChanged(FirstOCEntity.OC_FIRST_TYPE);
    }

    public void notifyFirst(List<FirstOCEntity> entities) {
        int firstType = FirstOCEntity.OC_FIRST_TYPE;
        mHelper.notifyMoudleHeaderAndDataAndFooterChanged(
                new TitleOCEntity(firstType, "类型1"),
                entities,
                new FooterOCEntity(firstType, getFooterTitle(firstType)),
                firstType);
    }

    public String getFooterTitle(int type) {
        return mHelper.isDataFolded(type) ? "查看更多" : "点击收回";
    }

    public void notifySecond(List<SecondOCEntity> entities) {
        int secondType = SecondOCEntity.OC_SECOND_TYPE;
        mHelper.notifyMoudleHeaderAndDataAndFooterChanged(
                new TitleOCEntity(secondType, "类型2"),
                entities,
                new FooterOCEntity(secondType, getFooterTitle(secondType)),
                secondType);
    }

    public void notifyThird(List<ThirdOCEntity> entities) {
        int thirdType = ThirdOCEntity.OC_THIRD_TYPE;
        mHelper.notifyMoudleHeaderAndDataAndFooterChanged(
                new TitleOCEntity(thirdType, "类型3"),
                entities,
                new FooterOCEntity(thirdType, getFooterTitle(thirdType)),
                thirdType);
    }

    private void renderError(BaseViewHolder helper, ErrorOCEntity item) {

        final int type = item.getItemType() + RecyclerViewAdapterHelper.ERROR_TYPE_DIFFER;
        helper.setText(R.id.title, String.format("我是错误 flag:%s type:%s", item.getFlag(), type));
        helper.setText(R.id.message, String.format("title:%s", item.getTitle()));
        helper.getView(R.id.retry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mErrorCallback != null) {
                    mErrorCallback.onError(type);
                }
            }
        });
    }

    public void setOnErrorCallback(OnErrorCallback callback) {
        mErrorCallback = callback;
    }


    public interface OnErrorCallback {
        void onError(int type);
    }

    private void renderEmpty(BaseViewHolder helper, EmptyOCEntity item) {
        helper.setText(R.id.empty_title, String.format("我是空 title:%s flag:%s type:%s", item.getTitle(), item.getFlag(), (item.getItemType() + RecyclerViewAdapterHelper.EMPTY_TYPE_DIFFER)));
    }

    private void renderFooter(BaseViewHolder helper, final FooterOCEntity item) {
        final int type = item.getItemType() + RecyclerViewAdapterHelper.FOOTER_TYPE_DIFFER;
        final TextView footer = helper.getView(R.id.item_footer);
        footer.setText(String.format("我是底 title:%s flag:%s type:%s", item.getTitle(), item.getFlag(), type));
        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnFooterClickListener != null) {
                    if ("查看更多".equals(item.getTitle())) {
                        mOnFooterClickListener.onFooterClick(type, false);
                        item.setTitle("点击收回");
                    } else {
                        mOnFooterClickListener.onFooterClick(type, true);
                        item.setTitle("查看更多");
                    }
                    footer.setText(String.format("我是底 title:%s flag:%s type:%s", item.getTitle(), item.getFlag(), type));
                }
            }
        });

    }

    public void setOnFooterClickListener(OnFooterClickListener listener) {
        mOnFooterClickListener = listener;
    }

    public interface OnFooterClickListener {
        void onFooterClick(int type, boolean isFlod);
    }

    private void renderHeader(BaseViewHolder helper, TitleOCEntity item) {
        helper.setText(R.id.item_header, String.format("我是头 title:%s flag:%s type:%s", item.getTitle(), item.getFlag(), (item.getItemType() + RecyclerViewAdapterHelper.HEADER_TYPE_DIFFER)));
    }

    private void renderThird(BaseViewHolder helper, ThirdOCEntity item) {
        helper.setText(R.id.item_name, String.format("title:%s flag:%s type:%s", item.getTitle(), item.getFlag(), item.getItemType()));
    }

    private void renderSecond(BaseViewHolder helper, SecondOCEntity item) {
        helper.setText(R.id.item_name, String.format("title:%s flag:%s type:%s", item.getTitle(), item.getFlag(), item.getItemType()));
    }

    private void renderFirst(BaseViewHolder helper, FirstOCEntity item) {
        helper.setText(R.id.item_name, String.format("title:%s flag:%s type:%s", item.getTitle(), item.getFlag(), item.getItemType()));
    }
}
