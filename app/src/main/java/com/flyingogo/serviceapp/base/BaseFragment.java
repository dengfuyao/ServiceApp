package com.flyingogo.serviceapp.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;

import butterknife.ButterKnife;

/**
 * 作者：dfy on 18/8/2017 10:36
 * <p> fragment 基类
 * 邮箱：dengfuyao@163.com
 */

public abstract class BaseFragment extends Fragment {
    public Gson mGson;
    //控件是否已经初始化
    protected       boolean isInit = false;
    protected       boolean isLoad = false;
    protected final String  TAG    = "LazyLoadFragment";
    public View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(getResLayoutId(), container, false);
        ButterKnife.bind(this, view);
        isInit = true;
        initData();
        init();
        return view;

    }

    protected void init() {

    }

    protected void initData() {
        mGson = new Gson();
        if (!isInit) {
            return;
        }

        if (getUserVisibleHint()) {
            lazyLoad();
            isLoad = true;
        } else {
            if (isLoad) {
                stopLoad();
            }
        }
    }


    /**
     * 视图是否已经对用户可见，系统的方法
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.e(TAG, "setUserVisibleHint:  = " + isVisibleToUser);
        if (isVisibleToUser) {
            initData();
        } else {
            if (view != null) {
                ViewGroup parent = (ViewGroup) view.getParent();
                if (parent != null) {
                   // parent.removeView(view);
                }
            }
        }
    }

    public abstract int getResLayoutId();

    public void go2Activity(Class<?> activity, boolean defaults) {
        Intent intent = new Intent(getContext(), activity);
        startActivity(intent);
        if (defaults) {
            getActivity().finish();
        }
    }

    /**
     * 视图销毁的时候讲Fragment是否初始化的状态变为false
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isInit = false;
        isLoad = false;

    }

    protected void showToast(String message) {
        if (!TextUtils.isEmpty(message)) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 获取设置的布局
     *
     * @return
     */
    protected View getContentView() {
        return view;
    }

    /**
     * 当视图初始化并且对用户可见的时候去真正的加载数据
     */
    protected abstract void lazyLoad();

    /**
     * 当视图已经对用户不可见并且加载过数据，如果需要在切换到其他页面时停止加载数据，可以覆写此方法
     */
    protected void stopLoad() {
    }

}
