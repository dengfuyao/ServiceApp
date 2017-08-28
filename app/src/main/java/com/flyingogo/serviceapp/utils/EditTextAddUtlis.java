package com.flyingogo.serviceapp.utils;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import static android.content.ContentValues.TAG;

/**
 * 作者：dfy on 24/8/2017 16:26
 * <p> 输入框为空时隐藏指定的控件
 * 邮箱：dengfuyao@163.com
 */

public  class EditTextAddUtlis implements TextWatcher {

    private static EditTextAddUtlis editTextAddUtlis = null;
    private static EditText editText;
    private static View view;
    private EditTextAddUtlis(EditText editText,View view) {
        this.view = view;
        this.editText = editText;
    }
    public static EditTextAddUtlis getInstance(EditText editText,View hideView){
        /*if (editTextAddUtlis==null){
            synchronized (EditTextAddUtlis.class){
                if (editTextAddUtlis==null){
                    editTextAddUtlis = new EditTextAddUtlis(editText,hideView);
                }
            }
        }*/
        return new EditTextAddUtlis(editText,hideView);
    }

    public void hideView(){
        editText.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!TextUtils.isEmpty(s) && view.getVisibility() == View.GONE) {
            Log.e(TAG, "afterTextChanged: s =:"+s.toString() );
            view.setVisibility(View.VISIBLE);
        } else if (TextUtils.isEmpty(s)) {
            view.setVisibility(View.GONE);
        }
    }
}
