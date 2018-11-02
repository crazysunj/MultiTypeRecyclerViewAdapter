package com.crazysunj.multityperecyclerviewadapter;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.crazysunj.multityperecyclerviewadapter.apt.AptDefaultHelperAdapter;
import com.crazysunj.multityperecyclerviewadapter.apt.AptDefaultHelperAdapterHelper;
import com.crazysunj.multityperecyclerviewadapter.data.FirstItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RxAptDefaultActivity extends AppCompatActivity {

    private AptDefaultHelperAdapterHelper helper;

    public static final Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apt_default);
        setTitle("RxApt默认条目刷新");
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        helper = new AptDefaultHelperAdapterHelper();
        AptDefaultHelperAdapter adapter = new AptDefaultHelperAdapter(helper);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        click(null);
    }

    public void click(View view) {

        Random random = new Random();
        int rand = random.nextInt(20);
        final List<FirstItem> list = new ArrayList<>();
        for (int i = 0, size = rand + 1; i < size; i++) {
            list.add(new FirstItem(String.format(Locale.getDefault(), "我是第一种类型%d", i)));
        }
        helper.notifyModuleDataChanged(list, 0);


    }

}
