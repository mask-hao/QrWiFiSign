package com.zhanghao.wifiqrsign.Bean;

/**
 * Created by 张浩 on 2016/4/13.
 */
public class WifiBean {
    public String getSSID() {
        return SSID;
    }

    public void setSSID(String SSID) {
        this.SSID = SSID;
    }

    public String getBSSID() {
        return BSSID;
    }

    public void setBSSID(String BSSID) {
        this.BSSID = BSSID;
    }

    public String getMACADDRESS() {
        return MACADDRESS;
    }

    public void setMACADDRESS(String MACADDRESS) {
        this.MACADDRESS = MACADDRESS;
    }

    private String SSID;
    private String BSSID;
    private String MACADDRESS;

    @Override
    public String toString() {
        return "SSID:"+SSID+"\n"+
                "BSSID:"+ BSSID+"\n"+
                "MACADDRESS:"+MACADDRESS+"\n";
    }
}
