package com.flyingogo.serviceapp.libs;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * 作者：dfy on 21/8/2017 09:15
 * <p>
 * 邮箱：dengfuyao@163.com
 */

public class CustomPopWindow implements PopupWindow.OnDismissListener {
    private static final String TAG = "CustomPopWindow";
    private static final float DEFAULT_ALPHA = 0.7F;
    private Context                       mContext;
    private int                           mWidth;
    private int                           mHeight;
    private boolean                       mIsFocusable;
    private boolean                       mIsOutside;
    private int                           mResLayoutId;
    private View                          mContentView;
    private PopupWindow                   mPopupWindow;
    private int                           mAnimationStyle;
    private boolean                       mClippEnable;
    private boolean                       mIgnoreCheekPress;
    private int                           mInputMode;
    private PopupWindow.OnDismissListener mOnDismissListener;
    private int                           mSoftInputMode;
    private boolean                       mTouchable;
    private View.OnTouchListener          mOnTouchListener;
    private Window                        mWindow;
    private boolean                       mIsBackgroundDark;
    private float                         mBackgroundDrakValue;
    private boolean                       enableOutsideTouchDisMiss;

    private CustomPopWindow(Context context,Context context1) {
        this.mIsFocusable = true;
        this.mIsOutside = true;
        this.mResLayoutId = -1;
        this.mAnimationStyle = -1;
        this.mClippEnable = true;
        this.mIgnoreCheekPress = false;
        this.mInputMode = -1;
        this.mSoftInputMode = -1;
        this.mTouchable = true;
        this.mIsBackgroundDark = false;
        this.mBackgroundDrakValue = 0.0F;
        this.enableOutsideTouchDisMiss = true;
        this.mContext = context;
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public CustomPopWindow showAsDropDown(View anchor, int xOff, int yOff) {
        if(this.mPopupWindow != null) {
            this.mPopupWindow.showAsDropDown(anchor, xOff, yOff);
        }

        return this;
    }

    public CustomPopWindow showAsDropDown(View anchor) {
        if(this.mPopupWindow != null) {
            this.mPopupWindow.showAsDropDown(anchor);
        }

        return this;
    }

    @RequiresApi(
            api = 19
    )
    public CustomPopWindow showAsDropDown(View anchor, int xOff, int yOff, int gravity) {
        if(this.mPopupWindow != null) {
            this.mPopupWindow.showAsDropDown(anchor, xOff, yOff, gravity);
        }

        return this;
    }

    public CustomPopWindow showAtLocation(View parent, int gravity, int x, int y) {
        if(this.mPopupWindow != null) {
            this.mPopupWindow.showAtLocation(parent, gravity, x, y);
        }

        return this;
    }

    private void apply(PopupWindow popupWindow) {
        popupWindow.setClippingEnabled(this.mClippEnable);
        if(this.mIgnoreCheekPress) {
            popupWindow.setIgnoreCheekPress();
        }

        if(this.mInputMode != -1) {
            popupWindow.setInputMethodMode(this.mInputMode);
        }

        if(this.mSoftInputMode != -1) {
            popupWindow.setSoftInputMode(this.mSoftInputMode);
        }

        if(this.mOnDismissListener != null) {
            popupWindow.setOnDismissListener(this.mOnDismissListener);
        }

        if(this.mOnTouchListener != null) {
            popupWindow.setTouchInterceptor(this.mOnTouchListener);
        }

        popupWindow.setTouchable(this.mTouchable);
    }

    private PopupWindow build() {
        if(this.mContentView == null) {
            this.mContentView = LayoutInflater.from(this.mContext).inflate(this.mResLayoutId, (ViewGroup)null);
        }

        Activity activity = (Activity)this.mContentView.getContext();
        if(activity != null && this.mIsBackgroundDark) {
            float alpha = this.mBackgroundDrakValue > 0.0F && this.mBackgroundDrakValue < 1.0F?this.mBackgroundDrakValue:0.7F;
            this.mWindow = activity.getWindow();
            WindowManager.LayoutParams params = this.mWindow.getAttributes();
            params.alpha = alpha;
            this.mWindow.addFlags(2);
            this.mWindow.setAttributes(params);
        }

        if(this.mWidth != 0 && this.mHeight != 0) {
            this.mPopupWindow = new PopupWindow(this.mContentView, this.mWidth, this.mHeight);
        } else {
            this.mPopupWindow = new PopupWindow(this.mContentView, -2, -2);
        }

        if(this.mAnimationStyle != -1) {
            this.mPopupWindow.setAnimationStyle(this.mAnimationStyle);
        }

        this.apply(this.mPopupWindow);
        if(this.mWidth == 0 || this.mHeight == 0) {
            this.mPopupWindow.getContentView().measure(0, 0);
            this.mWidth = this.mPopupWindow.getContentView().getMeasuredWidth();
            this.mHeight = this.mPopupWindow.getContentView().getMeasuredHeight();
        }

        this.mPopupWindow.setOnDismissListener(this);
        if(!this.enableOutsideTouchDisMiss) {
            this.mPopupWindow.setFocusable(true);
            this.mPopupWindow.setOutsideTouchable(false);
            this.mPopupWindow.setBackgroundDrawable((Drawable)null);
            this.mPopupWindow.getContentView().setFocusable(true);
            this.mPopupWindow.getContentView().setFocusableInTouchMode(true);
            this.mPopupWindow.getContentView().setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if(keyCode == 4) {
                        CustomPopWindow.this.mPopupWindow.dismiss();
                        return true;
                    } else {
                        return false;
                    }
                }
            });
            this.mPopupWindow.setTouchInterceptor(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    int x = (int)event.getX();
                    int y = (int)event.getY();
                    if(event.getAction() != 0 || x >= 0 && x < CustomPopWindow.this.mWidth && y >= 0 && y < CustomPopWindow.this.mHeight) {
                        if(event.getAction() == 4) {
                            Log.e("CustomPopWindow", "out side ...");
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        Log.e("CustomPopWindow", "out side ");
                        Log.e("CustomPopWindow", "width:" + CustomPopWindow.this.mPopupWindow.getWidth() + "height:" + CustomPopWindow.this.mPopupWindow.getHeight() + " x:" + x + " y  :" + y);
                        return true;
                    }
                }
            });
        } else {
            this.mPopupWindow.setFocusable(this.mIsFocusable);
            this.mPopupWindow.setBackgroundDrawable(new ColorDrawable(0));
            this.mPopupWindow.setOutsideTouchable(this.mIsOutside);
        }

        this.mPopupWindow.update();
        return this.mPopupWindow;
    }

    public void onDismiss() {
        this.dissmiss();
    }

    public void dissmiss() {
        if(this.mOnDismissListener != null) {
            this.mOnDismissListener.onDismiss();
        }

        if(this.mWindow != null) {
            WindowManager.LayoutParams params = this.mWindow.getAttributes();
            params.alpha = 1.0F;
            this.mWindow.setAttributes(params);
        }

        if(this.mPopupWindow != null && this.mPopupWindow.isShowing()) {
            this.mPopupWindow.dismiss();
        }

    }

    public PopupWindow getPopupWindow() {
        return this.mPopupWindow;
    }

    public static class PopupWindowBuilder {
        private CustomPopWindow mCustomPopWindow;

        public PopupWindowBuilder(Context context) {
            this.mCustomPopWindow = new CustomPopWindow(context, null);
        }

        public CustomPopWindow.PopupWindowBuilder size(int width, int height) {
            this.mCustomPopWindow.mWidth = width;
            this.mCustomPopWindow.mHeight = height;
            return this;
        }

        public CustomPopWindow.PopupWindowBuilder setFocusable(boolean focusable) {
            this.mCustomPopWindow.mIsFocusable = focusable;
            return this;
        }

        public CustomPopWindow.PopupWindowBuilder setView(int resLayoutId) {
            this.mCustomPopWindow.mResLayoutId = resLayoutId;
            this.mCustomPopWindow.mContentView = null;
            return this;
        }

        public CustomPopWindow.PopupWindowBuilder setView(View view) {
            this.mCustomPopWindow.mContentView = view;
            this.mCustomPopWindow.mResLayoutId = -1;
            return this;
        }

        public CustomPopWindow.PopupWindowBuilder setOutsideTouchable(boolean outsideTouchable) {
            this.mCustomPopWindow.mIsOutside = outsideTouchable;
            return this;
        }

        public CustomPopWindow.PopupWindowBuilder setAnimationStyle(int animationStyle) {
            this.mCustomPopWindow.mAnimationStyle = animationStyle;
            return this;
        }

        public CustomPopWindow.PopupWindowBuilder setClippingEnable(boolean enable) {
            this.mCustomPopWindow.mClippEnable = enable;
            return this;
        }

        public CustomPopWindow.PopupWindowBuilder setIgnoreCheekPress(boolean ignoreCheekPress) {
            this.mCustomPopWindow.mIgnoreCheekPress = ignoreCheekPress;
            return this;
        }

        public CustomPopWindow.PopupWindowBuilder setInputMethodMode(int mode) {
            this.mCustomPopWindow.mInputMode = mode;
            return this;
        }

        public CustomPopWindow.PopupWindowBuilder setOnDissmissListener(PopupWindow.OnDismissListener onDissmissListener) {
            this.mCustomPopWindow.mOnDismissListener = onDissmissListener;
            return this;
        }

        public CustomPopWindow.PopupWindowBuilder setSoftInputMode(int softInputMode) {
            this.mCustomPopWindow.mSoftInputMode = softInputMode;
            return this;
        }

        public CustomPopWindow.PopupWindowBuilder setTouchable(boolean touchable) {
            this.mCustomPopWindow.mTouchable = touchable;
            return this;
        }

        public CustomPopWindow.PopupWindowBuilder setTouchIntercepter(View.OnTouchListener touchIntercepter) {
            this.mCustomPopWindow.mOnTouchListener = touchIntercepter;
            return this;
        }

        public CustomPopWindow.PopupWindowBuilder enableBackgroundDark(boolean isDark) {
            this.mCustomPopWindow.mIsBackgroundDark = isDark;
            return this;
        }

        public CustomPopWindow.PopupWindowBuilder setBgDarkAlpha(float darkValue) {
            this.mCustomPopWindow.mBackgroundDrakValue = darkValue;
            return this;
        }

        public CustomPopWindow.PopupWindowBuilder enableOutsideTouchableDissmiss(boolean disMiss) {
            this.mCustomPopWindow.enableOutsideTouchDisMiss = disMiss;
            return this;
        }

        public CustomPopWindow create() {
            this.mCustomPopWindow.build();
            return this.mCustomPopWindow;
        }
    }
}