package com.twinkle.cdnubbs.java;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.roger.gifloadinglibrary.GifLoadingView;
import com.twinkle.cdnubbs.R;

/**
 * Created by TWINKLE on 2018/1/17.
 */

public class LoadDialog {

    private GifLoadingView mGifLoadingView;
    private AppCompatActivity activity;

    public LoadDialog(AppCompatActivity activity) {

        this.activity = activity;
        mGifLoadingView = new GifLoadingView();
        mGifLoadingView.setImageResource(R.drawable.num50);
        mGifLoadingView.setCancelable(false);


    }

    public void show() {
        mGifLoadingView.show(activity.getFragmentManager(), "123");
    }

    public void diss() {
        mGifLoadingView.dismiss();

    }
}
