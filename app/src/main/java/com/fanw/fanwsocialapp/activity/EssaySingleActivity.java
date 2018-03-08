package com.fanw.fanwsocialapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.ConsumerIrManager;
import android.os.AsyncTask;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fanw.fanwsocialapp.R;
import com.fanw.fanwsocialapp.adapter.CommentAdapter;
import com.fanw.fanwsocialapp.application.GlideApp;
import com.fanw.fanwsocialapp.callback.EssayReceiver;
import com.fanw.fanwsocialapp.callback.JsonCallback;
import com.fanw.fanwsocialapp.common.Constants;
import com.fanw.fanwsocialapp.listener.OnItemClickListener;
import com.fanw.fanwsocialapp.model.Comment;
import com.fanw.fanwsocialapp.model.Essay;
import com.fanw.fanwsocialapp.widget.CircleImageView;
import com.fanw.fanwsocialapp.widget.SquareImageView;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class EssaySingleActivity extends AppCompatActivity {

    private Essay mEssay;
    private Toolbar mToolbar;
    private AppBarLayout mAppBar;
    private CircleImageView essay_single_user_head;
    private TextView essay_single_user_name;
    private TextView essay_single_time;
    private TextView essay_single_thumbs_count;
    private ImageView essay_single_thumbs_pic;
    private TextView essay_single_content;
    private SquareImageView essay_single_iv_left;
    private SquareImageView essay_single_iv_middle;
    private SquareImageView essay_single_iv_right;
    private ButtonBarLayout essay_single_playButton;
    private RecyclerView essay_single_comments;
    private TextView new_comment_content;
    private ImageView new_comment_send;
    private SwipeRefreshLayout comments_swipe;
    private CircleImageView essay_single_playButton_user_head;
    private CollapsingToolbarLayoutState state;
    private Context mContext;
    private List<Comment> commentList = new ArrayList<Comment>();
    private List<Comment> moreCommentList = new ArrayList<Comment>();
    private CommentAdapter commentAdapter;

    private enum CollapsingToolbarLayoutState {
        EXPANDED,
        COLLAPSED,
        INTERNEDIATE
    }

    private void initData(){
        mEssay = (Essay) getIntent().getSerializableExtra("essay_single");
    }

    private void initView(){
        mAppBar = findViewById(R.id.essay_single_app_bar);
        mToolbar = findViewById(R.id.essay_single_toolbar);
        essay_single_comments = findViewById(R.id.essay_single_comments);
        essay_single_content = findViewById(R.id.essay_single_content);
        essay_single_iv_left = findViewById(R.id.essay_single_iv_left);
        essay_single_iv_middle = findViewById(R.id.essay_single_iv_middle);
        essay_single_iv_right = findViewById(R.id.essay_single_iv_right);
        essay_single_playButton = findViewById(R.id.essay_single_playButton);
        essay_single_thumbs_count = findViewById(R.id.essay_single_thumbs_count);
        essay_single_thumbs_pic = findViewById(R.id.essay_single_thumbs_pic);
        essay_single_time = findViewById(R.id.essay_single_time);
        essay_single_user_head = findViewById(R.id.essay_single_user_head);
        essay_single_user_name = findViewById(R.id.essay_single_user_name);
        comments_swipe = findViewById(R.id.comments_swipe);
        new_comment_content = findViewById(R.id.new_comment_content);
        new_comment_send = findViewById(R.id.new_comment_send);
        essay_single_playButton_user_head = findViewById(R.id.essay_single_playButton_user_head);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_essay_single);
        mContext = getApplicationContext();
        initView();
        initData();
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initSight();
        mAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    if (state != CollapsingToolbarLayoutState.EXPANDED) {
                        state = CollapsingToolbarLayoutState.EXPANDED;//修改状态标记为展开
//                        collapsingToolbarLayout.setTitle("EXPANDED");//设置title为EXPANDED
                    }
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    if (state != CollapsingToolbarLayoutState.COLLAPSED) {
//                        collapsingToolbarLayout.setTitle("");//设置title不显示
                        essay_single_playButton.setVisibility(View.VISIBLE);//隐藏播放按钮
                        state = CollapsingToolbarLayoutState.COLLAPSED;//修改状态标记为折叠
                    }
                } else {
                    if (state != CollapsingToolbarLayoutState.INTERNEDIATE) {
                        if(state == CollapsingToolbarLayoutState.COLLAPSED){
                            essay_single_playButton.setVisibility(View.GONE);//由折叠变为中间状态时隐藏播放按钮
                        }
//                        collapsingToolbarLayout.setTitle("INTERNEDIATE");//设置title为INTERNEDIATE
                        state = CollapsingToolbarLayoutState.INTERNEDIATE;//修改状态标记为中间
                    }
                }
            }
        });

        commentAdapter = new CommentAdapter(commentList,mContext);
        essay_single_comments.setHasFixedSize(true);
        essay_single_comments.setLayoutManager(new LinearLayoutManager(mContext));
        essay_single_comments.setAdapter(commentAdapter);
        commentAdapter.setOnItemClickListener(onItemClickListener);
        //设置初始状态加载动画
        comments_swipe.setColorSchemeColors(Color.RED,Color.BLUE);
        comments_swipe.post(new Runnable() {
            @Override
            public void run() {
                comments_swipe.setRefreshing(true);
                new LatestCommentTask().execute();
            }
        });
    }

    //等待后端添加分页属性
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
                page = page+20;
                new LatestEssayTask().execute();
            }
        }
    };*/

    /**
     * Item点击监听
     */
    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int position , View v) {
            switch (v.getId()){
                case R.id.comment_item_user_head:
                    Intent intent = new Intent(mContext,PersonalActivity.class);
                    intent.putExtra("user_id",commentList.get(position).getUser().getUser_id());
                    startActivity(intent);
                case R.id.new_comment_send:
                    SharedPreferences pre = getSharedPreferences("current_user", Context.MODE_PRIVATE);
                    int user_id = pre.getInt("user_id",0);
                    String comment_content = new_comment_content.getText().toString();
                    if (user_id != 0 && !comment_content.equals("")){
                        createOneComment(comment_content,user_id,mEssay.getEssay_id());
                    }

                default:
                /*case R.id.cv_Delete:
                    Snackbar.make(v,"del",Snackbar.LENGTH_LONG).show();
                    mList.remove(position);
                    newsAdapter.notifyDataSetChanged();
                    break;*/
            }
        }
    };

    private void createOneComment(String comment_content,int user_id,int essay_id){
        OkGo.<EssayReceiver>post(Constants.ESSAY_URL+Constants.ESSAY_comment+"/create")
                .tag(this)
                .params("user_id",user_id)
                .params("essay_id",essay_id)
                .params("comment_content",comment_content)
                .execute(new JsonCallback<EssayReceiver>() {
                    @Override
                    public void onSuccess(Response<EssayReceiver> response) {
                        if (response.body().getCode() == 200 && response.body().getMsg().equals("success")){
                            comments_swipe.setRefreshing(true);
                            new LatestCommentTask().execute();
                        }else {
                            Snackbar.make(getWindow().getDecorView(),response.body().getMsg(),Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void initSight() {
        essay_single_user_name.setText(mEssay.getUser().getUser_name());
        essay_single_time.setText(mEssay.getEssay_date().toString());
        essay_single_thumbs_count.setText(String.valueOf(mEssay.getEssay_thumbs()));
        essay_single_content.setText(mEssay.getEssay_content());
        GlideApp.with(mContext)
                .load(Constants.ESSAY_URL+Constants.ESSAY_HEAD+mEssay.getUser().getUser_head())
                .format(DecodeFormat.PREFER_ARGB_8888)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.drawable.ic_load_fail)
                .into(essay_single_playButton_user_head);
        GlideApp.with(mContext)
                .load(Constants.ESSAY_URL+Constants.ESSAY_HEAD+mEssay.getUser().getUser_head())
                .format(DecodeFormat.PREFER_ARGB_8888)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.drawable.ic_load_fail)
                .into(essay_single_user_head);
        GlideApp.with(mContext)
                .load(Constants.ESSAY_URL+Constants.ESSAY_PIC+mEssay.getEssay_pic_1())
                .format(DecodeFormat.PREFER_ARGB_8888)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.drawable.ic_load_fail)
                .into(essay_single_iv_left);
        GlideApp.with(mContext)
                .load(Constants.ESSAY_URL+Constants.ESSAY_PIC+mEssay.getEssay_pic_2())
                .format(DecodeFormat.PREFER_ARGB_8888)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.drawable.ic_load_fail)
                .into(essay_single_iv_middle);
        GlideApp.with(mContext)
                .load(Constants.ESSAY_URL+Constants.ESSAY_PIC+mEssay.getEssay_pic_3())
                .format(DecodeFormat.PREFER_ARGB_8888)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.drawable.ic_load_fail)
                .into(essay_single_iv_right);
    }

    protected void getNetData(){
        OkGo.<EssayReceiver>get(Constants.ESSAY_URL+Constants.ESSAY_comment+"/showAll")
                .tag(this)
                .execute(new JsonCallback<EssayReceiver>() {
                    @Override
                    public void onSuccess(Response<EssayReceiver> response) {
                        moreCommentList = response.body().getCommentList();
                    }
                });
    }

    protected class LatestCommentTask extends AsyncTask<Integer,Void,List<Comment>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //需要修改后端添加分页属性
            /*if(commentList != null&&commentList.size()>0){
                //添加footer
                commentList.add(null);
                // notifyItemInserted(int position)，这个方法是在第position位置
                // 被插入了一条数据的时候可以使用这个方法刷新，
                // 注意这个方法调用后会有插入的动画，这个动画可以使用默认的，也可以自己定义。
                commentAdapter.notifyItemInserted(commentList.size() -1);
            }*/
        }

        @Override
        protected void onPostExecute(List<Comment> comments) {
            super.onPostExecute(comments);
            if(comments_swipe != null){
                comments_swipe.setRefreshing(false);
            }
            if(commentList.size() == 0){
                commentList.addAll(comments);
                commentAdapter.notifyDataSetChanged();
            }else{
                //删除footer
                commentList.remove(commentList.size() -1);
                commentList.addAll(comments);
                commentAdapter.notifyDataSetChanged();
//                loading =false;
            }
        }

        @Override
        protected List<Comment> doInBackground(Integer... integers) {
            getNetData();
            try {
                Thread.sleep(1500);//沉睡一秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return moreCommentList;
        }
    }
}
