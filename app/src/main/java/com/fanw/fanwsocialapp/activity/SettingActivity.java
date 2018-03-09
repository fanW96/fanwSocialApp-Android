package com.fanw.fanwsocialapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.fanw.fanwsocialapp.R;
import com.fanw.fanwsocialapp.base.BaseActivity;
import com.fanw.fanwsocialapp.callback.EssayReceiver;
import com.fanw.fanwsocialapp.callback.JsonCallback;
import com.fanw.fanwsocialapp.common.Constants;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.io.File;

/**
 * Created by fanw on 2018/3/9.
 */

public class SettingActivity extends AppCompatActivity {

    private TextView btn_logout;
    private int user_id;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        btn_logout = findViewById(R.id.btn_logout);
        mToolbar = findViewById(R.id.setting_toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    private void logout(){
        SharedPreferences pre = getSharedPreferences("current_user", Context.MODE_PRIVATE);
        user_id = pre.getInt("user_id",0);
        OkGo.<EssayReceiver>post(Constants.ESSAY_URL+Constants.ESSAY_USER+"/signOut")
                .tag(this)
                .params("user_id",user_id)
                .execute(new JsonCallback<EssayReceiver>() {
                    @Override
                    public void onSuccess(Response<EssayReceiver> response) {
                        if (response.body().getCode() == 200 && response.body().getMsg().equals("success")){
                            File file= new File("/data/data/"+getPackageName()+"/shared_prefs","current_user.xml");
                            if(file.exists()){
                                file.delete();
                            }
                            Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }
}
