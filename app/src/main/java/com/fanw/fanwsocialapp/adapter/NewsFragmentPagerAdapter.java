package com.fanw.fanwsocialapp.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import com.fanw.fanwsocialapp.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fanw on 2018/2/6.
 */

public class NewsFragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

    private ArrayList<BaseFragment> fragments;
    private FragmentManager fm;

    public NewsFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        this.fm = fm;
    }

    public NewsFragmentPagerAdapter(FragmentManager fm,
                                    ArrayList<BaseFragment> fragments) {
        super(fm);
        this.fm = fm;
        this.fragments = fragments;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public BaseFragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void setFragments(ArrayList<BaseFragment> fragments) {
        if (this.fragments != null) {
            FragmentTransaction ft = fm.beginTransaction();
            for (BaseFragment bf : this.fragments) {
                ft.remove(bf);
            }
            ft.commit();
            ft = null;
            fm.executePendingTransactions();
        }
        this.fragments = fragments;
        notifyDataSetChanged();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        Object obj = super.instantiateItem(container, position);
        return obj;
    }
}
