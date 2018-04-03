package com.crazysunj.multityperecyclerviewadapter.testlevel;

import com.chad.library.adapter.base.BaseViewHolder;
import com.crazysunj.multityperecyclerviewadapter.R;

/**
 * author: sunjian
 * created on: 2018/4/3 下午1:34
 * description:
 */

public class LevelFirstItemConvert implements ItemConvert<MultiTypeTitleEntity, BaseViewHolder> {

    @Override
    public void convert(BaseViewHolder helper, MultiTypeTitleEntity item) {
        final int itemType = item.getItemType();
        switch (itemType) {
            case TypeOneItem.TYPE_ONE:
                renderTypeOne(helper, (TypeOneItem) item);
                break;
            case TypeTwoItem.TYPE_TWO:
                renderTypeTwo(helper, (TypeTwoItem) item);
                break;
            case TestLevelAdapter.TYPE_LEVEL_FIRST_HEADER:
                renderLevelFirstHeader(helper, (LevelTitleItem) item);
                break;
            case TestLevelAdapter.TYPE_LEVEL_FIRST_FOOTER:
                renderLevelFirstFooter(helper, (LevelFooterItem) item);
                break;
            default:
                break;
        }
    }

    private void renderLevelFirstFooter(BaseViewHolder helper, LevelFooterItem item) {
        helper.setText(R.id.item_footer, item.getMsg());
    }

    private void renderLevelFirstHeader(BaseViewHolder helper, LevelTitleItem item) {
        helper.setText(R.id.item_header, item.getMsg());
    }

    private void renderTypeTwo(BaseViewHolder helper, TypeTwoItem item) {
        helper.setText(R.id.item_name, item.getMsg());
    }

    private void renderTypeOne(BaseViewHolder helper, TypeOneItem item) {
        helper.setText(R.id.item_name, item.getMsg());
    }
}
