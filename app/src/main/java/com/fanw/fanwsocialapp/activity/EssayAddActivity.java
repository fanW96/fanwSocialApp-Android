package com.fanw.fanwsocialapp.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.fanw.fanwsocialapp.R;
import com.fanw.fanwsocialapp.adapter.ImagePickerAdapter;
import com.fanw.fanwsocialapp.callback.EssayReceiver;
import com.fanw.fanwsocialapp.callback.JsonCallback;
import com.fanw.fanwsocialapp.common.Constants;
import com.fanw.fanwsocialapp.imageLoader.GlideImageLoader;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.io.File;
import java.util.ArrayList;

public class EssayAddActivity extends AppCompatActivity implements ImagePickerAdapter.OnRecyclerViewItemClickListener {

    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private ImagePickerAdapter adapter;
    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private int maxImgCount = 3;               //允许选择图片最大数
    private RecyclerView recyclerView;
    private Toolbar mToolbar;
    private EditText essay_add_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_essay_add);

        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }

        /*
        * 最好在application中初始化，但是由于head修改部分需要改为裁剪circle，暂时将初始化放在activity中
        * */
        initImagePicker();
        initWidget();
    }

    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);                      //显示拍照按钮
        imagePicker.setCrop(false);                           //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setSelectLimit(maxImgCount);              //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);                         //保存文件的高度。单位像素
    }

    private void initWidget() {
        recyclerView = (RecyclerView) findViewById(R.id.essay_add_rv);
        mToolbar = findViewById(R.id.essay_add_toolbar);
        essay_add_content = findViewById(R.id.essay_add_content);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolbar.inflateMenu(R.menu.essay_add_toolbar_menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String essay_content = essay_add_content.getText().toString();
                int essay_pic_count = 0;
                String pic_1 = new String();
                String pic_2 = new String();
                String pic_3 = new String();
                SharedPreferences pre = getSharedPreferences("current_user", Context.MODE_PRIVATE);
                int user_id = pre.getInt("user_id",0);
                if(selImageList.size() == 3){
                    essay_pic_count = 3;
                    pic_1 = selImageList.get(0).path;
                    pic_2 = selImageList.get(1).path;
                    pic_3 = selImageList.get(2).path;
                    sendEssayPic3(essay_content,essay_pic_count,user_id,pic_1,pic_2,pic_3);
                }else if (selImageList.size() == 2){
                    essay_pic_count = 2;
                    pic_1 = selImageList.get(0).path;
                    pic_2 = selImageList.get(1).path;
                    /*
                    * 当前上传文文件要求文件为必须的，所以需要在设置两种文件上传的controller
                    * */
                    sendEssayPic2(essay_content,essay_pic_count,user_id,pic_1,pic_2);
                }else if (selImageList.size() == 1){
                    essay_pic_count = 1;
                    pic_1 = selImageList.get(0).path;
                    sendEssayPic1(essay_content,essay_pic_count,user_id,pic_1);
                }
                return false;
            }
        });
        selImageList = new ArrayList<>();
        adapter = new ImagePickerAdapter(this, selImageList, maxImgCount);
        adapter.setOnItemClickListener(this);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private void sendEssayPic3(String essay_content,int essay_pic_count,int user_id,String pic_1,String pic_2,String pic_3){
        OkGo.<EssayReceiver>post(Constants.ESSAY_URL+Constants.ESSAY_CONTENT+"/create")
                .tag(this)
                .params("essay_content",essay_content)
                .params("essay_pic_count",essay_pic_count)
                .params("user.user_id",user_id)
                .params("pic_1", new File(Uri.fromFile(new File(pic_1)).getPath()))
                .params("pic_2", new File(Uri.fromFile(new File(pic_2)).getPath()))
                .params("pic_3", new File(Uri.fromFile(new File(pic_3)).getPath()))
                .execute(new JsonCallback<EssayReceiver>() {
                    @Override
                    public void onSuccess(Response<EssayReceiver> response) {
                        if(response.body().getCode() == 200 && response.body().getMsg().equals("success")){
                            finish();
                        }else {
                            Snackbar.make(getWindow().getDecorView(),response.body().getMsg(),Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
    }
    private void sendEssayPic2(String essay_content,int essay_pic_count,int user_id,String pic_1,String pic_2){
        OkGo.<EssayReceiver>post(Constants.ESSAY_URL+Constants.ESSAY_CONTENT+"/create")
                .tag(this)
                .params("essay_content",essay_content)
                .params("essay_pic_count",essay_pic_count)
                .params("user.user_id",user_id)
                .params("pic_1", new File(Uri.fromFile(new File(pic_1)).getPath()))
                .params("pic_2", new File(Uri.fromFile(new File(pic_2)).getPath()))
                .execute(new JsonCallback<EssayReceiver>() {
                    @Override
                    public void onSuccess(Response<EssayReceiver> response) {
                        if(response.body().getCode() == 200 && response.body().getMsg().equals("success")){
                            finish();
                        }else {
                            Snackbar.make(getWindow().getDecorView(),response.body().getMsg(),Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
    }
    private void sendEssayPic1(String essay_content,int essay_pic_count,int user_id,String pic_1){
        OkGo.<EssayReceiver>post(Constants.ESSAY_URL+Constants.ESSAY_CONTENT+"/create")
                .tag(this)
                .params("essay_content",essay_content)
                .params("essay_pic_count",essay_pic_count)
                .params("user.user_id",user_id)
                .params("pic_1", new File(Uri.fromFile(new File(pic_1)).getPath()))
                .execute(new JsonCallback<EssayReceiver>() {
                    @Override
                    public void onSuccess(Response<EssayReceiver> response) {
                        if(response.body().getCode() == 200 && response.body().getMsg().equals("success")){
                            finish();
                        }else {
                            Snackbar.make(getWindow().getDecorView(),response.body().getMsg(),Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (position) {
            case IMAGE_ITEM_ADD:
                //打开选择,本次允许选择的数量
                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                Intent intent = new Intent(this, ImageGridActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SELECT);
                break;
            default:
                //打开预览
                Intent intentPreview = new Intent(this, ImagePreviewDelActivity.class);
                /*
                * wrong：直接勾选的图片在删除预览的时候没有获取到
                * solve：尝试直接传输list<object>直接获得，不使用弱引用
                * */
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter.getImages());
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                selImageList.addAll(images);
                adapter.setImages(selImageList);
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                selImageList.clear();
                selImageList.addAll(images);
                adapter.setImages(selImageList);
            }
        }
    }
}
