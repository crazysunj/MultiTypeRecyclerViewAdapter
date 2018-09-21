package com.crazysunj.multityperecyclerviewadapter.switchtype;

import com.crazysunj.multitypeadapter.entity.MultiTypeEntity;
import com.crazysunj.multitypeadapter.helper.AsynAdapterHelper;
import com.crazysunj.multityperecyclerviewadapter.R;

/**
 * @author: sunjian
 * created on: 2017/10/19 上午11:11
 * description:
 */

public class SwitchTypeAdapterHelper extends AsynAdapterHelper<MultiTypeEntity> {

    public static final int LEVEL_FIRST = 0;
    public static final int LEVEL_SECOND = 1;
    public static final int LEVEL_THIRD = 2;
    public static final int LEVEL_FOURTH = 3;
    public static final int LEVEL_SWITCH = 4;

    public SwitchTypeAdapterHelper() {
        super(null);
    }

    @Override
    protected void registerModule() {
        registerModule(LEVEL_FIRST)
                .type(FirstType.TYPE_1)
                .layoutResId(R.layout.item_first)
                .register();

        registerModule(LEVEL_SECOND)
                .type(SecondType.TYPE_2)
                .layoutResId(R.layout.item_second)
                .register();

        registerModule(LEVEL_THIRD)
                .type(ThirdType.TYPE_3)
                .layoutResId(R.layout.item_third)
                .register();

        registerModule(LEVEL_FOURTH)
                .type(FourthType.TYPE_4)
                .layoutResId(R.layout.item_fourth)
                .register();

        registerModule(LEVEL_SWITCH)
                .type(SwtichType.TYPE_A)
                .layoutResId(R.layout.item_switch_type)
                .type(SwtichType.TYPE_B)
                .layoutResId(R.layout.item_switch_type)
                .type(SwtichType.TYPE_C)
                .layoutResId(R.layout.item_switch_type)
                .type(SwtichType.TYPE_D)
                .layoutResId(R.layout.item_switch_type)
                .register();
    }
}
