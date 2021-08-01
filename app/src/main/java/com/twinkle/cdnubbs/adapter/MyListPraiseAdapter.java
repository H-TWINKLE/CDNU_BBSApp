package com.twinkle.cdnubbs.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.loopj.android.image.SmartImageView;
import com.twinkle.cdnubbs.R;
import com.twinkle.cdnubbs.entity.Comment;
import com.twinkle.cdnubbs.entity.Post;
import com.twinkle.cdnubbs.entity.User;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;

import cn.bmob.v3.BmobUser;

public class MyListPraiseAdapter extends BaseQuickAdapter<Post, BaseViewHolder> {

    public MyListPraiseAdapter(List<Post> data) {
        super(R.layout.content_cr_index, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Post item) {
        SmartImageView pic = (SmartImageView) helper.getView(R.id.siv_sr_index_header);
        x.image().bind(pic, item.getAuthor().getPic(), new ImageOptions.Builder().setCircular(true).build());

        helper.setText(R.id.tv_cr_index_username, item.getAuthor().getName());
        helper.setText(R.id.tv_cr_index_time, item.getCreatedAt());

        SmartImageView img = (SmartImageView) helper.getView(R.id.siv_cr_index_pic);

        TextView tv_content = helper.getView(R.id.tv_cr_index_content);

        int num;
        try {
            num = item.getImg().size();
        } catch (Exception e) {
            num = 0;
        }
        if (num > 0) {
            img.setImageUrl(item.getImg().get(0));
        } else {
            img.setVisibility(View.GONE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(120, 0, 0, 30);
            tv_content.setLayoutParams(params);
        }
        LinearLayout lly_focus = helper.getView(R.id.lly_focus);
        lly_focus.setVisibility(View.GONE);

        helper.setText(R.id.tv_cr_index_title, item.getTitle());
        helper.setText(R.id.tv_cr_index_content, item.getContent());


    }
}
