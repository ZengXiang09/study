package com.example.robort_test;

import android.os.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import android.os.Handler;

/**
 * Created by asus on 2016/4/8.
 */
public class MyThread extends Thread {
    DealData deal = null;
    int port ;
    Socket socket = null;
    String clientIp;
    Handler handler = null;

    public MyThread(DealData deal,Handler handler){
        this.deal = deal;
        this.port = 8899;
        this.handler = handler;
    }
    @Override
    public void run(){
        try{
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("监听成功"+serverSocket.toString());
            while (true) {
                socket = serverSocket.accept();
                setClientIp(socket);

                Message msg = new Message();
                msg.what = MainActivity.CONNECT_SUCCESS;
                handler.sendMessage(msg);

                new Thread(new ServerThread(socket, deal)).start();

                new Thread(new ReceiveThread(socket,handler)).start();
            }
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }
    public void setClientIp(Socket socket){
        this.clientIp = socket.getInetAddress().getHostAddress();
    }
    public String getClientIp(){
        return clientIp;
    }


}
