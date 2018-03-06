package com.fanw.fanwsocialapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.fanw.fanwsocialapp.fragment.EssayPhotoDetailFragment;

import java.util.List;

/**
 * Created by fanw on 2018/2/15.
 */

public class EssayPhotoPagerAdapter extends FragmentStatePagerAdapter {
    private List<EssayPhotoDetailFragment> mFragmentList;

    public EssayPhotoPagerAdapter(FragmentManager fm, List<EssayPhotoDetailFragment> fragmentList) {
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
