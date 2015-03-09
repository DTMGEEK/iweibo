package com.tg.iweibo.app;

import android.app.Application;
import android.content.Context;

import com.tg.iweibo.domain.WeiboInfos;
import com.tg.iweibo.imagecache.LazyImageLoader;
import com.tg.iweibo.weibocontentrplace.LazyWeiboContentLoader;

import java.util.List;

/**
 * Created by jake（lian_weijian@126.com）
 * Date: 2015-01-30
 * Time: 21:22
 */

public class WeiboApplication extends Application {


    public static LazyImageLoader lazyImageLoader;
    public static LazyWeiboContentLoader lazyWeiboContentLoader;
    public static Context context;
    public static String maxId;     //若指定此参数，则返回ID小于或等于max_id的微博，默认为0。
    public static String since_id;  //若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0。
    public static String maxId_personal;     //若指定此参数，则返回ID小于或等于max_id的微博，默认为0。
    public static String since_id_personal;  //若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0。
    public static List<WeiboInfos> weiboInfosList ;
    public static List<WeiboInfos> weiboInfos_person_List ;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this.getApplicationContext();
        lazyImageLoader = new LazyImageLoader();
        lazyWeiboContentLoader = new LazyWeiboContentLoader(context);

    }

}
