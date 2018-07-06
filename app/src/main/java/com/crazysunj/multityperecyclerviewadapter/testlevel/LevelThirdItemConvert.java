package com.crazysunj.multityperecyclerviewadapter.testlevel;

import com.crazysunj.multityperecyclerviewadapter.R;
import com.crazysunj.multityperecyclerviewadapter.helper.BaseViewHolder;

/**
 * author: sunjian
 * created on: 2018/4/3 下午1:34
 * description:
 */

public class LevelThirdItemConvert implements ItemConvert<MultiTypeTitleEntity, BaseViewHolder> {

    @Override
    public void convert(BaseViewHolder helper, MultiTypeTitleEntity item) {
        final int itemType = item.getItemType();
        switch (itemType) {
            case TypeFourItem.TYPE_FOUR:
                renderTypeFour(helper, (TypeFourItem) item);
                break;
            case TypeFiveItem.TYPE_FIVE:
                renderTypeFive(helper, (TypeFiveItem) item);
                break;
            case TestLevelAdapter.TYPE_LEVEL_THIRD_HEADER:
                renderLevelThirdHeader(helper, (LevelTitleItem) item);
                break;
            case TestLevelAdapter.TYPE_LEVEL_THIRD_FOOTER:
                renderLevelThirdFooter(helper, (LevelFooterItem) item);
                break;
            default:
                break;
        }
    }

    private void renderLevelThirdFooter(BaseViewHolder helper, LevelFooterItem item) {
        helper.setText(R.id.item_footer, item.getMsg());
    }

    private void renderLevelThirdHeader(BaseViewHolder helper, LevelTitleItem item) {
        helper.setText(R.id.item_header, item.getMsg());
    }

    private void renderTypeFive(BaseViewHolder helper, TypeFiveItem item) {
        helper.setText(R.id.item_name, item.getMsg());
    }

    private void renderTypeFour(BaseViewHolder helper, TypeFourItem item) {
        helper.setText(R.id.item_name, item.getMsg());
    }
}
