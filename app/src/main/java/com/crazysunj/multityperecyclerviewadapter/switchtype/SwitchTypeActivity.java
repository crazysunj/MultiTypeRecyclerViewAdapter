package com.crazysunj.multityperecyclerviewadapter.switchtype;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crazysunj.multitypeadapter.entity.LevelData;
import com.crazysunj.multitypeadapter.entity.MultiTypeEntity;
import com.crazysunj.multityperecyclerviewadapter.R;

import java.util.ArrayList;
import java.util.List;

public class SwitchTypeActivity extends AppCompatActivity {

    private SwitchTypeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_type);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        adapter = new SwitchTypeAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.reset(initData());
    }

    private SparseArray<LevelData<MultiTypeEntity>> initData() {
        SparseArray<LevelData<MultiTypeEntity>> levelData = new SparseArray<>();

        List<MultiTypeEntity> firstData = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            firstData.add(new FirstType(i, "我是type1类型：" + i));
        }
        levelData.put(SwitchTypeAdapterHelper.LEVEL_FIRST, new LevelData<>(firstData));

        List<MultiTypeEntity> secondData = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            secondData.add(new SecondType(i + 5, "我是type2类型：" + i));
        }
        levelData.put(SwitchTypeAdapterHelper.LEVEL_SECOND, new LevelData<>(secondData));

        List<MultiTypeEntity> thirdData = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            thirdData.add(new ThirdType(i + 10, "我是type3类型：" + i));
        }
        levelData.put(SwitchTypeAdapterHelper.LEVEL_SECOND, new LevelData<>(thirdData));

        List<MultiTypeEntity> fourthData = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            fourthData.add(new FourthType(i + 15, "我是type4类型：" + i));
        }
        levelData.put(SwitchTypeAdapterHelper.LEVEL_SECOND, new LevelData<>(fourthData));

        return levelData;
    }

    public void onClick1(View view) {
        adapter.reset(initData());
    }

    public void onClick2(View view) {
        adapter.notifyA();
    }

    public void onClick3(View view) {
        adapter.notifyB();
    }

    public void onClick4(View view) {
        adapter.notifyC();
    }

    public void onClick5(View view) {
        adapter.notifyD();
    }
}
