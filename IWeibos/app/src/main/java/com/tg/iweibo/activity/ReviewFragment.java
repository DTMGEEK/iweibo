package com.tg.iweibo.activity;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.tg.iweibo.R;
import com.tg.iweibo.customcontrols.RefreshListView;
import com.tg.iweibo.iservice.IWeiboActivity;
import com.tg.iweibo.weiboutil.AccessTokenKeeper;
import com.tg.iweibo.weiboutil.SendRequest;

import org.apache.http.HttpEntity;

import java.util.HashMap;
import java.util.Map;

public class ReviewFragment extends Fragment implements RefreshListView.IinterfaceListener  {


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        new Thread(){
            @Override
            public void run() {
                try {
                    Oauth2AccessToken token = AccessTokenKeeper.readAccessToken(getActivity());
                    String url = getResources().getString(R.string.requestPath_comment);
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("access_token", token.getToken());
                    HttpEntity entity = SendRequest.sendGetRequest(url, params);

                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_review,null);
    }


    @Override
    public void IRefresh() {

    }

    @Override
    public void onLoad() {

    }
}
