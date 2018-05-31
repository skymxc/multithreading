package com.skymxc.example.multithreading.stop;

public class MyThread extends Thread {

    public MyThread(String name) {
        super(name);
    }

    @Override
    public void run() {
        try {
            System.out.println(Thread.currentThread().getName()+"---》begin");
            for (int i=0;i<500000;i++){
                System.out.println(Thread.currentThread().getName()+"-->"+i);
                if (this.isInterrupted()){
                    System.out.println("检测到当前线程实例中断标志("+this.getName()+")-->"+this.isInterrupted());
                    throw new  InterruptedException();
                }
            }
            System.out.println("for循环后执行-当前线程实例（"+this.getName()+"）中断标识->"+this.isInterrupted()+";当前代码运行线程("+Thread.currentThread().getName()+")中断标志--》"+Thread.interrupted());
            System.out.println(Thread.currentThread().getName()+"----end--->");
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("异常法中断--》"+e.getMessage());
        }
    }
}
