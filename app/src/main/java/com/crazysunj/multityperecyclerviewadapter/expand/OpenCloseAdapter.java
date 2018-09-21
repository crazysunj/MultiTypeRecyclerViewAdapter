package com.crazysunj.multityperecyclerviewadapter.expand;


import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.crazysunj.multitypeadapter.adapter.LoadingEntityAdapter;
import com.crazysunj.multitypeadapter.helper.RecyclerViewAdapterHelper;
import com.crazysunj.multitypeadapter.util.IDUtil;
import com.crazysunj.multityperecyclerviewadapter.R;
import com.crazysunj.multityperecyclerviewadapter.constant.Constants;
import com.crazysunj.multityperecyclerviewadapter.helper.BaseHelperAdapter;
import com.crazysunj.multityperecyclerviewadapter.helper.BaseViewHolder;

import java.util.List;

/**
 * description
 * <p>
 * Created by sunjian on 2017/7/5.
 */

public class OpenCloseAdapter extends BaseHelperAdapter<OpenCloseItem, BaseViewHolder, OpenCloseAdapterHelper> {

    private OnErrorCallback mErrorCallback;
    private OnFooterClickListener mOnFooterClickListener;

    public OpenCloseAdapter() {
        super(new OpenCloseAdapterHelper());
        mHelper.setLoadingAdapter(new LoadingEntityAdapter<OpenCloseItem>() {
            @Override
            public OpenCloseItem createLoadingEntity(int type, int level) {
                return new LoadingOCEntity(type, IDUtil.getId());
            }

            @Override
            public OpenCloseItem createLoadingHeaderEntity(int type, int level) {
                return new LoadingOCEntity(type, IDUtil.getId());
            }

            @Override
            public void bindLoadingEntity(OpenCloseItem loadingEntity, int position) {

            }
        });
        mHelper.setEmptyAdapter((type, level) -> new EmptyOCEntity(type, "肚子好饿" + level));
        mHelper.setErrorAdapter((type, level) -> new ErrorOCEntity(type, "我错了" + level));
    }

    @Override
    protected void convert(BaseViewHolder holder, OpenCloseItem item) {
        int itemType = item.getItemType();
        switch (itemType) {
            case FirstOCEntity.OC_FIRST_TYPE:
                renderFirst(holder, (FirstOCEntity) item);
                break;
            case SecondOCEntity.OC_SECOND_TYPE:
                renderSecond(holder, (SecondOCEntity) item);
                break;
            case ThirdOCEntity.OC_THIRD_TYPE:
                renderThird(holder, (ThirdOCEntity) item);
                break;
            case OpenCloseAdapterHelper.LEVEL_FIRST - RecyclerViewAdapterHelper.HEADER_TYPE_DIFFER:
            case OpenCloseAdapterHelper.LEVEL_SECOND - RecyclerViewAdapterHelper.HEADER_TYPE_DIFFER:
            case OpenCloseAdapterHelper.LEVEL_THIRD - RecyclerViewAdapterHelper.HEADER_TYPE_DIFFER:
                renderHeader(holder, (TitleOCEntity) item);
                break;
            case OpenCloseAdapterHelper.LEVEL_FIRST - RecyclerViewAdapterHelper.FOOTER_TYPE_DIFFER:
            case OpenCloseAdapterHelper.LEVEL_SECOND - RecyclerViewAdapterHelper.FOOTER_TYPE_DIFFER:
            case OpenCloseAdapterHelper.LEVEL_THIRD - RecyclerViewAdapterHelper.FOOTER_TYPE_DIFFER:
                renderFooter(holder, (FooterOCEntity) item);
                break;
            case OpenCloseAdapterHelper.LEVEL_FIRST - RecyclerViewAdapterHelper.EMPTY_TYPE_DIFFER:
                renderEmpty(holder, (EmptyOCEntity) item);
                break;
            case OpenCloseAdapterHelper.LEVEL_FIRST - RecyclerViewAdapterHelper.ERROR_TYPE_DIFFER:
                renderError(holder, (ErrorOCEntity) item);
                break;
            default:
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
        mHelper.notifyModuleEmptyChanged(OpenCloseAdapterHelper.LEVEL_FIRST);
    }

    public void notifyFirstError() {
        mHelper.notifyModuleErrorChanged(OpenCloseAdapterHelper.LEVEL_FIRST);
    }

    public void notifyFirstLoading() {
        mHelper.notifyLoadingDataAndHeaderChanged(OpenCloseAdapterHelper.LEVEL_FIRST, 2);
    }

    public void notifyFirst(List<FirstOCEntity> entities) {
        int firstLevel = OpenCloseAdapterHelper.LEVEL_FIRST;
        mHelper.notifyModuleDataAndHeaderAndFooterChanged(
                entities, new TitleOCEntity(firstLevel, "类型1"),
                new FooterOCEntity(firstLevel, Constants.EXPAND),
                firstLevel);
    }

    public void notifySecond(List<SecondOCEntity> entities) {
        int secondLevel = OpenCloseAdapterHelper.LEVEL_SECOND;
        mHelper.notifyModuleDataAndHeaderAndFooterChanged(
                entities,
                new TitleOCEntity(secondLevel, "类型2"),
                new FooterOCEntity(secondLevel, Constants.FOLD),
                secondLevel);
    }

    public void notifyThird(List<ThirdOCEntity> entities) {
        int thirdLevel = OpenCloseAdapterHelper.LEVEL_THIRD;
        mHelper.notifyModuleDataAndHeaderAndFooterChanged(
                entities,
                new TitleOCEntity(thirdLevel, "类型3"),
                new FooterOCEntity(thirdLevel, Constants.FOLD),
                thirdLevel);
    }

    private void renderError(BaseViewHolder helper, ErrorOCEntity item) {

        final int level = item.getItemType() + RecyclerViewAdapterHelper.ERROR_TYPE_DIFFER;
        helper.setText(R.id.title, String.format("我是错误 flag:%s level:%s", item.getFlag(), level));
        helper.setText(R.id.message, String.format("title:%s", item.getTitle()));
        helper.getView(R.id.retry).setOnClickListener(v -> {
            if (mErrorCallback != null) {
                mErrorCallback.onError(level);
            }
        });
    }

    public void setOnErrorCallback(OnErrorCallback callback) {
        mErrorCallback = callback;
    }


    public interface OnErrorCallback {
        void onError(int level);
    }

    private void renderEmpty(BaseViewHolder helper, EmptyOCEntity item) {
        helper.setText(R.id.empty_title, String.format("我是空 title:%s flag:%s type:%s", item.getTitle(), item.getFlag(), (item.getItemType() + RecyclerViewAdapterHelper.EMPTY_TYPE_DIFFER)));
    }

    private void renderFooter(BaseViewHolder helper, final FooterOCEntity item) {
        final int type = item.getItemType() + RecyclerViewAdapterHelper.FOOTER_TYPE_DIFFER;
        final TextView footer = helper.getView(R.id.item_footer);
        String text = String.format("我是底 title:%s flag:%s type:%s", item.getTitle(), item.getFlag(), type);
        Log.d("FooterOCEntity", "footer: " + text);
        footer.setText(text);
        footer.setOnClickListener(v -> {
            if (mOnFooterClickListener != null) {
                if (Constants.EXPAND.equals(item.getTitle())) {
                    mOnFooterClickListener.onFooterClick(type, false);
                    item.setTitle(Constants.FOLD);
                } else {
                    mOnFooterClickListener.onFooterClick(type, true);
                    item.setTitle(Constants.EXPAND);
                }
                footer.setText(String.format("我是底 title:%s flag:%s type:%s", item.getTitle(), item.getFlag(), type));
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
        TextView header = helper.getView(R.id.item_header);
        final int type = item.getItemType() + RecyclerViewAdapterHelper.HEADER_TYPE_DIFFER;
        header.setText(String.format("我是头 title:%s flag:%s type:%s", item.getTitle(), item.getFlag(), type));
        header.setOnClickListener(v -> Toast.makeText(v.getContext(), "当前是否合拢？ " + mHelper.isDataFolded(type), Toast.LENGTH_SHORT).show());
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
