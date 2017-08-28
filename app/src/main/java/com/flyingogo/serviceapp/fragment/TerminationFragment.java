package com.flyingogo.serviceapp.fragment;

import android.view.View;
import android.widget.Button;

import com.flyingogo.serviceapp.R;
import com.flyingogo.serviceapp.activity.TerminateActivity;
import com.flyingogo.serviceapp.activity.UserIdTerminationCardActivity;
import com.flyingogo.serviceapp.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：dfy on 18/8/2017 10:44
 * <p> 注销卡fragment
 * 邮箱：dengfuyao@163.com
 */
public class TerminationFragment extends BaseFragment {


    @BindView(R.id.card_tremination)
    Button mCardTremination;
    @BindView(R.id.userid_tremination)
    Button mUseridTremination;

    @Override
    public int getResLayoutId() {
        return R.layout.fragment_termination_card;
    }

    @Override
    protected void lazyLoad() {
    }

    @OnClick({R.id.card_tremination, R.id.userid_tremination})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.card_tremination:
                //到刷卡界面,刷卡后获取卡信息,到注销界面;
              go2Activity(TerminateActivity.class,false);
                break;
            case R.id.userid_tremination:
                go2Activity(UserIdTerminationCardActivity.class,false);
                break;
        }
    }
}
