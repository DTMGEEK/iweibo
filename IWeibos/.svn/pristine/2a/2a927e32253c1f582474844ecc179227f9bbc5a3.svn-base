package com.tg.iweibo.weiboutil;

import android.content.Context;
import android.content.SharedPreferences;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

/**
 * Created by jake（lian_weijian@126.com）
 * Date: 2015-01-16
 * Time: 11:54
 * 该类定义了微博授权时所需要的参数。
 */

public class AccessTokenKeeper {

    private static final String PREFERENCE_NAME = "com.tg.iweibo";

    private static final String KEY_UID = "uid";

    private static final String KEY_ACCESS_TOKEN = "access_toke";

    private static final String KEY_EXPIRES_IN = "expires_in";


    /**
     * 保存 Token 对象到 SharedPreferences。
     *
     * @param context 应用程序上下文环境
     * @param token   Token 对象
     */
    public static void writeAccessToken(Context context,Oauth2AccessToken token){
            if(null == context || null == token){
                return;
            }
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME,Context.MODE_APPEND).edit();
        editor.putString(KEY_UID,token.getUid());
        editor.putString(KEY_ACCESS_TOKEN,token.getToken());
        editor.putLong(KEY_EXPIRES_IN,token.getExpiresTime());
        editor.commit();
    }


    /**
     * 从 SharedPreferences 读取 Token 信息。
     *
     * @param context 应用程序上下文环境
     *
     * @return 返回 Token 对象
     */
    public static Oauth2AccessToken readAccessToken(Context context){
           if (null == context ){
               return null;
           }
        Oauth2AccessToken toke = new Oauth2AccessToken();
        SharedPreferences pref = context.getSharedPreferences(PREFERENCE_NAME,Context.MODE_APPEND);
        toke.setUid(pref.getString(KEY_UID,""));
        toke.setToken(pref.getString(KEY_ACCESS_TOKEN,""));
        toke.setExpiresTime(pref.getLong(KEY_EXPIRES_IN,0));
        return toke;
    }

    /**
     * 清空 SharedPreferences 中 Token信息。
     *
     * @param context 应用程序上下文环境
     */
    public static void clear(Context context){
        if(null == context){
            return;
        }
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME,Context.MODE_APPEND).edit();
        editor.clear();
        editor.commit();
    }


}
