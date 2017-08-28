package com.flyingogo.serviceapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.flyingogo.serviceapp.R;
import com.flyingogo.serviceapp.bean.ListCardBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：dfy on 22/8/2017 21:19
 * <p>
 * 邮箱：dengfuyao@163.com
 */
public class CardListAdapter extends BaseAdapter implements View.OnClickListener {
    private static String                      state;
    private        Context                     mContext;
    private        List<ListCardBean.DataBean> mListCardBeen;
    private CallBackButtonOnclickListener mListener;
    public CardListAdapter(Context context, List<ListCardBean.DataBean> listCardBeen,CallBackButtonOnclickListener listener) {
        mContext = context;
        mListCardBeen = listCardBeen;
        mListener = listener;
    }

    @Override
    public int getCount() {
        return mListCardBeen == null ? 0 : mListCardBeen.size();
    }

    @Override
    public ListCardBean.DataBean getItem(int position) {
        if (mListCardBeen != null) {
            return mListCardBeen.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // TODO: 22/8/2017 构建卡列表的adapter 获取卡信息,对卡进行挂失解挂等操作;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_list_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
       holder.mUnLock.setOnClickListener(CardListAdapter.this);  //按钮的点击监听
        holder.mLock.setOnClickListener(CardListAdapter.this);
        holder.mUnLock.setTag(position);  //设置点击的按钮标签
        holder.mLock.setTag(position);
        holder.bingView(mListCardBeen.get(position));

        return convertView;
    }




    class ViewHolder  {
        @BindView(R.id.createDate)
        TextView mCreateDate;
        @BindView(R.id.sequence_no)
        TextView mSequenceNo;
        @BindView(R.id.card_cid)
        TextView mCardCid;
        @BindView(R.id.card_state)
        TextView mCardState;
        @BindView(R.id.card_no)
        TextView mCardNo;
        @BindView(R.id.card_deposit)
        TextView mCardDeposit;
        @BindView(R.id.card_balance)
        TextView mCardBalance;
        @BindView(R.id.realName)
        TextView mRealName;
        @BindView(R.id.mobilePhone)
        TextView mMobilePhone;
        @BindView(R.id.lock)
        Button   mLock;
        @BindView(R.id.unlock)
        Button   mUnLock;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void bingView(ListCardBean.DataBean bean) {
            mSequenceNo.setText(String.valueOf(bean.id));
            mCardCid.setText(bean.cardCid);
            mCardBalance.setText(bean.cardBalance + "");
            mCardDeposit.setText(bean.cardDeposit + "");
            mCardNo.setText(bean.cardNo);
            int cardState = bean.cardState;
            //卡片状态（0正常,1借车中,2异常 3已解约 4 已挂失）
            switch (cardState) {
                case 0:
                    state = "正常";
                    break;
                case 1:
                    state = "借车中";
                    break;
                case 2:
                    state = "异常";
                    break;
                case 3:
                    state = "已解约";
                    break;
                case 4:
                    state = "已挂失";
                    break;
                default:
                    break;
            }
            mCardState.setText(state);
            mRealName.setText(bean.realName);
            mMobilePhone.setText(bean.mobilePhone);
            mCreateDate.setText(bean.createDate);

        }


    }
    @Override
    public void onClick(View v) {
        mListener.click(v);
    }

    /**
     * 定义回调,监听item中的点击事件
     */
    public interface CallBackButtonOnclickListener{
        public void click(View v );
    }

}
