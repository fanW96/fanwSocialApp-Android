package com.fanw.fanwsocialapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fanw.fanwsocialapp.R;
import com.fanw.fanwsocialapp.application.GlideApp;
import com.fanw.fanwsocialapp.callback.EssayReceiver;
import com.fanw.fanwsocialapp.callback.JsonCallback;
import com.fanw.fanwsocialapp.common.Constants;
import com.fanw.fanwsocialapp.widget.CircleImageView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;

public class ModifyHeadActivity extends AppCompatActivity {
    private CircleImageView user_head;
    private FloatingActionButton mFab;
    private String initUrl;
    private String imagePath;
    private Context mContext;
    private int user_id;

    private void initData(){
        final File current_user= new File("/data/data/"+getPackageName()+"/shared_prefs","current_user.xml");
        if (current_user.exists()){
            SharedPreferences pre = getSharedPreferences("current_user", Context.MODE_PRIVATE);
            user_id = pre.getInt("user_id",0);
        }
        initUrl = getIntent().getStringExtra("head_url");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_head);
        initData();
        user_head = findViewById(R.id.modify_head_iv);
        mFab = findViewById(R.id.modify_head_confirm);
        mContext = getApplicationContext();
        GlideApp.with(mContext)
                .load(initUrl)
                .format(DecodeFormat.PREFER_ARGB_8888)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.drawable.ic_load_fail)
                .into(user_head);
        user_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity(null)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(ModifyHeadActivity.this);
            }
        });
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkGo.<EssayReceiver>post(Constants.ESSAY_URL+Constants.ESSAY_USER+"/headUpload")
                        .params("user_id",user_id)
                        .params("headFile",new File(imagePath))
                        .execute(new JsonCallback<EssayReceiver>() {
                            @Override
                            public void onSuccess(Response<EssayReceiver> response) {
                                if(response.body().getCode() == 200 && response.body().getMsg().equals("success")){
                                    finish();
                                }
                            }
                        });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                imagePath =result.getUri().getPath();
                GlideApp.with(mContext)
                        .load(result.getUri())
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.drawable.ic_load_fail)
                        .into(user_head);
                Toast.makeText(this, "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG).show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
