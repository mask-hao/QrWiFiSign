package com.zhanghao.wifiqrsign.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.widget.ImageView;

import com.zhanghao.wifiqrsign.R;

import java.util.List;

/**
 * Created by 张浩 on 2016/4/13.
 */
public class ImageHelper {
    private static Context mContext;
    public static void initialize(Context context){
        mContext=context;
    }
    /**
     *
     * @param ImageViewList
     * @param Rescolor
     * @param mode
     */
    public static void changeImageColor(List<ImageView> ImageViewList,int Rescolor,PorterDuff.Mode mode){
        for (ImageView imageView:ImageViewList){
            imageView.getDrawable().setColorFilter(Rescolor,mode);
        }
    }

    /**
     *
     * @param imageView
     * @param Rescolor
     * @param mode
     */
    public static void changeImageColor(ImageView imageView,int Rescolor,PorterDuff.Mode mode){
            imageView.getDrawable().setColorFilter(Rescolor,mode);
    }
}
