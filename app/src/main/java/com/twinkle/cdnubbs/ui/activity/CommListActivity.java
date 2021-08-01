package com.twinkle.cdnubbs.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.twinkle.cdnubbs.R;
import com.twinkle.cdnubbs.adapter.MyListCommentAdapter;
import com.twinkle.cdnubbs.adapter.MyListMessAdapter;
import com.twinkle.cdnubbs.adapter.MyListPraiseAdapter;
import com.twinkle.cdnubbs.bmob.BmobProxy;
import com.twinkle.cdnubbs.content.BaseActivity;
import com.twinkle.cdnubbs.content.LoadDialog;
import com.twinkle.cdnubbs.entity.Comment;
import com.twinkle.cdnubbs.entity.Post;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

public class CommListActivity extends BaseActivity implements BmobProxy.onGetListMessListener,BmobProxy.onGetListCommentListener,BmobProxy.onGetListPraiseListener {


    private static final String TAG = "CommListActivity";
    @ViewInject(value = R.id.tb_comm)
    private Toolbar tb_comm;

    @ViewInject(value = R.id.rv_comm)
    private RecyclerView rv_comm;

    @Override
    public int getLayoutId() {
        return R.layout.activity_comm_list;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        x.view().inject(this);
        rv_comm.setLayoutManager(new LinearLayoutManager(CommListActivity.this));
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        tb_comm.setTitle(intent.getStringExtra("title"));
        tb_comm.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getList(intent.getIntExtra("flag", 0));
    }


    private void getList(int flag) {
        switch (flag) {
            case -2:
                break;
            case -1:
                break;
            case 0: {
                LoadDialog.getInstance().show(CommListActivity.this);
                BmobProxy.getInstance().setOnGetListMessListener(this);
                BmobProxy.getInstance().getListMess();
            }
            break;
            case 1:{
                LoadDialog.getInstance().show(CommListActivity.this);
                BmobProxy.getInstance().setOnGetListCommentListener(this);
                BmobProxy.getInstance().getListComment();
            }
                break;
            case 3:
                break;
            case 4:{
                LoadDialog.getInstance().show(CommListActivity.this);
                BmobProxy.getInstance().setOnGetListPraiseListener(this);
                BmobProxy.getInstance().getListPraise();
            }
                break;
            default:
                break;
        }


    }


    @Override
    public void onGetListMessListenerSuccess( final List<Comment> list) {
        MyListMessAdapter myMessAdapter = new MyListMessAdapter(list);
        myMessAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(CommListActivity.this, ContentActivity.class);
                intent.putExtra("post_id", list.get(position).getPost().getObjectId());
                startActivity(intent);
            }
        });
        rv_comm.setAdapter(myMessAdapter);
        LoadDialog.getInstance().dismiss();
        Log.i(TAG, "onGetListMessListenerSuccess: "+ list.size());
    }

    @Override
    public void onGetListMessListenerFailure(String text) {
        LoadDialog.getInstance().dismiss();
        Toast.makeText(CommListActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetListCommentListenerSuccess(final List<Comment> list) {
        MyListCommentAdapter myCommentAdapter = new MyListCommentAdapter(list);
        myCommentAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(CommListActivity.this, ContentActivity.class);
                intent.putExtra("post_id", list.get(position).getPost().getObjectId());
                startActivity(intent);
            }
        });
        rv_comm.setAdapter(myCommentAdapter);
        LoadDialog.getInstance().dismiss();
        Log.i(TAG, "onGetListCommentListenerSuccess: "+ list.size());
    }

    @Override
    public void onGetListCommentListenerFailure(String text) {
        LoadDialog.getInstance().dismiss();
        Toast.makeText(CommListActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetListPraiseListenerSuccess(final List<Post> list) {
        MyListPraiseAdapter myListPraiseAdapter = new MyListPraiseAdapter(list);
        myListPraiseAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(CommListActivity.this, ContentActivity.class);
                intent.putExtra("post_id", list.get(position).getObjectId());
                startActivity(intent);
            }
        });
        rv_comm.setAdapter(myListPraiseAdapter);
        LoadDialog.getInstance().dismiss();
        Log.i(TAG, "onGetListPraiseListenerSuccess: "+ list.size());
    }

    @Override
    public void onGetListPraiseListenerFailure(String text) {
        LoadDialog.getInstance().dismiss();
        Toast.makeText(CommListActivity.this, text, Toast.LENGTH_SHORT).show();
    }
}
