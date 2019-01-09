package com.crazysunj.multityperecyclerviewadapter;

import android.os.Bundle;
import android.view.View;

import com.crazysunj.multitypeadapter.entity.MultiTypeEntity;
import com.crazysunj.multitypeadapter.helper.AdapterHelper;
import com.crazysunj.multityperecyclerviewadapter.testlevel.LevelFirstEmptyItem;
import com.crazysunj.multityperecyclerviewadapter.testlevel.LevelFooterItem;
import com.crazysunj.multityperecyclerviewadapter.testlevel.LevelSecondErrorItem;
import com.crazysunj.multityperecyclerviewadapter.testlevel.LevelTitleItem;
import com.crazysunj.multityperecyclerviewadapter.testlevel.MultiTypeTitleEntity;
import com.crazysunj.multityperecyclerviewadapter.testlevel.TestLevelAdapter;
import com.crazysunj.multityperecyclerviewadapter.testlevel.TestLevelAdapterHelper;
import com.crazysunj.multityperecyclerviewadapter.testlevel.TypeEmptyAllItem;
import com.crazysunj.multityperecyclerviewadapter.testlevel.TypeFiveItem;
import com.crazysunj.multityperecyclerviewadapter.testlevel.TypeFourItem;
import com.crazysunj.multityperecyclerviewadapter.testlevel.TypeThreeItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Refresh240Activity extends AppCompatActivity {

    Random random = new Random();

    private static final int MAX_RANDOM = 10;

    private TestLevelAdapterHelper mHelper;
    private LevelTitleItem mFirstHeaderItem;
    private LevelFooterItem mFirstFooterItem;
    private List<TypeThreeItem> mSecondList;
    private LevelTitleItem mThirdHeaderItem;
    private List<MultiTypeTitleEntity> mThirdList;
    private LevelFooterItem mThirdFooterItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh240);
        RecyclerView mDataList = findViewById(R.id.data_list);
        mHelper = new TestLevelAdapterHelper();
        TestLevelAdapter adapter = new TestLevelAdapter(mHelper);
        mDataList.setLayoutManager(new LinearLayoutManager(this));
        mDataList.setAdapter(adapter);
        mFirstHeaderItem = getLevelFirstHeader();
        mFirstFooterItem = getLevelFirstFooter();
        mSecondList = getLevelSecondData();
        mThirdHeaderItem = getLevelThirdHeader();
        mThirdFooterItem = getLevelThirdFooter();
        mThirdList = getLevelThirdData();
    }

    int levelFirstRefreshCount;

    private LevelTitleItem getLevelFirstHeader() {
        return new LevelTitleItem(TestLevelAdapter.TYPE_LEVEL_FIRST_HEADER, String.format(Locale.getDefault(), "我是level0的头，第%d次刷新啦", levelFirstRefreshCount));
    }

    private LevelFooterItem getLevelFirstFooter() {
        return new LevelFooterItem(TestLevelAdapter.TYPE_LEVEL_FIRST_FOOTER, String.format(Locale.getDefault(), "我是level0的尾，第%d次刷新啦", levelFirstRefreshCount));

    }

    int levelSecondRefreshCount;

    private List<TypeThreeItem> getLevelSecondData() {
        List<TypeThreeItem> data = new ArrayList<>();
        final int size = random.nextInt(MAX_RANDOM) + 2;
        for (int i = 0; i < size; i++) {
            data.add(new TypeThreeItem(i + 10, String.format(Locale.getDefault(), "我是level1第%d条，type为%d，第%d次刷新啦", i + 1, TypeThreeItem.TYPE_THREE, 0)));
        }
        return data;
    }

    private List<TypeThreeItem> getFixLevelSecondData() {
        List<TypeThreeItem> data = new ArrayList<>();
        final int size = 6;
        for (int i = 0; i < size; i++) {
            data.add(new TypeThreeItem(i + 10, String.format(Locale.getDefault(), "我是level1第%d条，type为%d，第%d次刷新啦", i + 1, TypeThreeItem.TYPE_THREE, 0)));
        }
        return data;
    }

    int levelThirdRefreshCount;

    private List<MultiTypeTitleEntity> getLevelThirdData() {
        List<MultiTypeTitleEntity> data = new ArrayList<>();
        final int size = random.nextInt(MAX_RANDOM) + 2;
        for (int i = 0; i < size; i++) {
            data.add(getLevelThirdItem(i));
        }
        return data;
    }

    private List<MultiTypeTitleEntity> getFixLevelThirdData() {
        List<MultiTypeTitleEntity> data = new ArrayList<>();
        final int size = 6;
        for (int i = 0; i < size; i++) {
            data.add(getLevelThirdItem(i));
        }
        return data;
    }

    private MultiTypeTitleEntity getLevelThirdItem(int i) {
        return i % 2 == 0 ? new TypeFourItem(i + 20, String.format(Locale.getDefault(), "我是level2第%d条，type为%d，第%d次刷新啦", i + 1, TypeFourItem.TYPE_FOUR, 0)) :
                new TypeFiveItem(i + 20, String.format(Locale.getDefault(), "我是level2第%d条，type为%d，第%d次刷新啦", i + 1, TypeFiveItem.TYPE_FIVE, 0));
    }

    private LevelTitleItem getLevelThirdHeader() {
        return new LevelTitleItem(TestLevelAdapter.TYPE_LEVEL_THIRD_HEADER, String.format(Locale.getDefault(), "我是level2的头，第%d次刷新啦", levelThirdRefreshCount));
    }

    private LevelFooterItem getLevelThirdFooter() {
        return new LevelFooterItem(TestLevelAdapter.TYPE_LEVEL_THIRD_FOOTER, String.format(Locale.getDefault(), "我是level2的尾，第%d次刷新啦", levelThirdRefreshCount));
    }

    public void click0(View view) {
        boolean isChange = random.nextInt(2) == 0;
        LevelTitleItem firstHeaderItem = null;
        LevelFooterItem firstFooterItem = null;
        if (isChange) {
            firstHeaderItem = mFirstHeaderItem;
            firstFooterItem = mFirstFooterItem;
            firstHeaderItem.setMsg(String.format(Locale.getDefault(), "我是level0的头，第%d次刷新啦", levelFirstRefreshCount));
            firstFooterItem.setMsg(String.format(Locale.getDefault(), "我是level0的尾，第%d次刷新啦", levelFirstRefreshCount));
        }
        levelFirstRefreshCount++;
        AdapterHelper.with(TestLevelAdapterHelper.LEVEL_FIRST)
                .header(firstHeaderItem)
                .footer(firstFooterItem)
                .header(firstHeaderItem)
                .footer(firstFooterItem)
                .header(firstHeaderItem)
                .footer(firstFooterItem)
                .data((List<? extends MultiTypeEntity>) null)
                .into(mHelper);
    }

    public void click1(View view) {
        boolean isOutChange = random.nextInt(2) == 0;
        List<TypeThreeItem> secondList;
        if (isOutChange) {
            secondList = new ArrayList<>(mSecondList);
            List<TypeThreeItem> removeList = new ArrayList<>();
            for (int i = 0; i < secondList.size(); i++) {
                TypeThreeItem threeItem = secondList.get(i);
                boolean isChange = random.nextInt(2) == 0;
                if (!isChange && removeList.size() < secondList.size() - 1) {
                    removeList.add(threeItem);
                } else {
                    threeItem.setMsg(String.format(Locale.getDefault(), "我是level1第%d条，type为%d，第%d次刷新啦", i + 1, TypeThreeItem.TYPE_THREE, levelSecondRefreshCount));
                }
            }
            secondList.removeAll(removeList);
        } else {
            secondList = getLevelSecondData();
        }
        levelSecondRefreshCount++;
        AdapterHelper.with(TestLevelAdapterHelper.LEVEL_SECOND)
                .data(secondList)
                .data(secondList)
                .data(secondList)
                .into(mHelper);
    }

    public void click2(View view) {
        LevelTitleItem thirdHeaderItem = null;
        LevelFooterItem thirdFooterItem = null;
        boolean isOutChange = random.nextInt(2) == 0;
        List<MultiTypeTitleEntity> thirdList;
        if (isOutChange) {
            thirdHeaderItem = mThirdHeaderItem;
            thirdFooterItem = mThirdFooterItem;
            thirdHeaderItem.setMsg(String.format(Locale.getDefault(), "我是level2的头，第%d次刷新啦", levelThirdRefreshCount));
            thirdFooterItem.setMsg(String.format(Locale.getDefault(), "我是level2的尾，第%d次刷新啦", levelThirdRefreshCount));
            thirdList = getLevelThirdData();
        } else {
            thirdList = new ArrayList<>(mThirdList);
            List<MultiTypeTitleEntity> removeList = new ArrayList<>();
            for (int i = 0; i < thirdList.size(); i++) {
                MultiTypeTitleEntity entity = thirdList.get(i);
                boolean isChange = random.nextInt(2) == 0;
                if (!isChange && removeList.size() < thirdList.size() - 1) {
                    removeList.add(entity);
                } else {
                    if (entity instanceof TypeFourItem) {
                        ((TypeFourItem) entity).setMsg(String.format(Locale.getDefault(), "我是level2第%d条，type为%d，第%d次刷新啦", i + 1, TypeFourItem.TYPE_FOUR, levelThirdRefreshCount));
                    } else {
                        ((TypeFiveItem) entity).setMsg(String.format(Locale.getDefault(), "我是level2第%d条，type为%d，第%d次刷新啦", i + 1, TypeFiveItem.TYPE_FIVE, levelThirdRefreshCount));
                    }
                }
            }
            thirdList.removeAll(removeList);
        }
        levelThirdRefreshCount++;
        AdapterHelper.with(TestLevelAdapterHelper.LEVEL_THIRD)
                .header(thirdHeaderItem)
                .data(thirdList)
                .footer(thirdFooterItem)
                .footer(thirdFooterItem)
                .data(thirdList)
                .header(thirdHeaderItem)
                .into(mHelper);
    }

    public void click3(View view) {
        AdapterHelper.with(TestLevelAdapterHelper.LEVEL_FIRST)
                .empty(new LevelFirstEmptyItem("我是level0的空页面"))
                .into(mHelper);
    }

    public void click4(View view) {
        AdapterHelper.with(TestLevelAdapterHelper.LEVEL_SECOND)
                .error(new LevelSecondErrorItem("我是level1的错误页面"))
                .into(mHelper);
    }

    public void click5(View view) {
        AdapterHelper.with(TestLevelAdapterHelper.LEVEL_THIRD)
                .loading()
                .header()
                .data(4)
                .into(mHelper);
    }

    public void click6(View view) {
        AdapterHelper.action().clear().into(mHelper);
    }

    public void click7(View view) {
        AdapterHelper.action().all(getLevelThirdData()).into(mHelper);
    }

    public void click8(View view) {
        AdapterHelper.action().all(new TypeEmptyAllItem("全局空页面")).into(mHelper);
    }

    public void click9(View view) {
        AdapterHelper.action()
                .add(getFixLevelThirdData())
                .add(0, new TypeThreeItem(15, String.format(Locale.getDefault(), "我是level1新插的，type为%d", TypeThreeItem.TYPE_THREE)))
                .add(0, getFixLevelSecondData())
                .remove(0)
                .remove(9, 2)
                .set(0, new TypeThreeItem(16, String.format(Locale.getDefault(), "我是level1修改的条目，type为%d", TypeThreeItem.TYPE_THREE)))
                .into(mHelper);
    }
}
