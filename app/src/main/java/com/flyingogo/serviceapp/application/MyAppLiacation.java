package com.flyingogo.serviceapp.application;

import android.app.Application;
import android.content.Context;

import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * 作者：dfy on 21/8/2017 10:51
 * <p>
 * 邮箱：dengfuyao@163.com
 */

public class MyAppLiacation extends Application {
    public Context mContext;
    @Override
    public void onCreate() {
        mContext = getApplicationContext();
        initOkHttpUtils();
    }

    private void initOkHttpUtils() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(20000L, TimeUnit.MILLISECONDS)
                .readTimeout(20000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);
    }
}
