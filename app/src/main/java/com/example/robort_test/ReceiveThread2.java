package com.example.robort_test;

import android.os.Handler;
import android.os.Message;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by asus on 2016/4/22.
 */
public class ReceiveThread2 implements Runnable {
    Socket socket = null;
    //BufferedReader reader = null;
    DataInputStream reader = null;
    Handler handler;
    int count  = 0;
    SensorTest sensor = null;

    public ReceiveThread2(Socket socket,Handler handler) throws IOException {
        this.socket = socket;
        this.handler = handler;
        sensor = new SensorTest();
        reader = new DataInputStream(socket.getInputStream());
    }

    @Override
    public void run() {
        try{
            while (true){
                byte recdat = reader.readByte();
                if (count == 0) {
                    sensor.setSensor0((int) recdat);
                    count++;
                }else if (count == 1){
                    sensor.setSensor1((int) recdat);
                    count++;
                }else if (count == 2){
                    sensor.setSensor2((int) recdat);
                    count++;
                }else if (count == 3){
                    sensor.setSensor3((int) recdat);
                    count++;
                }
                if (count == 4){
                    String res = " sensor0 " + sensor.getSensor0()
                            + " sensor1 " + sensor.getSensor1()
                            + " sensor2 " + sensor.getSensor2()
                            + " sensor3 " + sensor.getSensor3();
                    sendMessage(res);
                    count = 0;

                }




            }
        }catch (IOException ex){
            ex.printStackTrace();
        }

    }
    public void sendMessage(String res){
        Message msg = new Message();
        msg.what = MainActivity.GETNEW_MESSAGE;
        msg.obj = res;
        handler.sendMessage(msg);
    }
}
