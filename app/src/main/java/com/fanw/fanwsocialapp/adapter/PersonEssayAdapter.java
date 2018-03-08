package com.fanw.fanwsocialapp.adapter;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.daimajia.swipe.SwipeLayout;
import com.fanw.fanwsocialapp.R;
import com.fanw.fanwsocialapp.application.GlideApp;
import com.fanw.fanwsocialapp.base.BaseRecyclerViewAdapter;
import com.fanw.fanwsocialapp.callback.EssayReceiver;
import com.fanw.fanwsocialapp.callback.JsonCallback;
import com.fanw.fanwsocialapp.common.Constants;
import com.fanw.fanwsocialapp.model.Essay;
import com.fanw.fanwsocialapp.util.DimenUtil;
import com.fanw.fanwsocialapp.widget.CircleImageView;
import com.fanw.fanwsocialapp.widget.SquareImageView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.List;

/**
 * Created by fanw on 2018/3/8.
 */

public class PersonEssayAdapter extends BaseRecyclerViewAdapter<Essay> {
    public PersonEssayAdapter(List<Essay> mList, Context context) {
        super(mList, context);
    }

    public static final int TYPE_PHOTO_ITEM = 2;
    public static final int TYPE_HEADER = 3;

    private View mHeaderView;

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public View getHeaderView() {
        return mHeaderView;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View view;
        if(mHeaderView != null && viewType == TYPE_HEADER){
            return new HeaderViewHolder(mHeaderView);
        }
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
                view = getView(parent,R.layout.item_person_essay);
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
        if (holder instanceof HeaderViewHolder){
            return;
        }
        if (holder instanceof ItemEssayViewHolder){
            setItemValues((ItemEssayViewHolder)holder,position);
        }else if (holder instanceof PicEssayVIewHolder){
            setPhotoItemValues((PicEssayVIewHolder)holder,position);
            PicEssayVIewHolder picEssayVIewHolder = (PicEssayVIewHolder)holder;
            //swipeLayout设置
            //set show mode.
            picEssayVIewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
            //add drag edge.(If the BottomView has 'layout_gravity' attribute, this line is unnecessary)
            picEssayVIewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, picEssayVIewHolder.bottom_wrapper);
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
        holder.person_essay_user_name.setText(essay.getUser().getUser_name());
        holder.person_essay_time.setText(essay.getEssay_date().toString());
        holder.person_essay_thumbs_count.setText(String.valueOf(essay.getEssay_thumbs()));
        holder.person_essay_content.setText(essay.getEssay_content());
        holder.person_essay_thumbs_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkGo.<EssayReceiver>post(Constants.ESSAY_URL+Constants.ESSAY_CONTENT+"/give")
                        .tag(this)
                        .params("essay_id",essay.getEssay_id())
                        .execute(new JsonCallback<EssayReceiver>() {
                            @Override
                            public void onSuccess(Response<EssayReceiver> response) {
                                if (response.body().getCode() == 200 && response.body().getMsg().equals("success")){
                                    holder.person_essay_thumbs_pic.setImageResource(R.drawable.thumbs_get);
                                    holder.person_essay_thumbs_count.setText(String.valueOf(essay.getEssay_thumbs()+1));
                                }else
                                    Snackbar.make(holder.itemView, response.body().getMsg(), Snackbar.LENGTH_LONG).show();

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
                .into(holder.person_essay_user_head);
        int PhotoThreeHeight = (int) DimenUtil.dp2px(90);
        int PhotoTwoHeight = (int) DimenUtil.dp2px(120);
        int PhotoOneHeight = (int) DimenUtil.dp2px(200);

        String imgSrcLeft = null;
        String imgSrcMiddle = null;
        String imgSrcRight = null;

        ViewGroup.LayoutParams layoutParams = holder.person_essay_iv_group.getLayoutParams();

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
        holder.person_essay_iv_group.setLayoutParams(layoutParams);
        setPicImageView(holder,imgSrcLeft,imgSrcMiddle,imgSrcRight);
    }

    private void setPicImageView(PicEssayVIewHolder holder,String imgSrcLeft, String imgSrcMiddle, String imgSrcRight){
        if (imgSrcLeft != null){
            showAndSetPic(holder.person_essay_iv_left,imgSrcLeft);
        }else {
            hidePic(holder.person_essay_iv_left);
        }
        if (imgSrcMiddle != null){
            showAndSetPic(holder.person_essay_iv_middle,imgSrcMiddle);
        }else {
            hidePic(holder.person_essay_iv_middle);
        }
        if (imgSrcRight != null){
            showAndSetPic(holder.person_essay_iv_right,imgSrcRight);
        }else {
            hidePic(holder.person_essay_iv_right);
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
        if (position == 0 && mHeaderView != null){
            return TYPE_HEADER;
        }
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
        private CircleImageView person_essay_user_head;
        private TextView person_essay_user_name;
        private TextView person_essay_time;
        private TextView person_essay_thumbs_count;
        private ImageView person_essay_thumbs_pic;
        private TextView person_essay_content;
        private LinearLayout person_essay_iv_group;
        private SquareImageView person_essay_iv_left;
        private SquareImageView person_essay_iv_middle;
        private SquareImageView person_essay_iv_right;
        SwipeLayout swipeLayout;
        CardView cv_delete;
        LinearLayout bottom_wrapper;

        public PicEssayVIewHolder(View itemView) {
            super(itemView);
            swipeLayout = itemView.findViewById(R.id.person_essay_delete_swipe);
            cv_delete = itemView.findViewById(R.id.cv_Delete);
            bottom_wrapper = itemView.findViewById(R.id.bottom_wrapper);
            cv_delete.setOnClickListener(this);
            person_essay_user_head = itemView.findViewById(R.id.person_essay_user_head);
            person_essay_user_name = itemView.findViewById(R.id.person_essay_user_name);
            person_essay_time = itemView.findViewById(R.id.person_essay_time);
            person_essay_thumbs_count = itemView.findViewById(R.id.person_essay_thumbs_count);
            person_essay_thumbs_pic = itemView.findViewById(R.id.person_essay_thumbs_pic);
            person_essay_content = itemView.findViewById(R.id.person_essay_content);
            person_essay_iv_group = itemView.findViewById(R.id.person_essay_iv_group);
            person_essay_iv_left = itemView.findViewById(R.id.person_essay_iv_left);
            person_essay_iv_middle = itemView.findViewById(R.id.person_essay_iv_middle);
            person_essay_iv_right = itemView.findViewById(R.id.person_essay_iv_right);
            person_essay_iv_left.setOnClickListener(this);
            person_essay_iv_middle.setOnClickListener(this);
            person_essay_iv_right.setOnClickListener(this);
            person_essay_content.setOnClickListener(this);
            person_essay_user_head.setOnClickListener(this);
            person_essay_thumbs_pic.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(getAdapterPosition() , v);
            }
        }
    }

    protected class HeaderViewHolder extends RecyclerView.ViewHolder{

        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }
}
