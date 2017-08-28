package com.flyingogo.serviceapp.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flyingogo.serviceapp.R;
import com.flyingogo.serviceapp.activity.ReadCardActivity;
import com.flyingogo.serviceapp.activity.RechargeActivity;
import com.flyingogo.serviceapp.adapter.MyAdapter;
import com.flyingogo.serviceapp.base.BaseFragment;
import com.flyingogo.serviceapp.bean.CardBean;
import com.flyingogo.serviceapp.bean.CashBean;
import com.flyingogo.serviceapp.bean.NewCardBean;
import com.flyingogo.serviceapp.interfaces.Constant;
import com.flyingogo.serviceapp.libs.CustomPopWindow;
import com.flyingogo.serviceapp.utils.URLUtils;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 作者：dfy on 18/8/2017 10:28
 * <p>
 * 邮箱：dengfuyao@163.com
 */
public class DredageFragment extends BaseFragment  {

    private static final int    REQUEST_CODE    = 100;
    public static final  String CARD_NO         = "card_no";
    private static final int    WECHAT_RECHARGE = 0;
    private static final int    ALIPAY_RECHARGE = 1;
    private static final int    MONEY_RECHARGE  = 2;
    private static final int    RECHARGE_CODE   = 200; //充值结果查询;
    @BindView(R.id.input_name)
    EditText       mInputName;
    @BindView(R.id.input_userid)
    EditText       mInputUserid;
    @BindView(R.id.input_phone_no)
    EditText       mInputPhoneNo;
    @BindView(R.id.bt_open_card)
    Button         mBtOpenCard;
    @BindView(R.id.input_table)
    LinearLayout   mInputTable;
    @BindView(R.id.card_uid)
    TextView       mCardUid;
    @BindView(R.id.bt_recharge_cash)
    Button         mBtRechargeCash;
    @BindView(R.id.bt_cancel)
    Button         mBtCancel;
    @BindView(R.id.complete)
    Button         mComplete;
    @BindView(R.id.recharge_cash_pledge)
    LinearLayout   mRechargeCashPledge;
    @BindView(R.id.card_deposit)
    TextView       mCardDeposit;
    @BindView(R.id.card_balance)
    TextView       mCardBalance;
    @BindView(R.id.realName)
    TextView       mRealName;
    @BindView(R.id.mobilePhone)
    TextView       mMobilePhone;
    @BindView(R.id.open_card_statue)
    TextView       mOpenCardStatue;
    @BindView(R.id.relative_butt)
    RelativeLayout mRelativeButt;
    private String          mName;
    private String          mUserId;
    private String          mPhone_no;
    private CustomPopWindow mListPopWindow;
    private String          mCardId;  //芯片号
    private String          mCardNo;  //卡号
    private double deposit = -1; //押金;
    private int mPayType;

    @Override
    public int getResLayoutId() {
        return R.layout.fragment_dredage;
    }

    @Override
    protected void lazyLoad() {

    }

    @OnClick(R.id.bt_open_card)
    public void onClick() {
        mName = mInputName.getText().toString().trim();
        mUserId = mInputUserid.getText().toString().trim();
        mPhone_no = mInputPhoneNo.getText().toString().trim();
        if (TextUtils.isEmpty(mName)) {
            Toast.makeText(getContext(), "姓名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(mUserId)) {
            Toast.makeText(getContext(), "证件信息不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(mPhone_no)) {
            Toast.makeText(getContext(), "手机号码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(getContext(), ReadCardActivity.class);
        startActivityForResult(intent, REQUEST_CODE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == ReadCardActivity.RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                mCardId = data.getStringExtra(CARD_NO);
                Log.e(TAG, "onActivityResult: " + mCardId);
                mInputTable.setVisibility(View.GONE);
                mRechargeCashPledge.setVisibility(View.VISIBLE);
                mCardUid.setText(mCardId);
                dredagCard(mCardId);  //开卡

            } else if (requestCode == RECHARGE_CODE) {

                boolean rechargeStatue = data.getBooleanExtra(Constant.RECHARGE_RESULT, false);
                Log.e(TAG, "onActivityResult: restatue = " + rechargeStatue);
                if (rechargeStatue) {
                    Log.e(TAG, "onActivityResult: 开卡成功");
                    upDataCard();
                    mBtRechargeCash.setVisibility(View.GONE);
                    mBtCancel.setVisibility(View.GONE);
                    mComplete.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    /**
     * 开卡
     *
     * @param card
     */
    private void dredagCard(String card) {
        String newCardUrl = URLUtils.getSignedCardInfoUrl(card, mUserId, mPhone_no, mName);
        Log.e(TAG, "dredagCard: url = " + newCardUrl);
        OkHttpUtils.get().url(newCardUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(TAG, "onError:  = " + e.getMessage());
                Toast.makeText(getContext(), "网络错误开卡失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                Log.e(TAG, "onResponse: reponse = " + response.toString());
                NewCardBean newCardBean = mGson.fromJson(response.toString(), NewCardBean.class);
                bindView(newCardBean);
            }
        });
    }

    private void bindView(NewCardBean bean) {
        if (bean.state == 1) {
            mCardUid.setText(bean.data.cardNo);
            mCardBalance.setText(String.valueOf(bean.data.cardBalance));
            mCardDeposit.setText(String.valueOf(bean.data.cardDeposit));
            mRealName.setText(bean.data.realName);
            mMobilePhone.setText(bean.data.mobilePhone);
            mOpenCardStatue.setText(getResources().getString(R.string.open_card_succeed));
            mOpenCardStatue.setTextColor(Color.GREEN);
        } else if (bean.state == -1000) {
            mOpenCardStatue.setText(getResources().getString(R.string.card_exist));
            mOpenCardStatue.setTextColor(Color.RED);
            upDataCard();
        }
    }

    private void upDataCard() {
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
                if (bean.state == 1) {
                    mCardUid.setText(bean.data.cardNo);
                    mCardBalance.setText(String.valueOf(bean.data.cardBalance));
                    mCardDeposit.setText(String.valueOf(bean.data.cardDeposit));
                    mRealName.setText(bean.data.realName);
                    mMobilePhone.setText(bean.data.mobilePhone);
                    if (bean.data.cardDeposit>0){
                        mBtRechargeCash.setVisibility(View.GONE);
                    }
                }

            }
        });
    }

    @OnClick({R.id.bt_recharge_cash, R.id.bt_cancel, R.id.complete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_recharge_cash:  //充值押金
                showPopListView();  //弹出popwindow
                break;
            case R.id.complete:  //完成;
                mInputTable.setVisibility(View.VISIBLE);
                mRechargeCashPledge.setVisibility(View.GONE);
                break;
            case R.id.bt_cancel:
                mInputTable.setVisibility(View.VISIBLE);
                mRechargeCashPledge.setVisibility(View.GONE);
                break;
        }
    }

    //弹出popwindow;
    private void showPopListView() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.pop_list, null);
        //处理popWindow 显示内容
        initPopWinDoList(contentView);
        //创建并显示popWindow
        mListPopWindow = new CustomPopWindow.PopupWindowBuilder(getContext())
                .setView(contentView)
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)//显示大小
                .create()
                .showAsDropDown(mBtRechargeCash, 0, 20);
    }

    /**
     * 初始化popwindow的list数据
     *
     * @param contentView
     */
    private void initPopWinDoList(View contentView) {
        ListView listView = (ListView) contentView.findViewById(R.id.listView);
        MyAdapter adapter = new MyAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mListPopWindow != null) {
                    mListPopWindow.dissmiss();
                }
                switch (position) {
                    case WECHAT_RECHARGE:  //微信充值
                        mPayType = 1;
                        break;
                    case ALIPAY_RECHARGE:  //支付宝充值
                        //跳到充值界面,
                        mPayType = 2;
                        getPayDepositAmount();
                        break;
                    case MONEY_RECHARGE:   //现金充值
                        mPayType = 3;
                        break;
                    default:
                        break;
                }

            }
        });
    }

    /**
     * 获取要充值的押金金额; 跳转到充值押金界面
     */
    private void getPayDepositAmount() {
        String cashUrl = URLUtils.getPayDepositAmountUrl();
        OkHttpUtils.get().url(cashUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(getContext(), "网络错误稍后再试" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                Gson gson = new Gson();
                CashBean cashBean = gson.fromJson(response.toString(), CashBean.class);
                deposit = cashBean.data;
                Log.e(TAG, "onResponse: deposit = " + deposit);
                Intent intent = new Intent(getContext(), RechargeActivity.class);
                intent.putExtra(Constant.RECHARGE_TYPE, Constant.RECHARGE_VALUE_DEPOSIT);
                intent.putExtra(Constant.CARD_NO, mCardId); //卡号;
                Log.e(TAG, "onResponse: payType = " + mPayType);
                intent.putExtra(Constant.PAYTYPE, mPayType);
                intent.putExtra(Constant.CASH, deposit);//押金
                startActivityForResult(intent, RECHARGE_CODE);
            }
        });
    }

}
