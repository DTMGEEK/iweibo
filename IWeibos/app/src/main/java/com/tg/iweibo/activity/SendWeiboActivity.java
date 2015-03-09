package com.tg.iweibo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.tg.iweibo.R;
import com.tg.iweibo.customcontrols.SildingFinishLayout;
import com.tg.iweibo.iservice.IWeiboActivity;
import com.tg.iweibo.utils.LogManage;


public class SendWeiboActivity extends Activity implements IWeiboActivity{

    public static final String TAG = "SendWeiboActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sendweibo);

       //自定义控件，实现侧滑呼出
        SildingFinishLayout sildingFinishLayout  = (SildingFinishLayout) this.findViewById(R.id.sildingFinishLayout);
        //设置监听
        sildingFinishLayout.setOnSildingFinishListener(new SildingFinishLayout.OnSildingFinishListener() {


            @Override
            public void onSildingFinish() {
                //发送微博
                LogManage.i(TAG,"发送微博");
                SendWeiboActivity.this.finish();
            }
        });

        //添加侧滑的view
        sildingFinishLayout.setTouchView(sildingFinishLayout);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0,R.anim.base_slide_right_out);
    }

    @Override
    public void init() {

    }

    @Override
    public void refresh(Object... params) {

    }

    @Override
    public void onClick(View v) {

    }
}
