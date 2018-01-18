package com.twinkle.cdnubbs.java;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.twinkle.cdnubbs.user.User;

/**
 * Created by TWINKLE on 2018/1/18.
 */

public class Util {

    public static void toast(final AppCompatActivity appCompatActivity, final String text) {
        appCompatActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(appCompatActivity,text,Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void save_shareperfence(Context context,User user){
        SharedPreferences sharedPreferences = context.getSharedPreferences("User",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id",user.getObjectId());
        editor.putString("username",user.getUsername());
        editor.putString("pass",user.getPass());
        editor.putString("name",user.getName());
        editor.putString("time",user.getTime());
        editor.putString("cdnu_id",user.getCdnu_id());
        editor.putString("cdnu_pass",user.getCdnu_pass());
        editor.putInt("level",user.getLevel());
        editor.apply();
    }

    public static void remove_shareperfence(Context context,User user){
        SharedPreferences sharedPreferences = context.getSharedPreferences("User",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
