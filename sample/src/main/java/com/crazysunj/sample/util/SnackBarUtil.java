package com.crazysunj.sample.util;

import android.app.Activity;
import android.support.design.widget.Snackbar;

/**
 * author: sunjian
 * created on: 2017/8/5 下午4:52
 * description:
 */

public class SnackBarUtil {
    public static void show(Activity activity, String msg) {
        Snackbar.make(activity.findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG).show();
    }
}
