package com.flyingogo.serviceapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * 作者：dfy on 18/8/2017 10:22
 * <p>
 * 邮箱：dengfuyao@163.com
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragmengs;

    private String[] titles;

    public ViewPagerAdapter(FragmentManager fm, List<Fragment> fragmengs, String[] titles) {
        super(fm);
        this.fragmengs = fragmengs;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmengs.get(position);
    }

    @Override
    public int getCount() {
        return fragmengs==null?0:fragmengs.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];  //返回title名字;
    }
}
