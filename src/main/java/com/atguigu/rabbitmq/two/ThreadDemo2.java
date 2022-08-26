package com.atguigu.rabbitmq.two;

/**
 * 线程通信的例子：使用两个线程打印1—100，线程1，线程2交替打印
 *
 * 当我们不采取线程之间的通信时，无法达到线程1，2交替打印（cpu的控制权，是自动分配的）
 * 若想达到线程1，2交替打印，需要：
 * 1.当线程1获取锁以后，进入代码块里将number++（数字打印并增加）操作完以后，为了保证下个锁为线程2所有，需要将线程1阻塞（线程1你等等wait（））。（输出1，number为2）
 * 2.当线程2获取锁以后，此时线程1已经不能进入同步代码块中了，所以，为了让线程1继续抢占下一把锁，需要让线程1的阻塞状态取消（通知线程1不用等了notify（）及notifyAll（）），即应该在进入同步代码块时取消线程1的阻塞。
 *
 * */

class Number implements Runnable{

    private int number = 1;//设置共享数据（线程之间对于共享数据的共享即为通信）


    //对共享数据进行操作的代码块，需要线程安全
    @Override
    public synchronized void run() {

        while(true){
            //使得线程交替等待以及通知交替解等待
            notify();//省略了this.notify()关键字
            if(number<100){
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+":"+number);
                number++;
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else{
                break;
            }
        }
    }
}

class CommunicationTest {

    public static void main(String[] args){
        //创建runnable对象
        Number number = new Number();

        //创建线程，并实现runnable接口
        Thread t1 = new Thread(number);
        Thread t2 = new Thread(number);

        //给线程设置名字
        t1.setName("线程1");
        t2.setName("线程2");

        //开启线程
        t1.start();
        t2.start();

    }

}
