package com.flyingogo.serviceapp.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.flyingogo.serviceapp.R;
import com.flyingogo.serviceapp.activity.CardDetailsActivity;
import com.flyingogo.serviceapp.adapter.CardNoListAdapter;
import com.flyingogo.serviceapp.base.BaseFragment;
import com.flyingogo.serviceapp.bean.ListCardBean;
import com.flyingogo.serviceapp.interfaces.Constant;
import com.flyingogo.serviceapp.utils.EditTextAddUtlis;
import com.flyingogo.serviceapp.utils.ThreadUtils;
import com.flyingogo.serviceapp.utils.URLUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 作者：dfy on 18/8/2017 10:43
 * <p>  挂失解挂
 * 邮箱：dengfuyao@163.com
 */
public class ReportTheLossOfFillDoFragment extends BaseFragment implements AdapterView.OnItemClickListener {
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


    @Override
    public int getResLayoutId() {
        return R.layout.fragment_report_loss;
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void init() {
        EditTextAddUtlis.getInstance(mInputUserid, mQueryCardNo).hideView(); //输入监听;
        initListView();
    }

    private void initListView() {

        mDatas = new ArrayList<>();
        // mAdapter = new CardListAdapter(getContext(),mDatas,this);
        mAdapter = new CardNoListAdapter(getContext(), mDatas);
        mCardList.setAdapter(mAdapter);
        mCardList.setOnItemClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "onStart: 启动挂失页面" );
        isFalst = true;
        mUserId = null;
       // mInputUserid.setText("");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mUserId != null) {
            mDatas.clear();
            mCardListUrl = URLUtils.getCardListUrl(mUserId);
            queryCardList(mCardListUrl);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "onPause: 挂失的fragment失去焦点" );

    }


    @OnClick(R.id.query_card_no)
    public void onClick() {
        mDatas.clear();
        mUserId = mInputUserid.getText().toString().trim();
        if (TextUtils.isEmpty(mUserId)) {
            Toast.makeText(getContext(), "请输入完整身份信息", Toast.LENGTH_SHORT).show();
            return;
        }
        mCardListUrl = URLUtils.getCardListUrl(mUserId);
        queryCardList(mCardListUrl);
    }

    private void queryCardList(String cardListUrl) {
        Log.e(TAG, "queryCardList: 获取卡列表的URL = " + cardListUrl);
        OkHttpUtils.post().url(cardListUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(getContext(), "服务器连接异常", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onError: 获取卡列表异常 =" + e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                Log.e(TAG, "onResponse:  = " + response.toString());
                ListCardBean listCardBean = mGson.fromJson(response.toString(), ListCardBean.class);
                if (listCardBean.state == 1) {
                    Log.e(TAG, "onResponse: 获取数据成功");
                    List<ListCardBean.DataBean> data = listCardBean.data;
                    if (data.size() == 0) {
                        showDialog();
                    } else {
                        for (ListCardBean.DataBean bean : data) {
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
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
        Intent intent = new Intent(getContext(), CardDetailsActivity.class);
        intent.putExtra(Constant.CARD_DETAILS, bean);
        startActivity(intent);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            if (mDatas!= null){
                Log.e(TAG, "setUserVisibleHint: 1111111" );

                mInputUserid.setText("");
                mDatas.clear();
                mAdapter.notifyDataSetChanged();

            }
        }

    }
}
