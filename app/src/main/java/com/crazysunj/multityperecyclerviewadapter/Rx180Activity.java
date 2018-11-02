package com.crazysunj.multityperecyclerviewadapter;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.crazysunj.multityperecyclerviewadapter.data.FirstItem;
import com.crazysunj.multityperecyclerviewadapter.data.FourthItem;
import com.crazysunj.multityperecyclerviewadapter.data.SecondItem;
import com.crazysunj.multityperecyclerviewadapter.data.ThirdItem;
import com.crazysunj.multityperecyclerviewadapter.header.HeaderFirstItem;
import com.crazysunj.multityperecyclerviewadapter.header.HeaderFourthItem;
import com.crazysunj.multityperecyclerviewadapter.header.HeaderSecondItem;
import com.crazysunj.multityperecyclerviewadapter.header.HeaderThirdItem;
import com.crazysunj.multityperecyclerviewadapter.helper.MultiHeaderEntity;
import com.crazysunj.multityperecyclerviewadapter.helper.RxAdapterHelper;
import com.crazysunj.multityperecyclerviewadapter.helper.SimpleRxHelperAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Rx180Activity extends AppCompatActivity {

    private EditText editText;
    private RxAdapterHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx180);
        editText = (EditText) findViewById(R.id.edittext);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        helper = new RxAdapterHelper(initData());
        SimpleRxHelperAdapter adapter = new SimpleRxHelperAdapter(helper);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @NonNull
    private List<MultiHeaderEntity> initData() {

        List<MultiHeaderEntity> list = new ArrayList<>();
        list.add(new HeaderFirstItem("我是第一种类型的头"));
        list.add(new HeaderSecondItem("我是第二种类型的头"));
        list.add(new HeaderThirdItem("我是第三种类型的头"));
        list.add(new HeaderFourthItem("我是第四种类型的头"));

        List<FirstItem> list1 = new ArrayList<>();
        for (int i = 0, size = 4; i < size; i++) {
            list1.add(new FirstItem(String.format(Locale.getDefault(), "我是第一种类型%d", i)));
        }
        list.addAll(list1);

        List<SecondItem> list2 = new ArrayList<>();
        for (int i = 0, size = 4; i < size; i++) {
            list2.add(new SecondItem(String.format(Locale.getDefault(), "我是第二种类型%d", i)));
        }
        list.addAll(list2);

        final List<ThirdItem> list3 = new ArrayList<>();
        for (int i = 0, size = 4; i < size; i++) {
            list3.add(new ThirdItem(String.format(Locale.getDefault(), "我是第三种类型%d", i)));
        }
        list.addAll(list3);


        List<FourthItem> list4 = new ArrayList<>();
        for (int i = 0, size = 4; i < size; i++) {
            list4.add(new FourthItem(String.format(Locale.getDefault(), "我是第四种类型%d", i)));
        }
        list.addAll(list4);

        return list;
    }

    public void click1(View view) {
        helper.notifyDataByDiff(initData());
    }

    public void click2(View view) {
        String types = editText.getText().toString().trim();
        if (TextUtils.isEmpty(types)) {
            return;
        }
        String[] split = types.split("，");
        helper.clearModule(get(split));
    }

    private int[] get(String[] split) {
        int[] type = new int[split.length];
        for (int i = 0; i < split.length; i++) {
            type[i] = Integer.parseInt(split[i]);
        }
        return type;
    }

    public void click3(View view) {
        String types = editText.getText().toString().trim();
        if (TextUtils.isEmpty(types)) {
            return;
        }
        String[] split = types.split("，");
        helper.remainModule(get(split));
    }

    public void click4(View view) {
        helper.addData(2, new FirstItem("我是新加的第一种类型"));
    }

    public void click5(View view) {
        helper.addData(new FourthItem("我是新加的第四种类型"));
    }

    public void click6(View view) {
        List<FirstItem> list = new ArrayList<>();
        list.add(new FirstItem("位置1添加类型1集合1"));
        list.add(new FirstItem("位置1添加类型1集合2"));
        helper.addData(2, list);
    }

    public void click7(View view) {
        List<FourthItem> list = new ArrayList<>();
        list.add(new FourthItem("添加类型4集合1"));
        list.add(new FourthItem("添加类型4集合2"));
        helper.addData(list);
    }
}
