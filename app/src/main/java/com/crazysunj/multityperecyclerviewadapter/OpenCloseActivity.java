package com.crazysunj.multityperecyclerviewadapter;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crazysunj.multitypeadapter.entity.LevelData;
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

        mAdapter.setOnFooterClickListener((level, isFold) -> mAdapter.open(level, isFold));

        recyclerView.setAdapter(mAdapter);
        initData();
    }

    private void initData() {
        SparseArray<LevelData<OpenCloseItem>> levelData = new SparseArray<>();

        int firstTypeLevel = OpenCloseAdapterHelper.LEVEL_FIRST;
        levelData.put(firstTypeLevel, new LevelData<>(getFirst(),
                new TitleOCEntity(firstTypeLevel, "类型1"),
                new FooterOCEntity(firstTypeLevel, mAdapter.isDataFolded(firstTypeLevel) ? Constants.EXPAND : Constants.FOLD)));

        int secondType = OpenCloseAdapterHelper.LEVEL_SECOND;
        levelData.put(secondType, new LevelData<>(getSecond(),
                new TitleOCEntity(secondType, "类型2"),
                new FooterOCEntity(secondType, mAdapter.isDataFolded(secondType) ? Constants.EXPAND : Constants.FOLD)));

        int thirdType = OpenCloseAdapterHelper.LEVEL_THIRD;
        levelData.put(thirdType, new LevelData<>(getThird(),
                new TitleOCEntity(thirdType, "类型3"),
                new FooterOCEntity(thirdType, mAdapter.isDataFolded(thirdType) ? Constants.EXPAND : Constants.FOLD)));
        mAdapter.notifyAll(levelData);
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
