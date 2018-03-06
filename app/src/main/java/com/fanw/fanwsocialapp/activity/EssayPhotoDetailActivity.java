package com.fanw.fanwsocialapp.activity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.fanw.fanwsocialapp.R;
import com.fanw.fanwsocialapp.adapter.EssayPhotoPagerAdapter;
import com.fanw.fanwsocialapp.common.Constants;
import com.fanw.fanwsocialapp.fragment.EssayPhotoDetailFragment;
import com.fanw.fanwsocialapp.model.Essay;
import com.fanw.fanwsocialapp.widget.PhotoViewPager;

import java.util.ArrayList;
import java.util.List;

public class EssayPhotoDetailActivity extends AppCompatActivity {

    private Essay mEssay;
    private Toolbar mToolbar;
    private PhotoViewPager mPhotoViewPager;
    private List<EssayPhotoDetailFragment> mPhotoDetailFragmentList = new ArrayList<EssayPhotoDetailFragment>();

    private void initData(){
        mEssay = (Essay) getIntent().getSerializableExtra("essay_data");
    }

    private void initView(){
        mPhotoViewPager = findViewById(R.id.essay_photo_detail_viewpager);
        mToolbar = findViewById(R.id.essay_photo_detail_toolbar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_essay_photo_detail);
        initView();
        initData();
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        createFragment(mEssay);
        initViewPager();
    }

    private void initViewPager() {
        EssayPhotoPagerAdapter photoPagerAdapter = new EssayPhotoPagerAdapter(getSupportFragmentManager(), mPhotoDetailFragmentList);
        mPhotoViewPager.setAdapter(photoPagerAdapter);
        mPhotoViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
    }

    private void createFragment(Essay essay) {
        mPhotoDetailFragmentList.clear();
        EssayPhotoDetailFragment essayPhotoDetailFragment;
        if (essay.getEssay_pic_1() != null){
            essayPhotoDetailFragment = EssayPhotoDetailFragment.newInstance(Constants.ESSAY_URL+ Constants.ESSAY_PIC+essay.getEssay_pic_1());
            mPhotoDetailFragmentList.add(essayPhotoDetailFragment);
        }
        if (essay.getEssay_pic_2() != null){
            essayPhotoDetailFragment = EssayPhotoDetailFragment.newInstance(Constants.ESSAY_URL+ Constants.ESSAY_PIC+essay.getEssay_pic_2());
            mPhotoDetailFragmentList.add(essayPhotoDetailFragment);
        }
        if (essay.getEssay_pic_3() != null){
            essayPhotoDetailFragment = EssayPhotoDetailFragment.newInstance(Constants.ESSAY_URL+ Constants.ESSAY_PIC+essay.getEssay_pic_3());
            mPhotoDetailFragmentList.add(essayPhotoDetailFragment);
        }
    }
}
