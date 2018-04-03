package com.crazysunj.multityperecyclerviewadapter.testlevel;

import com.chad.library.adapter.base.BaseViewHolder;
import com.crazysunj.multityperecyclerviewadapter.R;

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
            default:
                break;
        }
    }

    private void renderLevelSecondHeader(BaseViewHolder helper, LevelTitleItem item) {
        helper.setText(R.id.item_header, item.getMsg());
    }

    private void renderTypeThree(BaseViewHolder helper, TypeThreeItem item) {
        helper.setText(R.id.item_name, item.getMsg());
    }
}
