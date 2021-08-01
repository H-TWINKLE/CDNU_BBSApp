package com.twinkle.cdnubbs.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.twinkle.cdnubbs.R;
import com.twinkle.cdnubbs.content.BaseActivity;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

public class TopicActivity extends BaseActivity implements Inputtips.InputtipsListener {

    private static final String TAG = "TopicActivity";

    public static int REQUEST_CODE = 1038;

    @ViewInject(value = R.id.actv_topic)
    private AutoCompleteTextView actv_topic;

    @ViewInject(value = R.id.tb_topic)
    private Toolbar tb_topic;

    private String[] str;

    @Override
    public int getLayoutId() {
        return R.layout.activity_topic;
    }


    @Override
    public void initView(Bundle savedInstanceState) {
        x.view().inject(this);
    }

    @Override
    public void initData() {
        tb_topic.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setAutoEditListener();
    }


    private  void setItemListener(){
        actv_topic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("local",str[position]);
                TopicActivity.this.setResult(REQUEST_CODE,intent);
                TopicActivity.this.finish();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }


    private void setAutoEditListener(){
        actv_topic.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = actv_topic.getText().toString();
                if(!TextUtils.isEmpty(text)){
                    InputtipsQuery inputquery = new InputtipsQuery(text,"");
                    inputquery.setCityLimit(true);//限制在当前城市
                    Inputtips inputTips = new Inputtips(TopicActivity.this, inputquery);
                    inputTips.setInputtipsListener(TopicActivity.this);
                    inputTips.requestInputtipsAsyn();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onGetInputtips(List<Tip> list, int i) {
        str = new String[list.size()];
        for(int x=0;x<list.size();x++){
            str[x] = list.get(x).getDistrict()+"  "+list.get(x).getName();
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(TopicActivity.this,android.R.layout.simple_dropdown_item_1line,str);
        actv_topic.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
        setItemListener();
    }

}
