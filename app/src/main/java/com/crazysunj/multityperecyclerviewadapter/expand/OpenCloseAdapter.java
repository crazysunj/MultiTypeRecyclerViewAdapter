package com.crazysunj.multityperecyclerviewadapter.expand;


import android.view.View;

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

    public void openFirst() {
        mHelper.openData(FirstOCEntity.OC_FIRST_TYPE);
    }

    public void openSecond() {
        mHelper.openData(SecondOCEntity.OC_SECOND_TYPE);
    }

    public void openThird() {
        mHelper.openData(ThirdOCEntity.OC_THIRD_TYPE);
    }

    public void notifyFirst(List<FirstOCEntity> entities) {
        mHelper.notifyMoudleHeaderAndDataAndFooterChanged(
                new TitleOCEntity(FirstOCEntity.OC_FIRST_TYPE, "类型1"),
                entities,
                new FooterOCEntity(FirstOCEntity.OC_FIRST_TYPE, "查看更多"),
                FirstOCEntity.OC_FIRST_TYPE);
    }

    public void notifySecond(List<SecondOCEntity> entities) {
        mHelper.notifyMoudleHeaderAndDataAndFooterChanged(
                new TitleOCEntity(SecondOCEntity.OC_SECOND_TYPE, "类型2"),
                entities,
                new FooterOCEntity(SecondOCEntity.OC_SECOND_TYPE, "查看更多"),
                SecondOCEntity.OC_SECOND_TYPE);
    }

    public void notifyThird(List<ThirdOCEntity> entities) {
        mHelper.notifyMoudleHeaderAndDataAndFooterChanged(
                new TitleOCEntity(ThirdOCEntity.OC_THIRD_TYPE, "类型3"),
                entities,
                new FooterOCEntity(ThirdOCEntity.OC_THIRD_TYPE, "查看更多"),
                ThirdOCEntity.OC_THIRD_TYPE);
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

    private void renderFooter(BaseViewHolder helper, FooterOCEntity item) {
        helper.setText(R.id.item_footer, String.format("我是底 title:%s flag:%s type:%s", item.getTitle(), item.getFlag(), (item.getItemType() + RecyclerViewAdapterHelper.FOOTER_TYPE_DIFFER)));
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
