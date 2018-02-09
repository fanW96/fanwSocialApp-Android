package com.fanw.fanwsocialapp.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fanw.fanwsocialapp.R;
import com.fanw.fanwsocialapp.application.GlideApp;
import com.fanw.fanwsocialapp.util.SystemUiVisibilityUtil;

import ooo.oxo.library.widget.PullBackLayout;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PhotoDetailActivity extends AppCompatActivity implements PullBackLayout.Callback{

    Toolbar photo_detail_toolbar;
    ImageView photo_detail_iv;
    PullBackLayout photo_detail_pull_back_layout;
    PhotoView photo_detail_touch_iv;

    private boolean mIsToolBarHidden;
    private ColorDrawable mBackground;
    private boolean mIsStatusBarHidden;

    private  String picUrl;
    private String title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);
        picUrl = this.getIntent().getStringExtra("picUrl");
        title = this.getIntent().getStringExtra("title");
        photo_detail_toolbar = (Toolbar) findViewById(R.id.photo_detail_toolbar);
        photo_detail_iv = (ImageView) findViewById(R.id.photo_detail_iv);
        photo_detail_pull_back_layout = (PullBackLayout) findViewById(R.id.photo_detail_pull_back_layout);
        photo_detail_touch_iv = (PhotoView) findViewById(R.id.photo_detail_touch_iv);

        photo_detail_pull_back_layout.setCallback(this);

        initLazyLoadView();

        initToolbar();
        initImageView();
        initBackground();
        setPhotoViewClickEvent();
    }

    private void initLazyLoadView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getEnterTransition().addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {

                }

                @Override
                public void onTransitionEnd(Transition transition) {
                    showToolBarAndPhotoTouchView();
                }

                @Override
                public void onTransitionCancel(Transition transition) {

                }

                @Override
                public void onTransitionPause(Transition transition) {

                }

                @Override
                public void onTransitionResume(Transition transition) {

                }
            });
        } else {
            showToolBarAndPhotoTouchView();
        }
    }

    private void showToolBarAndPhotoTouchView() {
        toolBarFadeIn();
        loadPhotoTouchIv();
    }

    private void toolBarFadeIn() {
        mIsToolBarHidden = true;
        hideOrShowToolbar();
    }

    private void loadPhotoTouchIv() {
        GlideApp.with(this)
                .load(picUrl)
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.ic_load_fail)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(photo_detail_touch_iv);
    }

    private void initToolbar() {
        photo_detail_toolbar.setTitle(title);
        photo_detail_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoDetailActivity.this.finish();
            }
        });
    }

    private void initImageView() {
        loadPhotoIv();
//        PhotoViewAttacher mAttacher=new PhotoViewAttacher(photo_detail_iv);
    }

    private void loadPhotoIv() {
        GlideApp.with(this)
                .load(picUrl)
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.ic_load_fail)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(photo_detail_touch_iv);
    }

    private void setPhotoViewClickEvent() {
        photo_detail_touch_iv.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float v, float v1) {
                hideOrShowToolbar();
                hideOrShowStatusBar();
            }

            @Override
            public void onOutsidePhotoTap() {
                hideOrShowToolbar();
                hideOrShowStatusBar();
            }
        });
    }

    private void hideOrShowStatusBar() {
        if (mIsStatusBarHidden) {
            SystemUiVisibilityUtil.enter(PhotoDetailActivity.this);
        } else {
            SystemUiVisibilityUtil.exit(PhotoDetailActivity.this);
        }
        mIsStatusBarHidden = !mIsStatusBarHidden;
    }

    @SuppressWarnings("deprecation")
    private void initBackground() {
        mBackground = new ColorDrawable(Color.BLACK);
        getWindow().getDecorView().setBackground(mBackground);
    }

    protected void hideOrShowToolbar() {
        photo_detail_toolbar.animate()
                .alpha(mIsToolBarHidden ? 1.0f : 0.0f)
                .setInterpolator(new DecelerateInterpolator(2))
                .start();
        mIsToolBarHidden = !mIsToolBarHidden;
    }

    @Override
    public void onPullStart() {
        toolBarFadeOut();

        mIsStatusBarHidden = true;
        hideOrShowStatusBar();
    }

    private void toolBarFadeOut() {
        mIsToolBarHidden = false;
        hideOrShowToolbar();
    }

    @Override
    public void onPull(float progress) {
        progress = Math.min(1f, progress * 3f);
        mBackground.setAlpha((int) (0xff/*255*/ * (1f - progress)));
    }

    @Override
    public void onPullCancel() {
        toolBarFadeIn();
    }

    @Override
    public void onPullComplete() {
        supportFinishAfterTransition();
    }
}
