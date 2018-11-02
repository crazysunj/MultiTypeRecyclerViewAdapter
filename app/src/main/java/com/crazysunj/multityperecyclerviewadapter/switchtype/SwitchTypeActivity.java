package com.crazysunj.multityperecyclerviewadapter.switchtype;

import android.os.Bundle;
import android.view.View;

import com.crazysunj.multitypeadapter.entity.MultiTypeEntity;
import com.crazysunj.multityperecyclerviewadapter.R;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    private List<MultiTypeEntity> initData() {
        List<MultiTypeEntity> data = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            data.add(new FirstType(i, "我是type1类型：" + i));
        }

        for (int i = 0; i < 5; i++) {
            data.add(new SecondType(i + 5, "我是type2类型：" + i));
        }

        for (int i = 0; i < 5; i++) {
            data.add(new ThirdType(i + 10, "我是type3类型：" + i));
        }

        for (int i = 0; i < 5; i++) {
            data.add(new FourthType(i + 15, "我是type4类型：" + i));
        }

        return data;
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
