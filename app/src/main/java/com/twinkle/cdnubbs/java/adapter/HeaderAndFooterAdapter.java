package com.twinkle.cdnubbs.java.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.loopj.android.image.SmartImageView;
import com.twinkle.cdnubbs.R;
import com.twinkle.cdnubbs.java.utils.Util;
import com.twinkle.cdnubbs.user.Post;
import com.twinkle.cdnubbs.user.User;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;


public class HeaderAndFooterAdapter extends BaseQuickAdapter<Post, BaseViewHolder> {



    public HeaderAndFooterAdapter(List<Post> data) {
        super(R.layout.content_cr_index, data);

    }



    @Override
    protected void convert(BaseViewHolder helper, Post item) {

        SmartImageView pic = (SmartImageView) helper.getView(R.id.siv_sr_index_header);
        x.image().bind(pic, item.getAuthor().getPic(), new ImageOptions.Builder().setCircular(true).build());

        helper.setText(R.id.tv_cr_index_username, item.getAuthor().getName());
        helper.setText(R.id.tv_cr_index_time, item.getCreatedAt());

        SmartImageView img = (SmartImageView) helper.getView(R.id.siv_cr_index_pic);
        img.setImageUrl(item.getImg());

        helper.setText(R.id.tv_cr_index_title, item.getTitle());
        helper.setText(R.id.tv_cr_index_content, item.getContent());

        helper.setText(R.id.tv_cr_index_love, item.getPraisenum()+"");
        helper.setText(R.id.tv_cr_index_comment, item.getCommentnum()+"");

        //addOnClickListener
        helper.addOnClickListener(R.id.siv_sr_index_header);
        helper.addOnClickListener(R.id.tv_cr_index_username);
        helper.addOnClickListener(R.id.tv_cr_index_focus);
        helper.addOnClickListener(R.id.ib_cr_index_love);
        helper.addOnClickListener(R.id.ib_cr_index_comment);
    }

}
