package com.fanw.fanwsocialapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.fanw.fanwsocialapp.R;
import com.fanw.fanwsocialapp.application.GlideApp;
import com.fanw.fanwsocialapp.base.BaseRecyclerViewAdapter;
import com.fanw.fanwsocialapp.listener.OnItemClickListener;
import com.fanw.fanwsocialapp.model.PhotoGirl;
import com.fanw.fanwsocialapp.util.DimenUtil;
import com.fanw.fanwsocialapp.widget.RatioImageView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by fanw on 2018/2/6.
 */

public class PhotoAdapter extends BaseRecyclerViewAdapter<PhotoGirl> {
    private int width = (int) (DimenUtil.getScreenSize() / 2);

    private Map<Integer, Integer> mHeights = new HashMap<>();

    public PhotoAdapter(List<PhotoGirl> mList, Context context) {
        super(mList, context);
    }

    public interface OnNewsListItemClickListener extends OnItemClickListener {
        void onItemClick(View view, int position, boolean isPhoto);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_FOOTER:
                view = getView(parent, R.layout.recyclerview_footer);
                return new FooterViewHolder(view);
            case TYPE_ITEM:
                view = getView(parent, R.layout.item_photo);
                final ItemViewHolder itemViewHolder = new ItemViewHolder(view);
//                itemViewHolder.setIsRecyclable(false);
                return itemViewHolder;
            default:
                throw new RuntimeException("there is no type that matches the type " +
                        viewType + " + make sure your using types correctly");
        }

    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof ItemViewHolder) {
//            ((ItemViewHolder) holder).mPhotoIv.setOriginalSize(width, getHeight(position));

/*            SimpleTarget<Bitmap> simpleTarget = new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    ((ItemViewHolder) holder).mPhotoIv.setOriginalSize(resource.getWidth(), resource.getHeight());
                    ((ItemViewHolder) holder).mPhotoIv.setImageBitmap(resource);
                }
            };*/  // 加载图片后设置实际图片宽高比，由于加载图片耗时，使用瀑布流比较混乱，容易重叠错位

/*            Glide.with(App.getAppContext())
                    .load(mList.get(position).getUrl())
//                    .asBitmap()*//*.format(DecodeFormat.PREFER_ARGB_8888)*//* // 没有动画
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .crossFade()
                    .placeholder(R.color.image_place_holder)
                    .error(R.drawable.ic_load_fail)
//                    .into(simpleTarget);
                    .into(((ItemViewHolder) holder).mPhotoIv);*/


            GlideApp.with(context)
                    .load(mList.get(position).getUrl())
                    .placeholder(R.color.image_place_holder)
                    .error(R.drawable.ic_load_fail)
                    .into( ((ItemViewHolder) holder).mPhotoIv);
            // 使用picasso加载图片可以自动计算图片实际宽高比进行设置，刷新也不会出现闪屏现象！
        }

        // 使用动画效果，当滑动过快时会引起item重叠，除了不是用动画，暂还没有想到更好的方法解决此冲突问题！
        // 跳转到图片详情有时共享动画会卡着不动，点一下屏幕才恢复
//        setItemAppearAnimation(holder, position, R.anim.anim_rotate_scale_in);
    }

    private int getHeight(int position) {
        int height;
        try {
            if (position >= mHeights.size()) {
                height = getRandomHeight();
                mHeights.put(position, height);
            } else {
                height = mHeights.get(position);
            }
        } catch (Exception e) {
            height = width / 2;
        }
        return height;
    }

    private int getRandomHeight() {
        int height;
        height = (int) (width * (new Random().nextFloat() / 2 + 1));
        return height;
    }

    @Override
    public int getItemViewType(int position) {
        PhotoGirl photoGirl = mList.get(position);
        if (photoGirl == null) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RatioImageView mPhotoIv;

        ItemViewHolder(View view) {
            super(view);
            mPhotoIv = view.findViewById(R.id.photo_iv);
            mPhotoIv.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(getAdapterPosition() , v);
            }
        }
    }
}
