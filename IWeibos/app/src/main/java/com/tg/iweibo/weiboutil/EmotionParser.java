package com.tg.iweibo.weiboutil;

import android.content.Context;
import android.graphics.drawable.Drawable;
import com.tg.iweibo.R;
import java.util.HashMap;
import java.util.Map;



/**
 * Created by jake（lian_weijian@126.com）
 * Date: 2015-01-31
 * Time: 14:58
 */


//表情解析类
public class EmotionParser {

    private Context context = null;
    private static Map<String,Integer> emotionMap;  //别名是key r中的id是值
    private static String[] phrases;
    //初始化 表情数组，值为r文件中的id值
    private static int[] emotino = new int[]{R.drawable.smile,R.drawable.smile,R.drawable.smile};

    public EmotionParser(Context context) {
        //取出在string.xml中已经定义好的 字符数组 做为表情数组的key
        this.context = context;
        this.phrases = context.getResources().getStringArray(R.array.default_emotion);
        emotionMap = new HashMap<String,Integer>();
        if(emotino.length != phrases.length){
            throw new RuntimeException("ERROR");
        }

        //初始化  //别名是key r中的id是值
        for(int i=0; i<emotino.length; i++){
            emotionMap.put(phrases[i],emotino[i]);
        }
    }


    /**
     * 根据传入的key 找到对应的表情
     * @param phrases 字符串key值
     * @return  Drawable对象
     */
    public Drawable getEmotionByPhrases(String phrases) {
        int emotionId = 0;
        Drawable emotionDrawable = null;
        if (null != phrases) {
            if (emotionMap.containsKey(phrases)) {
                emotionId = emotionMap.get(phrases);
                emotionDrawable = context.getResources().getDrawable(emotionId);
            }
        }
        return emotionDrawable;
    }



}
