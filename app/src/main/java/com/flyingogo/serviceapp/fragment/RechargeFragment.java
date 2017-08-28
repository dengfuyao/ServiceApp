package com.flyingogo.serviceapp.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.flyingogo.serviceapp.R;
import com.flyingogo.serviceapp.activity.ReadCardActivity;
import com.flyingogo.serviceapp.activity.RechargeActivity;
import com.flyingogo.serviceapp.base.BaseFragment;
import com.flyingogo.serviceapp.bean.CardBean;
import com.flyingogo.serviceapp.interfaces.Constant;
import com.flyingogo.serviceapp.utils.URLUtils;
import com.flyingogo.serviceapp.weiget.RadioGroupEx;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 作者：dfy on 18/8/2017 10:42
 * <p>充值fragment
 * 邮箱：dengfuyao@163.com
 */
public class RechargeFragment extends BaseFragment {
    @BindView(R.id.recharge_type)
    TextView     mRechargeType;
    @BindView(R.id.alipay_recharge)
    RadioButton  mAlipayRecharge;
    @BindView(R.id.wechat_recharge)
    RadioButton  mWechatRecharge;
    @BindView(R.id.radioGroup_normal)
    RadioGroup   mRadioGroupNormal;
    @BindView(R.id.recharge_10)
    RadioButton  mRecharge10;
    @BindView(R.id.recharge_30)
    RadioButton  mRecharge30;
    @BindView(R.id.recharge_50)
    RadioButton  mRecharge50;
    @BindView(R.id.recharge_100)
    RadioButton  mRecharge100;
    @BindView(R.id.recharge_200)
    RadioButton  mRecharge200;
    @BindView(R.id.radioGroup)
    RadioGroupEx mRadioGroup;
    @BindView(R.id.bt_slotcard)
    Button       mBtSlotcard;
    @BindView(R.id.recharge_details)
    LinearLayout mRechargeDetails;
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
    @BindView(R.id.open_card_statue)
    TextView     mOpenCardStatue;
    @BindView(R.id.recharge_bt)
    Button       mRechargeBt;
    @BindView(R.id.recharge_cancel)
    Button       mRechargeCancel;
    @BindView(R.id.input_card_info)
    LinearLayout mInputCardInfo;
    private String mCardId;
    private int    rechargeMethod = 2;
    private double rechargeAmount = 0.1;

    @Override
    public int getResLayoutId() {
        return R.layout.fragment_recharge;
    }

    @Override
    protected void lazyLoad() {
        mRadioGroup.setOnCheckedChangeListener(rechargeAmountListener);
        mRadioGroupNormal.setOnCheckedChangeListener(rechargeMethodListener);
    }

    private RadioGroup.OnCheckedChangeListener rechargeAmountListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.recharge_10:
                    rechargeAmount = 0.010;
                    break;
                case R.id.recharge_30:
                    rechargeAmount = 30;
                    break;
                case R.id.recharge_50:
                    rechargeAmount = 50;
                    break;
                case R.id.recharge_100:
                    rechargeAmount = 100;
                    break;
                case R.id.recharge_200:
                    rechargeAmount = 200;
                    break;

            }
        }
    };
    private RadioGroup.OnCheckedChangeListener rechargeMethodListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.wechat_recharge:
                    rechargeMethod = 1;  //微信充值
                    break;
                case R.id.alipay_recharge:
                    rechargeMethod = 2;  //支付宝
                    break;
            }
        }
    };

    private void go2SlotCard() {
        Intent intent = new Intent(getContext(), ReadCardActivity.class);
        startActivityForResult(intent, Constant.SLOT_CAED_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == ReadCardActivity.RESULT_OK) {
            if (requestCode == Constant.SLOT_CAED_CODE) {   //刷卡返回数据
                mCardId = data.getStringExtra(Constant.CARD_NO);
                Log.e(TAG, "onActivityResult: card = " + mCardId);
                showRechargeDetails(mCardId);
            } else if (requestCode == Constant.RECHARGE_CODE) {  //充值返回数据
                boolean rechargeResult = data.getBooleanExtra(Constant.RECHARGE_RESULT, false);
                getCardDetails(mCardId);
                mRechargeBt.setText("继续充值");
                mRechargeCancel.setText(getResources().getString(R.string.complete));
                Log.e(TAG, "onActivityResult: 充值完成 =" + rechargeResult);
            }
        }
    }

    private void showRechargeDetails(String cardId) {
        mInputCardInfo.setVisibility(View.VISIBLE);
        mRechargeDetails.setVisibility(View.GONE);
        getCardDetails(cardId);
    }

    /**
     * 查询卡信息;
     *
     * @param cardId
     */
    private void getCardDetails(String cardId) {
        String url = URLUtils.getCardInfoUrl(mCardId);
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
                    mCardNo.setText(bean.data.cardNo);
                    mCardBalance.setText(String.valueOf(bean.data.cardBalance));
                    mCardDeposit.setText(String.valueOf(bean.data.cardDeposit));
                    mRealName.setText(bean.data.realName);
                    mMobilePhone.setText(bean.data.mobilePhone);
                } else if (bean.state == -100) {
                    mOpenCardStatue.setText("该卡未签约不能充值");
                    mOpenCardStatue.setTextColor(Color.RED);
                    mRechargeBt.setVisibility(View.GONE);
                }
            }
        });
    }

    @OnClick({R.id.bt_slotcard, R.id.recharge_bt, R.id.recharge_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_slotcard:   //跳转到刷卡界面
                Log.e(TAG, "onClick: rechargeMethod = "+rechargeMethod +" 充值金额等于 ;"+rechargeAmount );
                go2SlotCard();
                break;
            case R.id.recharge_bt:  //充值按钮
                go2Recharge();
                break;
            case R.id.recharge_cancel:  //取消充值;
                mInputCardInfo.setVisibility(View.GONE);
                mRechargeDetails.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void go2Recharge() {
        Intent intent = new Intent(getContext(), RechargeActivity.class);
        intent.putExtra(Constant.RECHARGE_TYPE, Constant.RECHARGE_VALUE_BALANCE);//余额充值;
        intent.putExtra(Constant.CARD_NO, mCardId);//卡芯片号
        intent.putExtra(Constant.PAYTYPE, rechargeMethod);  //
        intent.putExtra(Constant.RECHARGE_AMOUNT, rechargeAmount);
        startActivityForResult(intent, Constant.RECHARGE_CODE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            Log.e(TAG, "setUserVisibleHint: 充值获取焦点" );

            mRechargeDetails.setVisibility(View.VISIBLE);
            mInputCardInfo.setVisibility(View.GONE);
        }else{
            Log.e(TAG, "setUserVisibleHint: 充值失去焦点" );
        }
    }
}
