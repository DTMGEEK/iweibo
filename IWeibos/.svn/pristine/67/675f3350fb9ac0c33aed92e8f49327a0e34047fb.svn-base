package com.tg.iweibo.weiboutil;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by jake（lian_weijian@126.com）
 * Date: 2015-01-19
 * Time: 14:07
 */

public class SendRequest {

    public static HttpEntity sendGetRequest(String url,Map<String,String> params) throws Exception{
        HttpEntity entity = null;
        StringBuilder builder = new StringBuilder(url);
        builder.append("?");
        if(!(params.isEmpty())) {
            Set<Map.Entry<String, String>> paramsSet = params.entrySet();
            for(Map.Entry<String,String> entry:paramsSet){
                builder.append(entry.getKey() + "=");
                builder.append(entry.getValue());
                builder.append("&");
            }
            builder.deleteCharAt(builder.length()-1);
        }

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(builder.toString());
        HttpResponse response = httpClient.execute(httpGet);
        if(response.getStatusLine().getStatusCode() == 200){
             entity = response.getEntity();
        }

        return  entity;
    }


}
