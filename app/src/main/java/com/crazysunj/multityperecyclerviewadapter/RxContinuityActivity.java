package com.crazysunj.multityperecyclerviewadapter;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;

import com.crazysunj.multitypeadapter.sticky.StickyHeaderDecoration;
import com.crazysunj.multityperecyclerviewadapter.data.FirstItem;
import com.crazysunj.multityperecyclerviewadapter.data.FourthItem;
import com.crazysunj.multityperecyclerviewadapter.data.SecondItem;
import com.crazysunj.multityperecyclerviewadapter.data.ThirdItem;
import com.crazysunj.multityperecyclerviewadapter.header.HeaderFirstItem;
import com.crazysunj.multityperecyclerviewadapter.header.HeaderFourthItem;
import com.crazysunj.multityperecyclerviewadapter.header.HeaderSecondItem;
import com.crazysunj.multityperecyclerviewadapter.header.HeaderThirdItem;
import com.crazysunj.multityperecyclerviewadapter.helper.RxAdapterHelper;
import com.crazysunj.multityperecyclerviewadapter.helper.SimpleHelper;
import com.crazysunj.multityperecyclerviewadapter.helper.SimpleRxHelperAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class RxContinuityActivity extends AppCompatActivity {

    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private RxAdapterHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concurrent);
        setTitle("Rx高频率线性排布");
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        textView1 = (TextView) findViewById(R.id.text1);
        textView2 = (TextView) findViewById(R.id.text2);
        textView3 = (TextView) findViewById(R.id.text3);
        textView4 = (TextView) findViewById(R.id.text4);
        helper = new RxAdapterHelper();
        SimpleRxHelperAdapter adapter = new SimpleRxHelperAdapter(helper);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new StickyHeaderDecoration(adapter));
        recyclerView.setAdapter(adapter);
        refresh();
    }

    private void refresh() {
        long timeMillis = System.currentTimeMillis();

        Random random = new Random();
        int rand1 = random.nextInt(6);
        final List<FirstItem> list1 = new ArrayList<>();
        for (int i = 0, size = rand1 + 1; i < size; i++) {
            list1.add(new FirstItem(String.format(Locale.getDefault(), "我是第一种类型%d--%d", i, random.nextInt(100)), timeMillis + i));
        }
        textView1.setText(String.format(Locale.getDefault(), "类型1的数量：%d", list1.size()));

        int rand2 = random.nextInt(6);
        List<SecondItem> list2 = new ArrayList<>();
        for (int i = 0, size = rand2 + 1; i < size; i++) {
            list2.add(new SecondItem(String.format(Locale.getDefault(), "我是第二种类型%d--%d", i, random.nextInt(100)), timeMillis + 6 + i));
        }
        textView2.setText(String.format(Locale.getDefault(), "类型2的数量：%d", list2.size()));

        int rand3 = random.nextInt(6);
        List<ThirdItem> list3 = new ArrayList<>();
        for (int i = 0, size = rand3 + 1; i < size; i++) {
            list3.add(new ThirdItem(String.format(Locale.getDefault(), "我是第三种类型%d--%d", i, random.nextInt(100)), timeMillis + 12 + i));
        }
        textView3.setText(String.format(Locale.getDefault(), "类型3的数量：%d", list3.size()));

        int rand4 = random.nextInt(6);
        List<FourthItem> list4 = new ArrayList<>();
        for (int i = 0, size = rand4 + 1; i < size; i++) {
            list4.add(new FourthItem(String.format(Locale.getDefault(), "我是第四种类型%d--%d", i, random.nextInt(100)), timeMillis + 18 + i));
        }
        textView4.setText(String.format(Locale.getDefault(), "类型4的数量：%d", list4.size()));

        helper.notifyModuleDataAndHeaderChanged(list1, new HeaderFirstItem("我是第一种类型头" + random.nextInt(100), timeMillis + 100), SimpleHelper.LEVEL_FIRST);
        helper.notifyModuleDataAndHeaderChanged(list2, new HeaderSecondItem("我是第二种类型头" + random.nextInt(100), timeMillis + 200), SimpleHelper.LEVEL_SENCOND);
        helper.notifyModuleDataAndHeaderChanged(list3, new HeaderThirdItem("我是第三种类型头" + random.nextInt(100), timeMillis + 300), SimpleHelper.LEVEL_THIRD);
        helper.notifyModuleDataAndHeaderChanged(list4, new HeaderFourthItem("我是第四种类型头" + random.nextInt(100), timeMillis + 400), SimpleHelper.LEVEL_FOURTH);
    }

    public void click(View view) {
        refresh();
    }
}
