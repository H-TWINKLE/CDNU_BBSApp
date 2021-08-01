package com.twinkle.cdnubbs.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.twinkle.cdnubbs.R;
import com.twinkle.cdnubbs.entity.ClassesBase;

import java.util.List;


public class TypeAdapter extends BaseQuickAdapter<ClassesBase, BaseViewHolder> {
    public TypeAdapter(List<ClassesBase>  data) {
        super(R.layout.content_cr_class,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ClassesBase item) {
        helper.setImageResource(R.id.iv_cr_class,item.getPic());
        helper.setText(R.id.tv_cr_class,item.getTitle());
    }


}
