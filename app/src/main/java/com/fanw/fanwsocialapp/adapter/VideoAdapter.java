package com.fanw.fanwsocialapp.adapter;

import android.content.Context;
import android.gesture.GestureLibraries;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fanw.fanwsocialapp.R;
import com.fanw.fanwsocialapp.application.GlideApp;
import com.fanw.fanwsocialapp.base.BaseRecyclerViewAdapter;
import com.fanw.fanwsocialapp.listener.OnItemClickListener;
import com.fanw.fanwsocialapp.model.Video;
import com.fanw.fanwsocialapp.widget.RoundImageView;
import com.superplayer.library.SuperPlayer;

import java.util.List;

/**
 * Created by fanw on 2018/2/5.
 */

/*
* http://gank.io/api/data/休息视频/10/1
* */

public class VideoAdapter extends BaseRecyclerViewAdapter<Video> {
    public VideoAdapter(List<Video> mList, Context context) {
        super(mList, context);
    }

    @Override
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        super.setOnItemClickListener(onItemClickListener);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View view;
        switch (viewType){
            default:
            case TYPE_FOOTER:
                view = getView(parent, R.layout.recyclerview_footer);
                viewHolder = new FooterViewHolder(view);
                return viewHolder;
            case TYPE_ITEM:
                view = getView(parent,R.layout.item_video);
                viewHolder = new VideoHolder(view);
                return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        if(holder instanceof VideoHolder){
            setItemValues((VideoHolder) holder,position);
        }
    }

    private void setItemValues(VideoHolder videoHolder,int position){
        Video video = mList.get(position);
        videoHolder.news_video_desc.setText(video.getTitle());
        videoHolder.news_video_topic_name.setText(video.getTopicName());
//        videoHolder.news_video_topic_desc.setText(video.getTopicDesc());
        videoHolder.news_video_topic_cate.setText(video.getVideosource());
        GlideApp.with(context)
                .load(video.getTopicImg())
                .format(DecodeFormat.PREFER_ARGB_8888)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.drawable.ic_load_fail)
                .into(videoHolder.news_video_topic_head);
        GlideApp.with(context)
                .load(video.getCover())
                .format(DecodeFormat.PREFER_ARGB_8888)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.drawable.ic_load_fail)
                .into(videoHolder.news_video_image);
    }

    @Override
    public int getItemViewType(int position) {
        Video videoTemp = mList.get(position);
        if(videoTemp == null){
            return TYPE_FOOTER;
        }else {
            return TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }




    class VideoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView news_video_desc;
        ImageView news_video_image;
        ImageView news_video_topic_head;
        TextView news_video_topic_name;
//        TextView news_video_topic_desc;
        TextView news_video_topic_cate;

        public VideoHolder(View itemView) {
            super(itemView);
            news_video_desc = itemView.findViewById(R.id.news_video_desc);
            news_video_image = itemView.findViewById(R.id.news_video_image);
            news_video_topic_head = itemView.findViewById(R.id.news_video_topic_head);
            news_video_topic_name = itemView.findViewById(R.id.news_video_topic_name);
//            news_video_topic_desc = itemView.findViewById(R.id.news_video_topic_desc);
            news_video_topic_cate = itemView.findViewById(R.id.news_video_topic_cate);
            news_video_image.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(getAdapterPosition() , v);
            }
        }
    }
}
