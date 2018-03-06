package com.fanw.fanwsocialapp.base;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fanw.fanwsocialapp.R;
import com.fanw.fanwsocialapp.activity.HomeActivity;
import com.fanw.fanwsocialapp.activity.LoginActivity;
import com.fanw.fanwsocialapp.activity.NewsActivity;
import com.fanw.fanwsocialapp.activity.PhotoActivity;
import com.fanw.fanwsocialapp.activity.RegisterActivity;
import com.fanw.fanwsocialapp.application.GlideApp;
import com.fanw.fanwsocialapp.application.MyApplication;
import com.fanw.fanwsocialapp.common.Constants;
import com.fanw.fanwsocialapp.util.MyUtils;
import com.fanw.fanwsocialapp.widget.CircleImageView;
import com.fanw.fanwsocialapp.widget.RoundImageView;
import com.lzy.okgo.OkGo;
import com.squareup.leakcanary.RefWatcher;

import org.w3c.dom.Text;

import java.io.File;

public abstract class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, DrawerLayout.DrawerListener {
    //夜间模式判断
    private boolean mIsChangeTheme;
    //
    private WindowManager mWindowManager = null;
    private View mNightView = null;
    private boolean mIsAddedView;
    //NavigationView准备
//    protected boolean mIsHasNavigationView;//暂时不使用bufferKnife
    private DrawerLayout mDrawerLayout;
    private Class mClass;
    protected NavigationView mBaseNavView;
    protected LinearLayout login_activity_group;
    protected  LinearLayout user_info;
    protected View drawHeader;
    protected FloatingActionButton mFloatingActionButton;
    protected TextView login_tv;
    protected TextView register_tv;
    protected View login_view;
    protected View user_view;
    protected CircleImageView hd_avatar;
    protected TextView hd_name;
//    protected LinearLayout ups_count;
//    protected LinearLayout fans_count;
    MyApplication myApplication;

    public abstract int getLayoutId();

    public abstract void initViews();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int layoutId = getLayoutId();
        setContentView(layoutId);
        initViews();
        //获取application
        myApplication = (MyApplication)getApplication();
        //初始化toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //每个页面显示的不一样
        /*mFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        //获得DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //设置点击之后nav收回的监听事件
        mDrawerLayout.addDrawerListener(this);
        //初始化左上方的三横杆旋转效果
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        //获取到NavigationView
        mBaseNavView = (NavigationView) findViewById(R.id.nav_view);
        mBaseNavView.setNavigationItemSelectedListener(this);
        //获取头部
        drawHeader = mBaseNavView.getHeaderView(0);
        login_view = (View) drawHeader.findViewById(R.id.login_activity_group_include);
        login_tv = (TextView) login_view.findViewById(R.id.login_activity);
        register_tv = (TextView) login_view.findViewById(R.id.register_activity);
        login_tv.setOnClickListener(this);
        register_tv.setOnClickListener(this);
        user_view = (View) drawHeader.findViewById(R.id.user_info_include);
        hd_avatar = (CircleImageView) user_view.findViewById(R.id.hd_avatar);
        hd_name = (TextView)user_view.findViewById(R.id.hd_name);
        hd_avatar.setOnClickListener(this);
        hd_name.setOnClickListener(this);

        final File current_user= new File("/data/data/"+getPackageName()+"/shared_prefs","current_user.xml");
        if (current_user.exists()){
            login_view.setVisibility(View.GONE);
            user_view.setVisibility(View.VISIBLE);
            SharedPreferences pre = getSharedPreferences("current_user", Context.MODE_PRIVATE);
            if(!pre.getString("user_head","").equals(Constants.ESSAY_URL+Constants.ESSAY_HEAD)){
                GlideApp.with(this)
                        .load(pre.getString("user_head",""))
                        .placeholder(R.drawable.ic_loading)
                        .error(R.drawable.ic_load_fail)
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(hd_avatar);
            }
            hd_name.setText(pre.getString("user_name",""));
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.base, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.nav_essay:
                mClass = HomeActivity.class;
                break;
            case R.id.nav_news:
                mClass = NewsActivity.class;
                break;
            case R.id.nav_photo:
                mClass = PhotoActivity.class;
                break;
            case R.id.nav_message:
                break;
            case R.id.nav_comment:
                break;
            case R.id.nav_search:
                break;
            case R.id.nav_night_mode:
                break;
            case R.id.nav_about:
                break;
            case R.id.nav_setting:
                break;
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /*
    * 登陆注册点击事件
    * */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent = new Intent();
        switch (id){
            case R.id.login_activity:
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.register_activity:
                intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.hd_avatar:
                break;
            case R.id.hd_name:
                break;
//            case R.id.fans_count:
//                break;
//            case R.id.ups_count:
//                break;
            default:
        }
    }

    //登出提示
    protected void userLogout()
    {
        //判断当前是否有用户信息
//        if (myApplication.person.getUserId().length() > 0) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);
//            builder.setTitle("提示");
//            builder.setMessage("确定退出当前用户吗?");
//            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                }
//            });
//            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    //填写登出事件
//                }
//            });
//            builder.show();
//        }else{
//            Toast.makeText(BaseActivity.this, "你还没有登录哦", Toast.LENGTH_SHORT).show();
//
//        }
    }

    @Override
    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(@NonNull View drawerView) {

    }

    @Override
    public void onDrawerClosed(@NonNull View drawerView) {
        if (mClass != null){
            Intent intent = new Intent(BaseActivity.this, mClass);
            // 此标志用于启动一个Activity的时候，若栈中存在此Activity实例，则把它调到栈顶。不创建多一个
//                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            overridePendingTransition(0, 0);
            mClass = null;
        }
        if (mIsChangeTheme){
            mIsChangeTheme = false;
            getWindow().setWindowAnimations(R.style.WindowAnimationFadeInOut);
            recreate();
        }
    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

    //设置夜/日模式
    private void setNightOrDayMode() {
        if (MyUtils.isNightMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

            initNightView();
            mNightView.setBackgroundResource(R.color.night_mask);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    //初始化夜间模块
    private void initNightView() {
        if (mIsAddedView) {
            return;
        }
        // 增加夜间模式蒙板
        WindowManager.LayoutParams nightViewParam = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSPARENT);
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mNightView = new View(this);
        mWindowManager.addView(mNightView, nightViewParam);
        mIsAddedView = true;
    }

    //切换到日间模式
    public void changeToDay() {
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        mNightView.setBackgroundResource(android.R.color.transparent);
    }

    //切换到夜间模式
    public void changeToNight() {
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        initNightView();
        mNightView.setBackgroundResource(R.color.night_mask);
    }

    //当ActivityA的LaunchMode为SingleTop时，如果ActivityA在栈顶,且现在要再启动ActivityA，这时会调用onNewIntent()方法
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        overridePendingTransition(0, 0);
//        if (mIsHasNavigationView) {
//            overridePendingTransition(0, 0);
//        }
//        getWindow().getDecorView().invalidate();
    }

    //重写销毁
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //结束OkGo
        OkGo.getInstance().cancelTag(this);
        //内存泄漏问题
        RefWatcher refWatcher = myApplication.getRefWatcher(this);
        refWatcher.watch(this);


//        removeNightModeMask();
    }

    private void removeNightModeMask() {
        if (mIsAddedView) {
            // 移除夜间模式蒙板
            mWindowManager.removeViewImmediate(mNightView);
            mWindowManager = null;
            mNightView = null;
        }
    }
}
