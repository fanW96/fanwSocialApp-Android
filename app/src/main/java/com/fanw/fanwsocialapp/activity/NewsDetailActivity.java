package com.fanw.fanwsocialapp.activity;

import android.os.AsyncTask;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fanw.fanwsocialapp.R;
import com.fanw.fanwsocialapp.application.GlideApp;
import com.fanw.fanwsocialapp.common.Constants;
import com.fanw.fanwsocialapp.model.NewsDetail;
import com.fanw.fanwsocialapp.model.NewsInfo;
import com.fanw.fanwsocialapp.util.MyUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class NewsDetailActivity extends AppCompatActivity {

    ImageView mNewsDetailPhotoIv;
    Toolbar mToolbar;
    CollapsingToolbarLayout mToolbarLayout;
    AppBarLayout mAppBar;
    TextView mNewsDetailFromTv;
    TextView mNewsDetailBodyTv;
    FloatingActionButton mFab;
    ProgressBar mProgressBar;
    View mMaskView;
    private NewsInfo mNewsInfo = new NewsInfo();
    private NewsDetail mNewsDetail;

    private void initView(){
        mNewsDetailPhotoIv = findViewById(R.id.news_detail_photo_iv);
        mToolbar = findViewById(R.id.toolbar_news_detail);
        mToolbarLayout = findViewById(R.id.toolbar_layout);
        mAppBar = findViewById(R.id.app_bar);
        mNewsDetailFromTv = findViewById(R.id.news_detail_from_tv);
        mNewsDetailBodyTv = findViewById(R.id.news_detail_body_tv);
        mFab = findViewById(R.id.fab);
        mProgressBar = findViewById(R.id.progress_bar);
        mMaskView = findViewById(R.id.mask_view);
    }

    private void initData(){
        this.mNewsInfo = (NewsInfo) getIntent().getSerializableExtra("news_data");
    }

    private void getDate(){
        OkGo.<NewsDetail>get("http://c.m.163.com/nc/article/"+mNewsInfo.getPostid()+"/full.html")
                .tag(this)
                .execute(new Callback<NewsDetail>() {
                    @Override
                    public void onStart(Request<NewsDetail, ? extends Request> request) {

                    }

                    @Override
                    public void onSuccess(Response<NewsDetail> response) {
                            mNewsDetail = response.body();
                    }

                    @Override
                    public void onCacheSuccess(Response<NewsDetail> response) {

                    }

                    @Override
                    public void onError(Response<NewsDetail> response) {

                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void uploadProgress(Progress progress) {

                    }

                    @Override
                    public void downloadProgress(Progress progress) {

                    }

                    @Override
                    public NewsDetail convertResponse(okhttp3.Response response) throws Throwable {
                        Type type = new TypeToken<Map<String, NewsDetail>>() {}.getType();
                        Gson gson = new Gson();
                        JsonReader jsonReader = new JsonReader(response.body().charStream());
                        Map<String,NewsDetail> map = gson.fromJson(jsonReader,type);
                        return map.get(mNewsInfo.getPostid());
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        initData();
        initView();
        new NewsDetailTask().execute();

    }

    protected class NewsDetailTask extends AsyncTask<String,Void,NewsDetail>{

        @Override
        protected NewsDetail doInBackground(String... strings) {
            getDate();
            try {
                Thread.sleep(1000);//沉睡一秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return mNewsDetail;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(NewsDetail newsDetail) {
            super.onPostExecute(newsDetail);
            mProgressBar.setVisibility(View.GONE);
            setToolBarLayout(newsDetail.getTitle());
            mNewsDetailFromTv.setText(getString(R.string.news_from
                    ,newsDetail.getSource()
                    ,new String(MyUtils.formatDate(newsDetail.getPtime()))));
            setNewsDetailPhotoIv(getImgSrcs(newsDetail));
            setNewsDetailBodyTv(newsDetail.getBody());
        }
    }

    private void setToolBarLayout(String newsTitle) {
        mToolbarLayout.setTitle(newsTitle);
        mToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, R.color.white));
        mToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.primary_text_white));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private String getImgSrcs(NewsDetail newsDetail) {
        List<NewsDetail.ImgBean> imgSrcs = newsDetail.getImg();
        String imgSrc;
        if (imgSrcs != null && imgSrcs.size() > 0) {
            imgSrc = imgSrcs.get(0).getSrc();
        } else {
            imgSrc = getIntent().getStringExtra(Constants.NEWS_IMG_RES);
        }
        return imgSrc;
    }

    //设置新闻详细的图片
    private void setNewsDetailPhotoIv(String imgSrc) {
        GlideApp.with(this)
                .load(imgSrc)
                .placeholder(R.drawable.ic_loading)
                .format(DecodeFormat.PREFER_ARGB_8888)
                .error(R.drawable.ic_load_fail)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mNewsDetailPhotoIv)/*(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        mNewsDetailPhotoIv.setImageBitmap(resource);
                        mMaskView.setVisibility(View.VISIBLE);
                    }
                })*/;
    }

    //新闻的详细信息
    private void setNewsDetailBodyTv( final String newsBody) {
        mNewsDetailBodyTv.setText(Html.fromHtml(newsBody));
    }

}
