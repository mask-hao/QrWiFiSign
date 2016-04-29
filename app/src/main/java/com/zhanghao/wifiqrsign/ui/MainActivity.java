package com.zhanghao.wifiqrsign.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.xys.libzxing.zxing.activity.CaptureActivity;
import com.zhanghao.wifiqrsign.Bean.WifiBean;
import com.zhanghao.wifiqrsign.Event.EventHelper;
import com.zhanghao.wifiqrsign.Fragment.SignHomeFragment;
import com.zhanghao.wifiqrsign.Fragment.SignMeFragment;
import com.zhanghao.wifiqrsign.R;
import com.zhanghao.wifiqrsign.adapter.MyPagerAdapter;
import com.zhanghao.wifiqrsign.utils.WiFiInfoHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.sliding_tabs)
    TabLayout slidingTabs;
    @Bind(R.id.viewpager)
    ViewPager viewpager;
    List<Fragment> fragmentList;
    List<String> fragmentTitles;
    Fragment signHomeFragment;
    Fragment signMeFragment;
    @Bind(R.id.sign_home)
    FloatingActionButton signHome;
    @Bind(R.id.action_share)
    FloatingActionButton actionShare;           //分享
    @Bind(R.id.action_login)
    FloatingActionButton actionLogin;           //登陆
    @Bind(R.id.action_product_qrcode)
    FloatingActionButton actionProductQrcode;   //生成二维码
    private static final String PACKAGE_URL_SCHEME = "package:";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
        initToolBar();
        initTabLayout();
        initFloatActionButton();
        initFile();                     //二维码保存到文件夹
    }

    private void initFile() {
        File storageDirectory= Environment.getExternalStorageDirectory();
        String path=storageDirectory.getPath()+"/QrWiFiSign";
        File file=new File(path);
        if (!file.exists()){
            file.mkdir();
        }
    }

    private void initFloatActionButton() {
        signHome.setOnClickListener(this);
        actionProductQrcode.setOnClickListener(this);
        actionLogin.setOnClickListener(this);
    }

    private void init() {
        fragmentList = new ArrayList<>();
        fragmentTitles = new ArrayList<>();
        fragmentTitles.add("签到主页");
        fragmentTitles.add("我的签到记录");
        if (signHomeFragment == null) {
            signHomeFragment = new SignHomeFragment();
        }
        if (signMeFragment == null) {
            signMeFragment = new SignMeFragment();
        }
        fragmentList.add(signHomeFragment);
        fragmentList.add(signMeFragment);
    }

    private void initTabLayout() {
        setupViewPager(viewpager);
        slidingTabs.addTab(slidingTabs.newTab().setText("签到主页"));
        slidingTabs.addTab(slidingTabs.newTab().setText("我的签到"));
        slidingTabs.setupWithViewPager(viewpager);
    }

    private void setupViewPager(ViewPager viewpager) {
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager(), this, fragmentList, fragmentTitles);
        viewpager.setAdapter(adapter);
    }

    private void initToolBar() {
        toolbar.setTitle("");
        toolbarTitle.setText("二维码签到");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign_home:
                initPermission();
                break;
            case R.id.action_product_qrcode:
                startActivity(new Intent(this,CreateSignAddressActivity.class));
            case R.id.action_login:
                LoginDialog dialog = new LoginDialog(MainActivity.this);
                dialog.show();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            Bundle bundle=data.getExtras();
            String result=bundle.getString("result");
            EventBus.getDefault().post(new EventHelper(Sign(result)));  //
        }
    }

    private String Sign(String result) {
        WifiBean wifiBean=new WifiBean();
        WiFiInfoHelper helper=new WiFiInfoHelper(this,wifiBean);
        helper.getWifiInfo();
        if (helper.WifiState()){
            Log.d("test",result+"\n"+wifiBean.getBSSID());
            if (result.equals(wifiBean.getBSSID()))
                return "签到成功";
            else
                return "签到失败";
        }else
            return "请链接Wifi";
    }

    private void initPermission() {
        PermissionGen.needPermission(MainActivity.this, 100,
                new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.FLASHLIGHT
                });
    }

    /**
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    /**
     * 权限获取成功
     */
    @PermissionSuccess(requestCode = 100)
    public void getPerMissionSuccess(){
        startActivityForResult(new Intent(MainActivity.this, CaptureActivity.class),0);
    }
    @PermissionFail(requestCode = 100)
    public void getPerMissionFailed(){
        AlertDialog dialog=new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.lackPermission)
                .setMessage(R.string.lackPermission_message)
                .setCancelable(false)
                .setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + getPackageName()));
        startActivity(intent);
    }

}
