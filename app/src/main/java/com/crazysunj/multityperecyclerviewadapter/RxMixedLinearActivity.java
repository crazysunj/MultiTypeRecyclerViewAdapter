package com.crazysunj.multityperecyclerviewadapter;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Toast;

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
import com.crazysunj.multityperecyclerviewadapter.helper.SimpleHelper;
import com.crazysunj.multityperecyclerviewadapter.helper.SimpleRxHelperAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RxMixedLinearActivity extends AppCompatActivity {

    private RxAdapterHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mixed);
        setTitle("Rx混合刷新线性排布");
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        helper = new RxAdapterHelper(initData(), RxAdapterHelper.MODE_MIXED);
        SimpleRxHelperAdapter adapter = new SimpleRxHelperAdapter(helper);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    @NonNull
    private List<MultiHeaderEntity> initData() {
        Random random = new Random();
        List<MultiHeaderEntity> list = new ArrayList<>();
        list.add(new HeaderFirstItem("我是第一种类型的头"));
        list.add(new HeaderSecondItem("我是第二种类型的头"));
        list.add(new HeaderThirdItem("我是第三种类型的头"));
        list.add(new HeaderFourthItem("我是第四种类型的头"));

        int rand1 = random.nextInt(4);
        List<SecondItem> list1 = new ArrayList<>();
        for (int i = 0, size = rand1 + 1; i < size; i++) {
            list1.add(new SecondItem(String.format(Locale.getDefault(), "1-我是第二种类型%d", i)));
        }
        list.addAll(list1);

        int rand2 = random.nextInt(4);
        List<FourthItem> list2 = new ArrayList<>();
        for (int i = 0, size = rand2 + 1; i < size; i++) {
            list2.add(new FourthItem(String.format(Locale.getDefault(), "1-我是第四种类型%d", i)));
        }
        list.addAll(list2);

        int rand3 = random.nextInt(4);
        final List<FirstItem> list3 = new ArrayList<>();
        for (int i = 0, size = rand3 + 1; i < size; i++) {
            list3.add(new FirstItem(String.format(Locale.getDefault(), "1-我是第一种类型%d", i)));
        }
        list.addAll(list3);


        int rand4 = random.nextInt(4);
        List<ThirdItem> list4 = new ArrayList<>();
        for (int i = 0, size = rand4 + 1; i < size; i++) {
            list4.add(new ThirdItem(String.format(Locale.getDefault(), "1-我是第三种类型%d", i)));
        }
        list.addAll(list4);

        int rand5 = random.nextInt(4);
        List<FourthItem> list5 = new ArrayList<>();
        for (int i = 0, size = rand5 + 1; i < size; i++) {
            list5.add(new FourthItem(String.format(Locale.getDefault(), "2-我是第四种类型%d", i)));
        }
        list.addAll(list5);

        int rand6 = random.nextInt(4);
        final List<FirstItem> list6 = new ArrayList<>();
        for (int i = 0, size = rand6 + 1; i < size; i++) {
            list6.add(new FirstItem(String.format(Locale.getDefault(), "2-我是第一种类型%d", i)));
        }
        list.addAll(list6);

        int rand7 = random.nextInt(4);
        List<ThirdItem> list7 = new ArrayList<>();
        for (int i = 0, size = rand7 + 1; i < size; i++) {
            list7.add(new ThirdItem(String.format(Locale.getDefault(), "2-我是第三种类型%d", i)));
        }
        list.addAll(list7);

        int rand8 = random.nextInt(4);
        List<SecondItem> list8 = new ArrayList<>();
        for (int i = 0, size = rand8 + 1; i < size; i++) {
            list8.add(new SecondItem(String.format(Locale.getDefault(), "2-我是第二种类型%d", i)));
        }
        list.addAll(list8);
        return list;
    }

    public void click1(View view) {
        helper.notifyDataSetChanged(initData(), SimpleHelper.MODE_MIXED);
    }

    public void click2(View view) {
        helper.notifyDataByDiff(initData(), SimpleHelper.MODE_MIXED);
    }


    public void click3(View view) {
        helper.switchMode(SimpleHelper.MODE_STANDARD);
        helper.notifyDataSetChanged();
    }

    public void click4(View view) {
        helper.notifyDataByDiff(initData(), SimpleHelper.MODE_STANDARD);
    }

    public void click5(View view) {
        Random random = new Random();
        int position = random.nextInt(helper.getData().size());
        MultiHeaderEntity addItem = getAddItem();
        helper.addData(position, addItem);
        Toast.makeText(this, "在" + position + "位置添加了数据,Type为" + addItem.getItemType(), Toast.LENGTH_SHORT).show();
    }

    public void click6(View view) {
        MultiHeaderEntity addItem = getAddItem();
        helper.addData(addItem);
        Toast.makeText(this, "添加的数据Type为" + addItem.getItemType(), Toast.LENGTH_SHORT).show();
    }

    private MultiHeaderEntity getAddItem() {
        Random random = new Random();
        int itemType = random.nextInt(4);
        String date = (String) DateFormat.format("HH:mm:ss", System.currentTimeMillis());
        if (itemType == SimpleHelper.TYPE_ONE) {
            return new FirstItem("我的天，类型1被增加了 " + date);
        } else if (itemType == SimpleHelper.TYPE_THREE) {
            return new SecondItem("我的天，类型2被增加了 " + date);
        } else if (itemType == SimpleHelper.TYPE_FOUR) {
            return new ThirdItem("我的天，类型3被增加了 " + date);
        }
        return new FourthItem("我的天，类型4被增加了 " + date);
    }

    private MultiHeaderEntity getChangeItem() {

        Random random = new Random();
        int itemType = random.nextInt(4);

        String date = (String) DateFormat.format("HH:mm:ss", System.currentTimeMillis());
        if (itemType == SimpleHelper.TYPE_ONE) {
            return new FirstItem("我的天，类型1被修改了 " + date);
        } else if (itemType == SimpleHelper.TYPE_THREE) {
            return new SecondItem("我的天，类型2被修改了 " + date);
        } else if (itemType == SimpleHelper.TYPE_FOUR) {
            return new ThirdItem("我的天，类型3被修改了 " + date);
        }
        return new FourthItem("我的天，类型4被修改了 " + date);
    }

    public void click7(View view) {
        int position = new Random().nextInt(helper.getData().size());
        MultiHeaderEntity removeData = helper.removeData(position);
        Toast.makeText(this, "移除了position为" + position + " Type为" + removeData.getItemType(), Toast.LENGTH_SHORT).show();
    }

    public void click8(View view) {
        int position = new Random().nextInt(helper.getData().size());
        MultiHeaderEntity changeItem = getChangeItem();
        helper.setData(position, changeItem);
        Toast.makeText(this, "修改了position为" + position + " Type为" + changeItem.getItemType(), Toast.LENGTH_SHORT).show();
    }

    public void click9(View view) {

        int position = new Random().nextInt(helper.getData().size());
        helper.addData(position, initData());
        Toast.makeText(this, "在" + position + "位置插入数据集合", Toast.LENGTH_SHORT).show();
    }

    public void click10(View view) {
        helper.addData(initData());
    }
}
