package com.crazysunj.multityperecyclerviewadapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

public class LoadingDialog extends ProgressDialog {
    private int mLayoutRes;

    public LoadingDialog(Context context, int theme, int layoutRes) {
        super(context, theme);
        this.mLayoutRes = layoutRes;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mLayoutRes);
    }

}
