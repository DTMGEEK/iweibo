package com.tg.iweibo.activity;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Entity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.tg.iweibo.R;
import com.tg.iweibo.app.WeiboApplication;
import com.tg.iweibo.domain.EmotionInfo;
import com.tg.iweibo.domain.Task;
import com.tg.iweibo.iservice.IWeiboActivity;
import com.tg.iweibo.utils.LogManage;
import com.tg.iweibo.weiboutil.AccessTokenKeeper;
import com.tg.iweibo.weiboutil.Constants;
import com.tg.iweibo.weiboutil.SendRequest;

import org.apache.http.HttpEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AuthActivity extends Activity implements IWeiboActivity{


    private Button oauth_bn = null;

    /** 微博 Web 授权类，提供登陆等功能 */
    private WeiboAuth mWeiboAuth = null;
    /** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能 */
    private Oauth2AccessToken mAccessToken = null;

    private SsoHandler ssoHandler = null;

    private Dialog dialog = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_layout);

       /* View dialogView = View.inflate(this,R.layout.oath,null);//实例化dialog布局文件
        dialog  = new Dialog(AuthActivity.this,R.style.dialog); //把样式文件设置上去
        dialog.setContentView(dialogView);   //设置布局文件
        dialog.show(); //显示自定义对话框*/

        this.mWeiboAuth = new WeiboAuth(this, Constants.APP_KEY,Constants.REDIRECT_URL,Constants.SCOPE);
        //this.oauth_bn = (Button) this.findViewById(R.id.oauth_bn);
        this.oauth_bn = (Button) this.findViewById(R.id.oauth_bn);
        oauth_bn.setOnClickListener(this);

    }


    @Override
    public void init() {

    }







    @Override
    public void refresh(Object... params) {

    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            //跳转到授权页面
            case R.id.oauth_bn:
                mWeiboAuth.anthorize(new AuthListener());
            break;
        }
    }


   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(this.ssoHandler != null){
            this.ssoHandler.authorizeCallBack(requestCode,resultCode,data);
        }
    }*/

    //授权监听
    private class AuthListener implements WeiboAuthListener{

        @Override
        public void onComplete(Bundle value) {
            mAccessToken = Oauth2AccessToken.parseAccessToken(value);
            if(mAccessToken.isSessionValid()){
                // 保存 Token 到 SharedPreferences
                AccessTokenKeeper.writeAccessToken(AuthActivity.this,mAccessToken);
                Toast.makeText(AuthActivity.this,AuthActivity.this.getString(R.string.auth_success),Toast.LENGTH_SHORT).show();
                //完成授权后就跳转到微博首页
                Intent homeIntent = new Intent(AuthActivity.this,HomeActivity.class);
                AuthActivity.this.startActivity(homeIntent);
                AuthActivity.this.finish();
            }else{
                //授权失败返回的code
                String code = value.getString("code");
                if(TextUtils.isEmpty(code)){
                    Toast.makeText(AuthActivity.this,AuthActivity.this.getString(R.string.auth_fail)+code,Toast.LENGTH_LONG).show();
                    AuthActivity.this.finish();
                }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(AuthActivity.this,e.getMessage().toString(),Toast.LENGTH_LONG).show();
            AuthActivity.this.finish();
        }

        @Override
        public void onCancel() {
            Toast.makeText(AuthActivity.this,AuthActivity.this.getString(R.string.auth_cancel),Toast.LENGTH_LONG).show();
            AuthActivity.this.finish();
        }
    }




}
