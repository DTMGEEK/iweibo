package com.tg.iweibo.imagecache;

/**
 * Created by jake（lian_weijian@126.com）
 * Date: 2015-01-30
 * Time: 11:36
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import com.tg.iweibo.R;
import com.tg.iweibo.utils.MD5Encoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;



/**
 *图片缓存管理
 */
public class ImageManager {

    Map<String,SoftReference<Bitmap>> imageCache ;
    private Context context = null;

    //用户默认头像
    public static Bitmap userDefaultHead = null;

    public ImageManager(Context context) {
        this.imageCache = new HashMap<String,SoftReference<Bitmap>>();
        this.context = context;
        userDefaultHead = drawableToBitmap(context.getResources().getDrawable(R.drawable.usericon));
    }



    //判断缓存中是否存在需要的图片
    public boolean contains(String url){
        return imageCache.containsKey(url);
    }


    //从缓存中读取图片
    public Bitmap getFromCache(String url){

        Bitmap bitmap = null ;
        bitmap = getFromMapCache(url);
        if(null == bitmap){//如果在map缓存中没有图片，就在文件缓存中寻找突破
            bitmap = this.getFromFile(url);
        }
        return  bitmap;
    }



    //map缓存中读取图片
    public Bitmap getFromMapCache(String url){
        //首先从map对象中读取图片
        Bitmap bitmap = null;

        SoftReference<Bitmap> ref = null;
        //加上线程锁，
        synchronized (this){
            ref = imageCache.get(url);
        }

        if(null != ref){   //如果不为空就取出图片对象
            bitmap = ref.get();
            if(null != bitmap){
                return  bitmap;
            }
        }
        return  null;
    }



    //从文件中读取图片缓存
    public Bitmap getFromFile(String url){
           //传入的文件名不能出现 分隔符,所以必须把url进行md5编码
        String fileName = this.getMD5(url);
        FileInputStream is = null;
        Bitmap bitmap = null;
        try {
            is = this.context.openFileInput(fileName);
                //直接返回一个bitmap对象
            bitmap = BitmapFactory.decodeStream(is);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if(null != is){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }


    //返回md5编码的字符串
     private String getMD5(String src){
         return MD5Encoder.encode(src);
     }


    //下载到图片
    public Bitmap downloadImg(String url){
        Bitmap bitmap = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            InputStream is = conn.getInputStream();
            String fileName = writeToFile(getMD5(url),is);
            bitmap = BitmapFactory.decodeFile(fileName); //从文件中读取图片
        } catch (IOException e) {
            e.printStackTrace();
        }
      return bitmap;
    }


    //保存到文件
    private String writeToFile(String fileName,InputStream is) {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(context.openFileOutput(fileName,Context.MODE_PRIVATE));
            int len = 0;
            byte[] buffer = new byte[1024];
            while((len = bis.read(buffer))!= -1){
                   bos.write(buffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (null != bis) {
                    bis.close();
                }
                if (null != bos) {
                    bos.flush();
                    bos.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return context.getFilesDir() + File.separator + fileName;//返回文件路径
    }



    //在进行下载前要判断一下 文件里面是否有这张图片
    public Bitmap safeGet(String url){
        Bitmap bitmap = this.getFromFile(url);
        synchronized (this) { //在线程里面读写文件必须加锁
            if (null != bitmap) {
                imageCache.put(url, new SoftReference<Bitmap>(bitmap));
                return bitmap;
            }
        }
        return downloadImg(url);
    }


    //drawable 转换成Bitmap
    private Bitmap drawableToBitmap(Drawable drawable){
        BitmapDrawable tempDrawable = (BitmapDrawable) drawable;
        return tempDrawable.getBitmap();
    }


}
