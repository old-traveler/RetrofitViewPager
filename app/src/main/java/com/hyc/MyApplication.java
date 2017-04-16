package com.hyc;

import android.app.Application;

/**
 * Created by hyc on 2017/4/13 22:43
 */

public class MyApplication extends Application {
    private static MyApplication cloudReaderApplication;

    public static MyApplication getInstance() {
        return cloudReaderApplication;
    }

    @SuppressWarnings("unused")
    @Override
    public void onCreate() {
        super.onCreate();
        cloudReaderApplication = this;

    }
}
