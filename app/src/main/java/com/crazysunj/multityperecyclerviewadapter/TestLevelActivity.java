package com.crazysunj.multityperecyclerviewadapter;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crazysunj.multitypeadapter.entity.LevelData;
import com.crazysunj.multityperecyclerviewadapter.testlevel.LevelFooterItem;
import com.crazysunj.multityperecyclerviewadapter.testlevel.LevelTitleItem;
import com.crazysunj.multityperecyclerviewadapter.testlevel.MultiTypeTitleEntity;
import com.crazysunj.multityperecyclerviewadapter.testlevel.TestLevelAdapter;
import com.crazysunj.multityperecyclerviewadapter.testlevel.TestLevelAdapterHelper;
import com.crazysunj.multityperecyclerviewadapter.testlevel.TypeFiveItem;
import com.crazysunj.multityperecyclerviewadapter.testlevel.TypeFourItem;
import com.crazysunj.multityperecyclerviewadapter.testlevel.TypeOneItem;
import com.crazysunj.multityperecyclerviewadapter.testlevel.TypeThreeItem;
import com.crazysunj.multityperecyclerviewadapter.testlevel.TypeTwoItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class TestLevelActivity extends AppCompatActivity {

    Random random = new Random();

    private static final int MAX_RANDOM = 6;
    private TestLevelAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_level);
        recyclerView = findViewById(R.id.recyclerview);
        levelFirstRefreshCount++;
        levelSecondRefreshCount++;
        levelThirdRefreshCount++;
        adapter = new TestLevelAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    public void click1(View view) {
        levelFirstRefreshCount++;
        levelSecondRefreshCount++;
        levelThirdRefreshCount++;
        adapter.notifyLoading();
        recyclerView.postDelayed(() -> adapter.notifyAll(getData()), 3000);
    }

    public void click2(View view) {
        levelFirstRefreshCount++;
        adapter.notifyLevelFirst(getLevelFirstHeader(), getLevelFirstData(), getLevelFirstFooter());
    }

    public void click3(View view) {
        levelSecondRefreshCount++;
        adapter.notifyLevelSecond(getLevelSecondHeader(), getLevelSecondData());
    }

    public void click4(View view) {
        levelThirdRefreshCount++;
        adapter.notifyLevelThird(getLevelThirdHeader(), getLevelThirdData(), getLevelThirdFooter());
    }


    private SparseArray<LevelData<MultiTypeTitleEntity>> getData() {
        SparseArray<LevelData<MultiTypeTitleEntity>> levelData = new SparseArray<>();

        MultiTypeTitleEntity firstHeader = getLevelFirstHeader();
        MultiTypeTitleEntity firstFooter = getLevelFirstFooter();
        List<MultiTypeTitleEntity> firstData = getLevelFirstData();

        levelData.put(TestLevelAdapterHelper.LEVEL_FIRST, new LevelData<>(firstData, firstHeader, firstFooter));


        List<MultiTypeTitleEntity> secondData = getLevelSecondData();
        MultiTypeTitleEntity secondHeader = getLevelSecondHeader();
        levelData.put(TestLevelAdapterHelper.LEVEL_SECOND, new LevelData<>(secondData, secondHeader, null));


        List<MultiTypeTitleEntity> thirdData = getLevelThirdData();
        MultiTypeTitleEntity thirdHeader = getLevelThirdHeader();
        MultiTypeTitleEntity thirdFooter = getLevelThirdFooter();
        levelData.put(TestLevelAdapterHelper.LEVEL_THIRD, new LevelData<>(thirdData, thirdHeader, thirdFooter));

        return levelData;
    }

    int levelFirstRefreshCount;

    private List<MultiTypeTitleEntity> getLevelFirstData() {
        List<MultiTypeTitleEntity> data = new ArrayList<>();
        final int size = random.nextInt(MAX_RANDOM) + 2;
        for (int i = 0; i < size; i++) {
            data.add(getLevelFirstItem(i));
        }
        return data;
    }

    private MultiTypeTitleEntity getLevelFirstHeader() {
        return new LevelTitleItem(TestLevelAdapter.TYPE_LEVEL_FIRST_HEADER, String.format(Locale.getDefault(), "我是level0的头，第%d次刷新啦", levelFirstRefreshCount));
    }

    private MultiTypeTitleEntity getLevelFirstFooter() {
        return new LevelFooterItem(TestLevelAdapter.TYPE_LEVEL_FIRST_FOOTER, String.format(Locale.getDefault(), "我是level0的尾，第%d次刷新啦", levelFirstRefreshCount));

    }

    private MultiTypeTitleEntity getLevelFirstItem(int i) {
        return i % 2 == 0 ? new TypeOneItem(i, String.format(Locale.getDefault(), "我是level0第%d条，type为%d，第%d次刷新啦", i + 1, TypeOneItem.TYPE_ONE, levelFirstRefreshCount)) :
                new TypeTwoItem(i, String.format(Locale.getDefault(), "我是level0第%d条，type为%d，第%d次刷新啦", i + 1, TypeTwoItem.TYPE_TWO, levelFirstRefreshCount));
    }

    int levelSecondRefreshCount;

    private List<MultiTypeTitleEntity> getLevelSecondData() {
        List<MultiTypeTitleEntity> data = new ArrayList<>();
        final int size = random.nextInt(MAX_RANDOM) + 2;
        for (int i = 0; i < size; i++) {
            data.add(new TypeThreeItem(i + 10, String.format(Locale.getDefault(), "我是level1第%d条，type为%d，第%d次刷新啦", i + 1, TypeThreeItem.TYPE_THREE, levelSecondRefreshCount)));
        }
        return data;
    }

    private MultiTypeTitleEntity getLevelSecondHeader() {
        return new LevelTitleItem(TestLevelAdapter.TYPE_LEVEL_SECOND_HEADER, String.format(Locale.getDefault(), "我是level1的头，第%d次刷新啦", levelSecondRefreshCount));
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

    private MultiTypeTitleEntity getLevelThirdItem(int i) {
        return i % 2 == 0 ? new TypeFourItem(i + 20, String.format(Locale.getDefault(), "我是level2第%d条，type为%d，第%d次刷新啦", i + 1, TypeFourItem.TYPE_FOUR, levelThirdRefreshCount)) :
                new TypeFiveItem(i + 20, String.format(Locale.getDefault(), "我是level2第%d条，type为%d，第%d次刷新啦", i + 1, TypeFiveItem.TYPE_FIVE, levelThirdRefreshCount));
    }

    private MultiTypeTitleEntity getLevelThirdHeader() {
        return new LevelTitleItem(TestLevelAdapter.TYPE_LEVEL_THIRD_HEADER, String.format(Locale.getDefault(), "我是level2的头，第%d次刷新啦", levelThirdRefreshCount));
    }

    private MultiTypeTitleEntity getLevelThirdFooter() {
        return new LevelFooterItem(TestLevelAdapter.TYPE_LEVEL_THIRD_FOOTER, String.format(Locale.getDefault(), "我是level2的尾，第%d次刷新啦", levelThirdRefreshCount));
    }

    public void click5(View view) {
        adapter.notifyRandomLevelItem();
    }

    public void click6(View view) {
        adapter.notifyRandomLevel();
    }

    public void click7(View view) {
        adapter.notifyRandom();
    }
}
