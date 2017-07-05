package com.crazysunj.multityperecyclerviewadapter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.crazysunj.multityperecyclerviewadapter.expand.FirstOCEntity;
import com.crazysunj.multityperecyclerviewadapter.expand.OpenCloseAdapter;

public class OpenCloseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_close);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        OpenCloseAdapter mAdapter = new OpenCloseAdapter();
        mAdapter.setOnErrorCallback(new OpenCloseAdapter.OnErrorCallback() {
            @Override
            public void onError(int type) {
                if (type == FirstOCEntity.OC_FIRST_TYPE) {

                }
            }
        });

        recyclerView.setAdapter(mAdapter);
    }

    public void click1(View view) {

    }

    public void click2(View view) {

    }

    public void click3(View view) {

    }

    public void click4(View view) {

    }

    public void click5(View view) {

    }

    public void click6(View view) {

    }
}
