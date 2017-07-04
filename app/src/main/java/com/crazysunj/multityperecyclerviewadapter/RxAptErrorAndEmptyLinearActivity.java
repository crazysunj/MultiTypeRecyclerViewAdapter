package com.crazysunj.multityperecyclerviewadapter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.crazysunj.multitypeadapter.sticky.StickyHeaderDecoration;
import com.crazysunj.multityperecyclerviewadapter.apt.RxAptHelperAdapter;
import com.crazysunj.multityperecyclerviewadapter.apt.RxAptHelperAdapterHelper;
import com.crazysunj.multityperecyclerviewadapter.data.FirstItem;
import com.crazysunj.multityperecyclerviewadapter.data.FourthItem;
import com.crazysunj.multityperecyclerviewadapter.data.MyEmptyEntity;
import com.crazysunj.multityperecyclerviewadapter.data.MyErrorEntity;
import com.crazysunj.multityperecyclerviewadapter.data.SecondItem;
import com.crazysunj.multityperecyclerviewadapter.data.ThirdItem;
import com.crazysunj.multityperecyclerviewadapter.header.HeaderFirstItem;
import com.crazysunj.multityperecyclerviewadapter.header.HeaderFourthItem;
import com.crazysunj.multityperecyclerviewadapter.header.HeaderThirdItem;
import com.crazysunj.multityperecyclerviewadapter.helper.SimpleHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class RxAptErrorAndEmptyLinearActivity extends AppCompatActivity {

    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private RxAptHelperAdapterHelper helper;
    private MyErrorEntity errorfourthEntity = new MyErrorEntity(SimpleHelper.TYPE_TWO, "我是第四种错误title", "我是第四种错误message");
    private MyEmptyEntity emptyEntity = new MyEmptyEntity(SimpleHelper.TYPE_FOUR, "我肚子好饿啊");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("RxApt错误和空线性排布");
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        textView1 = (TextView) findViewById(R.id.text1);
        textView2 = (TextView) findViewById(R.id.text2);
        textView3 = (TextView) findViewById(R.id.text3);
        textView4 = (TextView) findViewById(R.id.text4);
        helper = new RxAptHelperAdapterHelper();
        RxAptHelperAdapter adapter = new RxAptHelperAdapter(helper);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new StickyHeaderDecoration(adapter));
        recyclerView.setAdapter(adapter);
        adapter.setOnErrorCallback(new RxAptHelperAdapter.OnErrorCallback() {
            @Override
            public void onClick(View v, int type) {
                int id = v.getId();
                if (id == R.id.retry && type == SimpleHelper.TYPE_THREE) {
                    helper.notifyShimmerDataChanged(SimpleHelper.TYPE_THREE, 2);
                    textView2.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Random random = new Random();
                            int rand = random.nextInt(6);
                            List<SecondItem> list = new ArrayList<>();
                            for (int i = 0, size = rand + 1; i < size; i++) {
                                list.add(new SecondItem(String.format(Locale.getDefault(), "我是第二种类型%d", i), 6 + i));
                            }
                            textView2.setText(String.format(Locale.getDefault(), "类型2的数量：%d", list.size()));
                            helper.notifyMoudleDataChanged(list, SimpleHelper.TYPE_THREE);
                        }
                    }, 2000);
                } else if (id == R.id.retry && type == SimpleHelper.TYPE_TWO) {
                    helper.notifyShimmerDataAndHeaderChanged(SimpleHelper.TYPE_TWO, 3);
                    textView4.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Random random = new Random();
                            int rand = random.nextInt(6);
                            List<FourthItem> list = new ArrayList<>();
                            for (int i = 0, size = rand + 1; i < size; i++) {
                                list.add(new FourthItem(String.format(Locale.getDefault(), "我是第四种类型%d", i), 18 + i));
                            }
                            textView4.setText(String.format(Locale.getDefault(), "类型4的数量：%d", list.size()));
                            helper.notifyMoudleDataAndHeaderChanged(list, new HeaderFourthItem(String.format(Locale.getDefault(), "我是第四种类型的头,数量：%d", list.size()), helper.getRandomId()), SimpleHelper.TYPE_TWO);
                        }
                    }, 2000);
                }
            }
        });

        helper.notifyShimmerDataAndHeaderChanged(SimpleHelper.TYPE_ONE, 3);
        textView1.postDelayed(new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                int rand = random.nextInt(6);
                final List<FirstItem> list = new ArrayList<>();
                for (int i = 0, size = rand + 1; i < size; i++) {
                    list.add(new FirstItem(String.format(Locale.getDefault(), "我是第一种类型%d", i), i));
                }
                textView1.setText(String.format(Locale.getDefault(), "类型1的数量：%d", list.size()));
                helper.notifyMoudleDataAndHeaderChanged(list, new HeaderFirstItem(String.format(Locale.getDefault(), "我是第一种类型的头,点击次数：%d", refreshFirstCount++), helper.getRandomId()), SimpleHelper.TYPE_ONE);

            }
        }, 2000);

        helper.notifyShimmerDataAndHeaderChanged(SimpleHelper.TYPE_FOUR, 3);
        textView3.postDelayed(new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                int rand = random.nextInt(6);
                List<ThirdItem> list = new ArrayList<>();
                for (int i = 0, size = rand + 1; i < size; i++) {
                    list.add(new ThirdItem(String.format(Locale.getDefault(), "我是第三种类型%d", i), 12 + i));
                }
                textView3.setText(String.format(Locale.getDefault(), "类型3的数量：%d", list.size()));
                helper.notifyMoudleDataAndHeaderChanged(list, new HeaderThirdItem("我是第三种类型的头", helper.getRandomId()), SimpleHelper.TYPE_FOUR);
            }
        }, 3000);
    }

    public void click1(View view) {


        helper.notifyShimmerDataAndHeaderChanged(SimpleHelper.TYPE_ONE, 1);
        textView1.postDelayed(new Runnable() {
            @Override
            public void run() {
                textView1.setText(String.format(Locale.getDefault(), "类型1的数量：%d", 1));
                helper.notifyMoudleDataAndHeaderChanged(new FirstItem("我是新刷新的条目" + refreshFirstCount, System.currentTimeMillis()), new HeaderFirstItem(String.format("我是第一种类型的头,点击次数：%d", refreshFirstCount++), helper.getRandomId()), SimpleHelper.TYPE_ONE);
            }
        }, 2000);

    }

    public void click2(View view) {

        helper.notifyShimmerDataChanged(SimpleHelper.TYPE_THREE, 2);
        textView2.postDelayed(new Runnable() {
            @Override
            public void run() {
                helper.notifyMoudleErrorChanged(SimpleHelper.TYPE_THREE);
            }
        }, 2000);

    }

    private int refreshThirdCount = 0;
    private int refreshFirstCount = 0;


    public void click3(View view) {

        helper.notifyShimmerDataAndHeaderChanged(SimpleHelper.TYPE_FOUR, 1);
        textView3.postDelayed(new Runnable() {
            @Override
            public void run() {
                helper.notifyMoudleEmptyChanged(emptyEntity, SimpleHelper.TYPE_FOUR);
            }
        }, 2000);
    }

    public void click4(View view) {

        helper.notifyShimmerDataAndHeaderChanged(SimpleHelper.TYPE_TWO, 3);
        textView4.postDelayed(new Runnable() {
            @Override
            public void run() {
                helper.notifyMoudleErrorChanged(errorfourthEntity, SimpleHelper.TYPE_TWO);
            }
        }, 2000);
    }
}
