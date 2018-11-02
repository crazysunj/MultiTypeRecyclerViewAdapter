package com.crazysunj.multityperecyclerviewadapter;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.crazysunj.multitypeadapter.entity.LevelData;
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

public class RxStandardLinearActivity extends AppCompatActivity {

    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private RxAdapterHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard);
        setTitle("Rx标准刷新线性排布");
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        textView1 = (TextView) findViewById(R.id.text1);
        textView2 = (TextView) findViewById(R.id.text2);
        textView3 = (TextView) findViewById(R.id.text3);
        textView4 = (TextView) findViewById(R.id.text4);


        List<MultiHeaderEntity> list = initData();

        helper = new RxAdapterHelper(list);
        SimpleRxHelperAdapter adapter = new SimpleRxHelperAdapter(helper);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.addItemDecoration(new StickyHeaderDecoration(adapter));
        recyclerView.setAdapter(adapter);

    }

    private void notifyLevel(int level, Random random) {
        List<MultiHeaderEntity> list = new ArrayList<>();
        MultiHeaderEntity header;
        if (level == SimpleHelper.LEVEL_FIRST) {
            header = new HeaderFirstItem("我是第一种类型的头--新");
            int rand = random.nextInt(4);
            for (int i = 0, size = rand + 1; i < size; i++) {
                list.add(new FirstItem(String.format(Locale.getDefault(), "我是第一种类型%d--新", i)));
            }
            textView1.setText(String.format(Locale.getDefault(), "类型1的数量：%d", list.size()));
        } else if (level == SimpleHelper.LEVEL_SENCOND) {
            header = new HeaderSecondItem("我是第二种类型的头--新");
            int rand = random.nextInt(4);
            for (int i = 0, size = rand + 1; i < size; i++) {
                list.add(new SecondItem(String.format(Locale.getDefault(), "我是第二种类型%d--新", i)));
            }
            textView2.setText(String.format(Locale.getDefault(), "类型2的数量：%d", list.size()));
        } else if (level == SimpleHelper.LEVEL_THIRD) {
            header = new HeaderThirdItem("我是第三种类型的头--新");
            int rand = random.nextInt(4);
            for (int i = 0, size = rand + 1; i < size; i++) {
                list.add(new ThirdItem(String.format(Locale.getDefault(), "我是第三种类型%d--新", i)));
            }
            textView3.setText(String.format(Locale.getDefault(), "类型3的数量：%d", list.size()));
        } else {
            header = new HeaderFourthItem("我是第四种类型的头--新");
            int rand = random.nextInt(4);
            for (int i = 0, size = rand + 1; i < size; i++) {
                list.add(new FourthItem(String.format(Locale.getDefault(), "我是第四种类型%d--新", i)));
            }
            textView4.setText(String.format(Locale.getDefault(), "类型4的数量：%d", list.size()));
        }
        helper.notifyModuleDataAndHeaderChanged(list, header, level);
    }

    @NonNull
    private List<MultiHeaderEntity> initData() {
        Random random = new Random();
        List<MultiHeaderEntity> list = new ArrayList<>();
        int firstCount = 0;
        int secondCount = 0;
        int thirdCount = 0;
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
        thirdCount += list4.size();
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
        thirdCount += list7.size();
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
        textView3.setText(String.format(Locale.getDefault(), "类型3的数量：%d", thirdCount));
        textView4.setText(String.format(Locale.getDefault(), "类型4的数量：%d", fourthCount));
        return list;
    }

    public void click1(View view) {

        helper.notifyLoadingChanged();
        view.postDelayed(() -> helper.notifyDataSetChanged(initData()), 3000);

    }

    public void click2(View view) {
        helper.notifyLoadingChanged();
        view.postDelayed(() -> helper.notifyDataByDiff(initData()), 8000);

    }


    public void click3(View view) {

        try {
            Random random = new Random();
            List<MultiHeaderEntity> data = helper.getData();
            if (data == null || data.isEmpty()) {
                Toast.makeText(this, "data为空", Toast.LENGTH_SHORT).show();
                return;
            }
            int position = random.nextInt(data.size());
            MultiHeaderEntity removeData = helper.removeData(position);
            int itemType = removeData.getItemType();
            TextView textView;
            int level = helper.getLevel(itemType);
            if (itemType < 0) {
                Log.d("RxStandardLinearActivit", "itemType:" + itemType);
                textView = getOtherTextView(level);
            } else {
                textView = getDataTextView(itemType);
            }
            String text = "移除了第" + (position + 1) + "个数据,Type为" + itemType + " level为" + level;
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
            Log.d("RxStandardLinearActivit", text);
            List<MultiHeaderEntity> list = helper.getDataWithType(itemType).getData();
            int size = list == null ? 0 : list.size();
            textView.setText(String.format(Locale.getDefault(), "类型%d的数量：%d", level + 1, size));
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("RxStandardLinearActivit", e.getMessage());
        }

    }

    private TextView getDataTextView(int itemType) {
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

    private TextView getOtherTextView(int level) {
        if (level == SimpleHelper.LEVEL_FIRST) {
            return textView1;
        } else if (level == SimpleHelper.LEVEL_SENCOND) {
            return textView2;
        } else if (level == SimpleHelper.LEVEL_THIRD) {
            return textView3;
        } else if (level == SimpleHelper.LEVEL_FOURTH) {
            return textView4;
        }
        return null;
    }

    /**
     * 修改数据不可为空
     *
     * @param view
     */
    public void click4(View view) {
        try {
            Random random = new Random();
            List<MultiHeaderEntity> data = helper.getData();
            if (data == null || data.isEmpty()) {
                Toast.makeText(this, "data为空", Toast.LENGTH_SHORT).show();
                return;
            }
            int position = random.nextInt(data.size());
            int itemType = data.get(position).getItemType();
            int level = helper.getLevel(itemType);
            helper.setData(position, itemType < 0 ? getOtherChangeItem(level) : getDataChangeItem(itemType));
            String text = "修改了第" + (position + 1) + "个数据,Type为" + itemType + " level为" + level;
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
            Log.d("RxStandardLinearActivit", text);
        } catch (Exception e) {
            Log.d("RxStandardLinearActivit", e.getMessage());
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private MultiHeaderEntity getDataChangeItem(int itemType) {

        String date = (String) DateFormat.format("HH:mm:ss", System.currentTimeMillis());
        if (itemType == SimpleHelper.TYPE_ONE) {
            return new FirstItem("我的天，类型1被修改了 " + date);
        } else if (itemType == SimpleHelper.TYPE_THREE) {
            return new SecondItem("我的天，类型2被修改了 " + date);
        } else if (itemType == SimpleHelper.TYPE_FOUR) {
            return new ThirdItem("我的天，类型3被修改了 " + date);
        } else if (itemType == SimpleHelper.TYPE_TWO) {
            return new FourthItem("我的天，类型4被修改了 " + date);
        }
        throw new RuntimeException("返回为空");
    }

    public void click11(View view) {
        Random random = new Random();
        final int level = random.nextInt(4);
        LevelData<MultiHeaderEntity> levelData = helper.getDataWithLevel(level);
        List<MultiHeaderEntity> data = levelData.getData();
        if (data == null || data.isEmpty()) {
            Toast.makeText(this, "当前level=" + level + "数量不够，没法移除", Toast.LENGTH_SHORT).show();
            return;
        }
        if (levelData.getHeader() == null && data.size() <= 1) {
            Toast.makeText(this, "当前level=" + level + "数量不够，没法移除", Toast.LENGTH_SHORT).show();
            return;
        }
        int positionStart = helper.getLevelPositionStart(level);
        helper.removeData(positionStart, 2);
        Toast.makeText(this, "当前移除的是level=" + level, Toast.LENGTH_SHORT).show();
    }

    private MultiHeaderEntity getOtherChangeItem(int level) {

        String date = (String) DateFormat.format("HH:mm:ss", System.currentTimeMillis());
        if (level == SimpleHelper.LEVEL_FIRST) {
            return new HeaderFirstItem("我的天，类型1的头被修改了 " + date);
        } else if (level == SimpleHelper.LEVEL_SENCOND) {
            return new HeaderSecondItem("我的天，类型2的头被修改了 " + date);
        } else if (level == SimpleHelper.LEVEL_THIRD) {
            return new HeaderThirdItem("我的天，类型3的头被修改了 " + date);
        } else if (level == SimpleHelper.LEVEL_FOURTH) {
            return new HeaderFourthItem("我的天，类型4的头被修改了 " + date);
        }
        throw new RuntimeException("返回为空");
    }

    public void click5(View view) {

        helper.clearModule(SimpleHelper.LEVEL_FIRST);
    }

    public void click6(View view) {

        helper.clearModule(SimpleHelper.LEVEL_SENCOND);
    }

    public void click7(View view) {
        helper.clearModule(SimpleHelper.LEVEL_THIRD);

    }

    public void click8(View view) {
        helper.clearModule(SimpleHelper.LEVEL_FOURTH);
    }

    public void click9(View view) {
        helper.clear();
    }

    public void click10(View view) {
        try {
            Random random = new Random();
            final int level = random.nextInt(4);
            helper.notifyLoadingChanged(level);
            textView1.postDelayed(() -> notifyLevel(level, random), 1000);
        } catch (Exception e) {
            Log.d("RxStandardLinearActivit", e.getMessage());
        }
    }
}
