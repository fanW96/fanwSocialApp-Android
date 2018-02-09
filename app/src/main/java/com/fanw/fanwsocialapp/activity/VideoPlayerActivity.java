package com.fanw.fanwsocialapp.activity;

import android.content.res.Configuration;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.fanw.fanwsocialapp.R;
import com.fanw.fanwsocialapp.model.Video;
import com.superplayer.library.SuperPlayer;

public class VideoPlayerActivity extends AppCompatActivity implements SuperPlayer.OnNetChangeListener {

    private Video mVideo = new Video();
    private SuperPlayer mSuperPlayer;
    private TextView mPlayer_complete;
//    private Toolbar mToolbar;
    private View mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        initVideoData();
        initView();
//        mToolbar.setTitle(mVideo.getTitle());
//        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        setSupportActionBar(mToolbar);

        mSuperPlayer.setNetChangeListener(true)//设置监听手机网络的变化
                .setOnNetChangeListener(this)//实现网络变化的回调
                .onPrepared(new SuperPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared() {
                        /**
                         * 监听视频是否已经准备完成开始播放。（可以在这里处理视频封面的显示跟隐藏）
                         */
                    }
                })
                .onComplete(new Runnable() {
                    @Override
                    public void run() {
                        /**
                         * 监听视频是否已经播放完成了。（可以在这里处理视频播放完成进行的操作）
                         */
                    }
                })
                .onInfo(new SuperPlayer.OnInfoListener() {
                    @Override
                    public void onInfo(int what, int extra) {
                        /**
                         * 监听视频的相关信息。
                         */
                    }
                })
                .onError(new SuperPlayer.OnErrorListener() {
                    @Override
                    public void onError(int what, int extra) {
                        /**
                         * 监听视频播放失败的回调
                         */
                    }
                })
                .setTitle(mVideo.getTitle())//设置视频的titleName
                .play(mVideo.getMp4_url());//开始播放视频
        mSuperPlayer.setScaleType(SuperPlayer.SCALETYPE_FITXY);
    }

    private void initVideoData(){
        this.mVideo = (Video) getIntent().getSerializableExtra("video_data");
    }

    private void initView(){
        mSuperPlayer = findViewById(R.id.view_super_player);
        mPlayer_complete = findViewById(R.id.tv_super_player_complete);
//        mToolbar = findViewById(R.id.video_player_toolbar);
        mView = findViewById(R.id.video_bottom_view);
    }

    @Override
    public void onWifi() {
        Snackbar.make(mView,"当前网络环境是WIFI",Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onMobile() {
        Snackbar.make(mView,"当前网络环境是手机网络",Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onDisConnect() {
        Snackbar.make(mView,"网络链接断开",Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onNoAvailable() {
        Snackbar.make(mView,"无网络链接",Snackbar.LENGTH_LONG).show();
    }
    /**
     * 下面的这几个Activity的生命状态很重要
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (mSuperPlayer != null) {
            mSuperPlayer.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSuperPlayer != null) {
            mSuperPlayer.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSuperPlayer != null) {
            mSuperPlayer.onDestroy();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mSuperPlayer != null) {
            mSuperPlayer.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void onBackPressed() {
        if (mSuperPlayer != null && mSuperPlayer.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }
}
