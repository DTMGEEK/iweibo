package com.tg.iweibo.utils;

/* Created by jake（lian_weijian@126.com）
        * Date: 2015-01-07
        * Time: 09:38
        */

import android.util.Log;

/**
 * Log管理类，可以通过修改LOGLEVEL 来显示哪一个级别的log 开发阶段使用
 */
public class LogManage {

    private static int LOGLEVEL = 6;
    private static final int VERBOS = 1;
    private static final int DEBUG = 2;
    private static final int INFO = 3;
    private static final int WARN = 4;
    private static final int ERROR = 5;

    public static void v(String tag, String info){
        if(LOGLEVEL > VERBOS) {
            Log.v(tag, info);
        }
    }

    public static void d(String tag,String info){
        if(LOGLEVEL > DEBUG) {
            Log.d(tag, info);
        }
    }

    public static void i(String tag,String info){
        if(LOGLEVEL > INFO) {
            Log.i(tag, info);
        }
    }

    public static void w(String tag,String info){
        if(LOGLEVEL > WARN) {
            Log.w(tag, info);
        }
    }

    public static void e(String tag,String info){
        if(LOGLEVEL > ERROR) {
            Log.e(tag, info);
        }
    }
}
