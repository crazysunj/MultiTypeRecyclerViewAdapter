package com.crazysunj.multityperecyclerviewadapter;

import android.content.Context;

public class DialogUtil {
    public static LoadingDialog getDialog(Context context) {
        if (context == null)
            return null;
        LoadingDialog circleProgressDialog = new LoadingDialog(context, R.style.ProgressDialog, R.layout.layout_loading);
        circleProgressDialog.setCanceledOnTouchOutside(false);
        return circleProgressDialog;
    }
}
