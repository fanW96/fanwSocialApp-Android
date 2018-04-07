package com.fanw.fanwsocialapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fanw.fanwsocialapp.R;
import com.fanw.fanwsocialapp.adapter.PersonEssayAdapter;
import com.fanw.fanwsocialapp.application.GlideApp;
import com.fanw.fanwsocialapp.callback.EssayReceiver;
import com.fanw.fanwsocialapp.callback.JsonCallback;
import com.fanw.fanwsocialapp.common.Constants;
import com.fanw.fanwsocialapp.listener.OnItemClickListener;
import com.fanw.fanwsocialapp.model.Essay;
import com.fanw.fanwsocialapp.model.Profile;
import com.fanw.fanwsocialapp.model.User;
import com.fanw.fanwsocialapp.widget.CircleImageView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

public class PersonalActivity extends AppCompatActivity {
    private int user_id;
    private ImageView person_modify_or_follow;
    private ImageView person_user_sex;
    private TextView person_user_name;
    private TextView person_user_sign;
    private TextView person_user_location;
    private TextView person_user_follow_count;
    private TextView person_user_fan_count;
    private android.support.v7.widget.Toolbar mToolbar;
    private RecyclerView person_rv;
    private CircleImageView person_user_head;
    private SwipeRefreshLayout person_swipe_refresh;
    private Profile profile = new Profile();
    private List<Essay> essayList = new ArrayList<Essay>();
    private List<Essay> moreEssayList = new ArrayList<Essay>();
    private Context mContext;
    private PersonEssayAdapter personEssayAdapter;
    private int followCount = 0;
    private int fanCount = 0;
    private int page = 1;
    private boolean loading = false;

    private void initData(){
        user_id = getIntent().getIntExtra("user_id",0);
        getProfile();
        getFollowCount();
        getFanCount();
    }

    private void initView(){
        person_modify_or_follow = findViewById(R.id.person_modify_or_follow);
        person_modify_or_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                * 点击打开信息编辑界面或者是添加关注和取消关注
                * */
            }
        });
        person_user_sex = findViewById(R.id.person_user_sex);
        person_user_name =findViewById(R.id.person_user_name);
        person_user_location = findViewById(R.id.person_user_location);
        person_user_sign = findViewById(R.id.person_user_sign);
        person_user_follow_count  =findViewById(R.id.person_user_follow_count);
        person_user_fan_count = findViewById(R.id.person_user_fan_count);
        person_user_follow_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                * 打开关注列表
                * */
            }
        });
        person_user_fan_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                * 打开粉丝列表
                * */
            }
        });
        mToolbar = findViewById(R.id.person_toolbar);
        person_user_head = findViewById(R.id.person_user_head);
        person_user_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,PhotoDetailActivity.class);
                intent.putExtra("picUrl",Constants.ESSAY_URL+Constants.ESSAY_HEAD+profile.getUser().getUser_head());
                intent.putExtra("title","头像");
                startActivity(intent);
            }
        });
        person_rv = findViewById(R.id.person_rv);
        person_swipe_refresh = findViewById(R.id.person_swipe_refresh);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        mContext = getApplicationContext();
        initView();
        initData();
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        initProfile();
        personEssayAdapter = new PersonEssayAdapter(essayList,mContext);
        person_rv.setHasFixedSize(true);
        person_rv.setLayoutManager(new LinearLayoutManager(mContext));
        person_rv.setAdapter(personEssayAdapter);
        personEssayAdapter.setOnItemClickListener(onItemClickListener);
        person_rv.addOnScrollListener(mScrollListener);
        person_user_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(mContext,ModifyHeadActivity.class);
                intent.putExtra("head_url",Constants.ESSAY_URL+Constants.ESSAY_HEAD+profile.getUser().getUser_head());
                startActivityForResult(intent,0);
            }
        });
//        personEssayAdapter.setHeaderView(header);
        //设置初始状态加载动画
        person_swipe_refresh.setColorSchemeColors(Color.RED,Color.BLUE);
        //下拉刷新
        person_swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                essayList.clear();
                page = 1;
                new LatestEssayTask().execute();
            }
        });
        //初始加载
        person_swipe_refresh.post(new Runnable() {
            @Override
            public void run() {
                person_swipe_refresh.setRefreshing(true);
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
        public void onItemClick(final int position , View v) {
            switch (v.getId()){
                case R.id.person_essay_user_head:
                    Intent intent1 = new Intent(mContext,PersonalActivity.class);
                    intent1.putExtra("user_id",essayList.get(position).getUser().getUser_id());
                    startActivity(intent1);
                    break;
                case R.id.person_essay_iv_left:
                case R.id.person_essay_iv_middle:
                case R.id.person_essay_iv_right:
                    Intent intent = new Intent(mContext,EssayPhotoDetailActivity.class);
                    intent.putExtra("essay_data",essayList.get(position));
                    startActivity(intent);
                    break;
                case R.id.cv_Delete:
                    OkGo.<EssayReceiver>post(Constants.ESSAY_URL+Constants.ESSAY_CONTENT+"/delete")
                            .params("essay_id",essayList.get(position).getEssay_id())
                            .tag(this)
                            .execute(new JsonCallback<EssayReceiver>() {
                                @Override
                                public void onSuccess(Response<EssayReceiver> response) {
                                    if (response.body().getCode() == 200 && response.body().getMsg().equals("success")){
                                        Snackbar.make(getWindow().getDecorView(),"del",Snackbar.LENGTH_LONG).show();
                                        essayList.remove(position);
                                        personEssayAdapter.notifyDataSetChanged();
                                    }
                                    else {
                                        Snackbar.make(getWindow().getDecorView(),response.body().getMsg(),Snackbar.LENGTH_LONG).show();
                                    }
                                }
                            });
                    break;
                default:
                    Intent essaySingle = new Intent(mContext,EssaySingleActivity.class);
                    essaySingle.putExtra("essay_single",essayList.get(position));
                    startActivity(essaySingle);
            }
        }
    };

    private void initProfile() {
        GlideApp.with(mContext)
                .load(Constants.ESSAY_URL+Constants.ESSAY_HEAD+profile.getUser().getUser_head())
                .format(DecodeFormat.PREFER_ARGB_8888)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.drawable.ic_load_fail)
                .into(person_user_head);
        SharedPreferences pre = getSharedPreferences("current_user", Context.MODE_PRIVATE);
        if (pre.getInt("user_id",0) != user_id){
            person_modify_or_follow.setImageResource(R.drawable.ic_add);
            //TODO:关注请求
        }else{
            person_modify_or_follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext,PersonModifyActivity.class);
                    intent.putExtra("profile_data",profile);
                    startActivity(intent);
                }
            });
        }
        person_user_name.setText(profile.getUser().getUser_name());
        if (profile.isProfile_sex() == true){
            person_user_sex.setImageResource(R.drawable.ic_sex_male);
        }
        person_user_sign.setText(profile.getProfile_sign());
        person_user_location.setText(profile.getProfile_location());
        person_user_follow_count.setText(String.valueOf(followCount));
        person_user_fan_count.setText(String.valueOf(fanCount));
    }

    private void getFollowCount(){
        OkGo.<EssayReceiver>post(Constants.ESSAY_URL+Constants.ESSAY_RELATION+"/upsCount")
                .params("user_id",user_id)
                .tag(this)
                .execute(new JsonCallback<EssayReceiver>() {
                    @Override
                    public void onSuccess(Response<EssayReceiver> response) {
                        if (response.body().getCode() == 200 && response.body().getMsg().equals("success")){
                            followCount = response.body().getCount();
                            getFanCount();
                        }
//                        else {
//                            Snackbar.make(getWindow().getDecorView(),response.body().getMsg(),Snackbar.LENGTH_LONG).show();
//                        }
                    }
                });
    }

    private void getFanCount(){
        OkGo.<EssayReceiver>post(Constants.ESSAY_URL+Constants.ESSAY_RELATION+"/fansCount")
                .params("user_id",user_id)
                .tag(this)
                .execute(new JsonCallback<EssayReceiver>() {
                    @Override
                    public void onSuccess(Response<EssayReceiver> response) {
                        if (response.body().getCode() == 200 && response.body().getMsg().equals("success")){
                            fanCount = response.body().getCount();
                            initProfile();
                        }
//                        else {
//                            Snackbar.make(getWindow().getDecorView(),response.body().getMsg(),Snackbar.LENGTH_LONG).show();
//                        }
                    }
                });
    }

    private void getProfile(){
        OkGo.<EssayReceiver>post(Constants.ESSAY_URL+Constants.ESSAY_PROFILE+"/showProfile")
                .params("user_id",user_id)
                .tag(this)
                .execute(new JsonCallback<EssayReceiver>() {
                    @Override
                    public void onSuccess(Response<EssayReceiver> response) {
                        if (response.body().getCode() == 200 && response.body().getMsg().equals("success")){
                            profile = response.body().getProfile();
                            getFollowCount();
                        }else {
                            Snackbar.make(getWindow().getDecorView(),response.body().getMsg(),Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void getEssay(){
        OkGo.<EssayReceiver>post(Constants.ESSAY_URL+Constants.ESSAY_CONTENT+"/showOneUser/"+page+"/5")
                .params("user_id",user_id)
                .tag(this)
                .execute(new JsonCallback<EssayReceiver>() {
                    @Override
                    public void onSuccess(Response<EssayReceiver> response) {
                        moreEssayList = response.body().getEssayList();
                    }
                });
    }

    protected class LatestEssayTask extends AsyncTask<Integer,Void,List<Essay>> {
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
                personEssayAdapter.notifyItemInserted(essayList.size() );
            }
        }

        @Override
        protected void onPostExecute(List<Essay> essays) {
            super.onPostExecute(essays);

            if(person_swipe_refresh != null){
                person_swipe_refresh.setRefreshing(false);
            }
            if(essayList.size() == 0){
                essayList.addAll(essays);
                moreEssayList.clear();
                personEssayAdapter.notifyDataSetChanged();
            }else{
                //删除footer
                essayList.remove(essayList.size() -1);
                essayList.addAll(essays);
                personEssayAdapter.notifyDataSetChanged();
                loading =false;
            }
        }

        @Override
        protected List<Essay> doInBackground(Integer... integers) {
            getEssay();
            try {
                Thread.sleep(1500);//沉睡一秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return moreEssayList;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
