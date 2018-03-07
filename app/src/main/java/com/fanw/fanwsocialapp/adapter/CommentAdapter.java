package com.fanw.fanwsocialapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fanw.fanwsocialapp.R;
import com.fanw.fanwsocialapp.application.GlideApp;
import com.fanw.fanwsocialapp.base.BaseRecyclerViewAdapter;
import com.fanw.fanwsocialapp.common.Constants;
import com.fanw.fanwsocialapp.listener.OnItemClickListener;
import com.fanw.fanwsocialapp.model.Comment;
import com.fanw.fanwsocialapp.widget.CircleImageView;

import java.util.List;

/**
 * Created by fanw on 2018/3/7.
 */

public class CommentAdapter extends BaseRecyclerViewAdapter<Comment> {


    public CommentAdapter(List<Comment> mList, Context context) {
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
            case TYPE_FOOTER:
                view = getView(parent,R.layout.recyclerview_footer);
                viewHolder = new FooterViewHolder(view);
                return viewHolder;
            case TYPE_ITEM:
                view = getView(parent,R.layout.item_comment);
                viewHolder = new ItemCommentViewHolder(view);
                return viewHolder;
            default:
                throw new RuntimeException("there is no type that matches the type " +
                        viewType + " + make sure your using types correctly");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof ItemCommentViewHolder){
            setItemValues((ItemCommentViewHolder)holder,position);
        }
    }

    private void setItemValues(final ItemCommentViewHolder holder, int position){
        final Comment comment = mList.get(position);
        holder.comment_item_user_name.setText(comment.getUser().getUser_name());
        holder.comment_item_time.setText(comment.getComment_date().toString());
        holder.comment_item_content.setText(comment.getComment_content());
        GlideApp.with(context)
                .load(Constants.ESSAY_URL+Constants.ESSAY_HEAD+comment.getUser().getUser_head())
                .format(DecodeFormat.PREFER_ARGB_8888)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.drawable.ic_load_fail)
                .into(holder.comment_item_user_head);
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
    public List getList() {
        return super.getList();
    }

    @Override
    public int getItemViewType(int position) {
        Comment commentTemp = (Comment) mList.get(position);
        if(commentTemp == null){
            return TYPE_FOOTER;
        }else {
            return TYPE_ITEM;
        }
    }

    private class ItemCommentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private CircleImageView comment_item_user_head;
        private TextView comment_item_user_name;
        private TextView comment_item_time;
        private TextView comment_item_content;


        public ItemCommentViewHolder(View itemView) {
            super(itemView);
            comment_item_content = itemView.findViewById(R.id.comment_item_content);
            comment_item_time = itemView.findViewById(R.id.comment_item_time);
            comment_item_user_head = itemView.findViewById(R.id.comment_item_user_head);
            comment_item_user_name = itemView.findViewById(R.id.comment_item_user_name);
            comment_item_user_head.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(getAdapterPosition() , v);
            }
        }
    }
}
