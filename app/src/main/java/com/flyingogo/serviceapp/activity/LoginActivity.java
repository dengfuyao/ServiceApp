package com.flyingogo.serviceapp.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.flyingogo.serviceapp.R;
import com.flyingogo.serviceapp.weiget.DrawableTextView;
import com.flyingogo.serviceapp.weiget.KeyboardWatcher;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.flyingogo.serviceapp.R.id.body;
import static com.flyingogo.serviceapp.R.id.clean_password;
import static com.flyingogo.serviceapp.R.id.et_mobile;
import static com.flyingogo.serviceapp.R.id.et_password;
import static com.flyingogo.serviceapp.R.id.iv_clean_phone;
import static com.flyingogo.serviceapp.R.id.iv_show_pwd;

/**
 * 作者：dfy on 24/8/2017 14:29
 * <p>
 * 邮箱：dengfuyao@163.com
 */

public class LoginActivity extends AppCompatActivity implements KeyboardWatcher.SoftKeyboardStateListener, View.OnClickListener {


    @BindView(R.id.logo)
    DrawableTextView mLogo;
    @BindView(et_mobile)
    EditText         mEtMobile;
    @BindView(iv_clean_phone)
    ImageView        mIvCleanPhone;
    @BindView(et_password)
    EditText         mEtPassword;
    @BindView(clean_password)
    ImageView        mCleanPassword;
    @BindView(iv_show_pwd)
    ImageView        mIvShowPwd;
    @BindView(R.id.btn_login)
    Button           mBtnLogin;
    @BindView(body)
    LinearLayout     mBody;
    @BindView(R.id.service)
    LinearLayout     mService;
    @BindView(R.id.root)
    LinearLayout     mRoot;
    private int   screenHeight = 0;//屏幕高度
    private float scale        = 0.8f; //logo缩放比例

    private KeyboardWatcher keyboardWatcher;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        screenHeight = this.getResources().getDisplayMetrics().heightPixels; //获取屏幕高度

        initEvent();
        keyboardWatcher = new KeyboardWatcher(findViewById(Window.ID_ANDROID_CONTENT));
        keyboardWatcher.addSoftKeyboardStateListener(this);
    }

    private void initEvent() {
        mIvCleanPhone.setOnClickListener(this);
        mCleanPassword.setOnClickListener(this);
        mIvShowPwd.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);
        mEtMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && mIvCleanPhone.getVisibility() == View.GONE) {
                    mIvCleanPhone.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(s)) {
                    mIvCleanPhone.setVisibility(View.GONE);
                }
            }
        });
        mEtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && mCleanPassword.getVisibility() == View.GONE) {
                    mCleanPassword.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(s)) {
                    mCleanPassword.setVisibility(View.GONE);
                }
                if (s.toString().isEmpty())
                    return;
                if (!s.toString().matches("[A-Za-z0-9]+")) {
                    String temp = s.toString();
                    Toast.makeText(getApplicationContext(), R.string.please_input_limit_pwd, Toast.LENGTH_SHORT).show();
                    s.delete(temp.length() - 1, temp.length());
                    mEtPassword.setSelection(s.length());
                }
            }
        });
    }
    /**
     * 缩小
     *
     * @param view
     */
    public void zoomIn(final View view, float dist) {
        view.setPivotY(view.getHeight());
        view.setPivotX(view.getWidth() / 2);
        AnimatorSet mAnimatorSet = new AnimatorSet();
        ObjectAnimator mAnimatorScaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, scale);
        ObjectAnimator mAnimatorScaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, scale);
        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(view, "translationY", 0.0f, -dist);

        mAnimatorSet.play(mAnimatorTranslateY).with(mAnimatorScaleX).with(mAnimatorScaleY);

        mAnimatorSet.setDuration(300);
        mAnimatorSet.start();

    }

    /**
     * f放大
     *
     * @param view
     */
    public void zoomOut(final View view) {
        if (view.getTranslationY()==0){
            return;
        }
        view.setPivotY(view.getHeight());
        view.setPivotX(view.getWidth() / 2);
        AnimatorSet mAnimatorSet = new AnimatorSet();

        ObjectAnimator mAnimatorScaleX = ObjectAnimator.ofFloat(view, "scaleX", scale, 1.0f);
        ObjectAnimator mAnimatorScaleY = ObjectAnimator.ofFloat(view, "scaleY", scale, 1.0f);
        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(view, "translationY", view.getTranslationY(), 0);

        mAnimatorSet.play(mAnimatorTranslateY).with(mAnimatorScaleX).with(mAnimatorScaleY);
        mAnimatorSet.setDuration(300);
        mAnimatorSet.start();

    }


    @Override
    public void onSoftKeyboardOpened(int keyboardSize) {
        Log.e("wenzhihao", "----->show" + keyboardSize);
        int[] location = new int[2];
       mBody.getLocationOnScreen(location); //获取body在屏幕中的坐标,控件左上角
        int x = location[0];
        int y = location[1];
        Log.e("wenzhihao","y = "+y+",x="+x);
        int bottom = screenHeight - (y+mBody.getHeight()) ;
        Log.e("wenzhihao","bottom = "+bottom);
        Log.e("wenzhihao","con-h = "+mBody.getHeight());
        if (keyboardSize > bottom){
            ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(body, "translationY", 0.0f, -(keyboardSize - bottom));
            mAnimatorTranslateY.setDuration(300);
            mAnimatorTranslateY.setInterpolator(new AccelerateDecelerateInterpolator());
            mAnimatorTranslateY.start();
            zoomIn(mLogo, keyboardSize - bottom);

        }
    }

    @Override
    public void onSoftKeyboardClosed() {
        Log.e("wenzhihao", "----->hide");
        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(mBody, "translationY", mBody.getTranslationY(), 0);
        mAnimatorTranslateY.setDuration(300);
        mAnimatorTranslateY.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimatorTranslateY.start();
        zoomOut(mLogo);
    }
    private boolean flag = false;
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_login:
                login();
            case R.id.iv_clean_phone:
                mEtMobile.setText("");
                break;
            case R.id.clean_password:
                mEtPassword.setText("");
                break;
            case R.id.iv_show_pwd:
                if(flag == true){
                    mEtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mIvShowPwd.setImageResource(R.drawable.pass_gone);
                    flag = false;
                }else{
                    mEtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mIvShowPwd.setImageResource(R.drawable.pass_visuable);
                    flag = true;
                }
                String pwd = mEtPassword.getText().toString();
                if (!TextUtils.isEmpty(pwd))
                    mEtPassword.setSelection(pwd.length());
                break;
        }
    }

    private void login() {
        //发送登录请求,获取登录的Cookie

        //输入校验
        String paw = mEtPassword.getText().toString().trim();
        String mob = mEtMobile.getText().toString().trim();

        if(TextUtils.isEmpty(mob)){
            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
            return;
        }if (TextUtils.isEmpty(paw)){
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (paw.equals("6666")) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            mEtPassword.setText("");
            Toast.makeText(this, "密码或账号名不正确", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        keyboardWatcher.removeSoftKeyboardStateListener(this);
    }
}
