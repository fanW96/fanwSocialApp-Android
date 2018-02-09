package com.fanw.fanwsocialapp.adapter;

import android.content.ClipData;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.fanw.fanwsocialapp.application.MyApplication;
import com.fanw.fanwsocialapp.base.BaseRecyclerViewAdapter;
import com.fanw.fanwsocialapp.listener.OnItemClickListener;
import com.fanw.fanwsocialapp.model.AdsBean;
import com.fanw.fanwsocialapp.model.NewsInfo;
import com.fanw.fanwsocialapp.util.DimenUtil;

import java.util.List;

/**
 * Created by fanw on 2018/2/2.
 */

public class NewsAdapter extends BaseRecyclerViewAdapter<NewsInfo> {

    public static final int TYPE_PHOTO_ITEM = 2;

    public interface OnNewsListItemClickListener extends OnItemClickListener {
        void onItemClick(View view, int position, boolean isPhoto);
    }

    public NewsAdapter(List<NewsInfo> mList, Context context) {
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
                view = getView(parent,R.layout.item_news);
                viewHolder = new ItemNewsViewHolder(view);
                return viewHolder;
            case TYPE_PHOTO_ITEM:
                view = getView(parent,R.layout.item_news_photo);
                viewHolder = new PhotoViewHolder(view);
                return viewHolder;
            default:
                throw new RuntimeException("there is no type that matches the type " +
                        viewType + " + make sure your using types correctly");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        if (holder instanceof ItemNewsViewHolder){
            setItemValues((ItemNewsViewHolder) holder,position);
        }else if (holder instanceof PhotoViewHolder){
            setPhotoItemValues((PhotoViewHolder) holder, position);
        }
    }

    private void setItemValues(ItemNewsViewHolder holder,int position){
        NewsInfo newsInfo = mList.get(position);
        holder.news_title_tv.setText(
                newsInfo.getLtitle() == null ?
                        newsInfo.getTitle():newsInfo.getLtitle());
        holder.news_ptime_tv.setText(newsInfo.getPtime());
        holder.news_digest_tv.setText(newsInfo.getDigest());
        //GlideApp获取URL图片，使用placeholder在没有成功获取图片下显示默认图片
        GlideApp.with(context)
                .load(newsInfo.getImgsrc())
                .format(DecodeFormat.PREFER_ARGB_8888)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.drawable.ic_load_fail)
                .into(holder.news_photo_iv);
        /*//set show mode.
        holder.news_swipeLayout
                .setShowMode(SwipeLayout.ShowMode.PullOut);
        //add drag edge.(If the BottomView has 'layout_gravity' attribute, this line is unnecessary)
        //如果在layout中使用layout_gravity则不需要再设置
        holder.news_swipeLayout
                .addDrag(SwipeLayout.DragEdge.Left,holder.news_bottom_wrapper);*/
    }

    private void setPhotoItemValues(PhotoViewHolder holder, int position) {
        NewsInfo newsInfo = mList.get(position);
        setTextView(holder, newsInfo);
        setImageView(holder, newsInfo);
    }

    private void setTextView(PhotoViewHolder holder, NewsInfo newsInfo) {
        String title = newsInfo.getTitle();
        String ptime = newsInfo.getPtime();

        holder.news_photo_title_tv.setText(title);
        holder.news_photo_ptime_tv.setText(ptime);
    }

    private void setImageView(PhotoViewHolder holder, NewsInfo newsInfo) {
        int PhotoThreeHeight = (int) DimenUtil.dp2px(90);
        int PhotoTwoHeight = (int) DimenUtil.dp2px(120);
        int PhotoOneHeight = (int) DimenUtil.dp2px(150);

        String imgSrcLeft = null;
        String imgSrcMiddle = null;
        String imgSrcRight = null;

        ViewGroup.LayoutParams layoutParams = holder.news_photo_iv_group.getLayoutParams();

        if (newsInfo.getAds() != null) {
            List<AdsBean> adsBeanList = newsInfo.getAds();
            int size = adsBeanList.size();
            if (size >= 3) {
                imgSrcLeft = adsBeanList.get(0).getImgsrc();
                imgSrcMiddle = adsBeanList.get(1).getImgsrc();
                imgSrcRight = adsBeanList.get(2).getImgsrc();

                layoutParams.height = PhotoThreeHeight;

                holder.news_photo_title_tv.setText(MyApplication.getAppContext()
                        .getString(R.string.photo_collections, adsBeanList.get(0).getTitle()));
            } else if (size >= 2) {
                imgSrcLeft = adsBeanList.get(0).getImgsrc();
                imgSrcMiddle = adsBeanList.get(1).getImgsrc();

                layoutParams.height = PhotoTwoHeight;
            } else if (size >= 1) {
                imgSrcLeft = adsBeanList.get(0).getImgsrc();

                layoutParams.height = PhotoOneHeight;
            }
        } else if (newsInfo.getImgextra() != null) {
            int size = newsInfo.getImgextra().size();
            if (size >= 3) {
                imgSrcLeft = newsInfo.getImgextra().get(0).getImgsrc();
                imgSrcMiddle = newsInfo.getImgextra().get(1).getImgsrc();
                imgSrcRight = newsInfo.getImgextra().get(2).getImgsrc();

                layoutParams.height = PhotoThreeHeight;
            } else if (size >= 2) {
                imgSrcLeft = newsInfo.getImgextra().get(0).getImgsrc();
                imgSrcMiddle = newsInfo.getImgextra().get(1).getImgsrc();

                layoutParams.height = PhotoTwoHeight;
            } else if (size >= 1) {
                imgSrcLeft = newsInfo.getImgextra().get(0).getImgsrc();

                layoutParams.height = PhotoOneHeight;
            }
        } else {
            imgSrcLeft = newsInfo.getImgsrc();

            layoutParams.height = PhotoOneHeight;
        }

        setPhotoImageView(holder, imgSrcLeft, imgSrcMiddle, imgSrcRight);
        holder.news_photo_iv_group.setLayoutParams(layoutParams);
    }

    private void setPhotoImageView(PhotoViewHolder holder, String imgSrcLeft, String imgSrcMiddle, String imgSrcRight) {
        if (imgSrcLeft != null) {
            showAndSetPhoto(holder.news_photo_iv_left, imgSrcLeft);
        } else {
            hidePhoto(holder.news_photo_iv_left);
        }

        if (imgSrcMiddle != null) {
            showAndSetPhoto(holder.news_photo_iv_middle, imgSrcMiddle);
        } else {
            hidePhoto(holder.news_photo_iv_middle);
        }

        if (imgSrcRight != null) {
            showAndSetPhoto(holder.news_photo_iv_right, imgSrcRight);
        } else {
            hidePhoto(holder.news_photo_iv_right);
        }
    }

    private void showAndSetPhoto(ImageView imageView, String imgSrc) {
        imageView.setVisibility(View.VISIBLE);
        GlideApp.with(context)
                .load(imgSrc)
                .format(DecodeFormat.PREFER_ARGB_8888)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.color.image_place_holder)
                .error(R.drawable.ic_load_fail)
                .into(imageView);
    }

    private void hidePhoto(ImageView imageView) {
        imageView.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        NewsInfo newsInfoTemp = mList.get(position);
        if(newsInfoTemp == null){
            return TYPE_FOOTER;
        }else if (!TextUtils.isEmpty(mList.get(position).getDigest())){
            return TYPE_ITEM;
        }else {
            return TYPE_PHOTO_ITEM;
        }
    }

    public class ItemNewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView news_photo_iv;
        TextView news_title_tv;
        TextView news_digest_tv;
        TextView news_ptime_tv;
        /*SwipeLayout news_swipeLayout;
        CardView news_delete;
        LinearLayout news_bottom_wrapper;*/


        public ItemNewsViewHolder(View itemView) {
            super(itemView);//holder设定
            news_photo_iv = itemView.findViewById(R.id.news_photo_iv);
            news_title_tv = itemView.findViewById(R.id.news_title_tv);
            news_digest_tv = itemView.findViewById(R.id.news_digest_tv);
            news_ptime_tv = itemView.findViewById(R.id.news_ptime_tv);
            /*news_swipeLayout = itemView.findViewById(R.id.news_delete_swipe);
            news_delete = itemView.findViewById(R.id.news_Delete);
            news_bottom_wrapper = itemView.findViewById(R.id.news_bottom_wrapper);*/
            news_title_tv.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(getAdapterPosition() , v);
            }
        }
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView news_photo_title_tv;
        LinearLayout news_photo_iv_group;
        ImageView news_photo_iv_left;
        ImageView news_photo_iv_middle;
        ImageView news_photo_iv_right;
        TextView news_photo_ptime_tv;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            news_photo_title_tv = itemView.findViewById(R.id.news_photo_title_tv);
            news_photo_iv_group = itemView.findViewById(R.id.news_photo_iv_group);
            news_photo_iv_left = itemView.findViewById(R.id.news_photo_iv_left);
            news_photo_iv_middle = itemView.findViewById(R.id.news_photo_iv_middle);
            news_photo_iv_right = itemView.findViewById(R.id.news_photo_iv_right);
            news_photo_ptime_tv = itemView.findViewById(R.id.news_photo_ptime_tv);
            news_photo_iv_left.setOnClickListener(this);
            news_photo_iv_middle.setOnClickListener(this);
            news_photo_iv_right.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(getAdapterPosition() , v);
            }
        }
    }
}
