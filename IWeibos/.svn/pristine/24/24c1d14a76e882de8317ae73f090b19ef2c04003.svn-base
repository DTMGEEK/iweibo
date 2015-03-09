package com.tg.iweibo.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.tg.iweibo.R;


/**
 * Created by jake（lian_weijian@126.com）
 * Date: 2015-01-17
 * Time: 20:27
 */

public class NetUtil {

    /**
     * 检查网络是否已经链接
     * @param context
     * @return  boolean类型，true：网络已经链接  false：网络未链接
     */
    public static boolean checkNet(Context context) {
        boolean flag = false;
        //获取手机连接
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != conn) {
            NetworkInfo info = conn.getActiveNetworkInfo();
            if(null != info && info.isConnected()){
                flag = true;
            }
        }
        return flag;
    }


    /**
     * 如果没有连接网络就 弹出一个dialog然后跳转到网络设置页面
     * @param context
     */
    public static void isNeedSetNetWork(final Context context) {

            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setCancelable(false)
                    .setTitle(context.getResources().getString(R.string.network_dialog_title)).setMessage(R.string.network_dialog_message)
                    .setPositiveButton(R.string.network_dialog_postbtn, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent netWrokIntent = new Intent(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                            context.startActivity(netWrokIntent);
                        }
                    }).create();
            dialog.show();

    }



}
