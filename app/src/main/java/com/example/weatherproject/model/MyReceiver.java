package com.example.weatherproject.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.example.weatherproject.weather.MainActivity;

public class MyReceiver extends BroadcastReceiver {

    CheckConnectivityCallback checkConnectivityCallback;

    public MyReceiver(CheckConnectivityCallback checkConnectivityCallback){
        this.checkConnectivityCallback = checkConnectivityCallback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //Phần dùng để check xem điện thoại có đang kết nối vào internet hay không
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        //
        checkConnectivityCallback.setOnConnectivityChangeListener(netInfo != null && netInfo.isConnected());
    }

}
