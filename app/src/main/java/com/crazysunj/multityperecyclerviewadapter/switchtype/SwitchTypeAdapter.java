package com.crazysunj.multityperecyclerviewadapter.switchtype;

import com.crazysunj.multitypeadapter.entity.MultiTypeEntity;
import com.crazysunj.multityperecyclerviewadapter.R;
import com.crazysunj.multityperecyclerviewadapter.helper.BaseHelperAdapter;
import com.crazysunj.multityperecyclerviewadapter.helper.BaseViewHolder;

import java.util.List;

/**
 * @author: sunjian
 * created on: 2017/10/19 上午11:12
 * description:
 */
public class SwitchTypeAdapter extends BaseHelperAdapter<MultiTypeEntity, BaseViewHolder,SwitchTypeAdapterHelper> {



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

    public void reset(List<MultiTypeEntity> data) {
        mHelper.notifyDataByDiff(data);
    }

    public void notifyA() {
        mHelper.notifyDataByDiff(new SwtichType(SwtichType.TYPE_A, "我是typeA"));
    }

    public void notifyB() {
        mHelper.notifyDataByDiff(new SwtichType(SwtichType.TYPE_B, "我是typeB"));
    }

    public void notifyC() {
        mHelper.notifyDataByDiff(new SwtichType(SwtichType.TYPE_C, "我是typeC"));
    }

    public void notifyD() {
        mHelper.notifyDataByDiff(new SwtichType(SwtichType.TYPE_D, "我是typeD"));
    }
}
