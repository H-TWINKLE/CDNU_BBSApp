package com.twinkle.cdnubbs.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.twinkle.cdnubbs.R;
import com.twinkle.cdnubbs.adapter.MyListMessAdapter;
import com.twinkle.cdnubbs.adapter.MyListPraiseAdapter;
import com.twinkle.cdnubbs.bmob.BmobProxy;
import com.twinkle.cdnubbs.content.LoadDialog;
import com.twinkle.cdnubbs.entity.Comment;
import com.twinkle.cdnubbs.ui.activity.CommListActivity;
import com.twinkle.cdnubbs.ui.activity.ContentActivity;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment implements BmobProxy.onGetListMessListener {

    private static final String TAG = "NewsFragment";

    private RecyclerView rv_news;

    private boolean flag = false;

    public NewsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initView(View view) {
        rv_news = view.findViewById(R.id.rv_news);
    }

    private void setRv() {
        LoadDialog.getInstance().show(getActivity());
        rv_news.setLayoutManager(new LinearLayoutManager(getActivity()));
        BmobProxy.getInstance().setOnGetListMessListener(this);
        BmobProxy.getInstance().getListMess();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !flag) {
            setRv();
        }
    }

    @Override
    public void onGetListMessListenerSuccess(final List<Comment> list) {
        LoadDialog.getInstance().dismiss();
        MyListMessAdapter myMessAdapter = new MyListMessAdapter(list);
        myMessAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), ContentActivity.class);
                intent.putExtra("post_id", list.get(position).getPost().getObjectId());
                startActivity(intent);
            }
        });
        rv_news.setAdapter(myMessAdapter);
        flag = true;

    }

    @Override
    public void onGetListMessListenerFailure(String text) {
        LoadDialog.getInstance().dismiss();
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }
}
