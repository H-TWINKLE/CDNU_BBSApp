package com.twinkle.cdnubbs.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.loopj.android.image.SmartImageView;
import com.mob.wrappers.AnalySDKWrapper;
import com.twinkle.cdnubbs.R;
import com.twinkle.cdnubbs.adapter.CommentListAdapter;
import com.twinkle.cdnubbs.adapter.RecycleBaseAdapter;
import com.twinkle.cdnubbs.bmob.BmobProxy;
import com.twinkle.cdnubbs.content.BaseActivity;
import com.twinkle.cdnubbs.content.LoadDialog;
import com.twinkle.cdnubbs.entity.Comment;
import com.twinkle.cdnubbs.entity.Post;
import com.twinkle.cdnubbs.entity.User;

import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

public class ContentActivity extends BaseActivity implements BmobProxy.getOnePostListener, BmobProxy.onAddPostCommentListener,
        BmobProxy.onGetPostCommentListener, BmobProxy.onBmobPraiseListener, BmobProxy.onDeletePraiseListener,BmobProxy.bmobCheckIsPraiseListener {


    private static final String TAG = "ContentActivity";

    @ViewInject(value = R.id.tb_content)
    private Toolbar tb_content;

    @ViewInject(value = R.id.siv_content_header)
    private SmartImageView siv_content_header;


    @ViewInject(value = R.id.tv_content_name)
    private TextView tv_content_name;

    @ViewInject(value = R.id.tv_content_time)
    private TextView tv_content_time;

    @ViewInject(value = R.id.tv_content_content)
    private TextView tv_content_content;

    @ViewInject(value = R.id.tv_content_love)
    private TextView tv_content_love;

    @ViewInject(value = R.id.tv_content_comment)
    private TextView tv_content_comment;

    @ViewInject(value = R.id.ib_content_love)
    private ImageButton ib_content_love;

    @ViewInject(value = R.id.ib_content_comment)
    private ImageButton ib_content_comment;


    @ViewInject(value = R.id.rv_content_list)
    private RecyclerView rv_content_list;

    @ViewInject(value = R.id.rv_content_pic)
    private RecyclerView rv_content_pic;


    @ViewInject(value = R.id.tv_content_local)
    private TextView tv_content_local;


    @ViewInject(value = R.id.siv_content_comment_header)
    private SmartImageView siv_content_comment_header;

    @ViewInject(value = R.id.et_content_comment)
    private EditText et_content_comment;


    private CommentListAdapter contentListAdapter;


    private List<Comment> commentList;

    private Intent intent;

    private boolean praiseFlag = false;

    @Event(value = R.id.ib_content_love)
    private void onPraiseClick(View view) {
        Log.i(TAG, "onPraiseClick: "+praiseFlag);
        if (praiseFlag) {
            LoadDialog.getInstance().show(ContentActivity.this);
            BmobProxy.getInstance().setOnDeletePraiseListener(this);
            BmobProxy.getInstance().bmobDeletePraise(intent.getStringExtra("post_id"));
        } else {
            LoadDialog.getInstance().show(ContentActivity.this);
            BmobProxy.getInstance().setOnBmobPraiseListener(this);
            BmobProxy.getInstance().bmobPraise(intent.getStringExtra("post_id"));
        }

    }


    @Event(value = R.id.siv_content_comment_header)
    private void onClick(View view) {
        addPostComment();
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_content;
    }


    @Override
    public void initView(Bundle savedInstanceState) {
        x.view().inject(this);

        siv_content_comment_header.setEnabled(false);

        x.image().bind(siv_content_comment_header, BmobUser.getCurrentUser(User.class).getPic(),
                new ImageOptions.Builder().setCircular(true).build());

        tb_content.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rv_content_pic.setLayoutManager(new GridLayoutManager(ContentActivity.this, 3));

        rv_content_list.setLayoutManager(new LinearLayoutManager(ContentActivity.this));

        LoadDialog.getInstance().show(ContentActivity.this);

        et_content_comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    siv_content_comment_header.setEnabled(true);
                    siv_content_comment_header.setImageDrawable(getDrawable(R.drawable.ic_send_black_48dp));
                } else {
                    x.image().bind(siv_content_comment_header, BmobUser.getCurrentUser(User.class).getPic(),
                            new ImageOptions.Builder().setCircular(true).build());
                    siv_content_comment_header.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void initData() {
        intent = getIntent();
        commentList = new ArrayList<>();
        BmobProxy.getInstance().setGetOnePostListener(this);
        BmobProxy.getInstance().bmobGetOnePost(intent.getStringExtra("post_id"));
        BmobProxy.getInstance().setOnGetPostCommentListener(this);
        BmobProxy.getInstance().getPostComment(intent.getStringExtra("post_id"));
        BmobProxy.getInstance().setBmobCheckIsPraiseListener(this);
        BmobProxy.getInstance().bmobCheckIsPraise(intent.getStringExtra("post_id"));
        setListContent();
    }

    @Override
    public void onGetOnePostSuccess(Post post) {
        x.image().bind(siv_content_header, post.getAuthor().getPic(), new ImageOptions.Builder().setCircular(true).build());
        tv_content_name.setText(post.getAuthor().getName());
        tv_content_time.setText(post.getUpdatedAt());
        tv_content_content.setText(post.getContent());
        tv_content_local.setText(post.getLocal());

        RecycleBaseAdapter adapterPic = new RecycleBaseAdapter(post.getImg());
        rv_content_pic.setAdapter(adapterPic);
        LoadDialog.getInstance().dismiss();
    }

    @Override
    public void onGetOnePostFailure(String text) {
        LoadDialog.getInstance().dismiss();
        Toast.makeText(ContentActivity.this, text, Toast.LENGTH_SHORT).show();
    }


    private void addPostComment() {
        BmobProxy.getInstance().setOnAddPostCommentListener(this);
        BmobProxy.getInstance().addPostComment(intent.getStringExtra("post_id"), et_content_comment.getText().toString());
        LoadDialog.getInstance().show(ContentActivity.this);
        BmobProxy.getInstance().getPostComment(intent.getStringExtra("post_id"));

    }


    @Override
    public void onAddPostCommentListenerSuccess(String text) {
        LoadDialog.getInstance().dismiss();
        Toast.makeText(ContentActivity.this, text, Toast.LENGTH_SHORT).show();
        et_content_comment.getText().clear();
    }

    @Override
    public void onAddPostCommentListenerFailure(String text) {
        LoadDialog.getInstance().dismiss();
        Toast.makeText(ContentActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetPostCommentListenerSuccess(List<Comment> list) {
        Log.i(TAG, "onGetPostCommentListenerSuccess: " + list.size());
        LoadDialog.getInstance().dismiss();
        if (commentList.size() != 0) {
            commentList.clear();
        }
        commentList.addAll(list);
        contentListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetPostCommentListenerFailure(String text) {
        LoadDialog.getInstance().dismiss();
        Toast.makeText(ContentActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    private void setListContent() {
        contentListAdapter = new CommentListAdapter(R.layout.content_cr_comment, commentList);
        contentListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.siv_sr_comment_header:
                        break;
                    case R.id.tv_cr_comment_username:
                        break;

                    default:
                        break;
                }
            }
        });
        rv_content_list.setAdapter(contentListAdapter);
    }

    @Override
    public void onBmobPraiseListenerSuccess(String text) {
        LoadDialog.getInstance().dismiss();
        isPraise(true, R.drawable.ispraise);
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBmobPraiseListenerFailure(String text) {
        LoadDialog.getInstance().dismiss();
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }


    private void isUserPraise(List<User> list) {
        for (User user : list) {
            if (user.getObjectId().equals(BmobUser.getCurrentUser(User.class).getObjectId())) {
                isPraise(true, R.drawable.ispraise);
                break;
            }
        }
    }


    private void isPraise(boolean flag, int draw) {
        ib_content_love.setImageDrawable(getDrawable(draw));
        ib_content_love.setAdjustViewBounds(true);
        ib_content_love.setScaleType(ImageView.ScaleType.FIT_CENTER);
        praiseFlag = flag;
        Log.i(TAG, "isPraise: "+praiseFlag);
    }

    @Override
    public void onDeletePraiseListenerSuccess(String text) {
        LoadDialog.getInstance().dismiss();
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        isPraise(false,R.drawable.praise);
    }

    @Override
    public void onDeletePraiseListenerFailure(String text) {
        LoadDialog.getInstance().dismiss();
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void bmobCheckIsPraiseListenerListener(List<User> list) {
        LoadDialog.getInstance().dismiss();
        isUserPraise(list);
    }

    @Override
    public void bmobCheckIsPraiseListenerFailure(String text) {
        LoadDialog.getInstance().dismiss();
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
}
