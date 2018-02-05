package com.fanw.fanwsocialapp.application;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

/**
 * Created by fanw on 2018/2/2.
 */

/*
* 插件Application模块，继承AppGlideModule并且添加注释@GlideModule
* 编译后自动生成Api，使用GlideAPP可以使用更多的方法
* */
@GlideModule
public class MyAppGlideModule extends AppGlideModule {
}
