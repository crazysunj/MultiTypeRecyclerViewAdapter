package com.crazysunj.multityperecyclerviewadapter.testlevel;

import com.chad.library.adapter.base.BaseViewHolder;
import com.crazysunj.multitypeadapter.adapter.LoadingEntityAdapter;
import com.crazysunj.multitypeadapter.helper.LoadingConfig;
import com.crazysunj.multitypeadapter.helper.RecyclerViewAdapterHelper;

import java.util.List;

/**
 * author: sunjian
 * created on: 2018/4/3 上午11:20
 * description:
 */

public class TestLevelAdapter extends BaseAdapter<MultiTypeTitleEntity, BaseViewHolder, TestLevelAdapterHelper> {


    private LevelFirstItemConvert mFirstItemConvert;
    private LevelSecondItemConvert mSecondItemConvert;
    private LevelThirdItemConvert mThirdItemConvert;

    public static final int TYPE_LEVEL_FIRST_HEADER = TestLevelAdapterHelper.LEVEL_FIRST - RecyclerViewAdapterHelper.HEADER_TYPE_DIFFER;
    public static final int TYPE_LEVEL_FIRST_FOOTER = TestLevelAdapterHelper.LEVEL_FIRST - RecyclerViewAdapterHelper.FOOTER_TYPE_DIFFER;
    public static final int TYPE_LEVEL_SECOND_HEADER = TestLevelAdapterHelper.LEVEL_SECOND - RecyclerViewAdapterHelper.HEADER_TYPE_DIFFER;
    public static final int TYPE_LEVEL_THIRD_HEADER = TestLevelAdapterHelper.LEVEL_THIRD - RecyclerViewAdapterHelper.HEADER_TYPE_DIFFER;
    public static final int TYPE_LEVEL_THIRD_FOOTER = TestLevelAdapterHelper.LEVEL_THIRD - RecyclerViewAdapterHelper.FOOTER_TYPE_DIFFER;

    public TestLevelAdapter(List<MultiTypeTitleEntity> data) {
        super(new TestLevelAdapterHelper(data));
        mFirstItemConvert = new LevelFirstItemConvert();
        mSecondItemConvert = new LevelSecondItemConvert();
        mThirdItemConvert = new LevelThirdItemConvert();
        mHelper.setLoadingAdapter(new LoadingEntityAdapter<MultiTypeTitleEntity>() {
            @Override
            public MultiTypeTitleEntity createLoadingEntity(int type, int level) {
                return new LevelLoadingItem(type);
            }

            @Override
            public MultiTypeTitleEntity createLoadingHeaderEntity(int type, int level) {
                return new LevelLoadingItem(type);
            }

            @Override
            public void bindLoadingEntity(MultiTypeTitleEntity loadingEntity, int position) {

            }
        });
        mHelper.initGlobalLoadingConfig(new LoadingConfig.Builder()
                .setLoading(TestLevelAdapterHelper.LEVEL_FIRST, 3, true)
                .setLoading(TestLevelAdapterHelper.LEVEL_SECOND, 2, true)
                .setLoading(TestLevelAdapterHelper.LEVEL_THIRD, 2, true)
                .build());
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiTypeTitleEntity item) {
        final int itemType = item.getItemType();
        switch (itemType) {
            case TypeOneItem.TYPE_ONE:
            case TypeTwoItem.TYPE_TWO:
            case TYPE_LEVEL_FIRST_HEADER:
            case TYPE_LEVEL_FIRST_FOOTER:
                mFirstItemConvert.convert(helper, item);
                break;
            case TypeThreeItem.TYPE_THREE:
            case TYPE_LEVEL_SECOND_HEADER:
                mSecondItemConvert.convert(helper, item);
                break;
            case TypeFourItem.TYPE_FOUR:
            case TypeFiveItem.TYPE_FIVE:
            case TYPE_LEVEL_THIRD_HEADER:
            case TYPE_LEVEL_THIRD_FOOTER:
                mThirdItemConvert.convert(helper, item);
                break;
            default:
                break;
        }
    }

    public void notifyLoading() {
        mHelper.notifyLoadingChanged();
    }

    public void notifyAll(List<MultiTypeTitleEntity> data) {
        mHelper.notifyDataByDiff(data);
    }

    public void notifyLevelFirst(MultiTypeTitleEntity header, List<MultiTypeTitleEntity> data, MultiTypeTitleEntity footer) {
        mHelper.notifyMoudleDataAndHeaderAndFooterChanged(header, data, footer, TestLevelAdapterHelper.LEVEL_FIRST);
    }

    public void notifyLevelSecond(MultiTypeTitleEntity header, List<MultiTypeTitleEntity> data) {
        mHelper.notifyMoudleDataAndHeaderChanged(data, header, TestLevelAdapterHelper.LEVEL_SECOND);
    }

    public void notifyLevelThird(MultiTypeTitleEntity header, List<MultiTypeTitleEntity> data, MultiTypeTitleEntity footer) {
        mHelper.notifyMoudleDataAndHeaderAndFooterChanged(header, data, footer, TestLevelAdapterHelper.LEVEL_THIRD);
    }
}
