package com.twinkle.cdnubbs.adapter;

import android.widget.ImageView;

import com.bilibili.boxing.model.entity.BaseMedia;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.twinkle.cdnubbs.R;

import org.xutils.x;

import java.util.List;

/**
 * Created by TWINKLE on 2018/1/28.
 */

public class BoxingGlideAdapter extends BaseQuickAdapter<BaseMedia, BaseViewHolder> {



    public BoxingGlideAdapter(List<BaseMedia> data) {
        super(R.layout.content_cr_set_pic, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BaseMedia item) {
        ImageView imageView = (ImageView)helper.getView(R.id.iv_wm_setpic);
        x.image().bind(imageView,item.getPath());
        helper.addOnClickListener(R.id.iv_wm_setpic);
        helper.addOnClickListener(R.id.iv_wm_setpic_close);
    }
}
