package com.crazysunj.sample.app;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;

import androidx.multidex.MultiDex;

/**
 * description
 * <p>
 * Created by sunjian on 2017/6/24.
 */

public class APP extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }
}
