package com.flyingogo.serviceapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.flyingogo.serviceapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者：dfy on 21/8/2017 09:29
 * <p>
 * 邮箱：dengfuyao@163.com
 */
public class MyAdapter extends BaseAdapter {
    String recharges[] = {"微信支付", "支付宝支付", "现金支付"};

    @Override
    public int getCount() {
        return recharges.length;
    }

    @Override
    public Object getItem(int position) {
        return recharges[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView==null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, null);
            holder =new ViewHolder(convertView);
            convertView.setTag(holder);

        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.bandView(recharges[position]);
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.text_content)
        TextView mTextContent;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void bandView(String recharge) {
            mTextContent.setText(recharge);
        }
    }
}
