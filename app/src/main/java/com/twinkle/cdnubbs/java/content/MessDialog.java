package com.twinkle.cdnubbs.java.content;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.twinkle.cdnubbs.java.utils.Init;

public abstract class MessDialog extends AlertDialog {
    private  Context context;
    private String title;
    private View view;
    private String text;


    public abstract void then();


    protected MessDialog(Context context,String title,View view) {
        super(context);
        this.context = context;
        this.title = title;
        this.view = view;
        showAlertDialog_view();
    }

    protected MessDialog(Context context,String title,String text) {
        super(context);
        this.context = context;
        this.title = title;
        this.text = text;
        showAlertDialog_text();
    }

    private void showAlertDialog_view(){
        new android.app.AlertDialog.Builder(context)
                .setTitle(title).setView(view)
                .setPositiveButton(Init.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        then();
                    }
                }).setNegativeButton(Init.cancel, null).create().show();
    }
    private void showAlertDialog_text(){
        new android.app.AlertDialog.Builder(context)
                .setTitle(title).setMessage(text)
                .setPositiveButton(Init.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        then();
                    }
                }).create().show();
    }



}
