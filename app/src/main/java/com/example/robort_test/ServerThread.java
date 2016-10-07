package com.example.robort_test;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Created by asus on 2016/4/8.
 */
public class ServerThread  implements Runnable{
    Socket socket = null;
    DealData deal = null;
    DataOutputStream writer = null;
    public ServerThread(Socket socket,DealData deal)throws IOException {
        this.socket = socket;
        this.deal = deal;
        writer =new DataOutputStream(socket.getOutputStream());

    }
    @Override
    public void run() {
        System.out.println("client" + socket.hashCode() + "connected" );
        boolean isOK = false;
        boolean isHighOnce = true;
        boolean isMidOnce = true;
        boolean isLowOnce = true;

        try{
            while (true){
                if(deal.isUp()){
                    writer.write(deal.getData());
                    writer.flush();
                    System.out.println(deal.getData());
                    deal.setUp(false);
                    isOK = true;
                }
                if (deal.isDown()){
                    writer.write(deal.getData());
                    writer.flush();
                    System.out.println(deal.getData());
                    deal.setDown(false);
                    isOK = true;
                }
                if (deal.isLeft()){
                    writer.write(deal.getData());
                    writer.flush();
                    System.out.println(deal.getData());
                    deal.setLeft(false);
                    isOK = true;
                }
                if (deal.isRight()){
                    writer.write(deal.getData());
                    writer.flush();
                    System.out.println(deal.getData());
                    deal.setRight(false);
                    isOK = true;
                }
                if (deal.isMachine_on()){
                    writer.write(deal.getData());
                    writer.flush();
                    System.out.println(deal.getData());
                    deal.setMachine_on(false);
                    isOK = true;
                }
                if(deal.isMachine_off()){
                    if (isOK) {
                        writer.write(deal.getData());
                        writer.flush();
                        System.out.println(deal.getData());
                        isOK = false;
                        deal.setMachine_off(false);
                    }
                }



                if (deal.isStop2()){
                    writer.write(deal.getData());
                    writer.flush();
                    deal.setStop2(false);
                }



                if (deal.isHigh()){
                    if (isHighOnce){
                        writer.write(deal.getData());
                        writer.flush();
                        isHighOnce = false;
                        isLowOnce = true;
                        isMidOnce = true;
                    }
                }

                if (deal.isMid()){
                    if (isMidOnce){
                        writer.write(deal.getData());
                        writer.flush();
                        isMidOnce = false;
                        isHighOnce = true;
                        isLowOnce = true;
                    }
                }

                if (deal.isLow()){
                    if (isLowOnce){
                        writer.write(deal.getData());
                        writer.flush();
                        isLowOnce = false;
                        isHighOnce = true;
                        isMidOnce = true;
                    }
                }


            }
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }
}
