package com.flyingogo.serviceapp.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.flyingogo.serviceapp.R;
import com.flyingogo.serviceapp.adapter.ViewPagerAdapter;
import com.flyingogo.serviceapp.fragment.TerminationFragment;
import com.flyingogo.serviceapp.fragment.DredageFragment;
import com.flyingogo.serviceapp.fragment.RechargeFragment;
import com.flyingogo.serviceapp.fragment.ReportTheLossOfFillDoFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar           mToolbar;
    @BindView(R.id.tabs)
    TabLayout         mTabs;
    @BindView(R.id.appbar)
    AppBarLayout      mAppbar;
    @BindView(R.id.container)
    ViewPager         mViewPager;
    @BindView(R.id.main_content)
    CoordinatorLayout mMainContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initToolBar();
        initFragment();
    }

    private void initFragment() {

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new DredageFragment());  //开通卡
        fragments.add(new RechargeFragment());//充值
        fragments.add(new ReportTheLossOfFillDoFragment());//挂失补卡
        fragments.add(new TerminationFragment()); //解挂
        String[] titles = getResources().getStringArray(R.array.titles);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments, titles);
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        mTabs.setTabTextColors(Color.WHITE, Color.BLACK);
        mTabs.setSelectedTabIndicatorColor(Color.BLACK);
        mTabs.setupWithViewPager(mViewPager);
    }
    private void initToolBar() {
        mToolbar.setTitle("客服APP");
        mToolbar.setTitleTextColor(Color.WHITE);

        ActionBar actionBar = getSupportActionBar();
        setSupportActionBar(mToolbar);
        if(actionBar != null) {
            Log.e("lll", "initToolBar: 显示返回键" );
            actionBar.setDisplayHomeAsUpEnabled(true);
           // actionBar.setDisplayShowTitleEnabled(false);
        }
    }


}
