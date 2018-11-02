package com.crazysunj.multityperecyclerviewadapter;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.crazysunj.multitypeadapter.helper.RecyclerViewAdapterHelper;
import com.crazysunj.multitypeadapter.sticky.StickyHeaderDecoration;
import com.crazysunj.multityperecyclerviewadapter.apt.RxAptHelperAdapter;
import com.crazysunj.multityperecyclerviewadapter.apt.RxAptHelperAdapterHelper;
import com.crazysunj.multityperecyclerviewadapter.data.FirstItem;
import com.crazysunj.multityperecyclerviewadapter.data.FourthItem;
import com.crazysunj.multityperecyclerviewadapter.data.SecondItem;
import com.crazysunj.multityperecyclerviewadapter.data.ThirdItem;
import com.crazysunj.multityperecyclerviewadapter.header.HeaderFirstItem;
import com.crazysunj.multityperecyclerviewadapter.header.HeaderFourthItem;
import com.crazysunj.multityperecyclerviewadapter.header.HeaderSecondItem;
import com.crazysunj.multityperecyclerviewadapter.header.HeaderThirdItem;
import com.crazysunj.multityperecyclerviewadapter.helper.MultiHeaderEntity;
import com.crazysunj.multityperecyclerviewadapter.helper.SimpleHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class RxAptStandardLinearActivity extends AppCompatActivity {

    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private RxAptHelperAdapterHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard_apt);
        setTitle("RxApt标准刷新线性排布");
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        textView1 = (TextView) findViewById(R.id.text1);
        textView2 = (TextView) findViewById(R.id.text2);
        textView3 = (TextView) findViewById(R.id.text3);
        textView4 = (TextView) findViewById(R.id.text4);


        List<MultiHeaderEntity> list = initData();

        helper = new RxAptHelperAdapterHelper(list);
        RxAptHelperAdapter adapter = new RxAptHelperAdapter(helper);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new StickyHeaderDecoration(adapter));
        recyclerView.setAdapter(adapter);

    }

    @NonNull
    private List<MultiHeaderEntity> initData() {
        Random random = new Random();
        List<MultiHeaderEntity> list = new ArrayList<>();
        int firstCount = 0;
        int secondCount = 0;
        int thidrdCount = 0;
        int fourthCount = 0;
        list.add(new HeaderFirstItem("我是第一种类型的头"));
        list.add(new HeaderSecondItem("我是第二种类型的头"));
        list.add(new HeaderThirdItem("我是第三种类型的头"));
        list.add(new HeaderFourthItem("我是第四种类型的头"));

        int rand1 = random.nextInt(4);
        List<SecondItem> list1 = new ArrayList<>();
        for (int i = 0, size = rand1 + 1; i < size; i++) {
            list1.add(new SecondItem(String.format(Locale.getDefault(), "1-我是第二种类型%d", i)));
        }
        secondCount += list1.size();
        list.addAll(list1);

        int rand2 = random.nextInt(4);
        List<FourthItem> list2 = new ArrayList<>();
        for (int i = 0, size = rand2 + 1; i < size; i++) {
            list2.add(new FourthItem(String.format(Locale.getDefault(), "1-我是第四种类型%d", i)));
        }
        fourthCount += list2.size();
        list.addAll(list2);

        int rand3 = random.nextInt(4);
        final List<FirstItem> list3 = new ArrayList<>();
        for (int i = 0, size = rand3 + 1; i < size; i++) {
            list3.add(new FirstItem(String.format(Locale.getDefault(), "1-我是第一种类型%d", i)));
        }
        firstCount += list3.size();
        list.addAll(list3);


        int rand4 = random.nextInt(4);
        List<ThirdItem> list4 = new ArrayList<>();
        for (int i = 0, size = rand4 + 1; i < size; i++) {
            list4.add(new ThirdItem(String.format(Locale.getDefault(), "1-我是第三种类型%d", i)));
        }
        thidrdCount += list4.size();
        list.addAll(list4);

        int rand5 = random.nextInt(4);
        List<FourthItem> list5 = new ArrayList<>();
        for (int i = 0, size = rand5 + 1; i < size; i++) {
            list5.add(new FourthItem(String.format(Locale.getDefault(), "2-我是第四种类型%d", i)));
        }
        fourthCount += list5.size();
        list.addAll(list5);

        int rand6 = random.nextInt(4);
        final List<FirstItem> list6 = new ArrayList<>();
        for (int i = 0, size = rand6 + 1; i < size; i++) {
            list6.add(new FirstItem(String.format(Locale.getDefault(), "2-我是第一种类型%d", i)));
        }
        firstCount += list6.size();
        list.addAll(list6);

        int rand7 = random.nextInt(4);
        List<ThirdItem> list7 = new ArrayList<>();
        for (int i = 0, size = rand7 + 1; i < size; i++) {
            list7.add(new ThirdItem(String.format(Locale.getDefault(), "2-我是第三种类型%d", i)));
        }
        thidrdCount += list7.size();
        list.addAll(list7);

        int rand8 = random.nextInt(4);
        List<SecondItem> list8 = new ArrayList<>();
        for (int i = 0, size = rand8 + 1; i < size; i++) {
            list8.add(new SecondItem(String.format(Locale.getDefault(), "2-我是第二种类型%d", i)));
        }
        secondCount += list8.size();
        list.addAll(list8);

        textView1.setText(String.format(Locale.getDefault(), "类型1的数量：%d", firstCount));
        textView2.setText(String.format(Locale.getDefault(), "类型2的数量：%d", secondCount));
        textView3.setText(String.format(Locale.getDefault(), "类型3的数量：%d", thidrdCount));
        textView4.setText(String.format(Locale.getDefault(), "类型4的数量：%d", fourthCount));
        return list;
    }

    public void click1(View view) {

        helper.notifyDataSetChanged(initData());
    }

    public void click2(View view) {
        helper.notifyDataByDiff(initData());

    }


    public void click3(View view) {

        try {
            Random random = new Random();
            int position = random.nextInt(helper.getData().size());
            MultiHeaderEntity removeData = helper.removeData(position);
            int itemType = removeData.getItemType();
            if (itemType >= -1000 && itemType < 0) {
                itemType += RecyclerViewAdapterHelper.HEADER_TYPE_DIFFER;
            }
            Toast.makeText(this, "移除了第" + (position + 1) + "个数据,Type为" + itemType, Toast.LENGTH_SHORT).show();
            getTextView(itemType)
                    .setText(String.format(Locale.getDefault(), "类型%d的数量：%d", itemType + 1, helper.getDataWithLevel(itemType).getData().size()));
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private TextView getTextView(int itemType) {
        if (itemType == SimpleHelper.TYPE_ONE) {
            return textView1;
        } else if (itemType == SimpleHelper.TYPE_THREE) {
            return textView2;
        } else if (itemType == SimpleHelper.TYPE_FOUR) {
            return textView3;
        } else if (itemType == SimpleHelper.TYPE_TWO) {
            return textView4;
        }
        return null;
    }

    public void click4(View view) {
        try {
            Random random = new Random();
            int position = random.nextInt(helper.getData().size());
            int itemType = helper.getData().get(position).getItemType();
            helper.setData(position, getChangeItem(itemType));
            Toast.makeText(this, "修改了第" + (position + 1) + "个数据,Type为" + itemType, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private MultiHeaderEntity getChangeItem(int itemType) {

        String date = (String) DateFormat.format("HH:mm:ss", System.currentTimeMillis());
        if (itemType == SimpleHelper.TYPE_ONE) {
            return new FirstItem("我的天，类型1被修改了 " + date);
        } else if (itemType == SimpleHelper.TYPE_THREE) {
            return new SecondItem("我的天，类型2被修改了 " + date);
        } else if (itemType == SimpleHelper.TYPE_FOUR) {
            return new ThirdItem("我的天，类型3被修改了 " + date);
        } else if (itemType == SimpleHelper.TYPE_TWO) {
            return new FourthItem("我的天，类型4被修改了 " + date);
        } else if (itemType == SimpleHelper.TYPE_ONE - RecyclerViewAdapterHelper.HEADER_TYPE_DIFFER) {
            return new HeaderFirstItem("我的天，类型1的头被修改了 " + date);
        } else if (itemType == SimpleHelper.TYPE_THREE - RecyclerViewAdapterHelper.HEADER_TYPE_DIFFER) {
            return new HeaderSecondItem("我的天，类型2的头被修改了 " + date);
        } else if (itemType == SimpleHelper.TYPE_FOUR - RecyclerViewAdapterHelper.HEADER_TYPE_DIFFER) {
            return new HeaderThirdItem("我的天，类型3的头被修改了 " + date);
        } else if (itemType == SimpleHelper.TYPE_TWO - RecyclerViewAdapterHelper.HEADER_TYPE_DIFFER) {
            return new HeaderFourthItem("我的天，类型4的头被修改了 " + date);
        }
        return null;
    }

    public void click5(View view) {
        helper.clearModule(0);
    }

    public void click6(View view) {
        helper.clearModule(1);
    }

    public void click7(View view) {
        helper.clearModule(2);

    }

    public void click8(View view) {
        helper.clearModule(3);
    }

    public void click9(View view) {
        helper.clear();
    }
}
