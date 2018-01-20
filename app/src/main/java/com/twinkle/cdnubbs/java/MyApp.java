package com.twinkle.cdnubbs.java;

import android.app.Application;

import com.twinkle.cdnubbs.LoginActivity;

import org.xutils.x;

import cn.bmob.v3.Bmob;

/**
 * Created by TWINKLE on 2018/1/18.
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this, "a0084a6493c91624bb96851013e23a28");
        x.Ext.init(this);
        x.Ext.setDebug(false); //输出debug日志，开启会影响性能
    }
}
