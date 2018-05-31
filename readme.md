## safety 数据共享和非线程安全
线程共享数据的情况就是多个线程访问同一个变量。
多个线程在访问同一个变量的时候会出现非线程安全问题。

非线程安全主要是指多个线程对同一个对象中的同一个实例变量进行操作时会出现值被更改、值不同步的情况，进而影响程序的执行流程。

可以通过给代码上锁的方式解决这个问题。在方法上加上 synchronized 关键字。

当一个线程调用一个方法前会先判断这个方法有没有上锁，如果上锁了说明有其他线程正在调用此方法。必须等其他线程对run()方法调用结束后才能执行run()。在等待的同时线程会不断尝试去拿这个锁，而且是多个线程同时去拿，谁拿到谁 执行。这样就实现了排队调用run()方法。

synchronized 可以在任意对象及方法上枷锁，而加锁的这段代码成为“互斥区”或者“临界区”。

## method 常用方法
- Thread.currentThread() 当前代码运行所在的线程
- Thread.currentThread().getName() 当前代码运行所在的线程名字
- getName() 当前线程的名字
- isAlive() 测试当前线程是否处于活动状态。什么是活动状态呢？活动状态就是线程已经启动且尚未终止。线程处于正在运行或准备开始运行的状态，就是认为线程是“存活”的。
- sleep() 作用是在指定的毫秒数内让当前“正在执行的线程”休眠（暂停执行）。这个正在执行的线程就是 “Thread.currentThread()” 返回的线程
- getId() 作用是放回线程唯一id

## 停止线程

停止线程意味着在线程处理完任务之前停掉正在做的操作，也就是放弃当前的操作。
停止一个线程可以使用 Thread.stop()方法，但最好不用它，虽然它可以停止一个正在运行的线程，但是这个方法是不安全的，而且已经被废弃。

大多数停止一个线程的操作使用 Thread.interrupt()方法，尽管方法的名称是“停止，终止”的意思，当这个方法不会终止一个正在运行的线程，还需要加入一个判断才能完成线程的停止。

Java中有三种方法可以停止正在运行的线程
1. 使用退出标志，使线程正常退出，也就是当run方法完成后线程终止
2. 使用 stop 方法强行终止线程，但是不推荐使用，因为 stop 和 suspend 及 resume 一样，都是作废过期的方法，使用它们产生不可预料的后果
3. 使用 interrupt 停止线程



### 判断线程是否停止

1. Thread.interrupted()（静态方法） 作用是测试当前线程是否已经中断 执行后将停止状态清除为false
2. isInterrupted() 作用是测试线程是否已经中断。执行后不会清除状态标志。

> 先看一下 `Thread.interrupted()`

```java
    /**
     * Tests whether the current thread has been interrupted.  The
     * <i>interrupted status</i> of the thread is cleared by this method.  In
     * other words, if this method were to be called twice in succession, the
     * second call would return false (unless the current thread were
     * interrupted again, after the first call had cleared its interrupted
     * status and before the second call had examined it).
     *
     * <p>A thread interruption ignored because a thread was not alive
     * at the time of the interrupt will be reflected by this method
     * returning false.
     *
     * @return  <code>true</code> if the current thread has been interrupted;
     *          <code>false</code> otherwise.
     * @see #isInterrupted()
     * @revised 6.0
     */
    public static boolean interrupted() {
        return currentThread().isInterrupted(true);
    }

```

测试当前线程是否已经中断。 线程的 中断状态 通过此方法清除。换句话说就是，这个方法入如果连续两次调用，第二次将会返回 false（在第一次调用已清除了其中断状态后，且第二次调用检验完中断状态前，当前线程再次中断的情况除外）

线程中断被忽略是因为没有在存活的时候中断，这个方法将会返回 false

如果当前线程已经被中断了 将会返回 true

> 再看一下 `isInterrupted` 

```java
    /**
     * Tests whether this thread has been interrupted.  The <i>interrupted
     * status</i> of the thread is unaffected by this method.
     *
     * <p>A thread interruption ignored because a thread was not alive
     * at the time of the interrupt will be reflected by this method
     * returning false.
     *
     * @return  <code>true</code> if this thread has been interrupted;
     *          <code>false</code> otherwise.
     * @see     #interrupted()
     * @revised 6.0
     */
    public boolean isInterrupted() {
        return isInterrupted(false);
    }
```

测试该线程是否已经中断。线程的中断状态不受此方法影响。


可以看到两个方法的源码都是调用了 `isInterrupted()` 不同的是 `interrupted()` 是先调用 `currentThread()`获取到当前代码运行所在的线程。然后让当前线程调用`isInterrupted()`。而 `isInterrupted` 这是直接调用，所以判断的是这个实例线程的状态。

再看一下 `isInterrupted()`

```java
/**
 * Tests if some Thread has been interrupted.  The interrupted state
 * is reset or not based on the value of ClearInterrupted that is
 * passed.
 */
private native boolean isInterrupted(boolean ClearInterrupted);
```

测试某些线程是否被中断。中断状态根据传递的ClearInterrupted的值决定重置或不重置。

`Thread.interrupted()` 传入了 false 所以会重置中断状态。

### 异常法停止

这里的所谓异常法停止就是对你想中断的线程调用`interrupt()`打上中断标识。在你执行操作的线程中一定要在某个地方检测 **中断状态** 如果中断状态为 true 了就停止操作。



```java
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

```

测试

```java

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

```

Console

```
.....
myThread-->7665
myThread-->7666
myThread-->7667
myThread-->7668
myThread-->7669
myThread-->7670
myThread-->7671
myThread-->7672
myThread-->7673
myThread-->7674
myThread-->7675
myThread-->7676
中断myThread
myThread-->7677
检测到当前线程实例中断标志(myThread)-->true
异常法中断--》null
java.lang.InterruptedException
	at com.skymxc.example.multithreading.stop.MyThread.run(MyThread.java:17)
main ====end--->false

Process finished with exit code 0
```

当然，这里是用的抛出异常的方法强行中断。也可使用 break 然后继续一个收尾工作。

关于 stop（）方法 参考这篇文章 https://blog.csdn.net/jiangwei0910410003/article/details/19900007 

## 暂停线程

暂停线程意味着还可以恢复线程的执行，在 Java 中使用 `suspend()` 暂停线程的执行，使用 `ressume()` 恢复线程的执行。


> 用一个例子看看这两个方法怎么用

```java
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

```

测试

```java

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

```

Console

```
17:48:11.934--A->516318612;isInterrupted-->false
17:48:12.935--B->516318612;isInterrupted-->false
17:48:13.935--C->1043644966;isInterrupted-->false
17:48:15.935---停止--》countThread
17:48:16.935--D->1565065219;isInterrupted-->false

Process finished with exit code 0

```

确实达到了暂停和恢复的目的。

### suspend()和 resmue() 的缺点----独占

如果你在上面的 CountThread 中打印 i 你就会发现一个问题 在main 线程的 打印都没出来，而且程序已知在运行，没有结束，也没有log。

先看一下 println() 的实现

```java
   /**
     * Prints a String and then terminate the line.  This method behaves as
     * though it invokes <code>{@link #print(String)}</code> and then
     * <code>{@link #println()}</code>.
     *
     * @param x  The <code>String</code> to be printed.
     */
    public void println(String x) {
        synchronized (this) {
            print(x);
            newLine();
        }
    }

```

可以看到方法里 使用 synchronized 锁住了当前对象。

在例子中我们让 countThread 暂停，虽然它确实暂停了，但是没有释放锁，且一直在占着，这样的结果就是我们在main 线程的 打印一直在等锁，且一直等不到。

虽然 suspend()方法 已经被废弃，但是了解它为什么被废弃还是很有意义的。

### suspend()和 resmue() 的缺点----不同步

