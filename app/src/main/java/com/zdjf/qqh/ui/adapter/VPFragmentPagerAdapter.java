package com.zdjf.qqh.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

public class VPFragmentPagerAdapter<T extends Fragment> extends FragmentPagerAdapter {

    private List<Fragment> list;

    private String[] titles;
    private T mCurrentFragment;

    public VPFragmentPagerAdapter(FragmentManager fm, List<Fragment> pages, String[] titles) {
        super(fm);
        this.list = pages;
        this.titles = titles;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return this.titles[position];
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Fragment getItem(int arg0) {
        return list.get(arg0);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        mCurrentFragment = (T) object;
        super.setPrimaryItem(container, position, object);
    }

    public T getCurrentFragment() {
        return mCurrentFragment;
    }
}