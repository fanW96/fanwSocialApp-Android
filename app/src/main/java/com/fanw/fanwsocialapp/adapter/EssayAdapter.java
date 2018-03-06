package com.fanw.fanwsocialapp.adapter;

import android.content.Context;
import android.provider.SyncStateContract;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fanw.fanwsocialapp.R;
import com.fanw.fanwsocialapp.application.GlideApp;
import com.fanw.fanwsocialapp.base.BaseRecyclerViewAdapter;
import com.fanw.fanwsocialapp.callback.EssayReceiver;
import com.fanw.fanwsocialapp.callback.JsonCallback;
import com.fanw.fanwsocialapp.common.Constants;
import com.fanw.fanwsocialapp.listener.OnItemClickListener;
import com.fanw.fanwsocialapp.model.Essay;
import com.fanw.fanwsocialapp.util.DimenUtil;
import com.fanw.fanwsocialapp.util.MyUtils;
import com.fanw.fanwsocialapp.widget.CircleImageView;
import com.fanw.fanwsocialapp.widget.SquareImageView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.List;

import io.reactivex.CompletableOnSubscribe;

/**
 * Created by fanw on 2018/2/14.
 */

public class EssayAdapter extends BaseRecyclerViewAdapter<Essay> {
    public static final int TYPE_PHOTO_ITEM = 2;
    public EssayAdapter(List<Essay> mList, Context context) {
        super(mList, context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View view;
        switch (viewType){
            case TYPE_FOOTER:
                view = getView(parent,R.layout.recyclerview_footer);
                viewHolder = new FooterViewHolder(view);
                return viewHolder;
            case TYPE_ITEM:
                view = getView(parent,R.layout.item_essay);
                viewHolder = new ItemEssayViewHolder(view);
                return viewHolder;
            case TYPE_PHOTO_ITEM:
                view = getView(parent,R.layout.item_essay_pic);
                viewHolder = new PicEssayVIewHolder(view);
                return viewHolder;
            default:
                throw new RuntimeException("there is no type that matches the type " +
                        viewType + " + make sure your using types correctly");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof ItemEssayViewHolder){
            setItemValues((ItemEssayViewHolder)holder,position);
        }else if (holder instanceof PicEssayVIewHolder){
            setPhotoItemValues((PicEssayVIewHolder)holder,position);
        }
    }

    private void setItemValues(final ItemEssayViewHolder holder, int position){
        final Essay essay = mList.get(position);
        holder.essay_item_user_name.setText(essay.getUser().getUser_name());
        holder.essay_item_time.setText(essay.getEssay_date().toString());
        holder.essay_item_thumbs_count.setText(String.valueOf(essay.getEssay_thumbs()));
        holder.essay_item_content.setText(essay.getEssay_content());
        GlideApp.with(context)
                .load(Constants.ESSAY_URL+Constants.ESSAY_HEAD+essay.getUser().getUser_head())
                .format(DecodeFormat.PREFER_ARGB_8888)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.drawable.ic_load_fail)
                .into(holder.essay_item_user_head);
        holder.essay_item_thumbs_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkGo.<EssayReceiver>post(Constants.ESSAY_URL+Constants.ESSAY_CONTENT+"/give")
                        .tag(this)
                        .params("essay_id",essay.getEssay_id())
                        .execute(new JsonCallback<EssayReceiver>() {
                            @Override
                            public void onSuccess(Response<EssayReceiver> response) {
                                if (response.body().getCode() == 200 && response.body().getMsg().equals("success")){
                                    holder.essay_item_thumbs_pic.setImageResource(R.drawable.thumbs_get);
                                    holder.essay_item_thumbs_count.setText(String.valueOf(essay.getEssay_thumbs()+1));
                                }else {
                                    Snackbar.make(holder.itemView,response.body().getMsg(),Snackbar.LENGTH_LONG).show();
                                }

                            }
                        });

            }
        });
    }

    private void setPhotoItemValues(PicEssayVIewHolder holder,int position){
        Essay essay = mList.get(position);
        setTextView(holder,essay);
        setImageView(holder,essay);
    }

    private void setTextView(final PicEssayVIewHolder holder, final Essay essay){
        holder.essay_photo_user_name.setText(essay.getUser().getUser_name());
        holder.essay_photo_time.setText(essay.getEssay_date().toString());
        holder.essay_photo_thumbs_count.setText(String.valueOf(essay.getEssay_thumbs()));
        holder.essay_photo_content.setText(essay.getEssay_content());
        holder.essay_photo_thumbs_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkGo.<EssayReceiver>post(Constants.ESSAY_URL+Constants.ESSAY_CONTENT+"/give")
                        .tag(this)
                        .params("essay_id",essay.getEssay_id())
                        .execute(new JsonCallback<EssayReceiver>() {
                            @Override
                            public void onSuccess(Response<EssayReceiver> response) {
                                if (response.body().getCode() == 200 && response.body().getMsg().equals("success")){
                                    holder.essay_photo_thumbs_pic.setImageResource(R.drawable.thumbs_get);
                                    holder.essay_photo_thumbs_count.setText(String.valueOf(essay.getEssay_thumbs()+1));
                                }else {
                                    Snackbar.make(holder.itemView,response.body().getMsg(),Snackbar.LENGTH_LONG).show();
                                }

                            }
                        });
            }
        });
    }

    private void setImageView(PicEssayVIewHolder holder,Essay essay){
        GlideApp.with(context)
                .load(Constants.ESSAY_URL+Constants.ESSAY_HEAD+essay.getUser().getUser_head())
                .format(DecodeFormat.PREFER_ARGB_8888)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.drawable.ic_load_fail)
                .into(holder.essay_photo_user_head);
        int PhotoThreeHeight = (int) DimenUtil.dp2px(90);
        int PhotoTwoHeight = (int) DimenUtil.dp2px(120);
        int PhotoOneHeight = (int) DimenUtil.dp2px(200);

        String imgSrcLeft = null;
        String imgSrcMiddle = null;
        String imgSrcRight = null;

        ViewGroup.LayoutParams layoutParams = holder.essay_photo_iv_group.getLayoutParams();

        if (essay.getEssay_pic_count() == 3){
            imgSrcLeft = Constants.ESSAY_URL+ Constants.ESSAY_PIC+essay.getEssay_pic_1();
            imgSrcMiddle = Constants.ESSAY_URL+ Constants.ESSAY_PIC+essay.getEssay_pic_2();
            imgSrcRight = Constants.ESSAY_URL+ Constants.ESSAY_PIC+essay.getEssay_pic_3();
        }else if (essay.getEssay_pic_count() == 2){
            imgSrcLeft = Constants.ESSAY_URL+ Constants.ESSAY_PIC+essay.getEssay_pic_1();
            imgSrcMiddle = Constants.ESSAY_URL+ Constants.ESSAY_PIC+essay.getEssay_pic_2();
        }else{
            imgSrcLeft = Constants.ESSAY_URL+ Constants.ESSAY_PIC+essay.getEssay_pic_1();
            layoutParams.height = PhotoOneHeight;
        }
        holder.essay_photo_iv_group.setLayoutParams(layoutParams);
        setPicImageView(holder,imgSrcLeft,imgSrcMiddle,imgSrcRight);
    }

    private void setPicImageView(PicEssayVIewHolder holder,String imgSrcLeft, String imgSrcMiddle, String imgSrcRight){
        if (imgSrcLeft != null){
            showAndSetPic(holder.essay_photo_iv_left,imgSrcLeft);
        }else {
            hidePic(holder.essay_photo_iv_left);
        }
        if (imgSrcMiddle != null){
            showAndSetPic(holder.essay_photo_iv_middle,imgSrcMiddle);
        }else {
            hidePic(holder.essay_photo_iv_middle);
        }
        if (imgSrcRight != null){
            showAndSetPic(holder.essay_photo_iv_right,imgSrcRight);
        }else {
            hidePic(holder.essay_photo_iv_right);
        }
    }

    private void hidePic(SquareImageView squareImageView){
        squareImageView.setVisibility(View.GONE);
    }

    private void showAndSetPic(SquareImageView squareImageView,String imgSrc){
        squareImageView.setVisibility(View.VISIBLE);
        GlideApp.with(context)
                .load(imgSrc)
                .format(DecodeFormat.PREFER_ARGB_8888)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.color.image_place_holder)
                .error(R.drawable.ic_load_fail)
                .into(squareImageView);
    }

    @Override
    protected View getView(ViewGroup parent, int layoutId) {
        return super.getView(parent, layoutId);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        Essay essayTemp = mList.get(position);
        if(essayTemp == null){
            return TYPE_FOOTER;
        }else if (essayTemp.getEssay_pic_count() == 0){
            return TYPE_ITEM;
        }else {
            return TYPE_PHOTO_ITEM;
        }
    }

    private class ItemEssayViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private CircleImageView essay_item_user_head;
        private TextView essay_item_user_name;
        private TextView essay_item_time;
        private TextView essay_item_thumbs_count;
        private ImageView essay_item_thumbs_pic;
        private TextView essay_item_content;

        public ItemEssayViewHolder(View itemView) {
            super(itemView);
            essay_item_user_head = itemView.findViewById(R.id.essay_item_user_head);
            essay_item_user_name = itemView.findViewById(R.id.essay_item_user_name);
            essay_item_time = itemView.findViewById(R.id.essay_item_time);
            essay_item_thumbs_count = itemView.findViewById(R.id.essay_item_thumbs_count);
            essay_item_thumbs_pic = itemView.findViewById(R.id.essay_item_thumbs_pic);
            essay_item_content = itemView.findViewById(R.id.essay_item_content);
            essay_item_thumbs_pic.setOnClickListener(this);
            essay_item_user_head.setOnClickListener(this);
            essay_item_content.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(getAdapterPosition() , v);
            }
        }
    }

    private class PicEssayVIewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private CircleImageView essay_photo_user_head;
        private TextView essay_photo_user_name;
        private TextView essay_photo_time;
        private TextView essay_photo_thumbs_count;
        private ImageView essay_photo_thumbs_pic;
        private TextView essay_photo_content;
        private LinearLayout essay_photo_iv_group;
        private SquareImageView essay_photo_iv_left;
        private SquareImageView essay_photo_iv_middle;
        private SquareImageView essay_photo_iv_right;

        public PicEssayVIewHolder(View itemView) {
            super(itemView);
            essay_photo_user_head = itemView.findViewById(R.id.essay_photo_user_head);
            essay_photo_user_name = itemView.findViewById(R.id.essay_photo_user_name);
            essay_photo_time = itemView.findViewById(R.id.essay_photo_time);
            essay_photo_thumbs_count = itemView.findViewById(R.id.essay_photo_thumbs_count);
            essay_photo_thumbs_pic = itemView.findViewById(R.id.essay_photo_thumbs_pic);
            essay_photo_content = itemView.findViewById(R.id.essay_photo_content);
            essay_photo_iv_group = itemView.findViewById(R.id.essay_photo_iv_group);
            essay_photo_iv_left = itemView.findViewById(R.id.essay_photo_iv_left);
            essay_photo_iv_middle = itemView.findViewById(R.id.essay_photo_iv_middle);
            essay_photo_iv_right = itemView.findViewById(R.id.essay_photo_iv_right);
            essay_photo_iv_left.setOnClickListener(this);
            essay_photo_iv_middle.setOnClickListener(this);
            essay_photo_iv_right.setOnClickListener(this);
            essay_photo_content.setOnClickListener(this);
            essay_photo_user_head.setOnClickListener(this);
            essay_photo_thumbs_pic.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(getAdapterPosition() , v);
            }
        }
    }
}
