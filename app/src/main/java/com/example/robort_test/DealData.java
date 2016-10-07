package com.example.robort_test;

/**
 * Created by asus on 2016/4/8.
 */
public class DealData {
    private boolean up;
    private boolean down;
    private boolean left;
    private boolean right;
    private boolean low;
    private boolean mid;
    private boolean high;
    private boolean machine_on;
    private boolean machine_off;
    private boolean stop2;

    byte sframe[] ={(byte)0x40,(byte)0x55,(byte)0x45};//@UE
    byte address = (byte) 0x10;
    byte datalen = (byte) 0x03;
    int speed = 0;
    byte sum ;
    byte xor;
    byte ef = (byte) 0x23;
    byte []data = new byte[11];



    public boolean isMachine_on() {
        return machine_on;
    }

    public boolean isMachine_off(){
        return machine_off;
    }

    public void setMachine_on(boolean machine_on) {
        this.machine_on = machine_on;
    }
    public void setMachine_off(boolean machine_off){
        this.machine_off = machine_off;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isLow() {
        return low;
    }

    public void setLow(boolean low) {
        this.low = low;
    }

    public boolean isMid() {
        return mid;
    }

    public void setMid(boolean mid) {
        this.mid = mid;
    }

    public boolean isHigh() {
        return high;
    }

    public void setHigh(boolean high) {
        this.high = high;
    }
    public void  setStop2(boolean stop2){
        this.stop2 = stop2;
    }
    public boolean isStop2(){
        return stop2;
    }




    public DealData(){

    }

    public byte[]getData(){

        sum = 0;
        xor = 0;
        //帧头
        data[0] = sframe[0];
        data[1] = sframe[1];
        data[2] = sframe[2];
        //目的地址
        data[3] = address;
        sum += data[3];
        xor ^= data[3];

        //数据长度
        data[4] = datalen;
        sum += data[4];
        xor ^= data[4];

        //右转
        if (right) {
            data[5] |= (byte) 0x4;
            data[5] &= (byte) (~0x2);
        }
        //左转
        if (left) {
            data[5] |= (byte) 0x6;
        }
        //直行
        if (up) {
            data[5] &= (byte) (~0x4);
            data[5] |= (byte) 0x8;
        }
        //倒退
        if (down) {
            data[5] &= (byte) (~0x4);
            data[5] &= (byte) (~0x8);
        }
        //关闭电机
        if(machine_on){
            data[5] |= (byte) 0x1;
        }
        if (machine_off) {
            data[5] &= (byte) (~0x1);
        }


        sum += data[5];
        xor ^= data[5];

        //设置速度
        data[6] = (byte) (speed / 256);
        data[7] = (byte) (speed % 256);
        sum += data[6];
        xor ^= data[6];
        sum += data[7];
        xor ^= data[7];

        //赋值 和校验/异或校验 位

        data[8] = sum;
        data[9] = xor;
        //设置帧尾
        data[10] = ef;
        return data;
    }

    public void setHighSpeed(){
        this.speed = 8000;
    }
    public void setMidSpeed(){
        this.speed = 5000;
    }
    public void setLowSpeed(){
        this.speed = 3000;
    }
    public void setStop(){
        this.speed = 0;
    }


}
