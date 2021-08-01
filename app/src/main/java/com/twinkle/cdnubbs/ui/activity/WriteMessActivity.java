package com.twinkle.cdnubbs.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bilibili.boxing.Boxing;
import com.bilibili.boxing.BoxingMediaLoader;

import com.bilibili.boxing.model.config.BoxingConfig;
import com.bilibili.boxing.model.entity.BaseMedia;

import com.bilibili.boxing_impl.ui.BoxingActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.loopj.android.image.SmartImageView;
import com.twinkle.cdnubbs.R;
import com.twinkle.cdnubbs.adapter.BoxingGlideAdapter;
import com.twinkle.cdnubbs.content.BaseActivity;
import com.twinkle.cdnubbs.content.BoxingGlideLoader;
import com.twinkle.cdnubbs.content.LoadDialog;
import com.twinkle.cdnubbs.entity.User;
import com.twinkle.cdnubbs.utils.Init;
import com.twinkle.cdnubbs.utils.Util;
import com.twinkle.cdnubbs.entity.Post;

import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;

public class WriteMessActivity extends BaseActivity  {

    @ViewInject(value = R.id.iv_wm_finsh)
    private ImageView iv_wm_finsh;

    @ViewInject(value = R.id.iv_wm_send)
    private ImageView  iv_wm_send;

    @ViewInject(value = R.id.iv_wm_choose_pic)
    private ImageView  iv_wm_choose_pic;

    @ViewInject(value = R.id.iv_wm_choose_topic)
    private ImageView iv_wm_choose_topic;

    private static final int REQUEST_CODE = 1024;

    public static final int REQUEST_ACTIVITY = 1038;

    @ViewInject(value = R.id.siv_wm_header)
    private SmartImageView siv_wm_header;

    @ViewInject(value = R.id.et_wm_mess)
    private EditText et_wm_mess;

    @ViewInject(value = R.id.rv_wm_pic)
    private RecyclerView rv_wm_pic;


    @ViewInject(value = R.id.tv_wm_local)
    private TextView tv_wm_local;

    private BoxingGlideAdapter adapter;
    private List<BaseMedia> list;
    private Post post;

    @Override
    public int getLayoutId() {
        return R.layout.activity_write_mess;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        x.view().inject(this);
        rv_wm_pic.setLayoutManager(new GridLayoutManager(WriteMessActivity.this, 3));
        iv_wm_send.setVisibility(View.INVISIBLE);
        et_wm_mess.addTextChangedListener(textWatcher);
        x.image().bind(siv_wm_header, BmobUser.getCurrentUser(User.class).getPic(),
                new ImageOptions.Builder().setCircular(true).build());
    }


    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }
        @Override
        public void afterTextChanged(Editable editable) {
            if (!TextUtils.isEmpty(et_wm_mess.getText().toString())) {
                iv_wm_send.setVisibility(View.VISIBLE);
            } else {
                iv_wm_send.setVisibility(View.INVISIBLE);
            }
        }
    };

    @Override
    public void initData() {
        list = new ArrayList<>();
        post = new Post();
        initadapter();

    }
    @Event(value = R.id.iv_wm_finsh)
    private void toOut(View view){
        finish();
    }

    @Event(value = R.id.iv_wm_send)
    private void toSend(View view){
        LoadDialog.getInstance().show(WriteMessActivity.this);
        if(list.size()!=0){
            uploadMessPic();
        }else {
            sendMess();
        }
    }

    @Event(value = R.id.tv_wm_local)
    private void onClick(View view){
        tv_wm_local.setText("");
    }

    @Event(value = R.id.iv_wm_choose_pic)
    private void choosePic(View view){
        BoxingMediaLoader.getInstance().init(new BoxingGlideLoader());
        BoxingConfig config = new BoxingConfig(BoxingConfig.Mode.MULTI_IMG).needGif();
        Boxing.of(config).withIntent(this, BoxingActivity.class).start(this, REQUEST_CODE);
    }

    @Event(value = R.id.iv_wm_choose_topic)
    private void chooseTopic(View view){
        Intent intent = new Intent(WriteMessActivity.this, TopicActivity.class);
        startActivityForResult(intent,REQUEST_ACTIVITY);
    }

    private void initadapter() {
        adapter = new BoxingGlideAdapter(list);
        rv_wm_pic.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.iv_wm_setpic_close:
                        list.remove(position);
                        adapter.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if ( adapter == null) {
                return;
            }
            final ArrayList<BaseMedia> medias = Boxing.getResult(data);
            if (requestCode == REQUEST_CODE && medias != null) {
                list.clear();
                list.addAll(medias);
                adapter.notifyDataSetChanged();
            }
        }else if(REQUEST_ACTIVITY==requestCode && resultCode == TopicActivity.REQUEST_CODE){
            tv_wm_local.setText(data.getStringExtra("local"));
        }
    }


    private void uploadMessPic() {
        String[] filePaths = new String[list.size()];
        for (int x = 0; x < list.size(); x++) {
            filePaths[x] = list.get(x).getPath();
        }
        BmobFile.uploadBatch(filePaths, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> files, List<String> urls) {
                //1、files-上传完成后的BmobFile集合，是为了方便大家对其上传后的数据进行操作，例如你可以将该文件保存到表中
                //2、urls-上传文件的完整url地址
                if (urls.size() == list.size()) {//如果数量相等，则代表文件全部上传完成
                    Toast.makeText(WriteMessActivity.this,Init.success_update_pic,Toast.LENGTH_SHORT).show();
                    post.setImg(urls);
                    sendMess();
                }
            }

            @Override
            public void onError(int statuscode, String errormsg) {
            }

            @Override
            public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
            }
        });


    }

    private void sendMess() {
        post.setTitle("");
        post.setContent(et_wm_mess.getText().toString());
        post.setLocal(tv_wm_local.getText().toString());
        post.setAuthor(BmobUser.getCurrentUser(User.class));
        post.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    LoadDialog.getInstance().dismiss();
                    myUpdate();
                    finish();
                }
            }
        });
    }

    private void myUpdate() {                                          //广播更新画面
        Intent intent = new Intent();
        intent.setAction(Init.UpdateMess);
        sendBroadcast(intent);
    }

}
