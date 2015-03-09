package com.tg.iweibo.weibocontentrplace;

import android.text.SpannableStringBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jake（lian_weijian@126.com）
 * Date: 2015-02-02
 * Time: 10:53
 */


public class WeiboContetnCallBackManager {

    private ConcurrentHashMap<String,List<WeiboContentReplaceCallback>> callMap ;
    private static final String TAG = "spann";

    public WeiboContetnCallBackManager() {
        this.callMap = new ConcurrentHashMap<String,List<WeiboContentReplaceCallback>>();
    }

    public void callback(SpannableStringBuilder weiboContent){
        List<WeiboContentReplaceCallback> callbacks = callMap.get(TAG);
        if(null == callMap){
            return;
        }
        for(WeiboContentReplaceCallback callback:callbacks){
            callback.refresh(weiboContent);
        }
        callbacks.clear();
        callMap.remove(weiboContent);
    }


    //添加
    public void put(String weiboContent,WeiboContentReplaceCallback callback){
        if(!callMap.contains(weiboContent)){
            callMap.put(weiboContent,new ArrayList<WeiboContentReplaceCallback>());
        }
        callMap.get(weiboContent).add(callback);
    }


}
