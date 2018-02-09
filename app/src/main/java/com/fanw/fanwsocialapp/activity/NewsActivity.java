package com.fanw.fanwsocialapp.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.fanw.fanwsocialapp.R;
import com.fanw.fanwsocialapp.adapter.NewsFragmentPagerAdapter;
import com.fanw.fanwsocialapp.base.BaseActivity;
import com.fanw.fanwsocialapp.base.BaseFragment;
import com.fanw.fanwsocialapp.common.Constants;
import com.fanw.fanwsocialapp.fragment.NewsFragment;
import com.fanw.fanwsocialapp.fragment.VideoFragment;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends BaseActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ArrayList<BaseFragment> fragments = new ArrayList<BaseFragment>();


    @Override
    public int getLayoutId() {
        return R.layout.activity_news;
    }

    @Override
    public void initViews() {
        mTabLayout = findViewById(R.id.tabs);
        mViewPager = findViewById(R.id.view_pager);
        initViewPager();
        initTabLayout();
    }

    private void initViewPager(){
        NewsFragment newsFragment = NewsFragment.NewsInstance(Constants.NEWS_URL);
        VideoFragment videoFragment = VideoFragment.VideoInstance(Constants.VIDEO_URL);
//        Bundle data1=new Bundle();
//        data1.putString("param",Constants.NEWS_URL);
//        Bundle data2=new Bundle();
//        data2.putString("param",Constants.VIDEO_URL);
//        NewsFragment newsFragment = new NewsFragment();
//        newsFragment.setArguments(data1);
//        VideoFragment videoFragment = new VideoFragment();
//        videoFragment.setArguments(data2);

        fragments.add(newsFragment);
        fragments.add(videoFragment);
        NewsFragmentPagerAdapter newsFragmentPagerAdapter = new NewsFragmentPagerAdapter(getSupportFragmentManager(),fragments);
        mViewPager.setAdapter(newsFragmentPagerAdapter);
    }

    private void initTabLayout(){
        mTabLayout.addTab(mTabLayout.newTab().setText(Constants.NEWS_COLUMN));
        mTabLayout.addTab(mTabLayout.newTab().setText(Constants.VIDEO_COLUMN));
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
