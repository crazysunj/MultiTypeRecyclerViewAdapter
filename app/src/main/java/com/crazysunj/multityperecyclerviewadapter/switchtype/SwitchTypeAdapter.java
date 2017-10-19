package com.crazysunj.multityperecyclerviewadapter.switchtype;

import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.crazysunj.multitypeadapter.entity.MultiTypeEntity;
import com.crazysunj.multityperecyclerviewadapter.R;

import java.util.List;

/**
 * @author: sunjian
 * created on: 2017/10/19 上午11:12
 * description:
 */

public class SwitchTypeAdapter extends BaseQuickAdapter<MultiTypeEntity, BaseViewHolder> {


    private SwitchTypeAdapterHelper mHelper;

    public SwitchTypeAdapter() {
        this(new SwitchTypeAdapterHelper());
    }

    public SwitchTypeAdapter(SwitchTypeAdapterHelper helper) {
        super(helper.getData());
        mHelper = helper;
        mHelper.bindAdapter(this);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiTypeEntity item) {
        switch (item.getItemType()) {
            case FirstType.TYPE_1:
                helper.setText(R.id.item_name, ((FirstType) item).getTitle());
                break;
            case SecondType.TYPE_2:
                helper.setText(R.id.item_name, ((SecondType) item).getTitle());
                break;
            case ThirdType.TYPE_3:
                helper.setText(R.id.item_name, ((ThirdType) item).getTitle());
                break;
            case FourthType.TYPE_4:
                helper.setText(R.id.item_name, ((FourthType) item).getTitle());
                break;
            case SwtichType.TYPE_A:
            case SwtichType.TYPE_B:
            case SwtichType.TYPE_C:
            case SwtichType.TYPE_D:
                helper.setText(R.id.item_switch_type_title, ((SwtichType) item).getTitle());
                break;
            default:
                helper.setText(R.id.item_switch_type_title, ((SwtichType) item).getTitle());
                break;
        }
    }


    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        return createBaseViewHolder(parent, mHelper.getLayoutId(viewType));
    }

    @Override
    protected int getDefItemViewType(int position) {
        return mHelper.getItemViewType(position);
    }

    public SwitchTypeAdapterHelper getHelper() {
        return mHelper;
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
