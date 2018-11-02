package com.crazysunj.sample;

import android.animation.ArgbEvaluator;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.crazysunj.itemdecoration.grid.GridLayoutDividerItemDecoration;
import com.crazysunj.sample.adapter.FooterVPAdapter;
import com.crazysunj.sample.adapter.HeadAdapter;
import com.crazysunj.sample.entity.ItemEntity1;
import com.crazysunj.sample.entity.ItemEntity2;
import com.crazysunj.sample.entity.ItemEntity4;
import com.crazysunj.sample.normal.Type1Adapter;
import com.crazysunj.sample.normal.Type2Adapter;
import com.crazysunj.sample.normal.Type3Adapter;
import com.crazysunj.sample.util.SnackBarUtil;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

public class NormalActivity extends AppCompatActivity {

    private static final int RANDOM_DELAY_TIME = 1000;
    private Random mRandom = new Random();
    private ArgbEvaluator mArgbEvaluator = new ArgbEvaluator();

    private MaterialSearchView mSearchView;
    private Drawable mNavigationIcon;
    private Drawable mSearchItemIcon;
    private TextView mTitle;
    private Toolbar mToolbar;
    private AppBarLayout mAppBar;
    private NestedScrollView mScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal);

        initView();
        setSupportActionBar(mToolbar);

        initBar();
        initSearchView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        mSearchItemIcon = searchItem.getIcon();
        mSearchView.setMenuItem(searchItem);
        return true;
    }

    @Override
    public void onBackPressed() {

        if (mSearchView.isSearchOpen()) {
            mSearchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    private void initView() {

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mScrollView = (NestedScrollView) findViewById(R.id.scrollView);

        RecyclerView mHeadList = (RecyclerView) findViewById(R.id.head_list);
        mHeadList.setLayoutManager(new GridLayoutManager(this, 4));
        HeadAdapter headAdapter = new HeadAdapter();
        mHeadList.addItemDecoration(new GridLayoutDividerItemDecoration.Builder()
                .setLeftMargin(40)
                .setTopMargin(30)
                .setRightMargin(40)
                .setBottomMargin(30)
                .setDividerColor(getResources().getColor(R.color.color_line))
                .setDividerHeight(1)
                .build());
        mHeadList.setAdapter(headAdapter);


        RecyclerView mType1List = (RecyclerView) findViewById(R.id.type1_list);
        List<ItemEntity1> list1 = new ArrayList<ItemEntity1>();
        list1.add(new ItemEntity1("冠心病康复指导", "课程提醒", mRandom.nextInt(4) - 1));
        list1.add(new ItemEntity1("血管扩张剂输液", "输液提醒", mRandom.nextInt(4) - 1));
        list1.add(new ItemEntity1("服用银杏叶制剂", "用药提醒", mRandom.nextInt(4) - 1));
        list1.add(new ItemEntity1("血管", "输液", mRandom.nextInt(4) - 1));
        list1.add(new ItemEntity1("冠心", "课程", mRandom.nextInt(4) - 1));
        Type1Adapter type1Adapter = new Type1Adapter(list1);
        mType1List.setLayoutManager(new LinearLayoutManager(this));
        mType1List.setAdapter(type1Adapter);

        RecyclerView mType2List = (RecyclerView) findViewById(R.id.type2_list);
        List<ItemEntity2> list2 = new ArrayList<ItemEntity2>();
        list2.add(new ItemEntity2(R.mipmap.ic_bf_1, "住院康复营养A餐", "白粥+香菇肉丝+芹菜干丝", "￥ 26/餐"));
        list2.add(new ItemEntity2(R.mipmap.ic_bf_2, "住院康复营养B餐", "猪肉玉米粥+菌菇汤", "￥ 24/餐"));
        Type2Adapter type2Adapter = new Type2Adapter(list2);
        mType2List.setLayoutManager(new LinearLayoutManager(this));
        mType2List.setAdapter(type2Adapter);

        RecyclerView mType3List = (RecyclerView) findViewById(R.id.type3_list);
        List<ItemEntity4> list3 = new ArrayList<ItemEntity4>();
        list3.add(new ItemEntity4(R.mipmap.ic_doctor_1, "怎样系统预防及治疗冠心病", "刘飞", "首都医科大学宣武医院主治医师", "健康行家", "25人见过", "￥ 150/次"));
        list3.add(new ItemEntity4(R.mipmap.ic_doctor_2, "冠心病患者术后的综合管理", "刘淑芬", "北京协和医院主治医师", "健康行家", "32人见过", "￥ 300/次"));
        list3.add(new ItemEntity4(R.mipmap.ic_doctor_3, "心血管疾病怎么吃", "白元", "美国密歇根大学营养科学硕士", "养生行家", "28人见过", "￥ 280/次"));
        Type3Adapter type3Adapter = new Type3Adapter(list3);
        mType3List.setLayoutManager(new LinearLayoutManager(this));
        mType3List.setAdapter(type3Adapter);

        TabLayout mTab = (TabLayout) findViewById(R.id.footer_tab);
        ViewPager mPager = (ViewPager) findViewById(R.id.footer_pager);
        mPager.setAdapter(new FooterVPAdapter(getSupportFragmentManager()));
        mTab.setupWithViewPager(mPager);

        mSearchView = (MaterialSearchView) findViewById(R.id.search);
        mTitle = (TextView) findViewById(R.id.title);
        mAppBar = (AppBarLayout) findViewById(R.id.appbar);
    }

    private void initBar() {

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        mToolbar.setNavigationIcon(R.mipmap.ic_scan);
        mToolbar.setNavigationOnClickListener(v -> SnackBarUtil.show(NormalActivity.this, "哥，别扫了"));
        mNavigationIcon = mToolbar.getNavigationIcon();
        mAppBar.addOnOffsetChangedListener((AppBarLayout appBarLayout, int verticalOffset) -> {
            int totalScrollRange = appBarLayout.getTotalScrollRange();
            float percent = Math.abs(verticalOffset * 1.0f / totalScrollRange);
            if (mNavigationIcon != null) {
                mNavigationIcon.setColorFilter((int) mArgbEvaluator.evaluate(percent, Color.WHITE, Color.BLACK), PorterDuff.Mode.SRC_IN);
            }
            if (mSearchItemIcon != null) {
                mSearchItemIcon.setColorFilter((int) mArgbEvaluator.evaluate(percent, Color.WHITE, Color.BLACK), PorterDuff.Mode.SRC_IN);
            }
            mTitle.setTextColor((int) mArgbEvaluator.evaluate(percent, Color.WHITE, Color.BLACK));
        });
    }

    private void initSearchView() {

        mSearchView.setVoiceSearch(false);
        mSearchView.setCursorDrawable(R.drawable.shape_cursor);
        mSearchView.setEllipsize(true);
        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SnackBarUtil.show(NormalActivity.this, String.format(Locale.getDefault(), "哥，别搜%s了", query));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    public void onClick(View view) {
        SnackBarUtil.show(this, "哥，别点了");
    }

    public void scrollTop(View view) {
        mScrollView.fullScroll(NestedScrollView.FOCUS_UP);
    }
}
