package com.fanw.fanwsocialapp.activity;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fanw.fanwsocialapp.R;
import com.fanw.fanwsocialapp.application.GlideApp;
import com.fanw.fanwsocialapp.callback.EssayReceiver;
import com.fanw.fanwsocialapp.callback.JsonCallback;
import com.fanw.fanwsocialapp.common.Constants;
import com.fanw.fanwsocialapp.model.Profile;
import com.fanw.fanwsocialapp.widget.CircleImageView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

public class PersonModifyActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private CircleImageView person_modify_head;
    private EditText person_modify_name;
    private EditText person_modify_sex;
    private EditText person_modify_location;
    private EditText person_modify_sign;
    private TextView person_modify_confirm;
    private Context mContext;
    private String head_url;
    private String name;
    private String sex;
    private String location;
    private String sign;
    private Profile mProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_modify);
        mContext = getApplicationContext();
        initView();
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initData();
        person_modify_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm();
            }
        });
    }

    private void initData(){
        mProfile = (Profile) getIntent().getSerializableExtra("profile_data");
        head_url = mProfile.getUser().getUser_head();
        GlideApp.with(mContext)
                .load(Constants.ESSAY_URL+Constants.ESSAY_HEAD+mProfile.getUser().getUser_head())
                .format(DecodeFormat.PREFER_ARGB_8888)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.drawable.ic_load_fail)
                .into(person_modify_head);
        person_modify_name.setHint(mProfile.getUser().getUser_name());
        //使用三元条件设置性别
        person_modify_sex.setHint(mProfile.isProfile_sex()?"male":"female");
        person_modify_location.setHint(mProfile.getProfile_location());
        person_modify_sign.setHint(mProfile.getProfile_sign());
    }

    private void confirm(){
        getInfo();
        modifyProfile();
    }

    private void modifyProfile(){
        OkGo.<EssayReceiver>post(Constants.ESSAY_URL+Constants.ESSAY_PROFILE+"/updateProfile")
                .params("profile_id",mProfile.getProfile_id())
                .params("profile_sex",sex.equals("male")?1:0)
                .params("profile_location",location)
                .params("profile_sign",sign)
                .execute(new JsonCallback<EssayReceiver>() {
                    @Override
                    public void onSuccess(Response<EssayReceiver> response) {
                        if(response.body().getCode() == 200 && response.body().getMsg().equals("success")){
                            finish();
                        }else{
                            Snackbar.make(findViewById(R.id.person_modify_layout),response.body().getMsg(),Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void getInfo(){
        sex = person_modify_sex.getText().toString();
        location = person_modify_location.getText().toString();
        sign = person_modify_sign.getText().toString();
    }

    private void initView(){
        mToolbar = findViewById(R.id.person_modify_toolbar);
        person_modify_head = findViewById(R.id.person_modify_head);
        person_modify_name = findViewById(R.id.person_modify_name);
        person_modify_sex = findViewById(R.id.person_modify_sex);
        person_modify_location = findViewById(R.id.person_modify_location);
        person_modify_sign = findViewById(R.id.person_modify_sign);
        person_modify_confirm = findViewById(R.id.person_modify_confirm);
    }

    private boolean checkSex(String sex){
        if(sex.equals("男")){
            return true;
        }else {
            return false;
        }
    }
}
