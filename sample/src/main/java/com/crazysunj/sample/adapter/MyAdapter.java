package com.crazysunj.sample.adapter;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.coorchice.library.SuperTextView;
import com.crazysunj.multitypeadapter.adapter.LoadingEntityAdapter;
import com.crazysunj.multitypeadapter.helper.LoadingConfig;
import com.crazysunj.multitypeadapter.helper.RecyclerViewAdapterHelper;
import com.crazysunj.multitypeadapter.util.IDUtil;
import com.crazysunj.sample.R;
import com.crazysunj.sample.adapter.helper.MyAdapterHelper;
import com.crazysunj.sample.base.BaseAdapter;
import com.crazysunj.sample.base.MutiTypeTitleEntity;
import com.crazysunj.sample.constant.Constants;
import com.crazysunj.sample.entity.CommonFooterEntity;
import com.crazysunj.sample.entity.CommonHeadEntity;
import com.crazysunj.sample.entity.ItemEntity1;
import com.crazysunj.sample.entity.ItemEntity2;
import com.crazysunj.sample.entity.ItemEntity3;
import com.crazysunj.sample.entity.ItemEntity4;
import com.crazysunj.sample.entity.LoadingEntity;

import java.util.List;

/**
 * author: sunjian
 * created on: 2017/8/3 下午5:39
 * description:
 */

public class MyAdapter extends BaseAdapter<MutiTypeTitleEntity, BaseViewHolder, MyAdapterHelper> {


    private CommonHeadEntity entity1Header = new CommonHeadEntity(ItemEntity1.HEADER_TITLE, ItemEntity1.TYPE_1);
    private CommonHeadEntity entity2Header = new CommonHeadEntity(ItemEntity2.HEADER_TITLE, ItemEntity2.TYPE_2);
    private CommonHeadEntity entity4Header = new CommonHeadEntity(ItemEntity4.HEADER_TITLE, ItemEntity4.TYPE_4);
    private CommonFooterEntity entity1Footer = new CommonFooterEntity(Constants.EXPAND, ItemEntity1.TYPE_1);

    public MyAdapter() {
        super(new MyAdapterHelper());
        mHelper.setLoadingAdapter(new LoadingEntityAdapter<MutiTypeTitleEntity>() {
            @Override
            public MutiTypeTitleEntity createLoadingEntity(int type, int level) {
                return new LoadingEntity(type, IDUtil.getId());
            }

            @Override
            public MutiTypeTitleEntity createLoadingHeaderEntity(int type, int level) {
                return new LoadingEntity(type, IDUtil.getId());
            }

            @Override
            public void bindLoadingEntity(MutiTypeTitleEntity loadingEntity, int position) {

            }
        });

        mHelper.initGlobalLoadingConfig(new LoadingConfig.Builder()
                .setLoading(MyAdapterHelper.LEVEL_1, 2, true)
                .setLoading(MyAdapterHelper.LEVEL_2, 1, true)
                .setLoading(MyAdapterHelper.LEVEL_3, 1)
                .setLoading(MyAdapterHelper.LEVEL_4, 1, true)
                .build());
    }

    @Override
    protected void convert(BaseViewHolder helper, MutiTypeTitleEntity item) {
        int itemType = item.getItemType();
        switch (itemType) {
            case ItemEntity1.TYPE_1:
                renderEntity1(helper, (ItemEntity1) item);
                break;
            case ItemEntity2.TYPE_2:
                renderEntity2(helper, (ItemEntity2) item);
                break;
            case ItemEntity3.TYPE_3:
                renderEntity3(helper, (ItemEntity3) item);
                break;
            case ItemEntity4.TYPE_4:
                renderEntity4(helper, (ItemEntity4) item);
                break;
            case MyAdapterHelper.LEVEL_1 - RecyclerViewAdapterHelper.HEADER_TYPE_DIFFER:
            case MyAdapterHelper.LEVEL_2 - RecyclerViewAdapterHelper.HEADER_TYPE_DIFFER:
            case MyAdapterHelper.LEVEL_3 - RecyclerViewAdapterHelper.HEADER_TYPE_DIFFER:
                renderHeader(helper, (CommonHeadEntity) item);
                break;
            case MyAdapterHelper.LEVEL_1 - RecyclerViewAdapterHelper.FOOTER_TYPE_DIFFER:
                renderFooter(helper, (CommonFooterEntity) item);
                break;
            default:
                break;
        }
    }

    private void renderEntity1(BaseViewHolder helper, ItemEntity1 item) {
        helper.setImageResource(R.id.item_1_img, item.getImg());
        helper.setText(R.id.item_1_title, item.getTitle());
        helper.setText(R.id.item_1_content, item.getContent());
        helper.setText(R.id.item_1_time, item.getTime());
        helper.setText(R.id.item_1_time_flag, item.getTimeFlag());
    }

    private void renderEntity2(BaseViewHolder helper, ItemEntity2 item) {
        helper.setImageResource(R.id.item_2_img, item.getImg());
        helper.setText(R.id.item_2_title, item.getTitle());
        helper.setText(R.id.item_2_content, item.getContent());
        helper.setText(R.id.item_2_price, item.getPrice());
        LinearLayout tagParent = helper.getView(R.id.item_2_tags);
        tagParent.removeAllViews();
        List<String> tags = item.getTags();
        for (String tag : tags) {
            SuperTextView view = (SuperTextView) mLayoutInflater.inflate(R.layout.label_tag, tagParent, false);
            view.setText(tag);
            tagParent.addView(view);
        }
    }

    private void renderEntity3(BaseViewHolder helper, ItemEntity3 item) {
        ItemEntity3.ItemEntity3Item left = item.getLeft();
        helper.setImageResource(R.id.item_3_left_icon, left.getImg());
        helper.setText(R.id.item_3_left_title, left.getTitle());
        helper.setText(R.id.item_3_left_content, left.getConetent());

        ItemEntity3.ItemEntity3Item top = item.getTop();
        helper.setImageResource(R.id.item_3_top_icon, top.getImg());
        helper.setText(R.id.item_3_top_title, top.getTitle());
        helper.setText(R.id.item_3_top_content, top.getConetent());

        ItemEntity3.ItemEntity3Item bottom = item.getBottom();
        helper.setImageResource(R.id.item_3_bottom_icon, bottom.getImg());
        helper.setText(R.id.item_3_bottom_title, bottom.getTitle());
        helper.setText(R.id.item_3_bottom_content, bottom.getConetent());
    }


    private void renderEntity4(BaseViewHolder helper, ItemEntity4 item) {
        helper.setImageResource(R.id.item_4_icon, item.getImg());
        helper.setText(R.id.item_4_title, item.getTitle());
        helper.setText(R.id.item_4_name, item.getName());
        helper.setText(R.id.item_4_des, item.getDes());
        helper.setText(R.id.item_4_tag, item.getTag());
        helper.setText(R.id.item_4_people, item.getPeople());
        helper.setText(R.id.item_4_price, item.getPrice());
    }

    private void renderHeader(BaseViewHolder helper, CommonHeadEntity item) {
        helper.setText(R.id.title, item.getTitle());
    }

    private void renderFooter(BaseViewHolder helper, final CommonFooterEntity item) {
        final int type = item.getItemType() + RecyclerViewAdapterHelper.FOOTER_TYPE_DIFFER;
        final TextView footer = helper.getView(R.id.item_footer);
        footer.setText(item.getTitle());
        footer.setOnClickListener(v -> {
            if (Constants.EXPAND.equals(item.getTitle())) {
                item.setTitle(Constants.FOLD);
                mHelper.foldType(type, false);
            } else {
                item.setTitle(Constants.EXPAND);
                mHelper.foldType(type, true);
            }
            footer.setText(item.getTitle());
        });

    }

    public void notifyLoading() {
        mHelper.notifyLoadingChanged();
    }

    public void notifyType1(List<ItemEntity1> itemEntity1s) {
        mHelper.notifyMoudleDataAndHeaderAndFooterChanged(itemEntity1s, entity1Header, entity1Footer, MyAdapterHelper.LEVEL_1);
    }

    public void notifyType2(List<ItemEntity2> itemEntity2s) {
        mHelper.notifyMoudleDataAndHeaderChanged(itemEntity2s, entity2Header, MyAdapterHelper.LEVEL_2);
    }

    public void notifyType3(ItemEntity3 itemEntity3) {
        mHelper.notifyMoudleDataChanged(itemEntity3, MyAdapterHelper.LEVEL_3);
    }

    public void notifyType4(List<ItemEntity4> itemEntity4s) {
        mHelper.notifyMoudleDataAndHeaderChanged(itemEntity4s, entity4Header, MyAdapterHelper.LEVEL_4);
    }

    public void release() {
        mHelper.release();
    }
}
