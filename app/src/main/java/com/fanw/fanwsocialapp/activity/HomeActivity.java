package com.fanw.fanwsocialapp.activity;

import android.os.Bundle;
import android.view.Menu;

import com.fanw.fanwsocialapp.R;
import com.fanw.fanwsocialapp.base.BaseActivity;

/**
 * Created by fanw on 2018/2/4.
 */

public class HomeActivity extends BaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    public void initViews() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
