package com.crazysunj.sample.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.coorchice.library.SuperTextView;
import com.crazysunj.sample.R;
import com.crazysunj.sample.base.BaseViewHolder;

import androidx.recyclerview.widget.RecyclerView;

/**
 * author: sunjian
 * created on: 2017/8/6 上午10:57
 * description:
 */

public class HeadAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final int[] sImgs = {R.mipmap.ic_register, R.mipmap.ic_queue, R.mipmap.ic_recharge, R.mipmap.ic_healthy, R.mipmap.ic_nutrition, R.mipmap.ic_physical, R.mipmap.ic_fam_doc, R.mipmap.ic_all};
    private static final String[] sTitles = {"门诊挂号", "排队叫号", "账户充值", "健康商城", "营养订餐", "体检中心", "名医课堂", "全部"};

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_head_home, parent, false));
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        SuperTextView item = holder.getView(R.id.item_head_home);
        item.setText(sTitles[position]);
        item.setDrawable(sImgs[position]);
    }

    @Override
    public int getItemCount() {
        return sTitles.length;
    }
}
