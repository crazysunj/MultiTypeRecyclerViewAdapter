package com.crazysunj.sample.adapter;

import com.crazysunj.sample.FooterItemFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * author: sunjian
 * created on: 2017/8/13 下午4:20
 * description:
 */

public class FooterVPAdapter extends FragmentPagerAdapter {

    private static final String[] sTitle = {"疗养中心", "专科用药", "医疗器械", "营养保健"};

    private List<Fragment> mFragments = new ArrayList<Fragment>();

    public FooterVPAdapter(FragmentManager fm) {
        super(fm);
        for (int i = 0, size = sTitle.length; i < size; i++) {
            mFragments.add(FooterItemFragment.newInstance());
        }
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return sTitle.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return sTitle[position];
    }
}
