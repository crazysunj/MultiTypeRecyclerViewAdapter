package com.crazysunj.multityperecyclerviewadapter.testlevel;

import android.content.Context;

import com.crazysunj.multityperecyclerviewadapter.R;
import com.crazysunj.multityperecyclerviewadapter.Refresh240Activity;
import com.crazysunj.multityperecyclerviewadapter.helper.BaseViewHolder;

/**
 * author: sunjian
 * created on: 2018/4/3 下午1:34
 * description:
 */

public class LevelSecondItemConvert implements ItemConvert<MultiTypeTitleEntity, BaseViewHolder> {

    @Override
    public void convert(BaseViewHolder helper, MultiTypeTitleEntity item) {
        final int itemType = item.getItemType();
        switch (itemType) {
            case TypeThreeItem.TYPE_THREE:
                renderTypeThree(helper, (TypeThreeItem) item);
                break;
            case TestLevelAdapter.TYPE_LEVEL_SECOND_HEADER:
                renderLevelSecondHeader(helper, (LevelTitleItem) item);
                break;
            case TestLevelAdapter.TYPE_LEVEL_SECOND_ERROR:
                renderLevelSecondError(helper, item);
                break;
            default:
                break;
        }
    }

    private void renderLevelSecondError(BaseViewHolder helper, MultiTypeTitleEntity item) {
        helper.getTextView(R.id.error_text).setText(item.getMsg());
        helper.getView(R.id.retry).setOnClickListener(v -> {
            Context context = v.getContext();
            if (context instanceof Refresh240Activity) {
                ((Refresh240Activity) context).click1(v);
            }
        });
    }

    private void renderLevelSecondHeader(BaseViewHolder helper, LevelTitleItem item) {
        helper.setText(R.id.item_header, item.getMsg());
    }

    private void renderTypeThree(BaseViewHolder helper, TypeThreeItem item) {
        helper.setText(R.id.item_name, item.getMsg());
    }
}
