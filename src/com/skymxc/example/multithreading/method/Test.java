package com.skymxc.example.multithreading.method;

public class Test {

    public static void main(String[]args){
        System.out.println("当前运行所在的线程是："+Thread.currentThread().getName());
        MyThread thread = new MyThread();
        thread.start();
        System.out.println("MyThread是否正在运行---》"+thread.isAlive());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //2秒后再判断看看
        System.out.println("MyThread是否正在运行---》"+thread.isAlive());
    }


}
