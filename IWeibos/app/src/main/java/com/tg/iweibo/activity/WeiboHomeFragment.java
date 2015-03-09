package com.tg.iweibo.activity;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.tg.iweibo.R;
import com.tg.iweibo.adapters.WeiboHomeAdapter;
import com.tg.iweibo.app.WeiboApplication;
import com.tg.iweibo.customcontrols.RefreshListView;
import com.tg.iweibo.domain.WeiboInfos;
import com.tg.iweibo.utils.LogManage;
import com.tg.iweibo.weiboutil.AccessTokenKeeper;
import com.tg.iweibo.weiboutil.JsonParser;
import com.tg.iweibo.weiboutil.SendRequest;

import org.apache.http.HttpEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WeiboHomeFragment extends Fragment implements RefreshListView.IinterfaceListener {


    public static final int REFRESH_WEIBO = 1;
    public static final int LOAD_WEIBO = 2;
    private List<WeiboInfos> list = null;
    private RefreshListView rlv = null;
    private String TAG= "WeiboHomeFragment";

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case REFRESH_WEIBO:
                  WeiboHomeAdapter adapter = (WeiboHomeAdapter) msg.obj;
                  rlv.setAdapter(adapter);
                  adapter.notifyDataSetChanged();
                  rlv.refreshComplete();
                break;

                case LOAD_WEIBO:
                    adapter = (WeiboHomeAdapter) msg.obj;
                    rlv.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    rlv.refreshComplete();
                break;
            }
         }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_weibohome,null);
        rlv = (RefreshListView) view.findViewById(R.id.refreshlistview);
        rlv.setInterface(this);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    public interface IWeiboInfoListener {

        public List getWeiboInfo();
    }


    public void setInfo(List weiboinf){
        this.list = weiboinf;
        WeiboApplication.weiboInfosList = weiboinf;
        WeiboApplication.maxId = ((WeiboInfos)weiboinf.get(0)).getMid();
        WeiboApplication.since_id = ((WeiboInfos)weiboinf.get(weiboinf.size()-1)).getId();
         WeiboHomeAdapter adapter = new WeiboHomeAdapter(this.getActivity(),list);
         rlv.setAdapter(adapter);
        System.out.print(list.size());
    }



    @Override
    public void IRefresh() {
        LogManage.i(TAG,"iRefresh回调");
        new Thread(){
            @Override
            public void run() {
                super.run();
                refreshData();
            }
        }.start();

    }



     /*
     * 上拉更新数据回调
     */
    private void refreshData(){
        try {

            Oauth2AccessToken token = AccessTokenKeeper.readAccessToken(getActivity());
            Map<String, String> params = new HashMap<String, String>();
            params.put("access_token", token.getToken());
            params.put("since_id", WeiboApplication.since_id);
            String requestPath = this.getResources().getString(R.string.requestPath);
            HttpEntity entity = SendRequest.sendGetRequest(requestPath, params);

            List<WeiboInfos> friendsTimeLine_list = JsonParser.friendsTimeLine_To_Map(entity);
            WeiboApplication.maxId = ((WeiboInfos)friendsTimeLine_list.get(0)).getMid();
            WeiboApplication.since_id = ((WeiboInfos)friendsTimeLine_list.get(friendsTimeLine_list.size()-1)).getId();
            WeiboApplication.weiboInfosList = friendsTimeLine_list;
            Message msg = handler.obtainMessage();
            msg.what = REFRESH_WEIBO;
            WeiboHomeAdapter adapter = new WeiboHomeAdapter(getActivity(), WeiboApplication.weiboInfosList);
            msg.obj = adapter;
            handler.sendMessage(msg);
        }catch(Exception e){
            e.printStackTrace();
        }
    }


     /*
     * 下拉加载回调
     */
    @Override
    public void onLoad() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                onLoadData();
            }
        }.start();
    }


    private void onLoadData(){
        try {
            Oauth2AccessToken token = AccessTokenKeeper.readAccessToken(getActivity());
            Map<String, String> params = new HashMap<String, String>();
            params.put("access_token", token.getToken());
            params.put("max_id", WeiboApplication.maxId);
            String requestPath = this.getResources().getString(R.string.requestPath);
            HttpEntity entity = SendRequest.sendGetRequest(requestPath, params);
            List<WeiboInfos> friendsTimeLine_list = JsonParser.friendsTimeLine_To_Map(entity);
            WeiboApplication.weiboInfosList.addAll(friendsTimeLine_list);
            WeiboApplication.maxId = ((WeiboInfos)WeiboApplication.weiboInfosList.get(0)).getMid();
            WeiboApplication.since_id = ((WeiboInfos)  WeiboApplication.weiboInfosList.get(friendsTimeLine_list.size()-1)).getId();
            Message msg = handler.obtainMessage();
            msg.what = LOAD_WEIBO;
            WeiboHomeAdapter adapter = new WeiboHomeAdapter(getActivity(),  WeiboApplication.weiboInfosList);
            msg.obj = adapter;
            handler.sendMessage(msg);
        }catch(Exception e){
            e.printStackTrace();
        }
    }



}
