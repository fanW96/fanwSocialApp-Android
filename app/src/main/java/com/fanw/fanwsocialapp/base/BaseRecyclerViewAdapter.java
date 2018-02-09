package com.fanw.fanwsocialapp.base;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.fanw.fanwsocialapp.R;
import com.fanw.fanwsocialapp.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fanw on 2018/2/2.
 */

public class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_ITEM = 0;
    public static final int TYPE_FOOTER = 1;
    protected int mLastPosition = -1;
    protected boolean mIsShowFooter;
    //adapter显示信息
    protected List<T> mList;
    //点击事件监听
    protected OnItemClickListener mOnItemClickListener;
    //context,在子类中要可以获取，不能设置为private
    protected Context context;
    private LayoutInflater layoutInflater;

    public BaseRecyclerViewAdapter(List<T> mList, Context context) {
        if (mList != null){
            this.mList = mList;
        }else {
            this.mList = new ArrayList<T>();
        }
        this.context = context;
        this.layoutInflater =  LayoutInflater.from(context);
    }

    /*
    * 使用监听
    * */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }

    /*
    * 每个adapter各自重写，在各自的adapter中重写
    * getItemViewType给createViewHolder提供viewType做判断
    * */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    /*
    * 为Holder的绑定设置显示
    * */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_FOOTER){
            //在baseAdapter中的OnCreateViewHolder没有创建返回值，没办法使用instanceof FooterViewHolder
            //转而使用直接获得当前position的viewType来决定是否显示footer的progressBar
            ((FooterViewHolder) holder).progressBar.setVisibility(View.VISIBLE);
            return;
        }
    }

    /*
    * 获得Item数量
    * */
    @Override
    public int getItemCount() {
        if (mList == null){
            return 0;
        }else {
            int itemSize = mList.size();
            return itemSize;
        }
    }

    /*
    * 重新封装layoutInflater的获取
    * */
    protected View getView(ViewGroup parent, int layoutId) {
        return layoutInflater.inflate(layoutId, parent, false);
    }

    /*
    * 底部加载
    * */
    public class FooterViewHolder extends RecyclerView.ViewHolder{

        ProgressBar progressBar;

        public FooterViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.footer_progressBar);
        }
    }

    public List<T> getList() {
        return mList;
    }
}
