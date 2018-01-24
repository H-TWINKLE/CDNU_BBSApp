package com.twinkle.cdnubbs.java.proxy;

import android.content.Context;

import com.twinkle.cdnubbs.user.User;

import cn.bmob.v3.BmobUser;


public class BmobProxy {

    public static final String TAG = "BmobProxy";
    private Context mContext;

    public BmobProxy(Context context) {
        this.mContext = context;
    }

    public static User getUser() {
        return BmobUser.getCurrentUser(User.class);
    }




}
