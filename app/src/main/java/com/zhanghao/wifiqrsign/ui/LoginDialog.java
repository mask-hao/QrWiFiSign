package com.zhanghao.wifiqrsign.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zhanghao.wifiqrsign.R;

/**
 * Created by Ljx on 2016/4/21.
 */
public class LoginDialog extends Dialog{
    Context mContext;


    EditText location;
    EditText name;

    Button accept;
    Button cancel;

    String Loc;
    String Name;

    public LoginDialog(Context context){
        super(context);
        this.mContext = context;
    }

    public LoginDialog(Context context, int theme) {
        super(context, theme);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //LayoutInflater inflater =  LayoutInflater.from(mContext);//(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //View layout = inflater.inflate(R.layout.login, null);
        setContentView(R.layout.login);

        accept = (Button) findViewById(R.id.accept);
        cancel = (Button) findViewById(R.id.cancel);
        location = (EditText) findViewById(R.id.location);
        name = (EditText) findViewById(R.id.name);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Loc = location.getText().toString();
                    Name = name.getText().toString();
                    Log.d("Loc", Loc);
                    Log.d("Name", Name);
                    LoginDialog.this.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "请输入正确信息", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginDialog.this.dismiss();
            }
        });

    }
    /*@Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.accept:
                Log.d("click", "true");
                setAccept();
            case R.id.cancel:
                setCancel();
            default:
                Log.d("aaa", "aaa");
                break;
        }
        dismiss();
    }*/
}
