package com.tg.iweibo.activity;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tg.iweibo.R;
import com.tg.iweibo.customcontrols.RefreshListView;
import com.tg.iweibo.iservice.IWeiboActivity;

public class AttentionFragment extends Fragment implements RefreshListView.IinterfaceListener{

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_attention,null);
    }


    @Override
    public void IRefresh() {

    }

    @Override
    public void onLoad() {

    }
}
