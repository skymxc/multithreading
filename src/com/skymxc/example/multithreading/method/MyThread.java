package com.skymxc.example.multithreading.method;

public class MyThread extends Thread {
    public MyThread() {
        super();
        setName("MyThread");
    }

    @Override
    public void run() {
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("当前运行所在的线程是：" + Thread.currentThread().getName());
    }

}
