package com.tg.iweibo.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.tg.iweibo.R;
import com.tg.iweibo.app.WeiboApplication;
import com.tg.iweibo.customcontrols.TouchImageView;
import com.tg.iweibo.imagecache.ImageLoaderCallback;
import com.tg.iweibo.imagecache.SimpleImageLoader;
import com.tg.iweibo.utils.LogManage;

public class ScaleImgActivity extends Activity {


    private static final String TAG = "ScaleImgActivity";

    private TouchImageView activity_scale_img_iv = null;

    private Bitmap newBitmap = null;

    private  DisplayMetrics dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                ,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_scale_img);
        loadImage();
        //LogManage.i(TAG,bimMap_url);
    }


    //加载大图
    private void loadImage(){
        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

       String bimMap_url  =this.getIntent().getExtras().getString("bimMap_url");



        WeiboApplication.lazyImageLoader.get(bimMap_url,new ImageLoaderCallback(){

            @Override
            public void refresh(String url, Bitmap bitmap) {

                activity_scale_img_iv = (TouchImageView) findViewById(R.id.activity_scale_img_iv);

                newBitmap = bitmap;
                activity_scale_img_iv.setImageBitmap(bitmap);
                activity_scale_img_iv.initImageView(dm.widthPixels,dm.heightPixels - 80);
            }
        });
    }







}
