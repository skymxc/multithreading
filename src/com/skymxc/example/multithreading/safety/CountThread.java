package com.skymxc.example.multithreading.safety;


public class CountThread extends Thread {

    private int count = 5;

    /**
     * 使用 synchronized 同步这个方法 就能防止出现非线程安全问题
     * 意思是 给这个方法加了一把同步锁，当有线程在调用这个方法的时候，其他线程必须等待，以排队的方式执行此方法。
     * 当一个线程想要去执行这个方法的时候会先去拿这个锁，如果能够拿到这把锁，就可以执行 synchronized 里的代码。
     * 如果不能拿到这把锁，就会不断尝试去拿，直到拿到为止，而且是有多个线程同时去拿，谁拿到谁执行。
     */
    @Override
    public synchronized void run() {
        System.out.println(currentThread().getName()+" 计算-->"+count--);
    }
}
