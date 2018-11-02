package com.crazysunj.multityperecyclerviewadapter;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.crazysunj.multityperecyclerviewadapter.constant.Constants;
import com.crazysunj.multityperecyclerviewadapter.expand.FirstOCEntity;
import com.crazysunj.multityperecyclerviewadapter.expand.FooterOCEntity;
import com.crazysunj.multityperecyclerviewadapter.expand.OpenCloseAdapter;
import com.crazysunj.multityperecyclerviewadapter.expand.OpenCloseAdapterHelper;
import com.crazysunj.multityperecyclerviewadapter.expand.OpenCloseItem;
import com.crazysunj.multityperecyclerviewadapter.expand.SecondOCEntity;
import com.crazysunj.multityperecyclerviewadapter.expand.ThirdOCEntity;
import com.crazysunj.multityperecyclerviewadapter.expand.TitleOCEntity;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OpenCloseActivity extends AppCompatActivity {

    private OpenCloseAdapter mAdapter;
    private EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_close);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        input = (EditText) findViewById(R.id.input);
        input.setSelection(input.getText().length());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new OpenCloseAdapter();
        mAdapter.setOnErrorCallback(level -> {
            if (level == OpenCloseAdapterHelper.LEVEL_FIRST) {
                mAdapter.notifyFirst(getFirst());
            }
        });

        mAdapter.setOnFooterClickListener((type, isFlod) -> mAdapter.open(type, isFlod));

        recyclerView.setAdapter(mAdapter);
        initData();
    }

    private void initData() {

        List<OpenCloseItem> data = new ArrayList<OpenCloseItem>();

        int firstTypeLevel = OpenCloseAdapterHelper.LEVEL_FIRST;
        data.add(new TitleOCEntity(firstTypeLevel, "类型1"));
        data.addAll(getFirst());
        data.add(new FooterOCEntity(firstTypeLevel, Constants.EXPAND));
//        data.add(new FooterOCEntity(firstType, Constants.EXPAND, mAdapter.getHelper().getId()));

        int secondType = OpenCloseAdapterHelper.LEVEL_SECOND;
        data.add(new TitleOCEntity(secondType, "类型2"));
        data.addAll(getSecond());
        data.add(new FooterOCEntity(secondType, Constants.FOLD));

        int thirdType = OpenCloseAdapterHelper.LEVEL_THIRD;
        data.add(new TitleOCEntity(thirdType, "类型3"));
        data.addAll(getThird());
        data.add(new FooterOCEntity(thirdType, Constants.FOLD));
        mAdapter.notifyAll(data);
    }

    public static int COUNT = 15;

    private List<FirstOCEntity> getFirst() {
        List<FirstOCEntity> data = new ArrayList<FirstOCEntity>();
        for (int i = 0; i < COUNT; i++) {
            data.add(new FirstOCEntity("我是类型1-" + i));
        }
        return data;
    }

    private List<SecondOCEntity> getSecond() {
        List<SecondOCEntity> data = new ArrayList<SecondOCEntity>();
        for (int i = 0; i < COUNT; i++) {
            data.add(new SecondOCEntity("我是类型2-" + i));
        }
        return data;
    }

    private List<ThirdOCEntity> getThird() {
        List<ThirdOCEntity> data = new ArrayList<ThirdOCEntity>();
        for (int i = 0; i < COUNT; i++) {
            data.add(new ThirdOCEntity("我是类型3-" + i));
        }
        return data;
    }

    public void click1(View view) {
        mAdapter.notifyFirstEmpty();
    }

    public void click2(View view) {
        mAdapter.notifyFirstError();
    }

    public void click3(View view) {
        String count = input.getText().toString().trim();
        if (TextUtils.isEmpty(count)) {
            Toast.makeText(this, "请输入集合长度", Toast.LENGTH_SHORT).show();
        } else {
            COUNT = Integer.parseInt(count);
            initData();
        }
    }

    public void click4(View view) {
        mAdapter.notifyFirstLoading();
        view.postDelayed(() -> mAdapter.notifyFirst(getFirst()), 2000);
    }
}
