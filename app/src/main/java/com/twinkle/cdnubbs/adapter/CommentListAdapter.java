package com.twinkle.cdnubbs.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.loopj.android.image.SmartImageView;
import com.twinkle.cdnubbs.R;
import com.twinkle.cdnubbs.entity.Comment;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;

public class CommentListAdapter extends BaseQuickAdapter<Comment, BaseViewHolder> {


    public CommentListAdapter(int layoutResId, List<Comment> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, Comment item) {

        SmartImageView imageView = helper.getView(R.id.siv_sr_comment_header);
        x.image().bind(imageView, item.getAuthor().getPic(),new ImageOptions.Builder().setCircular(true).build());

        helper.setText(R.id.tv_cr_comment_username, item.getAuthor().getName());
        helper.setText(R.id.tv_cr_comment_time,item.getCreatedAt());
        helper.setText(R.id.tv_cr_comment_content,item.getContent());


    }
}
