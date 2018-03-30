package com.crazysunj.multityperecyclerviewadapter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.crazysunj.multitypeadapter.sticky.StickyHeaderDecoration;
import com.crazysunj.multityperecyclerviewadapter.data.FirstItem;
import com.crazysunj.multityperecyclerviewadapter.data.FourthItem;
import com.crazysunj.multityperecyclerviewadapter.data.SecondItem;
import com.crazysunj.multityperecyclerviewadapter.data.ThirdItem;
import com.crazysunj.multityperecyclerviewadapter.header.HeaderFirstItem;
import com.crazysunj.multityperecyclerviewadapter.header.HeaderFourthItem;
import com.crazysunj.multityperecyclerviewadapter.header.HeaderThirdItem;
import com.crazysunj.multityperecyclerviewadapter.helper.MultiHeaderEntity;
import com.crazysunj.multityperecyclerviewadapter.helper.SimpleHelper;
import com.crazysunj.multityperecyclerviewadapter.helper.SimpleHelperAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class LinearActivity extends AppCompatActivity {

    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private SimpleHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("线性排布");
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        textView1 = (TextView) findViewById(R.id.text1);
        textView2 = (TextView) findViewById(R.id.text2);
        textView3 = (TextView) findViewById(R.id.text3);
        textView4 = (TextView) findViewById(R.id.text4);
        helper = new SimpleHelper();
        SimpleHelperAdapter adapter = new SimpleHelperAdapter(helper);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new StickyHeaderDecoration(adapter));
        recyclerView.setAdapter(adapter);

        helper.notifyLoadingDataAndHeaderChanged(SimpleHelper.LEVEL_THIRD, 3);
        textView3.postDelayed(new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                int rand = random.nextInt(6);
                List<MultiHeaderEntity> list = new ArrayList<>();
                for (int i = 0, size = rand + 1; i < size; i++) {
                    list.add(new ThirdItem(String.format(Locale.getDefault(), "我是第三种类型%d", i), 12 + i));
                }
                textView3.setText(String.format(Locale.getDefault(), "类型3的数量：%d", list.size()));
                helper.notifyMoudleDataAndHeaderChanged(list, new HeaderThirdItem("我是第三种类型的头", helper.getRandomId()), SimpleHelper.LEVEL_THIRD);
            }
        }, 3000);
    }

    public void click1(View view) {
        helper.notifyLoadingDataAndHeaderChanged(SimpleHelper.LEVEL_FIRST, 3);
        textView1.postDelayed(new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                int rand = random.nextInt(6000);
                final List<MultiHeaderEntity> list = new ArrayList<>();
                for (int i = 0, size = rand + 1; i < size; i++) {
                    list.add(new FirstItem(String.format(Locale.getDefault(), "我是第一种类型%d", i), i));
                }
                textView1.setText(String.format(Locale.getDefault(), "类型1的数量：%d", list.size()));
                helper.notifyMoudleDataAndHeaderChanged(list, new HeaderFirstItem(String.format(Locale.getDefault(), "我是第一种类型的头,点击次数：%d", refreshFirstCount++), helper.getRandomId()), SimpleHelper.LEVEL_FIRST);

            }
        }, 2000);

    }

    public void click2(View view) {

        helper.notifyLoadingDataChanged(SimpleHelper.LEVEL_SENCOND, 2);
        textView2.postDelayed(new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                int rand = random.nextInt(6);
                List<MultiHeaderEntity> list = new ArrayList<>();
                for (int i = 0, size = rand + 1; i < size; i++) {
                    list.add(new SecondItem(String.format(Locale.getDefault(), "我是第二种类型%d", i), 6 + i));
                }
                textView2.setText(String.format(Locale.getDefault(), "类型2的数量：%d", list.size()));
                helper.notifyMoudleDataChanged(list, SimpleHelper.LEVEL_SENCOND);
            }
        }, 2000);

    }

    private int refreshThirdCount = 0;
    private int refreshFirstCount = 0;


    public void click3(View view) {

        helper.notifyLoadingHeaderChanged(SimpleHelper.LEVEL_THIRD);
        textView3.postDelayed(new Runnable() {
            @Override
            public void run() {
                helper.notifyMoudleHeaderChanged(new HeaderThirdItem(String.format(Locale.getDefault(), "我是第三种类型的头,点击次数：%d", refreshThirdCount++), helper.getRandomId()), SimpleHelper.LEVEL_THIRD);
            }
        }, 2000);
    }

    public void click4(View view) {

        helper.notifyLoadingDataAndHeaderChanged(SimpleHelper.LEVEL_FOURTH, 3);
        textView4.postDelayed(new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                int rand = random.nextInt(6);
                List<MultiHeaderEntity> list = new ArrayList<>();
                for (int i = 0, size = rand + 1; i < size; i++) {
                    list.add(new FourthItem(String.format(Locale.getDefault(), "我是第四种类型%d", i), 18 + i));
                }
                textView4.setText(String.format(Locale.getDefault(), "类型4的数量：%d", list.size()));
                helper.notifyMoudleDataAndHeaderChanged(list, new HeaderFourthItem(String.format(Locale.getDefault(), "我是第四种类型的头,数量：%d", list.size()), helper.getRandomId()), SimpleHelper.LEVEL_FOURTH);
            }
        }, 2000);
    }
}
