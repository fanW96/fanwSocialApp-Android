package com.fanw.fanwsocialapp.activity;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fanw.fanwsocialapp.R;
import com.fanw.fanwsocialapp.callback.EssayReceiver;
import com.fanw.fanwsocialapp.callback.JsonCallback;
import com.fanw.fanwsocialapp.common.Constants;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText register_input_name;
    private EditText register_input_ep;
    private EditText register_input_password;
    private AppCompatButton register_btn_signUp;
    private TextView register_link_login;
    private String user_name;
    private String user_email;
    private String user_pwd;
    private String user_phone;
    private View mainView;

    private void initView(){
        mainView = findViewById(R.id.register_view);
        register_input_name = findViewById(R.id.register_input_name);
        register_input_ep = findViewById(R.id.register_input_ep);
        register_input_password = findViewById(R.id.register_input_password);
        register_btn_signUp = findViewById(R.id.register_btn_signUp);
        register_link_login = findViewById(R.id.register_link_login);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        register_btn_signUp.setOnClickListener(this);
        register_link_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_btn_signUp:
                user_name = register_input_name.getText().toString();
                user_pwd = register_input_password.getText().toString();
                if(checkEmailOrPhone(register_input_ep.getText().toString())){
                    user_email = register_input_ep.getText().toString();
                    registerByMail();
                }else {
                    user_phone = register_input_ep.getText().toString();
                    registerByPhone();
                }
                break;
            case R.id.register_link_login:
                Intent intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private boolean checkEmailOrPhone(String EP){
        if(EP.indexOf("@") != -1){
            return true;
        }else {
            return false;
        }
    }

    private void registerByMail(){
        OkGo.<EssayReceiver>post(Constants.ESSAY_URL+Constants.ESSAY_USER+"/mailRegister")
                .tag(this)
                .params("user_mail",user_email)
                .params("user_pwd",user_pwd)
                .params("user_name",user_name)
                .execute(new JsonCallback<EssayReceiver>() {
                    @Override
                    public void onSuccess(Response<EssayReceiver> response) {
                        if(response.body().getCode() == 200 && response.body().getMsg().equals("success")){
                            finish();
                        }else {
                            Snackbar.make(mainView,response.body().getMsg(),Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void registerByPhone(){
        OkGo.<EssayReceiver>post(Constants.ESSAY_URL+Constants.ESSAY_USER+"/phoneRegister")
                .tag(this)
                .params("user_phone",user_phone)
                .params("user_pwd",user_pwd)
                .params("user_name",user_name)
                .execute(new JsonCallback<EssayReceiver>() {
                    @Override
                    public void onSuccess(Response<EssayReceiver> response) {
                        if(response.body().getCode() == 200 && response.body().getMsg().equals("success")){
                            finish();
                        }else {
                            Snackbar.make(mainView,response.body().getMsg(),Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
