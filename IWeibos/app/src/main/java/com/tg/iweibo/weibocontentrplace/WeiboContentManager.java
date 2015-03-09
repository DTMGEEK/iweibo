package com.tg.iweibo.weibocontentrplace;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;

import com.tg.iweibo.domain.Task;
import com.tg.iweibo.weiboutil.EmotionParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jake（lian_weijian@126.com）
 * Date: 2015-02-01
 * Time: 13:23
 */

public class WeiboContentManager {


    private static final String START = "start";
    private static final String END = "end";
    private static final String PARSE = "parse";

    private static final String TOPIC = "#.+?#";
    private static final String NAME = "@([\u4e00-\u9fa5A-Za-z0-9_]*)";
    private static final String URL = "http://.*";
    private static final String emotion = "(\\[[\u4E00-\u9FA5A-Za-z0-9]*\\])";


    private SpannableStringBuilder spannable = null;
    private Context context = null;

    public WeiboContentManager(Context context) {
        this.context = context;

    }


    public SpannableStringBuilder parseWeibo(String weiboContent){
        this.spannable = new SpannableStringBuilder(weiboContent);
        replaceWeiboContent(Pattern.compile(TOPIC),weiboContent,false);
        replaceWeiboContent(Pattern.compile(NAME),weiboContent,false);
        replaceWeiboContent(Pattern.compile(URL),weiboContent,false);
        //System.out.print(spannable.toString());
        return spannable;
    }





    /**
     * 高亮显示符合条件的内容
     * @param pattern 正则表达式对象
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void replaceWeiboContent(Pattern pattern,String weiboContent,boolean isEmotin) {

        List<Map<String, String>> startAndEndList = this.getStartAndEnd(pattern,weiboContent);
        if(isEmotin){         //是否替换表情
            EmotionParser emotionParser = new EmotionParser(context);

            for(Map<String,String> map:startAndEndList) {
                Drawable drawable = emotionParser.getEmotionByPhrases(map.get(emotion));
                if (null != drawable) {
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                    ImageSpan span_img = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
                    spannable.setSpan(span_img, Integer.parseInt(map.get(START)), Integer.parseInt(map.get(END)), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

                }
            }
        }else{
            CharacterStyle span_topic = new ForegroundColorSpan(Color.BLUE);
            for (Map<String, String> map : startAndEndList) {
                spannable.setSpan(span_topic, Integer.parseInt(map.get(START)), Integer.parseInt(map.get(END)), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }
        }
    }



    /**
     * 根据传入的正则表达式，和微博内容找到符合正在表达式的字符串
     * @param pattern 正则表达式对象
     * @return
     */
    private List<Map<String, String>> getStartAndEnd(Pattern pattern,String weiboContent){

        Matcher m = pattern.matcher(weiboContent);
        List<Map<String,String>> list = new ArrayList<Map<String,String>>();

        while (m.find()){
            Map<String,String> map = new HashMap<String,String>();
            map.put(PARSE,m.group());
            map.put(START,m.start()+"");
            map.put(END,m.end()+"");
            list.add(map);
        }
        return list;
    }




}
