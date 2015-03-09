package com.tg.iweibo.weibocontentrplace;

import android.graphics.Bitmap;
import android.text.SpannableStringBuilder;
import android.widget.ImageView;
import android.widget.TextView;

import com.tg.iweibo.app.WeiboApplication;
import com.tg.iweibo.imagecache.ImageLoaderCallback;

/**
 * Created by jake（lian_weijian@126.com）
 * Date: 2015-02-02
 * Time: 12:18
 */

public class SimpleWeiboContentLoader {


    public static void replaceContent(String weiboContent,final TextView textView){
        WeiboApplication.lazyWeiboContentLoader.getWeiboContent(weiboContent,new WeiboContentReplaceCallback() {
            @Override
            public void refresh(SpannableStringBuilder weiboContent) {
                textView.setText(weiboContent);
            }
        });
    }





}
