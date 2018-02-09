package com.fanw.fanwsocialapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fanw.fanwsocialapp.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText login_input_ep;
    private EditText login_input_password;
    private AppCompatButton login_btn_login;
    private AppCompatButton btn_login_weibo;
    private AppCompatButton btn_login_qq;
    private TextView login_link_signUp;

    private String user_email;
    private String user_phone;
    private String user_pwd;

    private void initView(){
        login_input_ep = findViewById(R.id.login_input_ep);
        login_input_password = findViewById(R.id.login_input_password);
        login_btn_login = findViewById(R.id.login_btn_login);
        btn_login_weibo = findViewById(R.id.btn_login_weibo);
        btn_login_qq = findViewById(R.id.btn_login_qq);
        login_link_signUp = findViewById(R.id.login_link_signUp);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        login_link_signUp.setOnClickListener(this);
        login_btn_login.setOnClickListener(this);
        btn_login_qq.setOnClickListener(this);
        btn_login_weibo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_link_signUp:
                Intent intent = new Intent(this,RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.login_btn_login:
                break;
            case R.id.btn_login_weibo:
                break;
            case R.id.btn_login_qq:
                break;
        }
    }
}
