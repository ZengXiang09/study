package com.example.robort_test;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import android.os.Message;

/**
 * Created by asus on 2016/4/16.
 */
public class ReceiveThread implements Runnable {
    Socket socket = null;
    //BufferedReader reader = null;
    DataInputStream reader = null;
    Handler handler;
    byte sframe[] ={(byte) 0x40, (byte) 0x55, (byte) 0x45};//@UE
    byte address = (byte) 0x10;
    byte datalen = 0;
    byte curh;
    byte curl;
    byte volh;
    byte voll;
    byte obj[] = new byte[8]; //接收到传感器的值
    byte sum = (byte) 0x00;
    byte xor = (byte) 0x00;
    byte eframe = (byte) 0x23;
    Sensor sensor = null;
    List<Sensor> list;


    double cur = 0;
    double vol = 0;

    int state = 0;
    int lencnt  = 0;



    public ReceiveThread(Socket socket,Handler handler) throws IOException{
        this.socket = socket;
        this.handler = handler;
        //reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        reader = new DataInputStream(socket.getInputStream());
        sensor = new Sensor();
        list = new ArrayList<Sensor>();
    }
    @Override
    public void run() {

        try {
            while (true) {
                byte recdata = reader.readByte();
                deal_getData(recdata);

                if (state == 20){
                    cur = (int) curh + ((int) curl) / 100;
                    sensor.setCur(cur);
                    vol = (int)volh + ((int)voll) / 100;
                    sensor.setVol(vol);

                    sensor.setSensor0(obj[0]);
                    sensor.setSensor1((int) obj[1]);
                    sensor.setSensor2((int) obj[2]);
                    sensor.setSensor3((int) obj[3]);
                    sensor.setSensor4((int) obj[4]);
                    sensor.setSensor5((int) obj[5]);
                    sensor.setSensor6((int) obj[6]);
                    sensor.setSensor7((int) obj[7]);
                    sendMessage();
                    state = 0;




                }


            }
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public void deal_getData(byte rcvdat ){

        if (state == 0){
            sum = 0;  //和位置零
            xor = 0;  //异或位置零

            if (sframe[0] == rcvdat)
                state = 1;
            else
                state = 0;
        }else if (state == 1){
            if (sframe[1] == rcvdat)
                state = 2;
            else
                state = 0;
        }else if (state == 2){
            if (sframe[2] == rcvdat)
                state = 3;
            else
                state = 0;
        }else if (state == 3){
            sum = rcvdat;
            xor = rcvdat;
            if (address == rcvdat)
                state = 4;
            else
                state = 0;
        }else if (state == 4){
            lencnt = 0;
            datalen = rcvdat;
            sum += rcvdat;
            xor ^= rcvdat;
            state = 5;

        }else if (state == 5){
            curh = rcvdat;
            sum += rcvdat;
            xor ^= rcvdat;
            lencnt++;
            state = 6;
        }else if (state == 6){
            curl = rcvdat;

            sum += rcvdat;
            xor ^= rcvdat;
            lencnt++;
            state = 7;

        }else if (state == 7){
            volh = rcvdat;

            sum += rcvdat;
            xor ^= rcvdat;
            lencnt++;
            state = 8;
        }else if (state == 8){
            voll = rcvdat;

            sum += rcvdat;
            xor ^= rcvdat;
            lencnt++;
            state = 9;
        }else if (state >= 9 && state <= 16){
            int index = 0;
            obj[index++] = rcvdat;

            sum += rcvdat;
            xor ^= rcvdat;
            if (lencnt == datalen)
                state = 17;
            else
                state++;
        }else if (state == 17){
            if (sum == rcvdat)
                state = 18;
            else
                state = 0;
        }else if (state == 18){
            if (xor == rcvdat)
                state = 19;
            else
                state = 0;
        }else if (state == 19){
            if (eframe == rcvdat)
                state = 20;
            else
                state = 0;
        }

    }

    //msg中设置一个Bundle 用 bundle传递sensor对象

    public void sendMessage(){
        Message msg = new Message();
        msg.what = MainActivity.GETNEW_MESSAGE;
        msg.obj = "cur:" + sensor.getCur() + "vol" + sensor.getVol() + "\n"
                + "sensor0:" + sensor.getSensor0() + " sensor1:" + sensor.getSensor1() + "\n"
                + "sensor2:" + sensor.getSensor2() + " sensor3:" + sensor.getSensor3() + "\n"
                + "sensor4:" + sensor.getSensor4() + " sensor5:" + sensor.getSensor5() + "\n"
                + "sensor6:" + sensor.getSensor6() + " sensor7:" + sensor.getSensor7();
        handler.sendMessage(msg);
    }
}
