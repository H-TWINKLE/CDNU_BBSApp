package com.twinkle.cdnubbs.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.twinkle.cdnubbs.R;
import com.twinkle.cdnubbs.content.BaseActivity;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class WebActivity extends BaseActivity {

    @ViewInject(value = R.id.tb_web)
    private Toolbar tb_web;

    @ViewInject(value = R.id.wv_web)
    private WebView wv_web;

    @Override
    public int getLayoutId() {
        return R.layout.activity_web;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        x.view().inject(this);
        tb_web.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        tb_web.setTitle(intent.getStringExtra("title"));
        initWebView();
        switch (intent.getIntExtra("flag",0)){
            case 0:setWv_web("http://i.waimai.meituan.com/home?lat=30.675726&lng=103.831696");break;
            case 1:setWv_web("https://m.weibo.cn/p/100803");break;
            case 2:setWv_web("https://m.maoyan.com/?from=m_nav_2_maoyandianying");break;
            case 3:setWv_web("https://xw.qq.com/");break;
            default:break;
        }
    }

    private  void initWebView(){
        WebSettings webSettings = wv_web.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格
    }

    private void setWv_web(String url){
        wv_web.loadUrl(url);
        wv_web.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }


}
