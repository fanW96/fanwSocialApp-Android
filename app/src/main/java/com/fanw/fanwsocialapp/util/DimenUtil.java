package com.fanw.fanwsocialapp.util;

import com.fanw.fanwsocialapp.application.MyApplication;

/**
 * Created by fanw on 2018/2/4.
 */

public class DimenUtil {
    public static float dp2px(float dp) {
        final float scale = MyApplication.getAppContext().getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public static float sp2px(float sp) {
        final float scale = MyApplication.getAppContext().getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    public static int getScreenSize() {
        return MyApplication.getAppContext().getResources().getDisplayMetrics().widthPixels;
    }
}
