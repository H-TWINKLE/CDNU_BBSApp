package com.twinkle.cdnubbs.ui.activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;

import com.twinkle.cdnubbs.R;
import com.twinkle.cdnubbs.content.BaseActivity;
import com.twinkle.cdnubbs.ui.fragment.IndexFragment;
import com.twinkle.cdnubbs.ui.fragment.InfoFragment;
import com.twinkle.cdnubbs.ui.fragment.NewsFragment;
import com.twinkle.cdnubbs.utils.Init;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    @ViewInject(value = R.id.navigation)
    private BottomNavigationView navigation;

    @ViewInject(value = R.id.vpr_main)
    private ViewPager vpr_main;


    private List<Fragment> list_frag;

    @ViewInject(value = R.id.fab_main)
    private FloatingActionButton fab_main;


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }



    @Override
    public void initView(Bundle savedInstanceState) {
        x.view().inject(this);
    }

    @Override
    public void initData() {
        navigation.setItemIconTintList(null);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        vpr_main.addOnPageChangeListener(this);
        initFragments();
    }

    @Event(value = R.id.fab_main)
    private void fabClick(View view){
        startActivity(new Intent(MainActivity.this, WriteMessActivity.class));
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_index:
                    vpr_main.setCurrentItem(0);
                    return true;
                case R.id.navigation_news:
                    vpr_main.setCurrentItem(1);
                    return true;
                case R.id.navigation_info:
                    vpr_main.setCurrentItem(2);
                    return true;
            }
            return false;
        }
    };

    private void initFragments() {
        list_frag = new ArrayList<>();
        list_frag.add(new IndexFragment());
        list_frag.add(new NewsFragment());
        list_frag.add(new InfoFragment());
        vpr_main.setOffscreenPageLimit(2);
        vpr_main.setAdapter(new FragAdapter(getSupportFragmentManager()));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                navigation.setSelectedItemId(R.id.navigation_index);
                break;
            case 1:
                navigation.setSelectedItemId(R.id.navigation_news);
                break;
            default:
                navigation.setSelectedItemId(R.id.navigation_info);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public class FragAdapter extends FragmentPagerAdapter {
        private FragAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return list_frag.get(position);
        }

        @Override
        public int getCount() {
            return list_frag.size();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Init.ExitApp);
        registerReceiver(getBroadcast, intentFilter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(getBroadcast);
    }




    protected  BroadcastReceiver getBroadcast  = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            MainActivity.this.finish();
        }
    };

}
