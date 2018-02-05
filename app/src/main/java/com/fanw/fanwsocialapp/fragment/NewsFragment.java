package com.fanw.fanwsocialapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanw.fanwsocialapp.R;
import com.fanw.fanwsocialapp.adapter.NewsAdapter;
import com.fanw.fanwsocialapp.base.BaseFragment;
import com.fanw.fanwsocialapp.base.BaseTask;
import com.fanw.fanwsocialapp.callback.JsonCallback;
import com.fanw.fanwsocialapp.listener.OnItemClickListener;
import com.fanw.fanwsocialapp.model.NewsInfo;
import com.fanw.fanwsocialapp.model.NewsReceiver;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.List;

/**
 * Created by fanw on 2018/2/4.
 */

public class NewsFragment extends BaseFragment<NewsInfo>{

    private NewsAdapter newsAdapter;
    private List<NewsInfo> temp;

    public static NewsFragment NewsInstance(String param) {
        NewsFragment fragment = new NewsFragment();
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

        newsAdapter = new NewsAdapter(mList,mAct);

        // 设置item及item中控件的点击事件
        newsAdapter.setOnItemClickListener(onItemClickListener);

        mRecyclerView.setAdapter(newsAdapter);
        //设置滚动监听
        mRecyclerView.addOnScrollListener(mScrollListener);
        //设置下拉刷新的事件
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mList.clear();
                new LatestNewsTask().execute();
            }
        });
        //设置初始状态加载动画
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                new LatestNewsTask().execute();
            }
        });
    }

    /**
     * Item点击监听
     */
    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int position , View v) {
            switch (v.getId()){
                case R.id.news_title_tv:
                    Snackbar.make(v,"tran", Snackbar.LENGTH_LONG).show();
                    break;
                /*case R.id.cv_Delete:
                    Snackbar.make(v,"del",Snackbar.LENGTH_LONG).show();
                    mList.remove(position);
                    newsAdapter.notifyDataSetChanged();
                    break;*/
            }
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
                new LatestNewsTask().execute();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected List<NewsInfo> getNetDate() {
        OkGo.<NewsReceiver<List<NewsInfo>>>get(url)//添加泛型
                .tag(this)
                .execute(new JsonCallback<NewsReceiver<List<NewsInfo>>>() {//编写需要的返回方法，在里面做OnSuccessConvert
                    @Override
                    public void onSuccess(Response<NewsReceiver<List<NewsInfo>>> response) {
                        temp = response.body().getT1348647909107();
                    }
                });
        return temp;
    }

    public class LatestNewsTask extends BaseTask<NewsInfo>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(mList != null&&mList.size()>0){
                //添加footer
//                NewsInfo newsInfoTemp = new NewsInfo();
                mList.add(null);
                // notifyItemInserted(int position)，这个方法是在第position位置
                // 被插入了一条数据的时候可以使用这个方法刷新，
                // 注意这个方法调用后会有插入的动画，这个动画可以使用默认的，也可以自己定义。
                newsAdapter.notifyItemInserted(mList.size() -1);
            }
        }

        @Override
        protected void onPostExecute(List<NewsInfo> newsInfos) {
            super.onPostExecute(newsInfos);
            if(mSwipeRefreshLayout != null){
                mSwipeRefreshLayout.setRefreshing(false);
            }
//            出错：data没有获得到数据
//            原因：doinbackground的return在okgo请求期间就返回了
//            解决方式：在return之前让线程休眠1秒
            if(mList.size() == 0){
                mList.addAll(newsInfos);
                newsAdapter.notifyDataSetChanged();
            }else{
                //删除footer
                mList.remove(mList.size() -1);
                mList.addAll(newsInfos);
                newsAdapter.notifyDataSetChanged();
                loading =false;
            }
        }

        @Override
        protected List<NewsInfo> doInBackground(Integer... integers) {
            mMoreList = getNetDate();
            try {
                Thread.sleep(1000);//沉睡一秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return mMoreList;
        }
    }
}


