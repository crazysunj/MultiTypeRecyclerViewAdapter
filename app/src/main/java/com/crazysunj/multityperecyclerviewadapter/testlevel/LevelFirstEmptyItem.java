package com.crazysunj.multityperecyclerviewadapter.testlevel;

import static com.crazysunj.multityperecyclerviewadapter.testlevel.TestLevelAdapter.TYPE_LEVEL_FIRST_EMPTY;

/**
 * @author: sunjian
 * created on: 2019/1/7 上午10:21
 * description:
 */
public class LevelFirstEmptyItem implements MultiTypeTitleEntity {

    private String msg;

    public LevelFirstEmptyItem(String msg) {
        this.msg = msg;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public int getItemType() {
        return TYPE_LEVEL_FIRST_EMPTY;
    }

    @Override
    public long getId() {
        return 0;
    }
}
