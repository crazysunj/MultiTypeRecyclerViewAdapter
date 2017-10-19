package com.crazysunj.multityperecyclerviewadapter.switchtype;

import com.crazysunj.multitypeadapter.entity.MultiTypeEntity;
import com.crazysunj.multitypeadapter.helper.AsynAdapterHelper;
import com.crazysunj.multityperecyclerviewadapter.R;

/**
 * @author: sunjian
 * created on: 2017/10/19 上午11:11
 * description:
 */

public class SwitchTypeAdapterHelper extends AsynAdapterHelper<MultiTypeEntity, SwitchTypeAdapter> {

    public SwitchTypeAdapterHelper() {
        super(null);
    }

    @Override
    protected void registerMoudle() {
        registerMoudle(FirstType.TYPE_1)
                .level(0)
                .layoutResId(R.layout.item_first)
                .register();

        registerMoudle(SecondType.TYPE_2)
                .level(1)
                .layoutResId(R.layout.item_second)
                .register();

        registerMoudle(ThirdType.TYPE_3)
                .level(2)
                .layoutResId(R.layout.item_third)
                .register();


        registerMoudle(FourthType.TYPE_4)
                .level(3)
                .layoutResId(R.layout.item_fourth)
                .register();


        registerMoudle(SwtichType.TYPE_A)
                .level(4)
                .layoutResId(R.layout.item_switch_type)
                .register();

        registerMoudle(SwtichType.TYPE_B)
                .level(5)
                .layoutResId(R.layout.item_switch_type)
                .register();


        registerMoudle(SwtichType.TYPE_C)
                .level(6)
                .layoutResId(R.layout.item_switch_type)
                .register();

        registerMoudle(SwtichType.TYPE_D)
                .level(7)
                .layoutResId(R.layout.item_switch_type)
                .register();
    }
}
