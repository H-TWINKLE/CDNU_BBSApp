package com.twinkle.cdnubbs.java.content;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;

import com.roger.gifloadinglibrary.GifLoadingView;
import com.twinkle.cdnubbs.R;
import com.twinkle.cdnubbs.java.utils.Init;
import com.twinkle.cdnubbs.java.utils.Util;

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
        mGifLoadingView.show(activity.getFragmentManager(), "");
        countDownTimer.start();
    }

    public void diss() {
        mGifLoadingView.dismiss();

    }

    private CountDownTimer countDownTimer = new CountDownTimer(10000, 1000) {
        @Override
        public void onTick(long l) {

        }

        @Override
        public void onFinish() {
            mGifLoadingView.dismiss();
            Util.toast(activity, Init.timeout);
        }
    };
}
