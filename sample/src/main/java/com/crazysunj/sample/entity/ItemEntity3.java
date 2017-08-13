package com.crazysunj.sample.entity;

import com.crazysunj.sample.R;
import com.crazysunj.sample.base.MutiTypeTitleEntity;
import com.crazysunj.sample.constant.Constants;

/**
 * author: sunjian
 * created on: 2017/8/10 下午5:59
 * description:
 */

public class ItemEntity3 implements MutiTypeTitleEntity {

    public static final int TYPE_3 = 2;
    private long id;
    private ItemEntity3Item left;
    private ItemEntity3Item top;
    private ItemEntity3Item bottom;

    public ItemEntity3() {
        id = System.currentTimeMillis();
        left = new ItemEntity3Item(R.mipmap.ic_boy, "冠心手术康复指导", "日常生活应该如何预防冠心病");
        top = new ItemEntity3Item(R.mipmap.ic_drug, "健康用药", "冠心病药物治疗策略");
        bottom = new ItemEntity3Item(R.mipmap.ic_device, "科学诊断", "这些方法诊断冠心病");
    }

    public ItemEntity3Item getLeft() {
        return left;
    }

    public ItemEntity3Item getTop() {
        return top;
    }

    public ItemEntity3Item getBottom() {
        return bottom;
    }

    @Override
    public int getItemType() {
        return TYPE_3;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return Constants.EMPTY;
    }

    public static class ItemEntity3Item {

        private int img;
        private String title;
        private String conetent;

        public ItemEntity3Item(int img, String title, String conetent) {
            this.img = img;
            this.title = title;
            this.conetent = conetent;
        }

        public int getImg() {
            return img;
        }

        public String getTitle() {
            return title;
        }

        public String getConetent() {
            return conetent;
        }
    }

}
