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

public class MyListCommentAdapter extends BaseQuickAdapter<Comment, BaseViewHolder> {
    public MyListCommentAdapter( List<Comment> data) {
        super(R.layout.content_list_comment, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Comment item) {
        SmartImageView siv_lv_mess_header = helper.getView(R.id.siv_lv_comment_header);
        x.image().bind(siv_lv_mess_header, item.getAuthor().getPic(), new ImageOptions.Builder().setCircular(true).build());

        helper.setText(R.id.tv_lv_comment_username, item.getAuthor().getName());

        helper.setText(R.id.tv_lv_comment_time, item.getCreatedAt());

        helper.setText(R.id.tv_lv_comment_content, item.getContent());

        helper.setText(R.id.tv_lv_comment_post, item.getPost().getContent());
    }
}
