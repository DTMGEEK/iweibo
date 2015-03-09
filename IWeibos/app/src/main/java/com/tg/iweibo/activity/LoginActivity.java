package com.tg.iweibo.activity;


import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.tg.iweibo.R;
import com.tg.iweibo.domain.EmotionInfo;
import com.tg.iweibo.engine.MainService;
import com.tg.iweibo.iservice.IWeiboActivity;
import com.tg.iweibo.weiboutil.AccessTokenKeeper;
import com.tg.iweibo.weiboutil.SendRequest;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.apache.http.HttpEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 登陆Activity
 * 实现登录功能
 */
public class LoginActivity extends Activity implements IWeiboActivity {

	 private static final String TAG = "LoginActivity";

	 private RelativeLayout loginlayout = null;

     private ImageView login_loginbn_iv = null;



	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
	        this.setContentView(R.layout.login_layout);
            this.login_loginbn_iv = (ImageView) this.findViewById(R.id.login_loginbn_iv);
            this.login_loginbn_iv.setOnClickListener(this);
            init();
	        AlphaAnimation anim = new AlphaAnimation(0.0f,1.0f);
	        anim.setDuration(5000);
	        this.loginlayout = (RelativeLayout) this.findViewById(R.id.loginlayout);
	        this.loginlayout.setAnimation(anim);
	        anim.start();


     }

		@Override
		public void onClick(View v) {
			switch(v.getId()){
                //点击授权按钮，判断是否进行了授权没有就跳转到授权的activity进行授权
                case R.id.login_loginbn_iv:
                    if(!(LoginActivity.this.isAuthroze())) {
                        Intent authIntent = new Intent(LoginActivity.this, AuthActivity.class);
                        LoginActivity.this.startActivity(authIntent);
                    }else{
                        //已经授权就跳转到微博首页
                        Intent homeIntent = new Intent(LoginActivity.this,HomeActivity.class);
                        LoginActivity.this.startActivity(homeIntent);
                    }
                break;
            }
		}


        //初始化方法
        @Override
        public void init() {
          /*  new Thread() {
                @Override
                public void run() {
                    Map<String,String> map = new HashMap<String,String>();
                    Oauth2AccessToken token = AccessTokenKeeper.readAccessToken(LoginActivity.this);
                    map.put("access_token",token.getToken());
                    map.put("type","face");
                    try {
                        HttpEntity entity = SendRequest.sendGetRequest("https://api.weibo.com/2/emotions.json", map);
                        byte[] b = getByte(entity.getContent());
                        String urlstr = new String(b);
                        List<EmotionInfo> list = parserEmotion(urlstr);
                        writeFile(list);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();*/

        }


   /* //从服务器得到byte数据
    private byte[] getByte(InputStream is){
        ByteArrayOutputStream baos = null;
        try {
            byte[] buffer = new byte[1024];
            baos = new ByteArrayOutputStream();
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(baos != null){
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return baos.toByteArray();
    }

    //解析json数据
    private List<EmotionInfo> parserEmotion(String str){
        List<EmotionInfo> list = new ArrayList<EmotionInfo>();
        try {
            JSONArray jsonArray = new JSONArray(str);
            for(int i = 0; i < jsonArray.length(); i++){
                EmotionInfo einfo = new EmotionInfo();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                einfo.setCategory(jsonObject.getString("category"));
                einfo.setCommon(jsonObject.getBoolean("common"));
                einfo.setHot(jsonObject.getBoolean("hot"));
                einfo.setIcon(jsonObject.getString("icon"));
                einfo.setPhrase(jsonObject.getString("phrase"));
                einfo.setType(jsonObject.getString("type"));
                einfo.setUrl(jsonObject.getString("url"));
                einfo.setValue(jsonObject.getString("value"));
                list.add(einfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }


    //写入文件
    public void writeFile(List<EmotionInfo> list){
        for(int i = 0 ; i < list.size() ; i++){
            try {
                URL url = new URL(list.get(i).getUrl());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                InputStream is = conn.getInputStream();

                File f = new File("/sdcard/emotinoseee/"+list.get(i).getUrl()+"."+list.get(i).getUrl().substring((list.get(i).getUrl().lastIndexOf(".")+1)));
                if(!f.getParentFile().exists()){
                    f.mkdirs();

                }
                FileOutputStream fos = new FileOutputStream(f);
                byte[] buffer = new byte[1024];
                int len = 0;
                while((len = is.read(buffer)) != -1){
                    fos.write(buffer,0,len);
                }
                fos.close();
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
*/



    @Override
		public void refresh(Object... params) {
			// TODO Auto-generated method stub
			
		}


        @Override
        protected void onResume() {

            super.onResume();
        }


        //判断用户有没有授权，没有就跳转到授权Activity
         public boolean isAuthroze(){
             boolean flag = false;
             Oauth2AccessToken token = AccessTokenKeeper.readAccessToken(LoginActivity.this);
             String uid = token.getUid();
             if(!TextUtils.isEmpty(uid)){
                 flag = true;
             }
             return flag;
        }


}
