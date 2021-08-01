package com.twinkle.cdnubbs.content;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

import com.roger.gifloadinglibrary.GifLoadingView;
import com.twinkle.cdnubbs.R;


public class LoadDialog {


    private static LoadDialog instance = new LoadDialog();

    public static LoadDialog getInstance() {
        return instance;
    }

    private GifLoadingView mGifLoadingView;



    private void setmGifLoadingView() {
        this.mGifLoadingView = new GifLoadingView();
        mGifLoadingView.setImageResource(R.drawable.num50);
        mGifLoadingView.setCancelable(false);
    }


    public void show(Activity activity) {
        setmGifLoadingView();
        mGifLoadingView.show(activity.getFragmentManager(), "");

    }

    public void dismiss() {
        mGifLoadingView.dismiss();
    }

}
