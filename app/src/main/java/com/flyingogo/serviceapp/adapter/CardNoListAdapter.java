package com.flyingogo.serviceapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.flyingogo.serviceapp.R;
import com.flyingogo.serviceapp.bean.ListCardBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：dfy on 23/8/2017 13:53
 * <p>
 * 邮箱：dengfuyao@163.com
 */

public class CardNoListAdapter extends BaseAdapter {
    private Context                     mContext;
    private List<ListCardBean.DataBean> mListCardBeen;

    public CardNoListAdapter(Context context, List<ListCardBean.DataBean> listCardBeen) {
        mContext = context;
        mListCardBeen = listCardBeen;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       ViewHolder holder ;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.card_no_item, null);//View.inflate(mContext, R.layout.card_no_item,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.bangView(mListCardBeen.get(position),position);

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.card_no)
        TextView mCardNo;
        @BindView(R.id.card_state)
        TextView mCardState;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void bangView(ListCardBean.DataBean dataBean, int position) {
            mCardNo.setText(dataBean.cardNo);
            int cardState = dataBean.cardState;
            //卡片状态（0正常,1借车中,2异常 3已解约 4 已挂失）
            String state = "";
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
        }
    }
}
