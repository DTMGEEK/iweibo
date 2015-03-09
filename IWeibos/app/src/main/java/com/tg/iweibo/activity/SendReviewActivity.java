package com.tg.iweibo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.tg.iweibo.R;
import com.tg.iweibo.iservice.IWeiboActivity;

public class SendReviewActivity extends Activity implements IWeiboActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendreview);
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
