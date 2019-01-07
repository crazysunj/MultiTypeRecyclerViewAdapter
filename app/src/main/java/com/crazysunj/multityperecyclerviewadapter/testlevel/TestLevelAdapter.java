package com.crazysunj.multityperecyclerviewadapter.testlevel;

import android.widget.Toast;

import com.crazysunj.multitypeadapter.adapter.LoadingEntityAdapter;
import com.crazysunj.multitypeadapter.entity.LevelData;
import com.crazysunj.multitypeadapter.helper.LoadingConfig;
import com.crazysunj.multitypeadapter.helper.RecyclerViewAdapterHelper;
import com.crazysunj.multityperecyclerviewadapter.R;
import com.crazysunj.multityperecyclerviewadapter.helper.BaseHelperAdapter;
import com.crazysunj.multityperecyclerviewadapter.helper.BaseViewHolder;

import java.util.List;
import java.util.Random;

/**
 * author: sunjian
 * created on: 2018/4/3 上午11:20
 * description:
 */

public class TestLevelAdapter extends BaseHelperAdapter<MultiTypeTitleEntity, BaseViewHolder, TestLevelAdapterHelper> {


    private LevelFirstItemConvert mFirstItemConvert;
    private LevelSecondItemConvert mSecondItemConvert;
    private LevelThirdItemConvert mThirdItemConvert;

    public static final int TYPE_LEVEL_FIRST_HEADER = TestLevelAdapterHelper.LEVEL_FIRST - RecyclerViewAdapterHelper.HEADER_TYPE_DIFFER;
    public static final int TYPE_LEVEL_FIRST_FOOTER = TestLevelAdapterHelper.LEVEL_FIRST - RecyclerViewAdapterHelper.FOOTER_TYPE_DIFFER;
    public static final int TYPE_LEVEL_FIRST_EMPTY = TestLevelAdapterHelper.LEVEL_FIRST - RecyclerViewAdapterHelper.EMPTY_TYPE_DIFFER;
    public static final int TYPE_LEVEL_SECOND_HEADER = TestLevelAdapterHelper.LEVEL_SECOND - RecyclerViewAdapterHelper.HEADER_TYPE_DIFFER;
    public static final int TYPE_LEVEL_SECOND_ERROR = TestLevelAdapterHelper.LEVEL_SECOND - RecyclerViewAdapterHelper.ERROR_TYPE_DIFFER;
    public static final int TYPE_LEVEL_THIRD_HEADER = TestLevelAdapterHelper.LEVEL_THIRD - RecyclerViewAdapterHelper.HEADER_TYPE_DIFFER;
    public static final int TYPE_LEVEL_THIRD_FOOTER = TestLevelAdapterHelper.LEVEL_THIRD - RecyclerViewAdapterHelper.FOOTER_TYPE_DIFFER;

    public TestLevelAdapter(TestLevelAdapterHelper helper) {
        super(helper);
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
    protected void convert(BaseViewHolder holder, MultiTypeTitleEntity item) {
        final int itemType = item.getItemType();
        switch (itemType) {
            case TypeOneItem.TYPE_ONE:
            case TypeTwoItem.TYPE_TWO:
            case TYPE_LEVEL_FIRST_HEADER:
            case TYPE_LEVEL_FIRST_FOOTER:
            case TYPE_LEVEL_FIRST_EMPTY:
                mFirstItemConvert.convert(holder, item);
                break;
            case TypeThreeItem.TYPE_THREE:
            case TYPE_LEVEL_SECOND_HEADER:
            case TYPE_LEVEL_SECOND_ERROR:
                mSecondItemConvert.convert(holder, item);
                break;
            case TypeFourItem.TYPE_FOUR:
            case TypeFiveItem.TYPE_FIVE:
            case TYPE_LEVEL_THIRD_HEADER:
            case TYPE_LEVEL_THIRD_FOOTER:
                mThirdItemConvert.convert(holder, item);
                break;
            case TypeEmptyAllItem.TYPE_EMPTY:
                holder.getTextView(R.id.empty_title).setText(item.getMsg());
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
        mHelper.notifyModuleDataAndHeaderAndFooterChanged(data, header, footer, TestLevelAdapterHelper.LEVEL_FIRST);
    }

    public void notifyLevelSecond(MultiTypeTitleEntity header, List<MultiTypeTitleEntity> data) {
        mHelper.notifyModuleDataAndHeaderChanged(data, header, TestLevelAdapterHelper.LEVEL_SECOND);
    }

    public void notifyLevelThird(MultiTypeTitleEntity header, List<MultiTypeTitleEntity> data, MultiTypeTitleEntity footer) {
        mHelper.notifyModuleDataAndHeaderAndFooterChanged(data, header, footer, TestLevelAdapterHelper.LEVEL_THIRD);
    }

    int clickCount;

    public void notifyRandomLevelItem() {
        Random random = new Random();
        int level = random.nextInt(3);
        LevelData<MultiTypeTitleEntity> levelData = mHelper.getDataWithLevel(level);
        int refreshType = clickCount % 3;
        if (refreshType == 0) {
            LevelTitleItem header = (LevelTitleItem) levelData.getHeader();
            header.setMsg("我是修改过的header,level=" + level + " 当前点击次数=" + clickCount);
            mHelper.notifyDataChanged(header);
            Toast.makeText(mContext, "修改level=" + level + "  =>header", Toast.LENGTH_SHORT).show();
        } else if (refreshType == 1) {
            List<MultiTypeTitleEntity> data = levelData.getData();
            MultiTypeTitleEntity entity = data.get(0);
            if (level == TestLevelAdapterHelper.LEVEL_FIRST) {
                int itemType = entity.getItemType();
                if (itemType == TypeOneItem.TYPE_ONE) {
                    TypeOneItem item = (TypeOneItem) entity;
                    item.setMsg("我是修改过的type=" + itemType + " level=" + level + " 当前点击次数=" + clickCount);
                } else if (itemType == TypeTwoItem.TYPE_TWO) {
                    TypeTwoItem item = (TypeTwoItem) entity;
                    item.setMsg("我是修改过的type=" + itemType + " level=" + level + " 当前点击次数=" + clickCount);
                }
            } else if (level == TestLevelAdapterHelper.LEVEL_SECOND) {
                int itemType = entity.getItemType();
                TypeThreeItem item = (TypeThreeItem) entity;
                item.setMsg("我是修改过的type=" + itemType + " level=" + level + " 当前点击次数=" + clickCount);
            } else if (level == TestLevelAdapterHelper.LEVEL_THIRD) {
                int itemType = entity.getItemType();
                if (itemType == TypeFourItem.TYPE_FOUR) {
                    TypeFourItem item = (TypeFourItem) entity;
                    item.setMsg("我是修改过的type=" + itemType + " level=" + level + " 当前点击次数=" + clickCount);
                } else if (itemType == TypeFiveItem.TYPE_FIVE) {
                    TypeFiveItem item = (TypeFiveItem) entity;
                    item.setMsg("我是修改过的type=" + itemType + " level=" + level + " 当前点击次数=" + clickCount);
                }
            }
            Toast.makeText(mContext, "修改level=" + level + "  =>data", Toast.LENGTH_SHORT).show();
            mHelper.notifyDataChanged(entity);
        } else if (refreshType == 2) {
            LevelFooterItem footer = (LevelFooterItem) levelData.getFooter();
            if (footer != null) {
                footer.setMsg("我是修改过的footer,level=" + level + " 当前点击次数=" + clickCount);
                Toast.makeText(mContext, "修改level=" + level + "  =>footer", Toast.LENGTH_SHORT).show();
                mHelper.notifyDataChanged(footer);
            }
        }
        clickCount++;
    }


    public void notifyRandomLevel() {
        Random random = new Random();
        int level = random.nextInt(3);
        handleLevel(level);
        Toast.makeText(mContext, "修改level=" + level, Toast.LENGTH_SHORT).show();
        mHelper.notifyDataChanged(level);
        clickCount++;
    }

    private void handleLevel(int level) {
        LevelData<MultiTypeTitleEntity> levelData = mHelper.getDataWithLevel(level);
        LevelTitleItem header = (LevelTitleItem) levelData.getHeader();
        header.setMsg("我是修改过的header,level=" + level + " 当前点击次数=" + clickCount);
        List<MultiTypeTitleEntity> data = levelData.getData();
        MultiTypeTitleEntity entity = data.get(0);
        if (level == TestLevelAdapterHelper.LEVEL_FIRST) {
            int itemType = entity.getItemType();
            if (itemType == TypeOneItem.TYPE_ONE) {
                TypeOneItem item = (TypeOneItem) entity;
                item.setMsg("我是修改过的type=" + itemType + " level=" + level + " 当前点击次数=" + clickCount);
            } else if (itemType == TypeTwoItem.TYPE_TWO) {
                TypeTwoItem item = (TypeTwoItem) entity;
                item.setMsg("我是修改过的type=" + itemType + " level=" + level + " 当前点击次数=" + clickCount);
            }
        } else if (level == TestLevelAdapterHelper.LEVEL_SECOND) {
            int itemType = entity.getItemType();
            TypeThreeItem item = (TypeThreeItem) entity;
            item.setMsg("我是修改过的type=" + itemType + " level=" + level + " 当前点击次数=" + clickCount);
        } else if (level == TestLevelAdapterHelper.LEVEL_THIRD) {
            int itemType = entity.getItemType();
            if (itemType == TypeFourItem.TYPE_FOUR) {
                TypeFourItem item = (TypeFourItem) entity;
                item.setMsg("我是修改过的type=" + itemType + " level=" + level + " 当前点击次数=" + clickCount);
            } else if (itemType == TypeFiveItem.TYPE_FIVE) {
                TypeFiveItem item = (TypeFiveItem) entity;
                item.setMsg("我是修改过的type=" + itemType + " level=" + level + " 当前点击次数=" + clickCount);
            }
        }
        LevelFooterItem footer = (LevelFooterItem) levelData.getFooter();
        if (footer != null) {
            footer.setMsg("我是修改过的footer,level=" + level + " 当前点击次数=" + clickCount);
        }
    }

    public void notifyRandom() {
        handleLevel(TestLevelAdapterHelper.LEVEL_FIRST);
        handleLevel(TestLevelAdapterHelper.LEVEL_SECOND);
        handleLevel(TestLevelAdapterHelper.LEVEL_THIRD);
        mHelper.notifyDataChanged();
        clickCount++;
    }
}
