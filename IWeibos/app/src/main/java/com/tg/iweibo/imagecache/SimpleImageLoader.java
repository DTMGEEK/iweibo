package com.tg.iweibo.imagecache;

import android.app.Application;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.tg.iweibo.app.WeiboApplication;

/**
 * Created by jake（lian_weijian@126.com）
 * Date: 2015-01-30
 * Time: 21:37
 */

public class SimpleImageLoader {

    public static void showImage(String url,ImageView imageView){
        imageView.setTag(url);//把url地址即key保存

        WeiboApplication.lazyImageLoader.get(url,getCallBack(url,imageView));
    }


    private static ImageLoaderCallback getCallBack(String url, final ImageView view){

        return new ImageLoaderCallback() {
            @Override
            public void refresh(String url, Bitmap bitmap) {
                if(url.equals(view.getTag().toString())){   //取出key
                        view.setImageBitmap(bitmap);
                }
            }
        };
    }



}
