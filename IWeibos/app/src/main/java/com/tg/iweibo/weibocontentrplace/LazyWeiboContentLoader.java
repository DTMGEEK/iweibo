package com.tg.iweibo.weibocontentrplace;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableStringBuilder;
import com.tg.iweibo.utils.LogManage;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


/**
 * Created by jake（lian_weijian@126.com）
 * Date: 2015-02-02
 * Time: 10:22
 */

public class LazyWeiboContentLoader {


    public String WeiboContent = null;
    private WeiboContentManager weiboContentManager = null;

    private static final int SPANNABLE_ID = 1;
    private static final String EXTRA_SPANNABLE = "spannable";
    private static final String TAG = "spann";

    private WeiboContetnCallBackManager callBackManager = new WeiboContetnCallBackManager();

    private BlockingQueue<String> weiboContentQueue = new ArrayBlockingQueue<String>(50);

    private WeiboContentReplaceThread weiboContentReplaceThread = new WeiboContentReplaceThread();



    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
                switch (msg.what){
                    case SPANNABLE_ID:
                        Bundle bundle = msg.getData();
                        SpannableStringBuilder weiboContent = (SpannableStringBuilder) bundle.getCharSequence(EXTRA_SPANNABLE);

                        callBackManager.callback(weiboContent);
                    break;
                }
        }
    };


    public LazyWeiboContentLoader(Context context) {

        this.weiboContentManager = new WeiboContentManager(context);

    }

    public void getWeiboContent(String weiboContent,WeiboContentReplaceCallback callback){
        if(null != weiboContent) {
            callBackManager.put(TAG,callback);
            putToWeiboContentQueue(weiboContent);
            this.startDownLoadThread(weiboContent);
        }
    }


    //开启线程下载
    private void startDownLoadThread(String weiboContent){
        Thread.State state = weiboContentReplaceThread.getState();
        //为什么要进行以下判断，不能开太多的线程，任务结束线程就必须结束
        if (state == Thread.State.NEW){
            weiboContentReplaceThread.start();
        }else if(state == Thread.State.TERMINATED){ //线程终止状态
            weiboContentReplaceThread = new WeiboContentReplaceThread();
            weiboContentReplaceThread.start();
        }
    }


    public void putToWeiboContentQueue(String WeiboContent){
        if(null != WeiboContent){
            if(!weiboContentQueue.contains(weiboContentQueue)){
                try {
                    weiboContentQueue.put(WeiboContent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }




    private class WeiboContentReplaceThread extends Thread{
        private boolean isRun = true;

        @Override
        public void run() {
            try {
                while (isRun) {
                    SpannableStringBuilder spannable = weiboContentManager.parseWeibo(weiboContentQueue.poll());
                    Message msg = handler.obtainMessage(SPANNABLE_ID);
                    Bundle bundle = msg.getData();
                    bundle.putCharSequence(EXTRA_SPANNABLE, spannable);
                    handler.sendMessage(msg);
                    LogManage.i("Wic",spannable.toString());
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                this.shutDown();
            }
        }

        public void shutDown(){
            isRun = false;
        }

    }


}
