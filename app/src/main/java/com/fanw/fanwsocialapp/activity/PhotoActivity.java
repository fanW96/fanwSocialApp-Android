package com.fanw.fanwsocialapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.fanw.fanwsocialapp.R;
import com.fanw.fanwsocialapp.adapter.PhotoAdapter;
import com.fanw.fanwsocialapp.base.BaseActivity;
import com.fanw.fanwsocialapp.callback.JsonCallback;
import com.fanw.fanwsocialapp.listener.OnItemClickListener;
import com.fanw.fanwsocialapp.model.PhotoGirl;
import com.fanw.fanwsocialapp.callback.PhotoGirlReceiver;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

public class PhotoActivity extends BaseActivity {
    private RecyclerView mPhotoRv;
    private TextView mEmptyView;//为空重新加载的页面
    private PhotoAdapter mPhotoAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<PhotoGirl> photoList = new ArrayList<PhotoGirl>();
    private List<PhotoGirl> morePhotoList = new ArrayList<PhotoGirl>();
    private Context context;
    private int page = 1;
    private boolean loading = false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_photo;
    }

    @Override
    public void initViews() {
        mPhotoRv = findViewById(R.id.photo_rv);
        mEmptyView = findViewById(R.id.empty_view);
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        mPhotoAdapter = new PhotoAdapter(photoList,context);
        mPhotoRv.setHasFixedSize(true);
        mPhotoRv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mPhotoRv.setItemAnimator(new DefaultItemAnimator());
        mPhotoRv.setAdapter(mPhotoAdapter);

        mPhotoAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Intent intent = new Intent(context,PhotoDetailActivity.class);
                intent.putExtra("picUrl",photoList.get(position).getUrl());
                intent.putExtra("title",photoList.get(position).getDesc());
                startActivity(intent);
            }
        });
        //设置滚动监听
        mPhotoRv.addOnScrollListener(mScrollListener);
        //设置下拉刷新的事件
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                photoList.clear();
                page = 0;
                new LatestPhotoTask().execute();
            }
        });
        //设置初始状态加载动画
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                new LatestPhotoTask().execute();
            }
        });
    }

    RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            StaggeredGridLayoutManager gridLayoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
            //获得全部以获得的Item数量
            int totalItemCount = gridLayoutManager.getItemCount();
            //后的当前可见的Item的position
            int[] lastVisibleItemPosition = gridLayoutManager.findLastVisibleItemPositions(null);
            if (!loading && totalItemCount < (lastVisibleItemPosition[1] + 2)){
                loading =true;
                page++;
                new LatestPhotoTask().execute();
            }
        }
    };

    protected void getNetDate() {
        OkGo.<PhotoGirlReceiver<List<PhotoGirl>>>get("http://gank.io/api/data/福利/10/"+page)//添加泛型
                .tag(this)
                .execute(new JsonCallback<PhotoGirlReceiver<List<PhotoGirl>>>() {//编写需要的返回方法，在里面做OnSuccessConvert
                    @Override
                    public void onSuccess(Response<PhotoGirlReceiver<List<PhotoGirl>>> response) {
                        morePhotoList = response.body().getResults();
                    }
                });
    }

    protected class LatestPhotoTask extends AsyncTask<Integer,Void,List<PhotoGirl>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(photoList != null&&photoList.size()>0){
                //添加footer
//                NewsInfo newsInfoTemp = new NewsInfo();
                photoList.add(null);
                // notifyItemInserted(int position)，这个方法是在第position位置
                // 被插入了一条数据的时候可以使用这个方法刷新，
                // 注意这个方法调用后会有插入的动画，这个动画可以使用默认的，也可以自己定义。
                mPhotoAdapter.notifyItemInserted(photoList.size() -1);
            }
        }

        @Override
        protected void onPostExecute(List<PhotoGirl> photoGirls) {
            super.onPostExecute(photoGirls);
            if(mSwipeRefreshLayout != null){
                mSwipeRefreshLayout.setRefreshing(false);
            }
//            出错：data没有获得到数据
//            原因：doinbackground的return在okgo请求期间就返回了
//            解决方式：在return之前让线程休眠1秒
            if(photoList.size() == 0){
                photoList.addAll(photoGirls);
                checkIsEmpty(photoList);
                mPhotoAdapter.notifyDataSetChanged();
            }else{
                //删除footer
                photoList.remove(photoList.size() -1);
                photoList.addAll(photoGirls);
                mPhotoAdapter.notifyDataSetChanged();
                loading =false;
            }
        }

        @Override
        protected List<PhotoGirl> doInBackground(Integer... integers) {
            getNetDate();
            try {
                Thread.sleep(1000);//沉睡一秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return morePhotoList;
        }
    }

    private void checkIsEmpty(List<PhotoGirl> photoGirls) {
        if (photoGirls == null && mPhotoAdapter.getList() == null) {
            mPhotoRv.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);

        } else {
            mPhotoRv.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        }
    }
}
