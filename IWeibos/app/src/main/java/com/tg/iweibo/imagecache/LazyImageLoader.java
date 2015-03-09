package com.tg.iweibo.imagecache;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.tg.iweibo.app.WeiboApplication;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import java.util.logging.LogRecord;

/**
 * Created by jake（lian_weijian@126.com）
 * Date: 2015-01-30
 * Time: 11:36
 */

/**
 * 提供给外部使用的laoder类型，改类调用ImageManager类的方法
 */
public class LazyImageLoader {


    private ImageManager imgManager = new ImageManager(WeiboApplication.context);
    private BlockingQueue<String> urlQueue = new ArrayBlockingQueue<String>(50);  //当存储队列满的时候，线程会被阻塞

    private DownLoadImageThread downLoadImageThread = new DownLoadImageThread();

    private static final int MESSAGE_ID = 1;
    private static final String EXTRA_IMG_RUL = "img_url";
    private static final String EXTRA_IMG = "extra_img";

    private  CallBackManager callBackManager = new CallBackManager();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MESSAGE_ID:
                    Bundle bundle = msg.getData();
                    String url = bundle.getString(EXTRA_IMG_RUL);
                    Bitmap bitmap = bundle.getParcelable(EXTRA_IMG);
                    //调用callback方法刷新ui
                    callBackManager.callback(url,bitmap);
                break;
            }
        }
    };



    public Bitmap get(String url,ImageLoaderCallback callback){

        Bitmap bitmap = ImageManager.userDefaultHead;

        //如果图片存在于缓存中就在缓存中读取图片
        if(imgManager.contains(url)){
            bitmap = imgManager.getFromCache(url);//从缓存中读取图片
            return  bitmap;
        }else{   //如果文件和map内存缓存中都不存在图片就开一个新的线程进行下载
            //开启线程前就必须把callback添加
            callBackManager.put(url,callback);
            this.startDownLoadThread(url);
        }
        return bitmap;
    }


    //开启线程下载
    private void startDownLoadThread(String url){
        this.putUrlToQueue(url); //放入队列
        Thread.State state = downLoadImageThread.getState();
         //为什么要进行以下判断，不能开太多的线程，任务结束线程就必须结束
        if (state == Thread.State.NEW){
            downLoadImageThread.start();
        }else if(state == Thread.State.TERMINATED){ //线程终止状态
            downLoadImageThread = new DownLoadImageThread();
            downLoadImageThread.start();
        }
    }


    //往下载队列里放url
    private void putUrlToQueue(String url){
        if(null != url){
            if(!urlQueue.contains(url)) { //里面不存在的时候才放进队列里面
                try {
                    this.urlQueue.put(url);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    //静态内部线程类
    private class DownLoadImageThread extends Thread{
        private boolean isRun = true;

        public void shutDown(){
            isRun = false;
        }
        @Override
        public void run() {
            try {

                while(isRun) {
                    String url = urlQueue.poll();
                    if(null == url){
                        break;
                    }
                    Bitmap bitmap = imgManager.safeGet(url);
                    Message msg = handler.obtainMessage(MESSAGE_ID);
                    //msg.what = MESSAGE_ID;
                    Bundle bundle = msg.getData();
                    bundle.putString(EXTRA_IMG_RUL,url);
                    bundle.putParcelable(EXTRA_IMG,bitmap);
                    handler.sendMessage(msg);
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                //关闭线程
                shutDown();
            }
        }
    }



}
