package com.fanw.fanwsocialapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.Layout;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fanw.fanwsocialapp.R;
import com.fanw.fanwsocialapp.callback.EssayReceiver;
import com.fanw.fanwsocialapp.callback.JsonCallback;
import com.fanw.fanwsocialapp.common.Constants;
import com.fanw.fanwsocialapp.model.User;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

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
    private View mainView;

    private void initView(){
        mainView = findViewById(R.id.login_view);
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
                user_pwd = login_input_password.getText().toString();
                if (checkEmailOrPhone(login_input_ep.getText().toString())){
                    user_email = login_input_ep.getText().toString();
                    loginByMail();
                }else {
                    user_phone = login_input_ep.getText().toString();
                    loginByPhone();
                }
                break;
            case R.id.btn_login_weibo:
                break;
            case R.id.btn_login_qq:
                break;
        }
    }

    private void loginByMail(){
        OkGo.<EssayReceiver>post(Constants.ESSAY_URL+Constants.ESSAY_USER+"/mailLogin")
                .tag(this)
                .params("user_mail",user_email)
                .params("user_pwd",user_pwd)
                .execute(new JsonCallback<EssayReceiver>() {
                    @Override
                    public void onSuccess(Response<EssayReceiver> response) {
                        if (response.body().getCode() == 200 && response.body().getMsg().equals("success")){
                            User userTemp = response.body().getUser();
                            SharedPreferences sharedPreferences = getSharedPreferences("current_user", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("user_id",userTemp.getUser_id());
                            /*private int user_id;
                             private String user_name;
                             private String user_pwd;
                             private String user_head;
                             private String user_mail;
                             private String user_phone;
                             private boolean user_status;
                            */
                            editor.putString("user_name",userTemp.getUser_name());
                            editor.putString("user_pwd",userTemp.getUser_pwd());
                            editor.putString("user_mail",userTemp.getUser_mail());
                            editor.putString("user_phone",userTemp.getUser_phone());
                            editor.putString("user_head",Constants.ESSAY_URL+Constants.ESSAY_HEAD+userTemp.getUser_head());
                            editor.putBoolean("user_status",userTemp.isUser_status());
                            editor.commit();
                            Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            Snackbar.make(mainView,response.body().getMsg(),Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void loginByPhone(){
        OkGo.<EssayReceiver>post(Constants.ESSAY_URL+Constants.ESSAY_USER+"/phoneLogin")
                .tag(this)
                .params("user_mail",user_phone)
                .params("user_pwd",user_pwd)
                .execute(new JsonCallback<EssayReceiver>() {
                    @Override
                    public void onSuccess(Response<EssayReceiver> response) {
                        if (response.body().getCode() == 200 && response.body().getMsg().equals("success")){
                            User userTemp = response.body().getUser();
                            SharedPreferences sharedPreferences = getSharedPreferences("current_user", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("user_id",userTemp.getUser_id());
                            /*private int user_id;
                             private String user_name;
                             private String user_pwd;
                             private String user_head;
                             private String user_mail;
                             private String user_phone;
                             private boolean user_status;
                            */
                            editor.putString("user_name",userTemp.getUser_name());
                            editor.putString("user_pwd",userTemp.getUser_pwd());
                            editor.putString("user_mail",userTemp.getUser_mail());
                            editor.putString("user_phone",userTemp.getUser_phone());
                            editor.putString("user_head",Constants.ESSAY_URL+Constants.ESSAY_HEAD+userTemp.getUser_head());
                            editor.putBoolean("user_status",userTemp.isUser_status());
                            editor.commit();
                            Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            Snackbar.make(mainView,response.body().getMsg(),Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private boolean checkEmailOrPhone(String EP){
        if(EP.indexOf("@") != -1){
            return true;
        }else {
            return false;
        }
    }
}
