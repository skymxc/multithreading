package com.skymxc.example.multithreading.stop;

public class Test {

    public static void main(String[]args) throws InterruptedException {
        MyThread myThread  =new MyThread("myThread");
        myThread.start();
        Thread.sleep(50);
        System.out.println("中断myThread");
        myThread.interrupt();
        Thread.sleep(1000);
        System.out.println(Thread.currentThread().getName()+" ====end--->"+myThread.isInterrupted());

    }
}
