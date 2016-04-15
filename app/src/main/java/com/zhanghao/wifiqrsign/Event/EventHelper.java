package com.zhanghao.wifiqrsign.Event;

/**
 * Created by 张浩 on 2016/4/13.
 */
public class EventHelper {
    private  boolean isSignEd;

    public String getResult() {
        return result;
    }

    private String result;
    public EventHelper(String result){
        this.result=result;
    }
    public EventHelper(boolean isSignEd){
        this.isSignEd=isSignEd;
    }
}
