package com.tg.iweibo.utils;

/* Created by jake（lian_weijian@126.com）
        * Date: 2015-01-07
        * Time: 14:42
        */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownLoadFileTask {

    /**
     * 下载文件方法
     * @param filePath 文件本地保存路径
     * @param urlpath  文件下载路径
     * @return   文件实例
     */
    public static File downLoadFile(String filePath,String urlpath){
        File file = new File(filePath);

        try {
            URL url = new URL(urlpath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(2*1000);
            InputStream is = conn.getInputStream();
            int len = 0;
            byte[] buffer = new byte[1024];
            FileOutputStream fos = new FileOutputStream(file);
            if(conn.getResponseCode() == 200) {
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer,0,len);
                    Thread.sleep(50);
                }
                fos.flush();
                is.close();
                fos.close();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return file;
    }
}
