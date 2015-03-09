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

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.tg.iweibo.R;
import com.tg.iweibo.adapters.WeiboHomeAdapter;
import com.tg.iweibo.adapters.WeiboPersonalAdapter;
import com.tg.iweibo.app.WeiboApplication;
import com.tg.iweibo.customcontrols.RefreshListView;
import com.tg.iweibo.domain.WeiboInfos;
import com.tg.iweibo.iservice.IWeiboActivity;
import com.tg.iweibo.utils.LogManage;
import com.tg.iweibo.weiboutil.AccessTokenKeeper;
import com.tg.iweibo.weiboutil.JsonParser;
import com.tg.iweibo.weiboutil.SendRequest;

import org.apache.http.HttpEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonalFragment extends Fragment  implements RefreshListView.IinterfaceListener{

    private static final int REFRESH_WEIBO_PERSONAL = 1;
    private static final int LOAD_WEIBO_PERSONAL = 2;

    private List<WeiboInfos> list = null;
    private RefreshListView rlv = null;
    private String TAG= "PersonalFragment";

    private WeiboPersonalAdapter adapter = null;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case REFRESH_WEIBO_PERSONAL:
                    adapter = (WeiboPersonalAdapter) msg.obj;
                    rlv.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    rlv.refreshComplete();

                break;

                case LOAD_WEIBO_PERSONAL:
                    adapter = (WeiboPersonalAdapter) msg.obj;
                    rlv.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                break;
            }
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        new Thread(){
            @Override
            public void run() {
                try {
                    Oauth2AccessToken token = AccessTokenKeeper.readAccessToken(getActivity());
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("access_token", token.getToken());
                    String requestPath = getResources().getString(R.string.requestPath_person);
                    HttpEntity entity = SendRequest.sendGetRequest(requestPath, params);

                    List<WeiboInfos> friendsTimeLine_list = JsonParser.friendsTimeLine_To_Map(entity);
                    WeiboApplication.maxId_personal = ((WeiboInfos) friendsTimeLine_list.get(0)).getMid();
                    WeiboApplication.since_id_personal = ((WeiboInfos) friendsTimeLine_list.get(friendsTimeLine_list.size() - 1)).getId();
                    WeiboApplication.weiboInfos_person_List = friendsTimeLine_list;
                    Message msg = handler.obtainMessage();
                    msg.what = REFRESH_WEIBO_PERSONAL;
                    WeiboPersonalAdapter adapter = new WeiboPersonalAdapter(getActivity(), WeiboApplication.weiboInfos_person_List);
                    msg.obj = adapter;
                    handler.sendMessage(msg);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal,null);
        rlv = (RefreshListView) view.findViewById(R.id.listView_PersonWeibo);
        rlv.setInterface(this);
        return view;
    }


    public void setInfo(List weiboinf_peronsal){
       // this.list = weiboinf_peronsal;
        this.rlv = (RefreshListView) LayoutInflater.from(this.getActivity())
                .inflate(R.layout.fragment_personal,null).findViewById(R.id.listView_PersonWeibo);
      //  WeiboApplication.weiboInfos_person_List = weiboinf_peronsal;
      //  WeiboApplication.maxId_personal = ((WeiboInfos)weiboinf_peronsal.get(0)).getMid();
     //   WeiboApplication.since_id_personal = ((WeiboInfos)weiboinf_peronsal.get(weiboinf_peronsal.size()-1)).getId();
        adapter = new WeiboPersonalAdapter(this.getActivity(),weiboinf_peronsal);

        rlv.setAdapter(adapter);
        System.out.print(weiboinf_peronsal.size());
    }




    @Override
    public void IRefresh() {
        LogManage.i(TAG, "iRefresh回调");
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
            String requestPath = this.getResources().getString(R.string.requestPath_person);
            HttpEntity entity = SendRequest.sendGetRequest(requestPath, params);

            List<WeiboInfos> friendsTimeLine_list = JsonParser.friendsTimeLine_To_Map(entity);
            WeiboApplication.maxId_personal = ((WeiboInfos)friendsTimeLine_list.get(0)).getMid();
            WeiboApplication.since_id_personal = ((WeiboInfos)friendsTimeLine_list.get(friendsTimeLine_list.size()-1)).getId();
            WeiboApplication.weiboInfos_person_List = friendsTimeLine_list;
            Message msg = handler.obtainMessage();
            msg.what = REFRESH_WEIBO_PERSONAL;
            WeiboHomeAdapter adapter = new WeiboHomeAdapter(getActivity(), WeiboApplication.weiboInfos_person_List);
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
            String requestPath = this.getResources().getString(R.string.requestPath_person);
            HttpEntity entity = SendRequest.sendGetRequest(requestPath, params);
            List<WeiboInfos> friendsTimeLine_list = JsonParser.friendsTimeLine_To_Map(entity);
            WeiboApplication.weiboInfos_person_List.addAll(friendsTimeLine_list);
            WeiboApplication.maxId_personal = ((WeiboInfos)WeiboApplication.weiboInfos_person_List.get(0)).getMid();
            WeiboApplication.since_id_personal = ((WeiboInfos)  WeiboApplication.weiboInfos_person_List.get(friendsTimeLine_list.size()-1)).getId();
            Message msg = handler.obtainMessage();
            msg.what = LOAD_WEIBO_PERSONAL;
            WeiboHomeAdapter adapter = new WeiboHomeAdapter(getActivity(),  WeiboApplication.weiboInfos_person_List);
            msg.obj = adapter;
            handler.sendMessage(msg);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
