package com.twinkle.cdnubbs.java.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;
import com.twinkle.cdnubbs.ui.LoginActivity;
import com.twinkle.cdnubbs.ui.NewPassActivity;
import com.twinkle.cdnubbs.R;
import com.twinkle.cdnubbs.java.utils.Init;
import com.twinkle.cdnubbs.java.utils.Util;
import com.twinkle.cdnubbs.user.User;

import java.io.File;
import java.util.List;

import cn.bmob.v3.BmobUser;

import org.xutils.image.ImageOptions;
import org.xutils.x;

public class AdminAdapter extends BaseAdapter {
    private Context context;
    private String[] name = Init.lv_admin;
    private List<File> actualImage;


    public AdminAdapter(Context context, List<File> actualImage) {
        this.context = context;
        this.actualImage = actualImage;
    }
    @Override
    public int getCount() {
        return name.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean isEnabled(int position) {
        return position == 0 || position == 2 || position == 5 && super.isEnabled(position);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view1 = View.inflate(context, R.layout.content_lv_admin, null);
        TextView tip = (TextView) view1.findViewById(R.id.lv_tv_admin_tip);
        TextView info = (TextView) view1.findViewById(R.id.lv_tv_admin_info);
        SmartImageView siv_admin_header = (SmartImageView) view1.findViewById(R.id.siv_admin_header);

        //settip
        tip.setText(name[i]);

        //setheader
        if (i != 0) {
            siv_admin_header.setVisibility(View.GONE);
        } else {
            x.image().bind(siv_admin_header, Util.getUser().getPic(), new ImageOptions.Builder().setCircular(true).build());
            //  siv_admin_header.setImageUrl(Util.getUser().getPic());
        }
        if (actualImage != null) {
            siv_admin_header.setImageBitmap(BitmapFactory.decodeFile(actualImage.get(0).getAbsolutePath()));
            // siv_admin_header.setImageUrl(actualImage.toString());
        }

        //setinf
        User user = Util.getUser();
        switch (i) {
            case 1:
                info.setText(user.getUsername());
                break;
            case 2:
                info.setText(user.getName());
                break;
            case 3:
                info.setText(user.getMobilePhoneNumber());
                break;
            case 4:
                info.setText(user.getCreatedAt());
                break;
            case 5:
                info.setText(String.valueOf(user.getLevel()));
                break;
            case 6:
                info.setText(context.getString(R.string.logout));
                info.setTextSize(18);
                tip.setTextColor(Color.rgb(255, 10, 10));
                info.setTextColor(Color.rgb(255, 10, 10));
                tip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        context.startActivity(new Intent(context, NewPassActivity.class));
                    }
                });
                info.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        BmobUser.logOut();
                        myExit();
                        context.startActivity(new Intent(context, LoginActivity.class));

                    }
                });
                break;
            default:
                break;
        }
        return view1;
    }

    private void myExit() {
        Intent intent = new Intent();
        intent.setAction("ExitApp");
        context.sendBroadcast(intent);
    }

}
