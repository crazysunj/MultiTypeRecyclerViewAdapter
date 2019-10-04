package com.crazysunj.multityperecyclerviewadapter.switchtype;

import android.util.SparseArray;

import com.crazysunj.multitypeadapter.entity.LevelData;
import com.crazysunj.multitypeadapter.entity.MultiTypeEntity;
import com.crazysunj.multityperecyclerviewadapter.R;
import com.crazysunj.multityperecyclerviewadapter.helper.BaseHelperAdapter;
import com.crazysunj.multityperecyclerviewadapter.helper.BaseViewHolder;

import java.util.Collections;

/**
 * @author: sunjian
 * created on: 2017/10/19 上午11:12
 * description:
 */
public class SwitchTypeAdapter extends BaseHelperAdapter<MultiTypeEntity, BaseViewHolder, SwitchTypeAdapterHelper> {


    public SwitchTypeAdapter() {
        this(new SwitchTypeAdapterHelper());
    }

    @Override
    protected void convert(BaseViewHolder holder, MultiTypeEntity item) {
        switch (item.getItemType()) {
            case FirstType.TYPE_1:
                holder.setText(R.id.item_name, ((FirstType) item).getTitle());
                break;
            case SecondType.TYPE_2:
                holder.setText(R.id.item_name, ((SecondType) item).getTitle());
                break;
            case ThirdType.TYPE_3:
                holder.setText(R.id.item_name, ((ThirdType) item).getTitle());
                break;
            case FourthType.TYPE_4:
                holder.setText(R.id.item_name, ((FourthType) item).getTitle());
                break;
            case SwtichType.TYPE_A:
            case SwtichType.TYPE_B:
            case SwtichType.TYPE_C:
            case SwtichType.TYPE_D:
                holder.setText(R.id.item_switch_type_title, ((SwtichType) item).getTitle());
                break;
            default:
                holder.setText(R.id.item_switch_type_title, ((SwtichType) item).getTitle());
                break;
        }
    }

    public SwitchTypeAdapter(SwitchTypeAdapterHelper helper) {
        super(helper);
    }

    public void reset(SparseArray<LevelData<MultiTypeEntity>> data) {
        mHelper.notifyDataSetChanged(data);
    }

    public void notifyA() {
        SparseArray<LevelData<MultiTypeEntity>> levelData = new SparseArray<>();
        levelData.put(SwitchTypeAdapterHelper.LEVEL_SWITCH,
                new LevelData<>(Collections.singletonList(new SwtichType(SwtichType.TYPE_A, "我是typeA")), null, null));
        mHelper.notifyDataSetChanged(levelData);
    }

    public void notifyB() {
        SparseArray<LevelData<MultiTypeEntity>> levelData = new SparseArray<>();
        levelData.put(SwitchTypeAdapterHelper.LEVEL_SWITCH,
                new LevelData<>(Collections.singletonList(new SwtichType(SwtichType.TYPE_B, "我是typeB")), null, null));
        mHelper.notifyDataSetChanged(levelData);
    }

    public void notifyC() {
        SparseArray<LevelData<MultiTypeEntity>> levelData = new SparseArray<>();
        levelData.put(SwitchTypeAdapterHelper.LEVEL_SWITCH,
                new LevelData<>(Collections.singletonList(new SwtichType(SwtichType.TYPE_C, "我是typeC")), null, null));
        mHelper.notifyDataSetChanged(levelData);
    }

    public void notifyD() {
        SparseArray<LevelData<MultiTypeEntity>> levelData = new SparseArray<>();
        levelData.put(SwitchTypeAdapterHelper.LEVEL_SWITCH,
                new LevelData<>(Collections.singletonList(new SwtichType(SwtichType.TYPE_D, "我是typeD")), null, null));
        mHelper.notifyDataSetChanged(levelData);
    }
}
