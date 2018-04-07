package com.fanw.fanwsocialapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.fanw.fanwsocialapp.R;
import com.fanw.fanwsocialapp.adapter.ChatPersonAdapter;
import com.fanw.fanwsocialapp.base.BaseActivity;
import com.fanw.fanwsocialapp.callback.EssayReceiver;
import com.fanw.fanwsocialapp.callback.JsonCallback;
import com.fanw.fanwsocialapp.common.Constants;
import com.fanw.fanwsocialapp.listener.OnItemClickListener;
import com.fanw.fanwsocialapp.model.Relation;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends BaseActivity {
    private RecyclerView chat_rv;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Context context;
    private ChatPersonAdapter chatPersonAdapter;
    private List<Relation> userList = new ArrayList<>();
    private List<Relation> moreUserLIst = new ArrayList<>();
    private int current_user_id;

    @Override
    public int getLayoutId() {
        return R.layout.activity_chat;
    }

    @Override
    public void initViews() {
        chat_rv = findViewById(R.id.chat_rv);
        mSwipeRefreshLayout = findViewById(R.id.chat_fresh);
        final File current_user= new File("/data/data/"+getPackageName()+"/shared_prefs","current_user.xml");
        if (current_user.exists()){
            SharedPreferences pre = getSharedPreferences("current_user", Context.MODE_PRIVATE);
            current_user_id = pre.getInt("user_id",0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        chatPersonAdapter = new ChatPersonAdapter(userList,context);
        chat_rv.setHasFixedSize(true);
        chat_rv.setLayoutManager(new LinearLayoutManager(context));
        //设置刷新图案的颜色
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED,Color.BLUE);
        chat_rv.setAdapter(chatPersonAdapter);
        chatPersonAdapter.setOnItemClickListener(onItemClickListener);
        //设置下拉刷新的事件
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                userList.clear();
                new LatestUserTask().execute();
            }
        });
        //设置初始状态加载动画
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                new LatestUserTask().execute();
            }
        });
    }

    /**
     * Item点击监听
     */
    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int position , View v) {
//            Toast.makeText(context, "123", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context,MessageActivity.class);
                    intent.putExtra("toSb",userList.get(position).getUp().getUser_name());
                    startActivity(intent);

        }
    };

    protected void getNetData(){
        OkGo.<EssayReceiver>post(Constants.ESSAY_URL+Constants.ESSAY_RELATION+"/showAllUps")
                .params("user_id",current_user_id)
                .execute(new JsonCallback<EssayReceiver>() {
                    @Override
                    public void onSuccess(Response<EssayReceiver> response) {
                        if(response.body().getCode() == 200 && response.body().getMsg().equals("success")){
                            moreUserLIst = response.body().getRelationList();
                        }
                    }
                });

    }

    protected class LatestUserTask extends AsyncTask<Integer,Void,List<Relation>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<Relation> users) {
            super.onPostExecute(users);
            /*
            * essays与moreEssayList相关联，只能在录入到essayList之后删除
            * */
//            moreEssayList.clear();
            if(mSwipeRefreshLayout != null){
                mSwipeRefreshLayout.setRefreshing(false);
            }
            if(userList.size() == 0){
                userList.addAll(users);
                moreUserLIst.clear();
                chatPersonAdapter.notifyDataSetChanged();
            }
        }

        @Override
        protected List<Relation> doInBackground(Integer... integers) {
            getNetData();
            try {
                Thread.sleep(1500);//沉睡一秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return moreUserLIst;
        }
    }
}
