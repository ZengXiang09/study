package com.example.robort_test;

import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogRecord;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public static final int CONNECT_SUCCESS = 1;
    public static final int GETNEW_MESSAGE = 2;
    private Button BtnListen;
    private Button BtnUp;
    private Button BtnDown;
    private Button BtnLeft;
    private Button BtnRight;
    private EditText ServerIP;  //显示当前的服务器的IP地址
    //private static EditText ClientIP;  //显示当前客户端的IP地址
    private static Spinner spinner;
    private WifiManager wifiManager;
    private WifiControl wifiControl;
    private static MyThread thread;
    private RadioGroup radioGroup;
    private Button BtnStart;
    private Button BtnStop;
    private DealData deal;
    private boolean state = true;
    private static ArrayAdapter<String> adapter;
    private List<String> client_ips ;
    public static Handler mHandler;
    private TextView textres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitView();

        BtnListen.setOnClickListener(this);
        BtnDown.setOnClickListener(this);
        BtnUp.setOnClickListener(this);
        BtnLeft.setOnClickListener(this);
        BtnRight.setOnClickListener(this);
        BtnStart.setOnClickListener(this);
        BtnStop.setOnClickListener(this);
        setRadioGroupListener();
        //adapter
        client_ips = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,client_ips);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch (msg.what) {
                    case CONNECT_SUCCESS: addClientIP(thread.getClientIp());
                        break;
                    case GETNEW_MESSAGE:
                        textres.setText(msg.obj.toString());
                }
            }

        };

    }

    public void InitView(){
        wifiManager = (WifiManager) getSystemService(this.WIFI_SERVICE);
        wifiControl = new WifiControl(wifiManager);
        BtnListen = (Button) findViewById(R.id.listen_btn);
        BtnDown = (Button) findViewById(R.id.down_btn);
        BtnLeft = (Button) findViewById(R.id.left_btn);
        BtnUp = (Button) findViewById(R.id.up_btn);
        BtnStart = (Button) findViewById(R.id.start_btn);
        BtnStop = (Button) findViewById(R.id.stop_btn);
        BtnRight = (Button) findViewById(R.id.right_btn);
        ServerIP = (EditText) findViewById(R.id.ip_content);
       // ClientIP = (EditText) findViewById(R.id.CIP_content);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        spinner = (Spinner) findViewById(R.id.id_spinner_clientIP);
        textres = (TextView) findViewById(R.id.id_text_res);
        deal = new DealData();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.listen_btn:
                String ip  = wifiControl.getServerIP();
                ServerIP.setText(ip);
                thread = new MyThread(deal, mHandler);
                thread.start();
                break;
            case R.id.up_btn:
                deal.setUp(true);
                break;

            case R.id.down_btn:
                deal.setDown(true);
                break;

            case R.id.right_btn:
                deal.setRight(true);
                break;

            case R.id.left_btn:
                deal.setLeft(true);
                break;

            case R.id.start_btn:
                if (state){
                    deal.setMachine_on(true);
                    BtnStart.setText("关闭电机");
                    state = false;
                }else {
                    deal.setMachine_off(true);
                    BtnStart.setText("打开电机");
                    state = true;
                }

                break;

            case R.id.stop_btn:
                deal.setStop2(true);
                deal.setStop();
                break;

            default:
                break;
        }

    }


    public void InitRadio(){
        deal.setLow(false);
        deal.setMid(false);
        deal.setHigh(false);
    }


    public void setRadioGroupListener(){
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioChecked = group.getCheckedRadioButtonId();
                switch (radioChecked) {
                    case R.id.high_radio:
                        InitRadio();
                        deal.setHigh(true);
                        deal.setHighSpeed();
                        break;
                    case R.id.mid_radio:
                        InitRadio();
                        deal.setMid(true);
                        deal.setMidSpeed();
                        break;
                    case R.id.low_radio:
                        InitRadio();
                        deal.setLow(true);
                        deal.setLowSpeed();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    //添加客户端的IP
    public static void addClientIP(String ip){

        //防止出现重复的
        for (int i = 0;i < adapter.getCount();i ++){
            if (ip.equals(adapter.getItem(i))){
                return;
            }
        }

        if (!ip.equals("")){
            adapter.add(ip);
            int position = adapter.getPosition(ip);
            spinner.setSelection(position);
        }
    }


}
