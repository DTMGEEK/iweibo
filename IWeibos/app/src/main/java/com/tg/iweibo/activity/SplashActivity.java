package com.tg.iweibo.activity;
/**
 * Created by jake（lian_weijian@126.com）
 * Date: 2015-01-08
 * Time: 15:55
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tg.iweibo.R;
import com.tg.iweibo.domain.UpdateInfo;
import com.tg.iweibo.iservice.IWeiboActivity;
import com.tg.iweibo.utils.UpdateInfoService;
import com.tg.iweibo.utils.DownLoadFileTask;
import com.tg.iweibo.utils.LogManage;
import com.tg.iweibo.utils.NetUtil;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;


/**
 *1. 后台完成数据库初始化的操作
 * 2. 联网访问服务器,获取服务器最新信息
 */
public class SplashActivity extends Activity implements IWeiboActivity {

    //
    private  static final String TAG = "SplashActivity";
    //升级信息doamin类
    private UpdateInfo info;
    //Splash布局文件实体
    private LinearLayout splash_layout = null;

    private ProgressDialog pd = null;
    //升级信息Handler
    private Handler updateInfoHandler = null;

    private boolean netWorkEnanbl = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.splash_layout);
        this.init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.init();
    }

    //与数据库的xml文件进行判断
    public void isNeedUpdate(){
        netWorkEnanbl = NetUtil.checkNet(SplashActivity.this);
        //判断网络是否已经链接
        if(netWorkEnanbl) {
            //启动一个新的线程，获取服务器上的升级信息
            new Thread(new UpdateinfoThread()).start();
            SplashActivity.this.updateInfoHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    info = (UpdateInfo) msg.obj;
                    if (info != null) {
                        //判断版本号是否与本地的一致
                        if (SplashActivity.this.getLocalViersion().equals(info.getVersion())) {
                            Toast.makeText(SplashActivity.this, "版本号一致", Toast.LENGTH_LONG).show();
                            SplashActivity.this.loadLoginScreen();
                        } else {
                            Toast.makeText(SplashActivity.this, "版本号不同", Toast.LENGTH_LONG).show();
                            SplashActivity.this.showUpdateDialog();
                        }
                    }
                }
            };
        }else{
            NetUtil.isNeedSetNetWork(SplashActivity.this);
        }
    }


    //获取本地的版本号
    public String getLocalViersion(){
        PackageManager pmanager = this.getPackageManager();
        PackageInfo info  = null;
        try {
            //获得本地的版本号
            info = pmanager.getPackageInfo(this.getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return info.versionName;
    }

    /**
     * 初始化方法
     */
    @Override
    public void init() {
        splash_layout = (LinearLayout) this.findViewById(R.id.splash_layout);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                ,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.pd = new ProgressDialog(this);
        this.pd.setMessage("正在下载。。。。");
        this.pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        //设置splash界面进入的动画
        AlphaAnimation anim = new AlphaAnimation(0.0f,1.0f);
        anim.setDuration(4000);
        //设置动画监听
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //判断是否需要升级
                SplashActivity.this.isNeedUpdate();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(info == null && netWorkEnanbl){
                    SplashActivity.this.loadLoginScreen();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        this.splash_layout.setAnimation(anim);
        anim.start();
    }

    @Override
    public void refresh(Object... params) {

    }

    @Override
    public void onClick(View v) {

    }


    //升级线程类
    private class UpdateinfoThread implements Runnable{
        @Override
        public void run() {
            Message msg = new Message();
            UpdateInfoService service = new UpdateInfoService(SplashActivity.this);
            try {
                msg.obj =service.getUpdateInof(R.string.update_url);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            updateInfoHandler.sendMessage(msg);
        }
    }


    //弹出升级信息对话框
    public void showUpdateDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
        builder.setCancelable(false);
        builder.setTitle(SplashActivity.this.getResources().getString(R.string.update_dialog_title));
        builder.setMessage(info.getDescription());
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LogManage.i(TAG, "下载新的apk");
                //判断是否有sd卡
                if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    pd.show();
                    String filepaths = SplashActivity.this.getResources().getString(R.string.new_apk_path);
                    //开启一个新线程，下载新的apk
                    new Thread(new DownLoadFileService(filepaths, info.getApkurl(), pd)).start();
                }else{
                    Toast.makeText(SplashActivity.this,"sd卡不存在",Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LogManage.i(TAG, "取消进入登陆界面");
                //进入登陆界面
                SplashActivity.this.loadLoginScreen();
            }
        });
        AlertDialog dialgo = builder.create();
        dialgo.show();
    }


    //进入登陆界面
    private void loadLoginScreen(){
        Intent loginIntent = new Intent(SplashActivity.this, LoginActivity.class);
        SplashActivity.this.startActivity(loginIntent);
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        SplashActivity.this.finish();//把splash界面从Activity栈中移除
    }

    //安装新下载的apk
    public void install(File file){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        this.startActivity(intent);
        this.finish();
    }


    //下载apk的线程内部类
    private class DownLoadFileService implements Runnable{
        //保存文件路径
        private String filePath;
        //新apk的下载url
        private String urlpath;
        private ProgressDialog pd;

        private DownLoadFileService(String filePath, String urlpath, ProgressDialog pd) {
            this.filePath = filePath;
            this.urlpath = urlpath;
            this.pd = pd;
        }

        @Override
        public void run() {
            File file = DownLoadFileTask.downLoadFile(this.filePath, this.urlpath);
            SplashActivity.this.install(file);
        }
    }

}
