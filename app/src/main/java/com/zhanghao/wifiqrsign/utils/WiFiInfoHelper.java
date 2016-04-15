package com.zhanghao.wifiqrsign.utils;

import android.content.Context;
import android.net.wifi.WifiManager;

import com.zhanghao.wifiqrsign.Bean.WifiBean;

/**
 * Created by 张浩 on 2016/4/13.
 */
public class WiFiInfoHelper {
    private Context context;
    private WifiManager wifiManager;
    private WifiBean wifiBean;
    private android.net.wifi.WifiInfo wifiInfo;
    public WiFiInfoHelper(Context context,WifiBean wifiBean){
        this.context=context;
        wifiManager= (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        this.wifiBean=wifiBean;
        wifiInfo=wifiManager.getConnectionInfo();
    }
    public boolean WifiState(){
        return wifiManager.isWifiEnabled();
    }
    public String getWifiInfo(){
        wifiBean.setSSID(wifiInfo.getSSID());
        wifiBean.setBSSID(wifiInfo.getBSSID());
        wifiBean.setMACADDRESS(wifiInfo.getMacAddress());
        return wifiBean.toString();
    }
}
