package com.twinkle.cdnubbs.java.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.twinkle.cdnubbs.R;
import com.twinkle.cdnubbs.java.utils.Init;


public class InfoAdapter extends BaseAdapter {

    private Context context;
    private String[] name = Init.lv_info;
    private int[] header = new int[]{R.drawable.mess, R.drawable.comment, R.drawable.post, R.drawable.coll, R.drawable.praise};

    public InfoAdapter(Context context) {
        this.context = context;
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view1 = View.inflate(context, R.layout.content_lv_menu, null);
        ImageView imageView = (ImageView) view1.findViewById(R.id.lv_iv_header);
        TextView textView = (TextView) view1.findViewById(R.id.lv_tv_name);
        imageView.setImageResource(header[i]);
        textView.setText(name[i]);
        return view1;
    }
}
