package com.twinkle.cdnubbs.ui.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.Toast;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.twinkle.cdnubbs.R;
import com.twinkle.cdnubbs.adapter.TypeAdapter;
import com.twinkle.cdnubbs.bmob.BmobProxy;
import com.twinkle.cdnubbs.content.GlideImageLoader;
import com.twinkle.cdnubbs.adapter.HeaderAndFooterAdapter;
import com.twinkle.cdnubbs.content.HidingScrollListener;
import com.twinkle.cdnubbs.content.LoadDialog;
import com.twinkle.cdnubbs.ui.activity.ContentActivity;
import com.twinkle.cdnubbs.ui.activity.UserActivity;
import com.twinkle.cdnubbs.ui.activity.WebActivity;
import com.twinkle.cdnubbs.utils.Init;
import com.twinkle.cdnubbs.entity.ClassesBase;
import com.twinkle.cdnubbs.entity.Post;
import com.yalantis.phoenix.PullToRefreshView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

public class IndexFragment extends Fragment implements OnBannerListener, BmobProxy.onGetPostListener {


    private static final String TAG = "IndexFragment";
    private PullToRefreshView ptrv_index;
    private RecyclerView rv_index;
    private List<Post> data;
    private Banner banner;
    private HeaderAndFooterAdapter headerAndFooterAdapter;
    private List<String> banner_img;
    private List<String> banner_title;
    private List<String> banner_id;
    private List<ClassesBase> classesBases;
    private BottomNavigationView navigation;
    private FloatingActionButton fab_main;
    private boolean isHide, isFirstCreate = false;

    protected BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            BmobProxy.getInstance().bmobGetPost();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        // 在当前的activity中注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(Init.UpdateMess);
        getActivity().registerReceiver(this.broadcastReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(this.broadcastReceiver);
    }

    public IndexFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_index, container, false);
        findView(view);   //找到控件          1             ///
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();  //初始化控件              2           ///
    }


    public void findView(View view) {           //1      ///
        ptrv_index = view.findViewById(R.id.ptrv_index);
        rv_index = view.findViewById(R.id.rv_index);
        navigation = getActivity().findViewById(R.id.navigation);
        fab_main = getActivity().findViewById(R.id.fab_main);
    }

    public void initView() {                   //2     ///
        rv_index.setLayoutManager(new LinearLayoutManager(getActivity()));
        ptrv_index.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ptrv_index.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ptrv_index.setRefreshing(false);
                        BmobProxy.getInstance().bmobGetPost();
                    }
                }, 3000);
            }
        });
        LoadDialog.getInstance().show(getActivity());
        initData();               //初始化数据             3     ///
        initAdapter();         //初始化配适器                 5    ///
        set_headerView();               //初始化header  并设置     6

    }

    private void initData() {                    //3     ///
        BmobProxy.getInstance().setOnGetPostListener(this);            //进行查询          4    ///
        BmobProxy.getInstance().bmobGetPost();                  //进行查询           4     ///
        data = new ArrayList<>();   //3.1    ///
        banner_img = new ArrayList<>();
        banner_title = new ArrayList<>();
        banner_id = new ArrayList<>();
        initData_classbase();          //3.2     ///

    }

    private void initData_classbase() {     //3.2
        classesBases = new ArrayList<>();
        for (int x = 0; x < Init.classes_title.length; x++) {
            classesBases.add(new ClassesBase(Init.classes_title[x], Init.classes[x]));
        }
    }

    private void set_headerView() {                  //6
        View head_view1 = getHeaderView1();   //第一个header
        headerAndFooterAdapter.addHeaderView(head_view1);
        View head_view2 = getHeaderView2();         //第二个header
        headerAndFooterAdapter.addHeaderView(head_view2);
        rv_index.setAdapter(headerAndFooterAdapter);
        rv_index.addOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                hideViews();
            }     //6.3          ///

            @Override
            public void onShow() {
                showViews();
            }    //6.4               ///
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
        if (isVisibleToUser) {
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
        //Log.i(TAG, "setUserVisibleHint: "+isVisibleToUser+"//"+isFirstCreate+"//"+isHide);
    }

    private View getHeaderView1() {               //6.1
        View view2 = getLayoutInflater().inflate(R.layout.content_index_banner, (ViewGroup) rv_index.getParent(), false);
        banner = view2.findViewById(R.id.bn_index);
        return view2;
    }

    private View getHeaderView2() {                         //6.2
        View view2 = getLayoutInflater().inflate(R.layout.content_cr, (ViewGroup) rv_index.getParent(), false);
        initRecylerVew(view2);
        return view2;
    }

    public void initBanner() {        //7

        if (banner_img.size() != 0 || banner_title.size() != 0 || banner_id.size() != 0) {
            banner_img.clear();
            banner_title.clear();
            banner_id.clear();
        }

        int num = 0;
        for (int x = 0; x < data.size(); x++) {
            if (!TextUtils.isEmpty(data.get(x).getTitle()) && data.get(x).getImg().size() != 0 && num < 4) {
                banner_img.add(data.get(x).getImg().get(0));
                banner_title.add(data.get(x).getTitle());
                banner_id.add(data.get(x).getObjectId());
                num++;
            }
        }

        banner.setImageLoader(new GlideImageLoader());
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        banner.isAutoPlay(true);//设置自动轮播，默认为true
        banner.setDelayTime(3000); //设置轮播时间
        banner.setIndicatorGravity(BannerConfig.CENTER);//设置指示器位置（当banner模式中有指示器时）
        banner.setOnBannerListener(this);
        banner.setImages(banner_img);  //设置图片集合
        banner.setBannerTitles(banner_title);   //设置标题集合（当banner样式有显示title时）
        banner.start();

    }

    public void initRecylerVew(View view2) {       //6.2
        RecyclerView recyclerView = view2.findViewById(R.id.rv_classes);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        TypeAdapter classAdapter = new TypeAdapter(classesBases);
        recyclerView.setAdapter(classAdapter);
        classAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("title", getString(classesBases.get(position).getTitle()));
                intent.putExtra("flag",position);
                startActivity(intent);
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

    @Override
    public void OnBannerClick(int position) {
        Intent intent = new Intent(getActivity(), ContentActivity.class);
        intent.putExtra("post_id", banner_id.get(position));
        startActivity(intent);
    }


    private void startActivity(int position) {
        Intent intent = new Intent(getActivity(), UserActivity.class);
        intent.putExtra("id", data.get(position).getAuthor().getObjectId());
        startActivity(intent);
    }

    private void initAdapter() {                               //       5
        headerAndFooterAdapter = new HeaderAndFooterAdapter(data);
        headerAndFooterAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        rv_index.setAdapter(headerAndFooterAdapter);
        headerAndFooterAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), ContentActivity.class);
                intent.putExtra("post_id", data.get(position).getObjectId());
                startActivity(intent);
            }
        });
        headerAndFooterAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.siv_sr_index_header:
                        startActivity(position);
                        break;
                    case R.id.tv_cr_index_username:
                        startActivity(position);
                        break;
                    case R.id.tv_cr_index_focus:
                        break;
                    case R.id.ib_cr_index_love:

                        break;
                    case R.id.ib_cr_index_comment:

                        break;
                    default:
                        break;
                }
            }
        });
    }


    @Override
    public void onGetPostSuccess(List<Post> list) {
        if (data.size() != 0) {
            data.clear();
        }
        data.addAll(list);
        headerAndFooterAdapter.notifyDataSetChanged();
        initBanner();
        LoadDialog.getInstance().dismiss();
    }

    @Override
    public void onGetPostFailure(String text) {
        LoadDialog.getInstance().dismiss();
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }
}
