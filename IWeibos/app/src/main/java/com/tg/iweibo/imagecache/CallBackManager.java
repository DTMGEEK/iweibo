package com.tg.iweibo.imagecache;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jake（lian_weijian@126.com）
 * Date: 2015-01-30
 * Time: 11:37
 */

//回调
public class CallBackManager {


    private ConcurrentHashMap<String,List<ImageLoaderCallback>> callMap;

    public CallBackManager() {
        this.callMap = new ConcurrentHashMap<String,List<ImageLoaderCallback>>();
    }

    public void callback(String url,Bitmap bitmap){
        List<ImageLoaderCallback> callbacks = callMap.get(url);
        if(null == callbacks){
            return;
        }

        for(ImageLoaderCallback callback:callbacks){
            callback.refresh(url,bitmap);
        }

        callbacks.clear();
        callMap.remove(url);
    }


    //添加
    public void put(String url,ImageLoaderCallback callback){
        if(!callMap.contains(url)){
            callMap.put(url,new ArrayList<ImageLoaderCallback>());
        }
        callMap.get(url).add(callback);
    }




}
