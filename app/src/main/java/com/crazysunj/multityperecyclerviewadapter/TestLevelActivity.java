package com.crazysunj.multityperecyclerviewadapter;

import android.os.Bundle;
import android.view.View;

import com.crazysunj.multityperecyclerviewadapter.testlevel.LevelFooterItem;
import com.crazysunj.multityperecyclerviewadapter.testlevel.LevelTitleItem;
import com.crazysunj.multityperecyclerviewadapter.testlevel.MultiTypeTitleEntity;
import com.crazysunj.multityperecyclerviewadapter.testlevel.TestLevelAdapter;
import com.crazysunj.multityperecyclerviewadapter.testlevel.TypeFiveItem;
import com.crazysunj.multityperecyclerviewadapter.testlevel.TypeFourItem;
import com.crazysunj.multityperecyclerviewadapter.testlevel.TypeOneItem;
import com.crazysunj.multityperecyclerviewadapter.testlevel.TypeThreeItem;
import com.crazysunj.multityperecyclerviewadapter.testlevel.TypeTwoItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
        adapter = new TestLevelAdapter(getData());
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


    private List<MultiTypeTitleEntity> getData() {
        List<MultiTypeTitleEntity> data = new ArrayList<>();
        List<MultiTypeTitleEntity> firstData = getLevelFirstData();
        List<MultiTypeTitleEntity> secondData = getLevelSecondData();
        List<MultiTypeTitleEntity> thirdData = getLevelThirdData();
        MultiTypeTitleEntity firstHeader = getLevelFirstHeader();
        MultiTypeTitleEntity firstFooter = getLevelFirstFooter();
        MultiTypeTitleEntity secondHeader = getLevelSecondHeader();
        MultiTypeTitleEntity thirdHeader = getLevelThirdHeader();
        MultiTypeTitleEntity thirdFooter = getLevelThirdFooter();
        data.addAll(firstData);
        data.addAll(secondData);
        data.addAll(thirdData);
        data.add(firstHeader);
        data.add(firstFooter);
        data.add(secondHeader);
        data.add(thirdHeader);
        data.add(thirdFooter);
        return data;
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
