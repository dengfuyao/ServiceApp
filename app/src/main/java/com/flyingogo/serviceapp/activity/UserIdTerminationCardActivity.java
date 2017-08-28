package com.flyingogo.serviceapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.flyingogo.serviceapp.R;
import com.flyingogo.serviceapp.adapter.CardNoListAdapter;
import com.flyingogo.serviceapp.bean.CardBean;
import com.flyingogo.serviceapp.bean.ListCardBean;
import com.flyingogo.serviceapp.interfaces.Constant;
import com.flyingogo.serviceapp.utils.EditTextAddUtlis;
import com.flyingogo.serviceapp.utils.ThreadUtils;
import com.flyingogo.serviceapp.utils.URLUtils;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 作者：dfy on 25/8/2017 15:18
 * <p>  无卡注销卡片;
 * 邮箱：dengfuyao@163.com
 */

public class UserIdTerminationCardActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {


    @BindView(R.id.input_userid)
    EditText mInputUserid;
    @BindView(R.id.query_card_no)
    Button   mQueryCardNo;
    @BindView(R.id.card_list)
    ListView mCardList;

    private List<ListCardBean.DataBean> datas;
    private String                      mCardListUrl;
    private List<ListCardBean.DataBean> mDatas;
    private CardNoListAdapter           mAdapter;
    private boolean isFalst = false;
    private String mUserId;
      Gson mGson = new Gson();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_unloconing);
        ButterKnife.bind(this);
        initActionBar();
        initListView();
    }

    private void initActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("卡查询");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initListView() {
        mDatas = new ArrayList<>();
        // mAdapter = new CardListAdapter(getContext(),mDatas,this);
        mAdapter = new CardNoListAdapter(this, mDatas);
        mCardList.setAdapter(mAdapter);
        mCardList.setOnItemClickListener(this);
        EditTextAddUtlis.getInstance(mInputUserid, mQueryCardNo).hideView(); //输入监听;
    }


    @OnClick(R.id.query_card_no)
    public void onClick() {

        mDatas.clear();
        mUserId = mInputUserid.getText().toString().trim();
        if (TextUtils.isEmpty(mUserId)) {
            Toast.makeText(getApplicationContext(), "请输入完整身份信息", Toast.LENGTH_SHORT).show();
            return;
        }
        mCardListUrl = URLUtils.getCardListUrl(mUserId);
        queryCardList(mCardListUrl);
    }

    private static final String TAG = "UserIdTerminationCardAc";
    private void queryCardList(String cardListUrl) {
        Log.e(TAG, "queryCardList: 获取卡列表的URL = " + cardListUrl);
        OkHttpUtils.post().url(cardListUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(TAG, "onError: 获取卡列表异常 =" + e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                Log.e(TAG, "onResponse:  = " + response.toString());
                ListCardBean listCardBean = mGson.fromJson(response.toString(), ListCardBean.class);
                if (listCardBean.state == 1) {
                    Log.e(TAG, "onResponse: 获取数据成功");
                    List<ListCardBean.DataBean> data = listCardBean.data;
                    Log.e(TAG, "onResponse: data .size = " + data.size());
                    if (data.size() == 0) {
                        //显示没有开卡
                        showDialog();

                    } else {
                        for (ListCardBean.DataBean bean : data) {
                            CardBean cardBean = bean;
                            mDatas.add(bean);
                        }
                        ThreadUtils.onRunUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.notifyDataSetChanged();
                                Log.e(TAG, "run: dataSize = " + mDatas.size());
                            }
                        });
                    }

                }
            }
        });

    }

    private void showDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("卡信息").setMessage("此用户没有开卡信息,请确保身份证信息是否正确!");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ListCardBean.DataBean bean = mDatas.get(position);
        Intent intent = new Intent(getApplicationContext(), TerminateActivity.class);
        intent.putExtra(Constant.CARD_DETAILS, bean);
        startActivity(intent);

    }

}
