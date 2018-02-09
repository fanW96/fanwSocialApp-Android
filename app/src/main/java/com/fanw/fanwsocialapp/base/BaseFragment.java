package com.fanw.fanwsocialapp.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanw.fanwsocialapp.R;
import com.lzy.okgo.OkGo;
import com.squareup.leakcanary.RefWatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fanw on 2018/2/4.
 */

public class BaseFragment<T> extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    protected static final String ARG_PARAM = "param";
    protected String url;
    private View news_fragment;
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected RecyclerView mRecyclerView;
    //获取 fragment 依赖的 Activity，方便使用 Context
    protected Activity mAct;
    protected boolean loading = false;
    protected List<T> mList = new ArrayList<T>();
    protected List<T> mMoreList = new ArrayList<T>();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param Parameter.
     * @return A new instance of fragment BaseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BaseFragment BaseInstance(String param) {
        BaseFragment fragment = new BaseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        url = args != null
                ? args.getString(ARG_PARAM) : "";
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        news_fragment = inflater.inflate(R.layout.fragment_base,container,false);
        mAct = getActivity();
        mSwipeRefreshLayout = (SwipeRefreshLayout) news_fragment.findViewById(R.id.news_swipeRefreshLayout);
        mRecyclerView = (RecyclerView) news_fragment.findViewById(R.id.news_recyclerView);
        return news_fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mAct));
        /*//设置滚动监听
        mRecyclerView.addOnScrollListener(mScrollListener);*/
        //设置刷新图案的颜色
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED,Color.BLUE);
        /*//设置下拉刷新的事件
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mList.clear();
                new BaseTask().execute();
            }
        });
        //设置初始状态加载动画
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                new BaseTask().execute();
            }
        });*/
    }

    /*RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
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
                new BaseTask().execute();
            }
        }
    };*/

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected void getNetDate(){
        
    }

}
