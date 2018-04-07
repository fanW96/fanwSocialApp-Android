package com.fanw.fanwsocialapp.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fanw.fanwsocialapp.R;
import com.fanw.fanwsocialapp.application.GlideApp;
import com.fanw.fanwsocialapp.base.BaseRecyclerViewAdapter;
import com.fanw.fanwsocialapp.common.Constants;
import com.fanw.fanwsocialapp.model.Relation;
import com.fanw.fanwsocialapp.widget.CircleImageView;

import java.util.List;

/**
 * Created by weifan on 2018/4/3.
 */

public class ChatPersonAdapter extends BaseRecyclerViewAdapter<Relation> {

    public ChatPersonAdapter(List<Relation> mList, Context context) {
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
                view = getView(parent,R.layout.item_chat_person);
                viewHolder = new ItemChatViewHolder(view);
                return viewHolder;
            default:
                throw new RuntimeException("there is no type that matches the type " +
                        viewType + " + make sure your using types correctly");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof ItemChatViewHolder){
            setItemValues((ItemChatViewHolder)holder,position);
        }
    }

    private void setItemValues(final ItemChatViewHolder holder, int position){
        final Relation user = mList.get(position);
        holder.chat_person_name.setText(user.getUp().getUser_name());
        GlideApp.with(context)
                .load(Constants.ESSAY_URL+Constants.ESSAY_HEAD+user.getUp().getUser_head())
                .format(DecodeFormat.PREFER_ARGB_8888)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.drawable.ic_load_fail)
                .into(holder.chat_person_head);
    }

    @Override
    public int getItemViewType(int position) {
        Relation commentTemp = (Relation) mList.get(position);
        if(commentTemp == null){
            return TYPE_FOOTER;
        }else {
            return TYPE_ITEM;
        }
    }

    private class ItemChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private CircleImageView chat_person_head;
        private TextView chat_person_name;

        public ItemChatViewHolder(View itemView) {
            super(itemView);
            chat_person_head = itemView.findViewById(R.id.chat_person_head);
            chat_person_name = itemView.findViewById(R.id.chat_person_name);
            chat_person_name.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(getAdapterPosition() , v);
            }
        }
    }
}
