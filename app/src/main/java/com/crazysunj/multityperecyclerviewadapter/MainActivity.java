package com.crazysunj.multityperecyclerviewadapter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.crazysunj.multitypeadapter.entity.MultiHeaderEntity;
import com.crazysunj.multityperecyclerviewadapter.data.FirstItem;
import com.crazysunj.multityperecyclerviewadapter.data.FourthItem;
import com.crazysunj.multityperecyclerviewadapter.data.SecondItem;
import com.crazysunj.multityperecyclerviewadapter.data.ThirdItem;
import com.crazysunj.multityperecyclerviewadapter.header.HeaderFirstItem;
import com.crazysunj.multityperecyclerviewadapter.header.HeaderFourthItem;
import com.crazysunj.multityperecyclerviewadapter.header.HeaderThirdItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SampleAdapter adapter;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private GridLayoutManager layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Random random = new Random();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        textView1 = (TextView) findViewById(R.id.text1);
        textView2 = (TextView) findViewById(R.id.text2);
        textView3 = (TextView) findViewById(R.id.text3);
        textView4 = (TextView) findViewById(R.id.text4);
        adapter = new SampleAdapter();
//        layout = new GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false) {
//            @Override
//            public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
//                try {
//                    super.onLayoutChildren(recycler, state);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
//                try {
//                    return super.scrollVerticallyBy(dy, recycler, state);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    return 0;
//                }
//            }
//        };

//        recyclerView.setLayoutManager(layout);

        recyclerView.setLayoutManager(new LinearLayoutManager(this) {

            @Override
            public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                try {
                    super.onLayoutChildren(recycler, state);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
                try {
                    return super.scrollVerticallyBy(dy, recycler, state);
                } catch (Exception e) {
                    e.printStackTrace();
                    return 0;
                }
            }

            @Override
            public boolean canScrollVertically() {
                try {
                    return super.canScrollVertically();
                } catch (Exception e) {
                    return true;
                }
            }
        });
//        StickyHeaderDecoration decoration = new StickyHeaderDecoration(adapter, true);
//        recyclerView.addItemDecoration(decoration);
//        recyclerView.setAdapter(adapter);
        adapter.bindToRecyclerView(recyclerView);
//        layout.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                MultiHeaderEntity entity = adapter.getData().get(position);
//                int itemType = entity.getItemType();
//                Log.d("MainActivity", "itemType:" + itemType);
//                if (itemType == SampleAdapter.DEFAULT_HEADER_TYPE || itemType == SampleAdapter.TYPE_HEADER_IMG) {
//                    return layout.getSpanCount();
//                }
//                return 1;
//            }
//        });

        int rand = random.nextInt(6);
        List<MultiHeaderEntity> list = new ArrayList<>();
        for (int i = 0, size = rand + 1; i < size; i++) {
            list.add(new ThirdItem(String.format("我是第三种类型%d", i), 12 + i));
        }
        textView3.setText(String.format("类型3的数量：%d", list.size()));
        for (MultiHeaderEntity entity : list) {
            Log.d("MainActivity", "getItemType:" + entity.getItemType());
        }
        adapter.notifyMoudleDataAndHeaderChanged(list, new HeaderThirdItem("我是第三种类型的头", adapter.getRandomId()), SampleAdapter.TYPE_FOUR);
//        adapter.notifyShimmerDataAndHeaderChanged(SampleAdapter.TYPE_FOUR, 3);
    }

    public void click1(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                long s = System.currentTimeMillis();
                Random random = new Random();
                int rand = random.nextInt(60);
                final List<MultiHeaderEntity> list = new ArrayList<>();
                for (int i = 0, size = rand + 1; i < size; i++) {
                    list.add(new FirstItem(String.format("我是第一种类型%d", i), i));
                }
                final int size = list.size();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        textView1.setText(String.format("类型1的数量：%d", size));

                        long start = System.currentTimeMillis();
                        adapter.notifyMoudleDataAndHeaderChanged(list, new HeaderFirstItem(String.format("我是第一种类型的头,点击次数：%d", refreshFirstCount++), adapter.getRandomId()), SampleAdapter.TYPE_ONE);
                        Log.d("MainActivity", String.format("刷新使用时间：%d", System.currentTimeMillis() - start));
                    }
                });
                Log.d("MainActivity", String.format("添加数据使用时间：%d", System.currentTimeMillis() - s));


            }
        }).start();

    }

    private boolean isSecondClick = true;

    public void click2(View view) {
        if (isSecondClick) {
            isSecondClick = false;
            Random random = new Random();
            int rand = random.nextInt(6);
            List<MultiHeaderEntity> list = new ArrayList<>();
            for (int i = 0, size = rand + 1; i < size; i++) {
                list.add(new SecondItem(String.format("我是第二种类型%d", i), 6 + i));
            }
            textView2.setText(String.format("类型2的数量：%d", list.size()));
            adapter.notifyMoudleDataChanged(list, SampleAdapter.TYPE_THREE);
        } else {
            isSecondClick = true;
            adapter.notifyShimmerDataChanged(SampleAdapter.TYPE_THREE, 2);
        }

    }

    private int refreshThirdCount = 0;
    private int refreshFirstCount = 0;

    private boolean isThirdClick = true;

    public void click3(View view) {
        if (isThirdClick) {
            isThirdClick = false;
            adapter.notifyMoudleHeaderChanged(new HeaderThirdItem(String.format("我是第三种类型的头,点击次数：%d", refreshThirdCount++), adapter.getRandomId()), SampleAdapter.TYPE_FOUR);
        } else {
            isThirdClick = true;
            adapter.notifyShimmerHeaderChanged(SampleAdapter.TYPE_FOUR);
        }
    }

    private boolean isFourthClick = true;

    public void click4(View view) {
        if (isFourthClick) {
            isFourthClick = false;
            Random random = new Random();
            int rand = random.nextInt(6);
            List<MultiHeaderEntity> list = new ArrayList<>();
            for (int i = 0, size = rand + 1; i < size; i++) {
                list.add(new FourthItem(String.format("我是第四种类型%d", i), 18 + i));
            }
            textView4.setText(String.format("类型4的数量：%d", list.size()));
            adapter.notifyMoudleDataAndHeaderChanged(list, new HeaderFourthItem(String.format("我是第四种类型的头,数量：%d", list.size()), adapter.getRandomId()), SampleAdapter.TYPE_TWO);
        } else {
            isFourthClick = true;
            adapter.notifyShimmerDataAndHeaderChanged(SampleAdapter.TYPE_TWO, 3);
        }
    }
}
