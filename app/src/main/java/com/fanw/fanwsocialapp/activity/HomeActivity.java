package com.fanw.fanwsocialapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fanw.fanwsocialapp.R;
import com.fanw.fanwsocialapp.adapter.EssayAdapter;
import com.fanw.fanwsocialapp.application.GlideApp;
import com.fanw.fanwsocialapp.base.BaseActivity;
import com.fanw.fanwsocialapp.callback.EssayReceiver;
import com.fanw.fanwsocialapp.callback.JsonCallback;
import com.fanw.fanwsocialapp.common.Constants;
import com.fanw.fanwsocialapp.listener.OnItemClickListener;
import com.fanw.fanwsocialapp.model.Essay;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fanw on 2018/2/4.
 */

public class HomeActivity extends BaseActivity {

    private RecyclerView mEssayRv;
    private TextView mEmptyView;//为空重新加载的页面
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Context context;
    private EssayAdapter mEssayAdapter;
    private List<Essay> essayList = new ArrayList<Essay>();
    private List<Essay> moreEssayList = new ArrayList<Essay>();
    private FloatingActionButton mFloat;
    private int page = 1;
    private boolean loading = false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    public void initViews() {
        mEssayRv = findViewById(R.id.essay_rv);
        mEmptyView = findViewById(R.id.essay_empty_view);
        mSwipeRefreshLayout = findViewById(R.id.essay_swipe_refresh_layout);
        mFloat = findViewById(R.id.essay_fab);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        final File current_user= new File("/data/data/"+getPackageName()+"/shared_prefs","current_user.xml");
//        if (current_user.exists()){
//            login_view.setVisibility(View.GONE);
//            user_view.setVisibility(View.VISIBLE);
//            SharedPreferences pre = getSharedPreferences("current_user", Context.MODE_PRIVATE);
//            if(!pre.getString("user_head","").equals(Constants.ESSAY_URL+Constants.ESSAY_HEAD)){
//                GlideApp.with(this)
//                        .load(pre.getString("user_head",""))
//                        .placeholder(R.drawable.ic_loading)
//                        .error(R.drawable.ic_load_fail)
//                        .format(DecodeFormat.PREFER_ARGB_8888)
//                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .into(hd_avatar);
//            }
//            hd_name.setText(pre.getString("user_name",""));
//        }
        context = getApplicationContext();
        mEssayAdapter = new EssayAdapter(essayList,context);
        mEssayRv.setHasFixedSize(true);
        mEssayRv.setLayoutManager(new LinearLayoutManager(context));
        //设置刷新图案的颜色
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED,Color.BLUE);
        mEssayRv.setAdapter(mEssayAdapter);

        mEssayAdapter.setOnItemClickListener(onItemClickListener);

        mFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,EssayAddActivity.class);
                startActivity(intent);
            }
        });

        //设置滚动监听
        mEssayRv.addOnScrollListener(mScrollListener);

        //设置下拉刷新的事件
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                essayList.clear();
                page = 1;
                new LatestEssayTask().execute();
            }
        });
        //设置初始状态加载动画
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                new LatestEssayTask().execute();
            }
        });
    }

    //等待后端添加分页属性
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
                page = page+1;
                new LatestEssayTask().execute();
            }
        }
    };

    /**
     * Item点击监听
     */
    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int position , View v) {
            switch (v.getId()){
                case R.id.essay_item_user_head:
                case R.id.essay_photo_user_head:
                    Intent intent1 = new Intent(context,PersonalActivity.class);
                    intent1.putExtra("user_id",essayList.get(position).getUser().getUser_id());
                    startActivity(intent1);
                    break;
                case R.id.essay_photo_iv_left:
                case R.id.essay_photo_iv_middle:
                case R.id.essay_photo_iv_right:
                    Intent intent = new Intent(context,EssayPhotoDetailActivity.class);
                    intent.putExtra("essay_data",essayList.get(position));
                    startActivity(intent);
                    break;
                default:
                    Intent essaySingle = new Intent(context,EssaySingleActivity.class);
                    essaySingle.putExtra("essay_single",essayList.get(position));
                    startActivity(essaySingle);
                /*case R.id.cv_Delete:
                    Snackbar.make(v,"del",Snackbar.LENGTH_LONG).show();
                    mList.remove(position);
                    newsAdapter.notifyDataSetChanged();
                    break;*/
            }
        }
    };

    protected void getNetData(){
        OkGo.<EssayReceiver>get(Constants.ESSAY_URL+Constants.ESSAY_CONTENT+"/showAll/"+page+"/5")
                .tag(this)
                .execute(new JsonCallback<EssayReceiver>() {
                    @Override
                    public void onSuccess(Response<EssayReceiver> response) {
                        moreEssayList = response.body().getEssayList();
                    }
                });
    }

    protected class LatestEssayTask extends AsyncTask<Integer,Void,List<Essay>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //需要修改后端添加分页属性
            if(essayList != null&&essayList.size()>0){
                //添加footer
//                NewsInfo newsInfoTemp = new NewsInfo();
                essayList.add(null);
                // notifyItemInserted(int position)，这个方法是在第position位置
                // 被插入了一条数据的时候可以使用这个方法刷新，
                // 注意这个方法调用后会有插入的动画，这个动画可以使用默认的，也可以自己定义。
                mEssayAdapter.notifyItemInserted(essayList.size() -1);
            }
        }

        @Override
        protected void onPostExecute(List<Essay> essays) {
            super.onPostExecute(essays);
            /*
            * essays与moreEssayList相关联，只能在录入到essayList之后删除
            * */
//            moreEssayList.clear();
            if(mSwipeRefreshLayout != null){
                mSwipeRefreshLayout.setRefreshing(false);
            }
            if(essayList.size() == 0){
                essayList.addAll(essays);
                checkIsEmpty(essayList);
                moreEssayList.clear();
                mEssayAdapter.notifyDataSetChanged();
            }else{
                //删除footer
                essayList.remove(essayList.size() -1);
                essayList.addAll(essays);
                mEssayAdapter.notifyDataSetChanged();
                loading =false;
            }
        }

        @Override
        protected List<Essay> doInBackground(Integer... integers) {
            getNetData();
            try {
                Thread.sleep(1500);//沉睡一秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return moreEssayList;
        }
    }

    private void checkIsEmpty(List<Essay> essays) {
        if (essays == null && mEssayAdapter.getList() == null) {
            mEssayRv.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);

        } else {
            mEssayRv.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
