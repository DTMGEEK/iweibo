package com.tg.iweibo.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.util.Log;

import com.tg.iweibo.domain.UpdateInfo;
import com.tg.iweibo.utils.XmlInfoParser;

public class UpdateInfoService {
	
	 private Context context;

	    public UpdateInfoService(Context context) {
	        this.context = context;
	    }

	    /**
	     * 获取服务器上的升级信息xml文件
	     * @param urlid 保存在stinrg.xml文件中的字符串id
	     * @return   升级信息实体
	     * @throws org.xmlpull.v1.XmlPullParserException
	     */
	    public UpdateInfo getUpdateInof(int urlid) throws XmlPullParserException {
	        String path = this.context.getResources().getString(urlid);
	        UpdateInfo info = null;
	        try {
	            URL url = new URL(path);
	            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	            InputStream is  = conn.getInputStream();
	            info =  XmlInfoParser.getUpdateInfo(is);
	        } catch (MalformedURLException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	            Log.i("UpdateInfoService", e.getMessage());
	        }
	        return info;
	    }

}
