package com.flyingogo.serviceapp.activity;

import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.flyingogo.serviceapp.R;
import com.flyingogo.serviceapp.base.BaseActivity;

/**
 * Created by Mr.ZhangWei on 2017/6/8.
 */

public class WebViewActivity extends BaseActivity {

    private WebView webview;


    @Override
    public void initView() {
        webview = (WebView) findViewById(R.id.webview);
    }

    @Override
    public void initTitle() {

    }


    @Override
    public void initData() {
        //设置webview
        WebSettings ws = webview.getSettings();
        ws.setJavaScriptEnabled(true);//启用js功能
        ws.setBuiltInZoomControls(true);//启用放大缩小功能
        ws.setUseWideViewPort(true);//启用双击缩放功能
        ws.setLoadWithOverviewMode(true);//适应屏幕大小
        ws.setSupportZoom(true);
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);

        //加载webview
        String url =getIntent().getStringExtra("web");
        webview.loadUrl(url);

        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (webview.canGoBack()) {
            webview.goBack();
        }else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(webview != null) {
            webview.destroy();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webview.canGoBack()) {
                webview.goBack();
            }else {
                finish();
            }
        }
        return true;
    }
}
