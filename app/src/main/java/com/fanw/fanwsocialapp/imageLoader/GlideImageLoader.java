package com.fanw.fanwsocialapp.imageLoader;

import android.app.Activity;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fanw.fanwsocialapp.R;
import com.fanw.fanwsocialapp.application.GlideApp;
import com.lzy.imagepicker.loader.ImageLoader;

import java.io.File;

/**
 * Created by fanw on 2018/2/14.
 */

public class GlideImageLoader implements ImageLoader {
    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
        GlideApp.with(activity)
                .load(Uri.fromFile(new File(path)))
                .error(R.drawable.ic_load_fail)
                .placeholder(R.drawable.ic_loading)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }


    @Override
    public void clearMemoryCache() {

    }
}
