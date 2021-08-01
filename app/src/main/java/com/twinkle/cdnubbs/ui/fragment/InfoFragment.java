package com.twinkle.cdnubbs.ui.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;
import com.twinkle.cdnubbs.R;
import com.twinkle.cdnubbs.adapter.InfoAdapter;
import com.twinkle.cdnubbs.bmob.BmobProxy;
import com.twinkle.cdnubbs.ui.activity.AdminActivity;
import com.twinkle.cdnubbs.ui.activity.CommListActivity;
import com.twinkle.cdnubbs.ui.activity.UserActivity;
import com.twinkle.cdnubbs.utils.Init;
import com.twinkle.cdnubbs.utils.Util;
import com.twinkle.cdnubbs.entity.User;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import cn.bmob.v3.BmobUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends Fragment implements View.OnClickListener {

    private ListView lv_info;
    private TextView tvw_info_fans, tvw_info_foucs, tvw_info_admin, tvw_info_level, tvw_info_name;
    private ImageView ivw_info_fans, ivw_info_focus;
    private LinearLayout llt_info_admin;
    private SmartImageView siv_info_header;
    private InfoAdapter infoAdapter;
    private BottomNavigationView navigation;
    private FloatingActionButton fab_main;


    protected BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            User user = BmobUser.getCurrentUser(User.class);
            tvw_info_name.setText(user.getName());
            x.image().bind(siv_info_header, user.getPic(), new ImageOptions.Builder().setCircular(true).build());
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        // 在当前的activity中注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(Init.UpdateInfo);
        getActivity().registerReceiver(this.broadcastReceiver, filter);
    }

    public InfoFragment() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(this.broadcastReceiver);
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        findView(view);
        return view;
    }

    public void findView(View view) {
        lv_info = (ListView) view.findViewById(R.id.lv_info);

        tvw_info_fans = (TextView) view.findViewById(R.id.tvw_info_fans);
        tvw_info_foucs = (TextView) view.findViewById(R.id.tvw_info_foucs);

        ivw_info_fans = (ImageView) view.findViewById(R.id.ivw_info_fans);
        ivw_info_focus = (ImageView) view.findViewById(R.id.ivw_info_focus);

        llt_info_admin = (LinearLayout) view.findViewById(R.id.llt_info_admin);

        tvw_info_admin = (TextView) view.findViewById(R.id.tvw_info_admin);
        tvw_info_level = (TextView) view.findViewById(R.id.tvw_info_level);
        tvw_info_name = (TextView) view.findViewById(R.id.tvw_info_name);
        siv_info_header = (SmartImageView) view.findViewById(R.id.siv_info_header);
        navigation = (BottomNavigationView) getActivity().findViewById(R.id.navigation);
        fab_main = (FloatingActionButton) getActivity().findViewById(R.id.fab_main);
    }


    private void showView() {
        navigation.animate().translationY(0).setInterpolator(new AccelerateInterpolator(2)).start();
        fab_main.setVisibility(View.GONE);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            showView();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        set_lv();
        user_info();


    }

    private void user_info() {
        User user = BmobUser.getCurrentUser(User.class);
        tvw_info_name.setText(user.getName());
        tvw_info_admin.setText(user.getUsername());
        tvw_info_level.setText(String.valueOf(user.getLevel()));
        x.image().bind(siv_info_header, user.getPic(), new ImageOptions.Builder().setCircular(true).build());
        llt_info_admin.setOnClickListener(this);
        ivw_info_fans.setOnClickListener(this);
        ivw_info_focus.setOnClickListener(this);

    }

    private void set_lv() {
        infoAdapter = new InfoAdapter(getActivity());
        lv_info.setAdapter(infoAdapter);
        lv_info.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==2){
                    Intent intent = new Intent(getActivity(), UserActivity.class);
                    intent.putExtra("id", BmobUser.getCurrentUser(User.class).getObjectId());
                    startActivity(intent);
                }else {
                    startIntent(getResources().getString(Init.lv_info[position]),position);
                }

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llt_info_admin:
                startActivity(new Intent(getActivity(), AdminActivity.class));
                break;
            case R.id.ivw_info_focus:
                startIntent("我的关注",-2);
                break;
            case R.id.ivw_info_fans:
                startIntent("我的粉丝",-1);
                break;
            default:
                break;
        }
    }


    private void startIntent(String title,int flag){
        Intent intent = new Intent(getActivity(),CommListActivity.class);
        intent.putExtra("title",title);
        intent.putExtra("flag",flag);
        startActivity(intent);
    }
}
