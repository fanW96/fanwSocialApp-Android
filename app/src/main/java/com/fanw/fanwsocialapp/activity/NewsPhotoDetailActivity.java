package com.fanw.fanwsocialapp.activity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.fanw.fanwsocialapp.R;
import com.fanw.fanwsocialapp.adapter.NewsPhotoPagerAdapter;
import com.fanw.fanwsocialapp.fragment.NewsPhotoDetailFragment;
import com.fanw.fanwsocialapp.model.AdsBean;
import com.fanw.fanwsocialapp.model.ImgextraBean;
import com.fanw.fanwsocialapp.model.NewsInfo;
import com.fanw.fanwsocialapp.model.NewsPhotoDetail;
import com.fanw.fanwsocialapp.widget.PhotoViewPager;

import java.util.ArrayList;
import java.util.List;

public class NewsPhotoDetailActivity extends AppCompatActivity{

    private NewsInfo mNewsInfo;
    private NewsPhotoDetail mNewsPhotoDetail;
    private Toolbar mToolbar;
    private PhotoViewPager mPhotoViewPager;
    private TextView mPhotoTv;
    private List<NewsPhotoDetailFragment> mPhotoDetailFragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_photo_detail);
        initData();
        initView();
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        createFragment(mNewsPhotoDetail);
        initViewPager();
        setPhotoDetailTitle(0);
    }

    private void initViewPager() {
        NewsPhotoPagerAdapter photoPagerAdapter = new NewsPhotoPagerAdapter(getSupportFragmentManager(), mPhotoDetailFragmentList);
        mPhotoViewPager.setAdapter(photoPagerAdapter);
        mPhotoViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setPhotoDetailTitle(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setPhotoDetailTitle(int position) {
        String title = getTitle(position);
        mPhotoTv.setText(getString(R.string.photo_detail_title, position + 1,
                mPhotoDetailFragmentList.size(), title));
    }

    private String getTitle(int position) {
        String title = mNewsPhotoDetail.getPictures().get(position).getTitle();
        if (title == null) {
            title = mNewsPhotoDetail.getTitle();
        }
        return title;
    }

    private void createFragment(NewsPhotoDetail newsPhotoDetail) {
        mPhotoDetailFragmentList.clear();
        for (NewsPhotoDetail.Picture picture : newsPhotoDetail.getPictures()) {
            NewsPhotoDetailFragment fragment = NewsPhotoDetailFragment.newInstance(picture.getImgSrc());
            mPhotoDetailFragmentList.add(fragment);
        }
    }

    private void initView(){
        mPhotoTv = findViewById(R.id.news_photo_detail_title_tv);
        mPhotoViewPager = findViewById(R.id.news_photo_detail_viewpager);
        mToolbar = findViewById(R.id.news_photo_detail_toolbar);
    }

    private void initData(){
        mNewsInfo = (NewsInfo)getIntent().getSerializableExtra("news_data");
        mNewsPhotoDetail = getPhotoDetail(mNewsInfo);
    }

    private NewsPhotoDetail getPhotoDetail(NewsInfo newsInfo) {
        NewsPhotoDetail newsPhotoDetail = new NewsPhotoDetail();
        newsPhotoDetail.setTitle(newsInfo.getTitle());
        setPictures(newsInfo, newsPhotoDetail);
        return newsPhotoDetail;
    }

    private void setPictures(NewsInfo newsInfo, NewsPhotoDetail newsPhotoDetail) {
        List<NewsPhotoDetail.Picture> pictureList = new ArrayList<>();

        if (newsInfo.getAds() != null) {
            for (AdsBean entity : newsInfo.getAds()) {
                setValuesAndAddToList(pictureList, entity.getTitle(), entity.getImgsrc());
            }
        } else if (newsInfo.getImgextra() != null) {
            for (ImgextraBean entity : newsInfo.getImgextra()) {
                setValuesAndAddToList(pictureList, null, entity.getImgsrc());
            }
        } else {
            setValuesAndAddToList(pictureList, null, newsInfo.getImgsrc());
        }

        newsPhotoDetail.setPictures(pictureList);
    }

    private void setValuesAndAddToList(List<NewsPhotoDetail.Picture> pictureList, String title, String imgsrc) {
        NewsPhotoDetail.Picture picture = new NewsPhotoDetail.Picture();
        if (title != null) {
            picture.setTitle(title);
        }
        picture.setImgSrc(imgsrc);

        pictureList.add(picture);
    }
}
