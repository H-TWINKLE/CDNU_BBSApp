package com.twinkle.cdnubbs.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;
import com.twinkle.cdnubbs.R;
import com.twinkle.cdnubbs.entity.Post;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;

public class UserPostAdapter extends BaseAdapter {

    private static final String TAG = "UserPostAdapter";
    private List<Post> post;
    private Context context;

    public UserPostAdapter(Context context, List<Post> post) {
        this.context = context;
        this.post = post;
    }

    @Override
    public int getCount() {
        return post.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = View.inflate(context, R.layout.content_cr_index, null);

        SmartImageView siv_sr_index_header = (SmartImageView) view.findViewById(R.id.siv_sr_index_header);
        x.image().bind(siv_sr_index_header, post.get(position).getAuthor().getPic(), new ImageOptions.Builder().setCircular(true).build());

        TextView tv_cr_index_username = (TextView) view.findViewById(R.id.tv_cr_index_username);
        tv_cr_index_username.setText(post.get(position).getAuthor().getName());

        TextView tv_cr_index_time = (TextView) view.findViewById(R.id.tv_cr_index_time);
        tv_cr_index_time.setText(post.get(position).getAuthor().getCreatedAt());

        SmartImageView siv_cr_index_pic = (SmartImageView) view.findViewById(R.id.siv_cr_index_pic);

        TextView tv_cr_index_content = (TextView) view.findViewById(R.id.tv_cr_index_content);
        tv_cr_index_content.setText(post.get(position).getContent());

        int num;
        try {
            num = post.get(position).getImg().size();
        } catch (Exception e) {
            num = 0;
        }
        if (num > 0) {
            x.image().bind(siv_cr_index_pic, post.get(position).getImg().get(0));
        } else {
            siv_cr_index_pic.setVisibility(View.GONE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(120, 0, 0, 30);
            tv_cr_index_content.setLayoutParams(params);

        }


        LinearLayout lly_focus = (LinearLayout) view.findViewById(R.id.lly_focus);
        lly_focus.setVisibility(View.GONE);

        return view;
    }
}
