package com.flyingogo.serviceapp.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * 作者：dfy on 19/8/2017 09:31
 * <p>
 * 邮箱：dengfuyao@163.com
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initTitle();
        initData();
    }

    public abstract void initView();
    public abstract void initTitle();
    public abstract void initData();
}
