package com.crazysunj.multityperecyclerviewadapter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.crazysunj.multitypeadapter.sticky.StickyHeaderDecoration;
import com.crazysunj.multitypeadapter.util.IDUtil;
import com.crazysunj.multityperecyclerviewadapter.data.FirstItem;
import com.crazysunj.multityperecyclerviewadapter.data.FourthItem;
import com.crazysunj.multityperecyclerviewadapter.data.SecondItem;
import com.crazysunj.multityperecyclerviewadapter.data.ThirdItem;
import com.crazysunj.multityperecyclerviewadapter.header.HeaderFirstItem;
import com.crazysunj.multityperecyclerviewadapter.header.HeaderFourthItem;
import com.crazysunj.multityperecyclerviewadapter.header.HeaderThirdItem;
import com.crazysunj.multityperecyclerviewadapter.helper.MultiHeaderEntity;
import com.crazysunj.multityperecyclerviewadapter.helper.RxAdapterHelper;
import com.crazysunj.multityperecyclerviewadapter.helper.SimpleHelper;
import com.crazysunj.multityperecyclerviewadapter.helper.SimpleRxHelperAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RxLinearActivity extends AppCompatActivity {

    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private RxAdapterHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Rx线性排布");
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

        helper.notifyLoadingDataAndHeaderChanged(SimpleHelper.LEVEL_THIRD, 3);
        textView3.postDelayed(() -> {
            Random random = new Random();
            int rand = random.nextInt(6);
            List<MultiHeaderEntity> list = new ArrayList<>();
            for (int i = 0, size = rand + 1; i < size; i++) {
                list.add(new ThirdItem(String.format(Locale.getDefault(),"我是第三种类型%d", i), 12 + i));
            }
            textView3.setText(String.format(Locale.getDefault(),"类型3的数量：%d", list.size()));
            helper.notifyModuleDataAndHeaderChanged(list, new HeaderThirdItem("我是第三种类型的头", IDUtil.getId()), SimpleHelper.LEVEL_THIRD);
        }, 3000);
    }

    @SuppressLint("CheckResult")
    public void click1(View view) {
        helper.notifyLoadingDataAndHeaderChanged(SimpleHelper.LEVEL_FIRST, 3);
        Flowable.timer(2, TimeUnit.SECONDS)
                .observeOn(Schedulers.io())
                .map(aLong -> {
                    Random random = new Random();
                    int rand = random.nextInt(6000);
                    final List<MultiHeaderEntity> list = new ArrayList<>();
                    for (int i = 0, size = rand + 1; i < size; i++) {
                        list.add(new FirstItem(String.format(Locale.getDefault(), "我是第一种类型%d", i), i));
                    }
                    return list;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(list -> {
                    textView1.setText(String.format(Locale.getDefault(), "类型1的数量：%d", list.size()));
                    helper.notifyModuleDataAndHeaderChanged(list, new HeaderFirstItem(String.format(Locale.getDefault(), "我是第一种类型的头,点击次数：%d", refreshFirstCount++), IDUtil.getId()), SimpleHelper.LEVEL_FIRST);
                });

    }

    public void click2(View view) {

        helper.notifyLoadingDataChanged(SimpleHelper.LEVEL_SENCOND, 2);
        textView2.postDelayed(() -> {
            Random random = new Random();
            int rand = random.nextInt(6);
            List<MultiHeaderEntity> list = new ArrayList<>();
            for (int i = 0, size = rand + 1; i < size; i++) {
                list.add(new SecondItem(String.format(Locale.getDefault(),"我是第二种类型%d", i), 6 + i));
            }
            textView2.setText(String.format(Locale.getDefault(),"类型2的数量：%d", list.size()));
            helper.notifyModuleDataChanged(list, SimpleHelper.LEVEL_SENCOND);
        }, 2000);

    }

    private int refreshThirdCount = 0;
    private int refreshFouthCount = 0;
    private int refreshFirstCount = 0;


    public void click3(View view) {

        helper.notifyLoadingHeaderChanged(SimpleHelper.LEVEL_THIRD);
        textView3.postDelayed(() -> helper.notifyModuleHeaderChanged(new HeaderThirdItem(String.format(Locale.getDefault(),"我是第三种类型的头,点击次数：%d", refreshThirdCount++), IDUtil.getId()), SimpleHelper.LEVEL_THIRD), 2000);
    }

    public void click4(View view) {

        textView4.postDelayed(() -> {
            List<MultiHeaderEntity> list = new ArrayList<>();
            for (int i = 0, size = 6; i < size; i++) {
                list.add(new FourthItem(String.format(Locale.getDefault(),"我是第四种类型%d", i), 18 + i));
            }
            textView4.setText(String.format(Locale.getDefault(),"类型4的数量：%d", list.size()));
            String title = String.format(Locale.getDefault(), "我是第四种类型的头,点击次数：%d", refreshFouthCount++);
            helper.notifyModuleDataAndHeaderChanged(list, new HeaderFourthItem(title, title.hashCode()), SimpleHelper.LEVEL_FOURTH);
        }, 0);
    }
}
