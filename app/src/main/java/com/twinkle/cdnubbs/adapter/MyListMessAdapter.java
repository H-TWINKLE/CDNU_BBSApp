package com.twinkle.cdnubbs.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.loopj.android.image.SmartImageView;
import com.twinkle.cdnubbs.R;
import com.twinkle.cdnubbs.entity.Comment;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;

public class MyListMessAdapter extends BaseQuickAdapter<Comment, BaseViewHolder> {


    public MyListMessAdapter(List<Comment> data) {
        super(R.layout.content_list_mess, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Comment item) {
        SmartImageView siv_lv_mess_header = helper.getView(R.id.siv_lv_mess_header);
        x.image().bind(siv_lv_mess_header, item.getAuthor().getPic(), new ImageOptions.Builder().setCircular(true).build());

        helper.setText(R.id.tv_lv_mess_username, item.getAuthor().getName());

        helper.setText(R.id.tv_lv_mess_time, item.getCreatedAt());

        helper.setText(R.id.tv_lv_mess_content, item.getContent());

        helper.setText(R.id.tv_lv_mess_post, item.getPost().getContent());


    }
}
