package com.fanw.fanwsocialapp.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanw.fanwsocialapp.activity.VideoPlayerActivity;
import com.fanw.fanwsocialapp.adapter.VideoAdapter;
import com.fanw.fanwsocialapp.base.BaseFragment;
import com.fanw.fanwsocialapp.callback.JsonCallback;
import com.fanw.fanwsocialapp.listener.OnItemClickListener;
import com.fanw.fanwsocialapp.model.Video;
import com.fanw.fanwsocialapp.callback.VideoReceiver;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.List;

/**
 * Created by fanw on 2018/2/6.
 */

public class VideoFragment extends BaseFragment<Video> {

    private VideoAdapter videoAdapter;
    private List<Video> temp;
    private static int page = 0;

    public static VideoFragment VideoInstance(String param) {
        VideoFragment fragment = new VideoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        videoAdapter = new VideoAdapter(mList,mAct);
        mRecyclerView.setAdapter(videoAdapter);

        videoAdapter.setOnItemClickListener(onItemClickListener);

        //设置滚动监听
        mRecyclerView.addOnScrollListener(mScrollListener);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mList.clear();
                new LatestVideoTask().execute();
            }
        });
        //设置初始状态加载动画
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                new LatestVideoTask().execute();
            }
        });
    }

    /**
     * Item点击监听
     */
    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int position , View v) {
            Intent intent = new Intent(mAct, VideoPlayerActivity.class);
            intent.putExtra("video_data",mList.get(position));
            startActivity(intent);
        }
    };

    RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
            //获得全部以获得的Item数量
            int totalItemCount = linearLayoutManager.getItemCount();
            //后的当前可见的Item的position
            int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
            if (!loading && totalItemCount < (lastVisibleItemPosition + 3)){
                loading =true;
                page = page+20;
                new LatestVideoTask().execute();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void getNetDate() {
        OkGo.<VideoReceiver<List<Video>>>get("http://c.3g.163.com/nc/video/list/V9LG4B3A0/n/"+page+"-20.html")
                .tag(this)
                .execute(new JsonCallback<VideoReceiver<List<Video>>>() {
                    @Override
                    public void onSuccess(Response<VideoReceiver<List<Video>>> response) {
                        mMoreList = response.body().getV9LG4B3A0();
                    }
                });
    }

    protected class LatestVideoTask extends AsyncTask<Integer,Void,List<Video>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<Video> videos) {
            super.onPostExecute(videos);
            if(mSwipeRefreshLayout != null){
                mSwipeRefreshLayout.setRefreshing(false);
            }
//            出错：data没有获得到数据
//            原因：doinbackground的return在okgo请求期间就返回了
//            解决方式：在return之前让线程休眠1秒
            if(mList.size() == 0){
                mList.addAll(videos);
                videoAdapter.notifyDataSetChanged();
            }else{
                //删除footer
                mList.remove(mList.size() -1);
                mList.addAll(videos);
                videoAdapter.notifyDataSetChanged();
                loading =false;
            }
        }

        @Override
        protected List<Video> doInBackground(Integer... integers) {
            getNetDate();
            try {
                Thread.sleep(1000);//沉睡一秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return mMoreList;
        }
    }
}
