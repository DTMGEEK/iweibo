package com.tg.iweibo.imagecache;

/**
 * Created by jake（lian_weijian@126.com）
 * Date: 2015-01-30
 * Time: 11:34
 */

import android.graphics.Bitmap;

/**
 *回调接口
 */
public interface ImageLoaderCallback {

    //回调接口方法，刷新UI
    public void refresh(String url,Bitmap bitmap);
}
