package com.twinkle.cdnubbs.java;

import android.app.Application;

import org.xutils.x;

import cn.bmob.v3.Bmob;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this, "a0084a6493c91624bb96851013e23a28");
        x.Ext.init(this);
        x.Ext.setDebug(false); //输出debug日志，开启会影响性能
    }
}
