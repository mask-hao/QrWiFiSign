package com.zhanghao.wifiqrsign.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 张浩 on 2016/4/11.
 */
public class MyPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;
    private List<String> fragmentTitles;
    private Context context;
    public MyPagerAdapter(FragmentManager fm,Context context, List<Fragment> list,List<String> titles) {
        super(fm);
        this.context=context;
        this.fragmentList=list;
        this.fragmentTitles=titles;
    }


    //    public void addFragment(Fragment fragment,String title){
//        fragmentList.add(fragment);
//        fragmentTitles.add(title);
//    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitles.get(position);
    }
}
