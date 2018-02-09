package com.fanw.fanwsocialapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.fanw.fanwsocialapp.fragment.NewsPhotoDetailFragment;

import java.util.List;

/**
 * Created by fanw on 2018/2/8.
 */

public class NewsPhotoPagerAdapter extends FragmentStatePagerAdapter {

    private List<NewsPhotoDetailFragment> mFragmentList;

    public NewsPhotoPagerAdapter(FragmentManager fm, List<NewsPhotoDetailFragment> fragmentList) {
        super(fm);
        mFragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
