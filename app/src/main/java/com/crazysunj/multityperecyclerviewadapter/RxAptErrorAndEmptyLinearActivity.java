package com.crazysunj.multityperecyclerviewadapter;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.crazysunj.multitypeadapter.helper.RecyclerViewAdapterHelper;
import com.crazysunj.multitypeadapter.sticky.StickyHeaderDecoration;
import com.crazysunj.multitypeadapter.util.IDUtil;
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
    private MyErrorEntity errorfourthEntity = new MyErrorEntity(SimpleHelper.LEVEL_FOURTH - RecyclerViewAdapterHelper.ERROR_TYPE_DIFFER, "我是第四种错误title", "我是第四种错误message");
    private MyEmptyEntity emptyEntity = new MyEmptyEntity(SimpleHelper.LEVEL_THIRD - RecyclerViewAdapterHelper.EMPTY_TYPE_DIFFER, "我肚子好饿啊");

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
        adapter.setOnErrorCallback((v, type) -> {
            int id = v.getId();
            if (id == R.id.retry && type == 1) {
                helper.notifyLoadingDataChanged(1, 2);
                textView2.postDelayed(() -> {
                    Random random = new Random();
                    int rand = random.nextInt(6);
                    List<SecondItem> list = new ArrayList<>();
                    for (int i = 0, size = rand + 1; i < size; i++) {
                        list.add(new SecondItem(String.format(Locale.getDefault(), "我是第二种类型%d", i), 6 + i));
                    }
                    textView2.setText(String.format(Locale.getDefault(), "类型2的数量：%d", list.size()));
                    helper.notifyModuleDataChanged(list, 1);
                }, 2000);
            } else if (id == R.id.retry && type == 3) {
                helper.notifyLoadingDataAndHeaderChanged(3, 3);
                textView4.postDelayed(() -> {
                    Random random = new Random();
                    int rand = random.nextInt(6);
                    List<FourthItem> list = new ArrayList<>();
                    for (int i = 0, size = rand + 1; i < size; i++) {
                        list.add(new FourthItem(String.format(Locale.getDefault(), "我是第四种类型%d", i), 18 + i));
                    }
                    textView4.setText(String.format(Locale.getDefault(), "类型4的数量：%d", list.size()));
                    helper.notifyModuleDataAndHeaderChanged(list, new HeaderFourthItem(String.format(Locale.getDefault(), "我是第四种类型的头,数量：%d", list.size()), IDUtil.getId()), 3);
                }, 2000);
            }
        });

        helper.notifyLoadingDataAndHeaderChanged(0, 3);
        textView1.postDelayed(() -> {
            Random random = new Random();
            int rand = random.nextInt(6);
            final List<FirstItem> list = new ArrayList<>();
            for (int i = 0, size = rand + 1; i < size; i++) {
                list.add(new FirstItem(String.format(Locale.getDefault(), "我是第一种类型%d", i), i));
            }
            textView1.setText(String.format(Locale.getDefault(), "类型1的数量：%d", list.size()));
            helper.notifyModuleDataAndHeaderChanged(list, new HeaderFirstItem(String.format(Locale.getDefault(), "我是第一种类型的头,点击次数：%d", refreshFirstCount++), IDUtil.getId()), 0);

        }, 2000);

        helper.notifyLoadingDataAndHeaderChanged(2, 3);
        textView3.postDelayed(() -> {
            Random random = new Random();
            int rand = random.nextInt(6);
            List<ThirdItem> list = new ArrayList<>();
            for (int i = 0, size = rand + 1; i < size; i++) {
                list.add(new ThirdItem(String.format(Locale.getDefault(), "我是第三种类型%d", i), 12 + i));
            }
            textView3.setText(String.format(Locale.getDefault(), "类型3的数量：%d", list.size()));
            helper.notifyModuleDataAndHeaderChanged(list, new HeaderThirdItem("我是第三种类型的头", IDUtil.getId()), 2);
        }, 3000);
    }

    public void click1(View view) {


        helper.notifyLoadingDataAndHeaderChanged(0, 1);
        textView1.postDelayed(() -> {
            textView1.setText(String.format(Locale.getDefault(), "类型1的数量：%d", 1));
            helper.notifyModuleDataAndHeaderChanged(new FirstItem("我是新刷新的条目" + refreshFirstCount, System.currentTimeMillis()), new HeaderFirstItem(String.format(Locale.getDefault(), "我是第一种类型的头,点击次数：%d", refreshFirstCount++), IDUtil.getId()), 0);
        }, 2000);

    }

    public void click2(View view) {

        helper.notifyLoadingDataChanged(1, 2);
        textView2.postDelayed(() -> helper.notifyModuleErrorChanged(1), 2000);

    }

    private int refreshThirdCount = 0;
    private int refreshFirstCount = 0;


    public void click3(View view) {

        helper.notifyLoadingDataAndHeaderChanged(2, 1);
        textView3.postDelayed(() -> helper.notifyModuleEmptyChanged(emptyEntity, 2), 2000);
    }

    public void click4(View view) {

        helper.notifyLoadingDataAndHeaderChanged(3, 3);
        textView4.postDelayed(() -> helper.notifyModuleErrorChanged(errorfourthEntity, 3), 2000);
    }
}
