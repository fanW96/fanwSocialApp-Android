package com.fanw.fanwsocialapp.adapter;

import android.content.ClipData;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanw.fanwsocialapp.R;
import com.fanw.fanwsocialapp.base.BaseRecyclerViewAdapter;

import java.util.List;

/**
 * Created by weifan on 2018/4/3.
 */

public class MessageAdapter extends BaseRecyclerViewAdapter<String> {
    public MessageAdapter(List<String> mList, Context context) {
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
                view = getView(parent,R.layout.item_message);
                viewHolder = new ItemMessageViewHolder(view);
                return viewHolder;
            default:
                throw new RuntimeException("there is no type that matches the type " +
                        viewType + " + make sure your using types correctly");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof ItemMessageViewHolder){
            ((ItemMessageViewHolder) holder).message_item.setText(mList.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        String Temp = (String) mList.get(position);
        if(Temp == null){
            return TYPE_FOOTER;
        }else {
            return TYPE_ITEM;
        }
    }

    private class ItemMessageViewHolder extends RecyclerView.ViewHolder{
        private TextView message_item;

        public ItemMessageViewHolder(View itemView) {
            super(itemView);
            message_item = itemView.findViewById(R.id.message_item);
        }
    }
}
