package com.twinkle.cdnubbs.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.twinkle.cdnubbs.R;

import org.xutils.x;

import java.util.List;

public class RecycleBaseAdapter extends BaseQuickAdapter<String,BaseViewHolder> {

    public RecycleBaseAdapter(List<String> data) {
        super(R.layout.content_base_cr_pic, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ImageView view = helper.getView(R.id.content_base_iv_pic);
        x.image().bind(view,item);
    }
}
