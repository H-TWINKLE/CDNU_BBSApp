package com.twinkle.cdnubbs.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.Toast;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.twinkle.cdnubbs.R;
import com.twinkle.cdnubbs.java.adapter.TypeAdapter;
import com.twinkle.cdnubbs.java.content.GlideImageLoader;
import com.twinkle.cdnubbs.java.adapter.HeaderAndFooterAdapter;
import com.twinkle.cdnubbs.java.content.HidingScrollListener;
import com.twinkle.cdnubbs.java.utils.Init;
import com.twinkle.cdnubbs.java.utils.Util;
import com.twinkle.cdnubbs.user.ClassesBase;
import com.twinkle.cdnubbs.user.Post;
import com.twinkle.cdnubbs.user.User;
import com.yalantis.phoenix.PullToRefreshView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class IndexFragment extends Fragment implements OnBannerListener {

    private PullToRefreshView ptrv_index;
    private RecyclerView rv_index;
    private List<Post> data;
    private Banner banner;
    private HeaderAndFooterAdapter headerAndFooterAdapter;
    private List<String> banner_img;
    private List<String> banner_title;
    private List<ClassesBase> classesBases;
    private static final String TAG = "IndexFragment";
    private BottomNavigationView navigation;
    private FloatingActionButton fab_main;
    private boolean isHide, isFirstCreate = false;

    public IndexFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_index, container, false);
        findView(view);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }


    public void findView(View view) {
        ptrv_index = (PullToRefreshView) view.findViewById(R.id.ptrv_index);
        rv_index = (RecyclerView) view.findViewById(R.id.rv_index);
        navigation = (BottomNavigationView) getActivity().findViewById(R.id.navigation);
        fab_main = (FloatingActionButton) getActivity().findViewById(R.id.fab_main);
    }

    public void initView() {
        rv_index.setLayoutManager(new LinearLayoutManager(getActivity()));
        initData();
        initAdapter();
        set_headerView();
    }


    private void initData() {
        getPost();
        data = new ArrayList<>();
        banner_img = new ArrayList<>();
        banner_title = new ArrayList<>();
        initData_classbase();
    }

    private void initData_classbase() {
        classesBases = new ArrayList<>();
        for (int x = 0; x < Init.classes_title.length; x++) {
            classesBases.add(new ClassesBase(Init.classes_title[x], Init.classes[x]));
        }
    }

    private void set_headerView() {
        View head_view1 = getHeaderView1();
        headerAndFooterAdapter.addHeaderView(head_view1);
        View head_view2 = getHeaderView2();
        headerAndFooterAdapter.addHeaderView(head_view2);
        rv_index.setAdapter(headerAndFooterAdapter);
        rv_index.addOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                hideViews();
            }

            @Override
            public void onShow() {
                showViews();
            }
        });
    }

    private void hideViews() {
        navigation.animate().translationY(navigation.getHeight()).setInterpolator(new AccelerateInterpolator(2)).start();

        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) fab_main.getLayoutParams();
        int fabBottomMargin = lp.bottomMargin;
        fab_main.animate().translationX(fab_main.getWidth() + fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();
        //  navigation.setVisibility(View.GONE);
        isHide = true;

    }

    private void showViews() {
        navigation.animate().translationY(0).setInterpolator(new AccelerateInterpolator(2)).start();
        fab_main.animate().translationX(0).setInterpolator(new AccelerateInterpolator(2)).start();
        // navigation.setVisibility(View.VISIBLE);
        isHide = false;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            if (isFirstCreate) {
                    fab_main.setVisibility(View.VISIBLE);
                if (isHide) {
                    hideViews();
                } else {
                    showViews();
                }
            } else {
                isFirstCreate = true;
            }
        }
        Log.i(TAG, "setUserVisibleHint: "+isVisibleToUser+"//"+isFirstCreate+"//"+isHide);
    }

    private void initAdapter() {
        headerAndFooterAdapter = new HeaderAndFooterAdapter(data);
        headerAndFooterAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        rv_index.setAdapter(headerAndFooterAdapter);
        headerAndFooterAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(getActivity(), "item" + position, Toast.LENGTH_SHORT).show();
            }
        });
        headerAndFooterAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.siv_sr_index_header:
                        Util.BaseToast(getActivity(), "heaer" + position);
                        break;
                    case R.id.tv_cr_index_username:
                        Util.BaseToast(getActivity(), "username" + position);
                        break;
                    case R.id.tv_cr_index_focus:
                        break;
                    case R.id.ib_cr_index_love:
                        Util.BaseToast(getActivity(), "love" + position);
                        break;
                    case R.id.ib_cr_index_comment:
                        Util.BaseToast(getActivity(), "comment" + position);
                        break;
                    default:
                        break;
                }
            }
        });


    }

    private View getHeaderView1() {
        View view2 = getLayoutInflater().inflate(R.layout.content_index_banner, (ViewGroup) rv_index.getParent(), false);
        initBanner(view2);
        return view2;
    }

    private View getHeaderView2() {
        View view2 = getLayoutInflater().inflate(R.layout.content_cr, (ViewGroup) rv_index.getParent(), false);
        initRecylerVew(view2);
        return view2;
    }

    public void initBanner(View view2) {
        banner = (Banner) view2.findViewById(R.id.bn_index);
        banner.setImageLoader(new GlideImageLoader());
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        banner.setImages(banner_img);  //设置图片集合
        banner.setBannerTitles(banner_title);   //设置标题集合（当banner样式有显示title时）
        banner.isAutoPlay(true);//设置自动轮播，默认为true
        banner.setDelayTime(3000); //设置轮播时间
        banner.setIndicatorGravity(BannerConfig.CENTER);//设置指示器位置（当banner模式中有指示器时）
        banner.setOnBannerListener(this);
        banner.start(); //banner设置方法全部调用完毕时最后调用

    }

    public void initRecylerVew(View view2) {
        RecyclerView recyclerView = (RecyclerView) view2.findViewById(R.id.rv_classes);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        TypeAdapter classAdapter = new TypeAdapter(classesBases);
        recyclerView.setAdapter(classAdapter);
        classAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Util.BaseToast(getActivity(), "grid " + position);
            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();
        //开始轮播
        banner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        //结束轮播
        banner.stopAutoPlay();
    }

    public void getPost() {
        BmobQuery<User> innerQuery = new BmobQuery<>();
        BmobQuery<Post> query = new BmobQuery<Post>();
        query.include("author");
        // query.addWhereMatchesQuery("praise", "_User", innerQuery);
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> object, BmobException e) {
                if (e == null) {
                    data.addAll(object);
                    headerAndFooterAdapter.notifyDataSetChanged();

                } else {
                    Log.i("bmob", "失败：" + e.getMessage());
                }
            }
        });
    }


    @Override
    public void OnBannerClick(int position) {
        Toast.makeText(getActivity(), "banner" + position, Toast.LENGTH_SHORT).show();
    }
}
