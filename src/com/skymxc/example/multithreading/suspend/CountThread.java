package com.skymxc.example.multithreading.suspend;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CountThread extends Thread {


    public CountThread() {
        super("countThread");
    }

    private int i;

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    @Override
    public void run() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
        while (true){
            i++;
//            System.out.println(dateFormat.format(new Date())+"--->"+i++);
            if (this.isInterrupted()){
                break;
            }
        }
        System.out.println(dateFormat.format(new Date())+"---停止--》"+this.getName());
    }
}
