package com.crazysunj.multityperecyclerviewadapter.component;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crazysunj.multitypeadapter.entity.MultiTypeEntity;
import com.crazysunj.multityperecyclerviewadapter.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ComponentActivity extends AppCompatActivity {

    private MainAdapter mainAdapter;
    private SmartRefreshLayout mRefreshLayout;
    private Random random = new Random();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_component);
        mRefreshLayout = findViewById(R.id.refreshLayout);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshlayout) {
                mRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mainAdapter.refreshDiff(getNewData());
                        mRefreshLayout.finishRefreshWithNoMoreData();
                        mainAdapter.getLoadMoreHelper().loadMoreDefault();
                    }
                }, 2000);
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mainAdapter = new MainAdapter();
        mainAdapter.setOnLoadMoreRequestListener(new LoadMoreHelper.OnLoadMoreRequestListener() {
            @Override
            public void onLoadMore() {
                Log.d("ComponentActivity", "onLoadMore");
//                mRefreshLayout.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mainAdapter.loadMoreGone();
//                    }
//                }, 1000);
//                mRefreshLayout.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mainAdapter.loadMoreFailed();
//                    }
//                }, 3000);
                mRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mainAdapter.getData().size() > 40) {
                            mainAdapter.getLoadMoreHelper().loadMoreEnd();
                        } else {
                            mainAdapter.getLoadMoreHelper().loadMoreCompleted();
                        }
                        mainAdapter.loadMore(getNewData());
                    }
                }, 5000);
//                mRefreshLayout.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mainAdapter.loadMoreEnd();
//                    }
//                }, 7000);
//                mRefreshLayout.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mainAdapter.loadMoreDefault();
//                    }
//                }, 9000);
            }
        });
        recyclerView.setAdapter(mainAdapter);
        mRefreshLayout.finishLoadMoreWithNoMoreData();
        mainAdapter.refresh(getNewData());
    }

    private List<MultiTypeEntity> getNewData() {
        List<MultiTypeEntity> data = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            data.add(getItem());
        }
        return data;
    }

    private MultiTypeEntity getItem() {
        int type = random.nextInt(3);
        if (type == 1) {
            return new TypeTwo();
        }
        if (type == 2) {
            return new TypeThree();
        }
        return new TypeOne();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainAdapter.onVisible();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mainAdapter.onInVisible();
    }
}
