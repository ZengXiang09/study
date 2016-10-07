package com.example.robort_test;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * Created by asus on 2016/4/8.
 */
public class WifiControl {
    WifiManager wifiManager = null;

    public WifiControl( WifiManager wifiManager){
        this.wifiManager = wifiManager;
    }

    public String  getServerIP(){

        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        String ip = intToIp(ipAddress);
        return ip;
    }

    private String intToIp(int i) {
        return (i & 0xFF ) + "." +
                ((i >> 8 ) & 0xFF) + "." +
                ((i >> 16 ) & 0xFF) + "." +
                ( i >> 24 & 0xFF) ;
    }

}
