package com.flyingogo.serviceapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.flyingogo.serviceapp.bean.ReportTheLossBean;
import com.flyingogo.serviceapp.interfaces.Constant;
import com.flyingogo.serviceapp.utils.ThreadUtils;
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
public class CardDetailsActivity extends AppCompatActivity {

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
    @BindView(R.id.bt_cancel)
    Button       mBtCancel;
    @BindView(R.id.lock)
    Button       mLock;
    @BindView(R.id.ll_lock)
    LinearLayout mLlLock;
    @BindView(R.id.go_back)
    Button       mGoBack;
    @BindView(R.id.unlock)
    Button       mUnlock;
    @BindView(R.id.ll_unlock)
    LinearLayout mLlUnlock;
    private String mState;
    private int    mCardState1;
    private int    mId;
    private String mUserId;
    private String mCardid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_list_item);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        ListCardBean.DataBean bean = (ListCardBean.DataBean) intent.getSerializableExtra(Constant.CARD_DETAILS);
        if (bean!=null) {
            initData(bean);
            initActionBar();
        }else {
            Log.e(TAG, "onCreate: intent = null" );

        }
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
        mRealName.setText(bean.realName);
        mMobilePhone.setText(bean.mobilePhone);
        mCreateDate.setText(bean.createDate);
    }

    private void showUnlock() {
        mLlUnlock.setVisibility(View.VISIBLE);
        mLlLock.setVisibility(View.GONE);
    }

    private void showLock() {
        mLlUnlock.setVisibility(View.GONE);
        mLlLock.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.lock, R.id.unlock, R.id.bt_cancel, R.id.go_back})
    public void onClick(View view) {
        Log.e(TAG, "onClick: mCardState1 == "+mCardState1 );
        switch (view.getId()) {

            case R.id.lock:  //挂失
                if (mCardState1 != 3 && mCardState1 != 4) {

                    if (mUserId != null || mId != 0) {
                        String reportedLossCardUrl = URLUtils.getReportedLossCardUrl(mId, mUserId);
                        lockCard(reportedLossCardUrl);
                    }else{
                        Toast.makeText(this, "无效卡", Toast.LENGTH_SHORT).show();
                   }
                } else if (mCardState1==3){
                    ThreadUtils.onRunUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "挂失失败!请查看卡状态", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                break;
            case R.id.unlock:  //解挂

                if (mCardState1 == 4) {
                    if (mUserId != null || mId != 0) {
                        String solutionToHangCardUrl = URLUtils.getSolutionToHangCardUrl(mId, mUserId);
                        lockCard(solutionToHangCardUrl);
                    }
                }
                break;
            case R.id.go_back:
            case R.id.bt_cancel:
                finish();
                break;

        }
    }

    private static final String TAG = "CardDetailsActivity";

    private void lockCard(String url) {
        Log.e(TAG, "lockCard:挂失/解挂URL = " + url);
        OkHttpUtils.post().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                Gson gson = new Gson();
                ReportTheLossBean reportTheLossBean = gson.fromJson(response.toString(), ReportTheLossBean.class);
                int state = reportTheLossBean.state;
                Log.e(TAG, "onResponse: res = " + state);
                //-10002 参数异常  -1001 客服人员不存在 -1002 客服人员状态异常 -101 卡片不存在
                // -102 卡片已经解约 -103 卡片已挂失 -103 挂失失败 1 成功
                switch (state) {
                    case -10002:
                        break;
                    case -1001:
                        break;
                    case -1002:
                        break;
                    case -101:

                        break;
                    case -102:

                        break;
                    case -103:

                        break;
                    case 1:
                        //刷新UI;重新加载一次数据;

                        break;
                }
                upData();
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
