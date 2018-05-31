package com.skymxc.example.multithreading.suspend;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SuspendTest {
    public static void main(String[]args) throws InterruptedException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
        CountThread countThread = new CountThread();
        countThread.start();
        Thread.sleep(1000);
        //阶段 A 1000ms 后
        countThread.suspend();
        System.out.println( dateFormat.format(new Date())+"--A->"+countThread.getI()+";isInterrupted-->"+countThread.isInterrupted());

        Thread.sleep(1000);
        //阶段B 1000ms 后
        System.out.println( dateFormat.format(new Date())+"--B->"+countThread.getI()+";isInterrupted-->"+countThread.isInterrupted());

        countThread.resume();

        Thread.sleep(1000);
        //阶段C
        countThread.suspend();
        System.out.println( dateFormat.format(new Date())+"--C->"+countThread.getI()+";isInterrupted-->"+countThread.isInterrupted());

        Thread.sleep(1000);
        countThread.resume();
        Thread.sleep(1000);
        countThread.interrupt();
        //阶段D
        Thread.sleep(1000);
        System.out.println( dateFormat.format(new Date())+"--D->"+countThread.getI()+";isInterrupted-->"+countThread.isInterrupted());

    }
}
