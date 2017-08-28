package com.flyingogo.serviceapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.flyingogo.serviceapp.R;
import com.flyingogo.serviceapp.bean.DepositBean;
import com.flyingogo.serviceapp.bean.RechargeResultBena;
import com.flyingogo.serviceapp.interfaces.Constant;
import com.flyingogo.serviceapp.utils.ThreadUtils;
import com.flyingogo.serviceapp.utils.URLUtils;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * 作者：dfy on 21/8/2017 11:26
 * <p> 充值的Activity;
 * 发送卡号到服务器生成一个二维码,创建子线程循环监听是否有充值成功
 * 收到充值成功的响应后将充值状态返回给上一个activity,销毁这个activity;
 * 邮箱：dengfuyao@163.com
 */
public class RechargeActivity extends AppCompatActivity {

    boolean recharge_ok = false;  //充值成功的标志位;
    @BindView(R.id.qr_code)
    ImageView mQrCode;
    @BindView(R.id.recharge_amount)
    TextView  mRechargeAmount;
    private String mCardId;
    private double deposit;  //押金
    private Gson   mGson;
    private String mTradeNo;
    private String mRechargeResultUrl = null;
    private RechargeResultBena mRechargeBean;
    private double             mAmount;
    private int                mPayType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        ButterKnife.bind(this);
        initData();
        initActionBar();
        initEvent();
    }

    private void initActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //点击back键finish当前activity
        switch (item.getItemId()) {
            case android.R.id.home:
                recharge_ok =true;
                finish();
                break;
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!recharge_ok){
            recharge_ok= true;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }

    private static final String TAG = "RechargeActivity";

    private void initData() {
        mGson = new Gson();

        Intent intent = getIntent();
        int intExtra = intent.getIntExtra(Constant.RECHARGE_TYPE, -1);
        Log.e(TAG, "initData: int = " + intExtra);
        switch (intent.getIntExtra(Constant.RECHARGE_TYPE, -1)) {
            case Constant.RECHARGE_VALUE_BALANCE:  //充值余额
                mCardId = intent.getStringExtra(Constant.CARD_NO);
                mAmount = intent.getDoubleExtra(Constant.RECHARGE_AMOUNT, 0);
                mPayType = intent.getIntExtra(Constant.PAYTYPE, 0);
                Log.e(TAG, "initData: 充值余额的卡号 = "+mCardId+" 金额 = "+mAmount+"充值方式 = "+mPayType );
                rechargeBalance(mAmount,mPayType,mCardId);
                break;
            case Constant.RECHARGE_VALUE_DEPOSIT:  //充值押金
                mCardId = intent.getStringExtra(Constant.CARD_NO);  //获取卡号
                //获取押金
                deposit = intent.getDoubleExtra(Constant.CASH, -1);
                mPayType = intent.getIntExtra(Constant.PAYTYPE, 0);
                if (mPayType != 0)
                    rechargeDeposit(deposit, mPayType, mCardId);
                Log.e(TAG, "initData: 卡号 = " + mCardId + " 押金 = " + deposit);
                break;
            default:
                break;
        }


    }

    private void rechargeBalance(double amount, int payType, String cardId) {
      String url = URLUtils.getRechargeBalanceUrl(amount,payType,cardId);
        Log.e(TAG, "rechargeBalance: url"+url );
        recharge(url);
        mRechargeAmount.setText(String.valueOf(amount));
    }

    /**
     * 发起充值押金请求
     *
     * @param deposit 押金金额
     * @param payType 充值方式
     * @param cardId  芯片号
     */
    private void rechargeDeposit(double deposit, int payType, String cardId) {
        String depositUrl = URLUtils.getRechargeDeposit(deposit, payType, cardId);
        Log.e(TAG, "rechargeDeposit: 充值押金接口" + depositUrl);
        recharge(depositUrl);
        mRechargeAmount.setText(String.valueOf(deposit));
    }

    private void recharge(String url) {
        OkHttpUtils.post().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(TAG, "onError:  充值押金失败=" + e.getMessage());
                Toast.makeText(getApplicationContext(), "充值押金失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                Log.e(TAG, "onResponse: response充值押金 = " + response.toString());

                DepositBean depositBean = mGson.fromJson(response.toString(), DepositBean.class);
                switch (depositBean.state) {
                    case -1002:
                        Toast.makeText(RechargeActivity.this, "卡片不存在", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    case -1005:
                        Toast.makeText(RechargeActivity.this, "押金已缴纳", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    case -1006:
                        Toast.makeText(RechargeActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    case 1:
                        bingView(depositBean);
                        break;
                    default:
                        break;
                }
            }
        });
    }
    private void bingView(DepositBean depositBean) {
        mQrCode.setImageBitmap(getBitmap(depositBean.content));
        mTradeNo = depositBean.outTradeNo;
        Message message = Message.obtain();
        message.obj = mTradeNo;
        mRechargeResultUrl = URLUtils.getRechargeResultUrl(mTradeNo);
        // mHandler.postDelayed(mRunnable,5000);

        // mHandler.sendEmptyMessage(0);
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.e(TAG, "handleMessage: msg.what = " + msg.what);
            if (msg.what == 0)
                //  mRunnable.run();
                // postDelayed(mRunnable,5000);

                super.handleMessage(msg);
        }
    };


    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            queryRechargeResult();
        }

        private void queryRechargeResult() {
            //定义标志位,
            if (!recharge_ok) {
                while (!recharge_ok) {

                    if (mTradeNo != null) {
                        if (mRechargeResultUrl != null) {
                            getRechargeResult();

                            if (mRechargeBean != null && mRechargeBean.state == 1000) {
                                recharge_ok = true;
                                Intent intent = new Intent();
                                intent.putExtra(Constant.RECHARGE_RESULT, true);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                           /* count++;

                            Log.e(TAG, "run: count = " + count);
                            if (count == 100) {

                            }*/
                        }
                    }
                    SystemClock.sleep(5000);
                }
            }
        }
    };

    private void initEvent() {
        ThreadUtils.onRunBigThread(new Runnable() {
            @Override
            public void run() {
                mRunnable.run();
            }
        });


    }

    int count = 0;


    // TODO: 22/8/2017 拿订单号
    private void getRechargeResult() {

        Log.e(TAG, "getRechargeResult: 充值结果查询URL = " + mRechargeResultUrl);
        OkHttpUtils.post().url(mRechargeResultUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(TAG, "这句话都会执行的: e = " + e.getMessage());
                recharge_ok = true;
                Toast.makeText(RechargeActivity.this, "服务器错误,查询失败!" + e.getMessage(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onResponse(String response, int id) {
                Log.e(TAG, "response = " + response.toString());
                mRechargeBean = mGson.fromJson(response.toString(), RechargeResultBena.class);

            }
        });
    }

    /**
     * 获取请求回来的二维码图片;
     *
     * @param url
     * @return
     */
    private Bitmap getBitmap(String url) {

        byte[] byt = (Base64.decode(url.getBytes(), 0));

        return BitmapFactory.decodeByteArray(byt, 0, byt.length);
    }
}
