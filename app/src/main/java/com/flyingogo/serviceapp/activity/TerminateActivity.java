package com.flyingogo.serviceapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flyingogo.serviceapp.R;
import com.flyingogo.serviceapp.bean.CardBean;
import com.flyingogo.serviceapp.bean.ListCardBean;
import com.flyingogo.serviceapp.bean.TerminationCardBean;
import com.flyingogo.serviceapp.interfaces.Constant;
import com.flyingogo.serviceapp.utils.URLUtils;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 作者：dfy on 23/8/2017 14:15
 * <p>
 * 邮箱：dengfuyao@163.com
 */
public class TerminateActivity extends AppCompatActivity {


    private static final int SLOT_CAED_TERMINATION = 10010;  //刷卡注销跳转
    @BindView(R.id.createDate)
    TextView     mCreateDate;
    @BindView(R.id.sequence_no)
    TextView     mSequenceNo;
    @BindView(R.id.card_cid)
    TextView     mCardCid;
    @BindView(R.id.card_state)
    TextView     mCardState;
    @BindView(R.id.card_no)
    TextView     mCardNo;
    @BindView(R.id.card_deposit)
    TextView     mCardDeposit;
    @BindView(R.id.card_balance)
    TextView     mCardBalance;
    @BindView(R.id.realName)
    TextView     mRealName;
    @BindView(R.id.mobilePhone)
    TextView     mMobilePhone;
    @BindView(R.id.termination)
    Button       mTermination;
    @BindView(R.id.ll_lock)
    LinearLayout mLlLock;
    @BindView(R.id.go_back)
    Button       mGoBack;
    @BindView(R.id.bt_go_back)
    Button       mBtGoBack;
    @BindView(R.id.container)
    LinearLayout mContainer;
    @BindView(R.id.bt_resume)
    Button       mBtResume;
    private String mState;
    private int    mCardState1;
    private int    mId;
    private String mUserId;
    private String mCardid;
    private String mCardId;
    private Gson mGson = new Gson();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treminatio_card);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        ListCardBean.DataBean bean = (ListCardBean.DataBean) intent.getSerializableExtra(Constant.CARD_DETAILS);
        if (bean != null) {
            Log.e(TAG, "onCreate: 点击条目专递过来的bean" );
            initData(bean);
            initActionBar();
        } else {
            //重新刷卡获取 数据
            mContainer.setVisibility(View.GONE);
            intent = new Intent(getApplicationContext(), ReadCardActivity.class);
            startActivityForResult(intent, SLOT_CAED_TERMINATION);
            initActionBar();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ReadCardActivity.RESULT_OK) {
            if (requestCode == SLOT_CAED_TERMINATION) {
                mCardId = data.getStringExtra(Constant.CARD_NO);
                String url = URLUtils.getCardInfoUrl(mCardId);
                getCardDetails(url);
                mContainer.setVisibility(View.VISIBLE);
            }
        }
    }

    private void getCardDetails(String url) {
        Log.e(TAG, "bindView: 查询卡的URL = " + url);
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(TAG, "onError: 查询卡失败+" + e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                CardBean bean = mGson.fromJson(response.toString(), CardBean.class);
                Log.e(TAG, "onResponse: bean.state = " + bean.state);
                if (bean.state == 1) {
                   /* mCardNo.setText(bean.data.cardNo);
                    mCardBalance.setText(String.valueOf(bean.data.cardBalance));
                    mCardDeposit.setText(String.valueOf(bean.data.cardDeposit));
                    mRealName.setText(bean.data.realName);
                    mMobilePhone.setText(bean.data.mobilePhone);*/
                    initUpData(bean);
                } else if (bean.state == -100) {
                    mCardState.setText("该卡未签约不能充值");
                    mCardState.setTextColor(Color.RED);

                }
            }
        });
    }

    private void initData(ListCardBean.DataBean bean) {

        if (bean != null) {
            mId = bean.id;
            mCardid = bean.cardCid;
            mUserId = bean.idCard;
            bindView(bean);
        }
    }

    private void bindView(ListCardBean.DataBean bean) {
        mSequenceNo.setText(String.valueOf(bean.id));
        mCardCid.setText(bean.cardCid);
        mCardBalance.setText(bean.cardBalance + "");
        mCardDeposit.setText(bean.cardDeposit + "");
        mCardNo.setText(bean.cardNo);
        mCardState1 = bean.cardState;
        //卡片状态（0正常,1借车中,2异常 3已解约 4 已挂失）
        mState = "";
        switch (mCardState1) {
            case 0:
                mState = "正常";
                break;
            case 1:
                mState = "借车中";
                break;
            case 2:
                mState = "异常";
                break;
            case 3:
                mState = "已解约";
                break;
            case 4:
                mState = "已挂失";
                break;
            default:
                break;
        }
        mCardState.setText(mState);
        mRealName.setText(bean.realName);
        mMobilePhone.setText(bean.mobilePhone);
        mCreateDate.setText(bean.createDate);
    }

    private void showUnlock() {

    }

    private void showLock() {
    }

    @OnClick({R.id.termination, R.id.go_back,R.id.bt_resume,R.id.bt_go_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.termination:  //注销卡
                if (mCardState1 != 3 || mCardState1 != 4) {
                    if (mUserId != null || mCardid != null) {

                        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                        dialog.setTitle("注销此卡");
                        dialog.setMessage("卡注销后将不能继续使用,你确定要注销么?");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String terminationCardUrl = URLUtils.getTerminationCardUrl(mCardid, mUserId);
                                Log.e(TAG, "onClick: caard = "+mCardid+" 身份证号 = "+mUserId );
                                terminationCard(terminationCardUrl);
                            }
                        }).setNegativeButton("返回", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();

                    }
                } else {
                    Toast.makeText(this, "请查看卡状态", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bt_go_back:
            case R.id.go_back:
                finish();
                break;
            case R.id.bt_resume:
                Intent intent ;
                intent = new Intent(getApplicationContext(), ReadCardActivity.class);
                startActivityForResult(intent, SLOT_CAED_TERMINATION);
                break;
        }
    }

    private static final String TAG = "CardDetailsActivity";

    private void terminationCard(String url) {
        Log.e(TAG, "terminationCard:注销 = " + url);
        OkHttpUtils.post().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                Gson gson = new Gson();
                TerminationCardBean reportTheLossBean = gson.fromJson(response.toString(), TerminationCardBean.class);
                if (reportTheLossBean != null) {
                    int state = reportTheLossBean.state;
                    mCardState.setText(reportTheLossBean.message);
                    Log.e(TAG, "onResponse: res = " + state);
                    //-10002 参数异常  -1001 客服人员不存在 -1002 客服人员状态异常 -101 卡片不存在
                    // -102 卡片已经解约 -103 卡片已挂失 -103 挂失失败 1 成功
                    switch (state) {
                        case 1:
                            //刷新UI;重新加载一次数据;
                            upData();
                            break;

                    }
                }
            }
        });

    }

    /**
     * 重新加载数据获取卡 信息
     */
    private void upData() {
        String cardInfoUrl = URLUtils.getCardInfoUrl(mCardid);
        Log.e(TAG, "upData: 刷新界面的URL = " + cardInfoUrl);
        OkHttpUtils.post().url(cardInfoUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(String response, int id) {
                Log.e(TAG, "onResponse: 刷新ui");
                Gson gson = new Gson();
                CardBean cardBean = gson.fromJson(response.toString(), CardBean.class);
                initUpData(cardBean);

            }
        });

    }

    private void initUpData(CardBean cardBean) {

        if (cardBean != null) {
            mId = cardBean.data.id;
            mCardid = cardBean.data.cardCid;
            mUserId = cardBean.data.idCard;
            mContainer.setVisibility(View.VISIBLE);
            bindview(cardBean);
        }
    }

    private void bindview(CardBean cardBean) {
        mSequenceNo.setText(cardBean.data.id + "");
        mCardCid.setText(cardBean.data.cardCid);
        mCardBalance.setText(cardBean.data.cardBalance + "");
        mCardDeposit.setText(cardBean.data.cardDeposit + "");
        mCardNo.setText(cardBean.data.cardNo);
        mCardState1 = cardBean.data.cardState;
        //卡片状态（0正常,1借车中,2异常 3已解约 4 已挂失）
        mState = "";
        switch (mCardState1) {
            case 0:
                mState = "正常";
                showLock();
                break;
            case 1:
                showLock();
                mState = "借车中";
                break;
            case 2:
                showLock();
                mState = "异常";
                break;
            case 3:
                showLock();
                mState = "已解约";
                break;
            case 4:
                showUnlock();
                mState = "已挂失";
                break;
            default:
                break;
        }
        mCardState.setText(mState);
        mRealName.setText(cardBean.data.realName);
        mMobilePhone.setText(cardBean.data.mobilePhone);
        mCreateDate.setText(cardBean.data.createDate);
    }

    private void initActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("刷卡注销");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //点击back键finish当前activity
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }


}
