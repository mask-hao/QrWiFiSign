package com.zhanghao.wifiqrsign.ui;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.xys.libzxing.zxing.encoding.EncodingUtils;
import com.zhanghao.wifiqrsign.Bean.WifiBean;
import com.zhanghao.wifiqrsign.R;
import com.zhanghao.wifiqrsign.utils.ImageHelper;
import com.zhanghao.wifiqrsign.utils.WiFiInfoHelper;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 张浩 on 2016/4/13.
 */
public class CreateSignAddressActivity extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.id_wifi)
    TextView tvWifi;
    @Bind(R.id.id_wifi_image)
    ImageView ivWifiImage;
    @Bind(R.id.id_wifi_name)
    TextView tvWifiName;
    @Bind(R.id.id_changeWifi)
    RelativeLayout RelChangeWifi;
    @Bind(R.id.id_signAddress)
    TextInputEditText edSignAddress;
    @Bind(R.id.id_signName)
    TextInputEditText edSignName;
    @Bind(R.id.id_refresh_wifiInfo)
    SwipeRefreshLayout RefreshWifiInfo;
    @Bind(R.id.id_sign_data)
    TextView SignTimeDate;
    @Bind(R.id.id_sign_time)
    TextView SignTimeFromTo;
    @Bind(R.id.id_chooseTime)
    RelativeLayout ChooseTime;
    @Bind(R.id.create_qrCode)
    Button createQrCode_Button;
    @Bind(R.id.qrcode)
    ImageView imageView;
    private WifiBean wifiBean;
    private AlertDialog TimeAlertDialog;
    private TimePickerDialog TimePickerDialog;
    private RelativeLayout SelectTimeFromRe,SelectTimeToRe;
    private RadioButton RadioButtonToday,RadioButtonEveryday,UserDefine;
    private String TimeOne="",TimeTwoFrom="",TimeTwoTo="";
    private Calendar calendar;
    private TimePickerDialog.OnTimeSetListener timeSetListener;
    private DateFormat dateFormat;
    private TextView TimeFrom,TimeTo;
    private int times;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createaddress);
        ButterKnife.bind(this);
        initToolbar();
        initView();
        initWiFi();
        initRefreshWifiData();
    }

    private void initRefreshWifiData() {
        RefreshWifiInfo.setColorSchemeColors(
                R.color.colorAccent,
                R.color.colorAccent,
                R.color.colorAccent,
                R.color.colorAccent);
        RefreshWifiInfo.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        initWiFi();
                        handler.sendEmptyMessage(200);
                    }
                }).start();
            }
        });
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 200:
                    RefreshWifiInfo.setRefreshing(false);
                    break;
            }
        }
    };

    private void initWiFi() {
        wifiBean = new WifiBean();
        WiFiInfoHelper wiFiInfoHelper = new WiFiInfoHelper(this, wifiBean);
        if (wiFiInfoHelper.WifiState()) {
            String wifiinfo = wiFiInfoHelper.getWifiInfo();
            ivWifiImage.setImageResource(R.drawable.ic_network_wifi_white_36dp);
            ImageHelper.changeImageColor(ivWifiImage, getResources().getColor(R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
            tvWifiName.setText("已连接到:" + wifiBean.getSSID());
            Log.d("wifiinfon", wifiinfo);
        } else {
            tvWifiName.setText(R.string.NoWifiConnect);
        }
    }


    private void initView() {
        RelChangeWifi.setOnClickListener(this);
        ChooseTime.setOnClickListener(this);
        createQrCode_Button.setOnClickListener(this);
        ImageHelper.changeImageColor(ivWifiImage, getResources().getColor(R.color.gray), PorterDuff.Mode.MULTIPLY);
    }


    private void initToolbar() {
        toolbar.setTitle(R.string.createadress);
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
        switch (v.getId()) {
            case R.id.id_changeWifi:
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                break;
            case R.id.id_chooseTime:
                showTimeChooseDiaLog();
                break;
            case R.id.select_timeFrom_re:
                showTimePickerDiaLog();
                initTimeFromPickerListener();
                break;
            case R.id.select_timeTo_re:
                showTimePickerDiaLog();
                initTimeToPickerListener();
                break;
            case R.id.create_qrCode:
                if (new WiFiInfoHelper(this,wifiBean).WifiState()){
                    CreateQrCode();
                }else{
                    Toast.makeText(this,"请链接wifi",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void showTimePickerDiaLog() {
        calendar=Calendar.getInstance();
        new TimePickerDialog(this,timeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),true).show();
    }

    private void initTimeFromPickerListener() {
        timeSetListener=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                times=hourOfDay;
                TimeTwoFrom= "上午:"+times+"时";
                TimeFrom.setText(TimeTwoFrom);
            }
        };
    }
    private void initTimeToPickerListener() {
        timeSetListener=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                times=hourOfDay;
                TimeTwoTo="下午:"+times+"时";
                TimeTo.setText(TimeTwoTo);
            }
        };
    }

    private void showTimeChooseDiaLog() {
        View view = LayoutInflater.from(this).inflate(R.layout.signtime, null);
        initDiaView(view);
        initCheckBoxsState();
        TimeAlertDialog = new AlertDialog.Builder(this)
                .setTitle("时间选择")
                .setView(view)
                .setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("time",TimeOne);
                        SignTimeDate.setText(TimeOne);
                        SignTimeFromTo.setText(TimeTwoFrom+"---"+TimeTwoTo);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TimeAlertDialog.dismiss();
                    }
                })
                .show();

    }

    private void initDiaView(View view) {
        SelectTimeFromRe= (RelativeLayout) view.findViewById(R.id.select_timeFrom_re);
        SelectTimeToRe= (RelativeLayout) view.findViewById(R.id.select_timeTo_re);
        RadioButtonToday= (RadioButton) view.findViewById(R.id.time_today);
        RadioButtonEveryday= (RadioButton) view.findViewById(R.id.time_everyday);
        UserDefine= (RadioButton) view.findViewById(R.id.user_define_time);
        TimeFrom= (TextView) view.findViewById(R.id.select_timeFrom_tv);
        TimeTo= (TextView) view.findViewById(R.id.select_timeTo_tv);
        SelectTimeFromRe.setOnClickListener(this);
        SelectTimeToRe.setOnClickListener(this);
    }

    private void initCheckBoxsState() {
        TimeOne="";
        if (RadioButtonToday.isChecked()){
            TimeOne="今天";
        }else if (RadioButtonEveryday.isChecked()){
            TimeOne="每天";
        }else if (UserDefine.isChecked()){
            TimeOne="自定义";
        }
    }
    public void CreateQrCode(){
        Bitmap bitmap=EncodingUtils.createQRCode(wifiBean.getBSSID(),300,300,null);
        imageView.setImageBitmap(bitmap);
        saveBitmap2file(bitmap,"qrcode.jpg");
    }



    public static boolean saveBitmap2file(Bitmap bmp, String filename)
    {
        Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
        int quality = 100;
        OutputStream stream = null;
        try
        {
            stream = new FileOutputStream("/sdcard/QrWiFiSign/" + filename);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return bmp.compress(format, quality, stream);
    }
}
