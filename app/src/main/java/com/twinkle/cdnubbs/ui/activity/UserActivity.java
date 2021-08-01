package com.twinkle.cdnubbs.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;


import com.twinkle.cdnubbs.R;
import com.twinkle.cdnubbs.adapter.UserPostAdapter;
import com.twinkle.cdnubbs.bmob.BmobProxy;
import com.twinkle.cdnubbs.content.BaseActivity;
import com.twinkle.cdnubbs.content.LoadDialog;
import com.twinkle.cdnubbs.entity.Post;
import com.twinkle.cdnubbs.entity.User;

import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

public class UserActivity extends BaseActivity implements BmobProxy.onBombGetUserPostListener {

    private static final String TAG = "UserActivity";

    @ViewInject(value = R.id.app_bar_user)
    private AppBarLayout app_bar_user;

    @ViewInject(value = R.id.lv_user)
    private ListView lv_user;

    @ViewInject(value = R.id.iv_user_header)
    private ImageView iv_user_header;

    @ViewInject(value = R.id.tb_user)
    private Toolbar tb_user;

    @Override
    public int getLayoutId() {
        return R.layout.activity_user;
    }


    @Override
    public void initView(Bundle savedInstanceState) {
        x.view().inject(this);
        LoadDialog.getInstance().show(UserActivity.this);
        tb_user.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        lv_user.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) { }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if(firstVisibleItem+visibleItemCount==totalItemCount&&totalItemCount>0)
                {
                     app_bar_user.setExpanded(false);//滚动到最后一行,在这里可以处理ListView上拉加载更多
                }else {
                    app_bar_user.setExpanded(true);
                }
            }
        });
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        BmobProxy.getInstance().setOnBombGetUserPostListener(this);
        BmobProxy.getInstance().bmobGetUserPost(intent.getStringExtra("id"));

    }

    @Override
    public void onGetUserPostSuccess(final List<Post> list) {
        UserPostAdapter userPostAdapter = new UserPostAdapter(UserActivity.this, list);
        lv_user.setAdapter(userPostAdapter);
        lv_user.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(UserActivity.this, ContentActivity.class);
                intent.putExtra("post_id", list.get(position).getObjectId());
                startActivity(intent);
            }
        });
        tb_user.setTitle(list.get(0).getAuthor().getName());
        x.image().bind(iv_user_header, list.get(0).getAuthor().getPic(), new ImageOptions.Builder().setCircular(true).build());
        LoadDialog.getInstance().dismiss();
    }

    @Override
    public void onGetUserPostFailure(String text) {
        LoadDialog.getInstance().dismiss();
        Toast.makeText(UserActivity.this, text, Toast.LENGTH_SHORT).show();
    }
}
