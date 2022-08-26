package com.atguigu.rabbitmq.thread;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atguigu.rabbitmq.thread.util.SendMessage;
import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

class NumberThread implements Runnable {

    public static final String QUEUE_NAME = "hello";
    private int number = 0;
    private Channel channel;
//    public  SendMessage sendMessage;

    //    1.实例化锁
    private ReentrantLock lock = new ReentrantLock();

    public NumberThread() throws Exception {

    }

    /**
     * int到byte[]
     *
     * @param i
     * @return
     */

    public static byte[] intToByteArray(int i) {

        byte[] result = new byte[4];

//由高位到低位

        result[0] = (byte) ((i >> 24) & 0xFF);

        result[1] = (byte) ((i >> 16) & 0xFF);

        result[2] = (byte) ((i >> 8) & 0xFF);

        result[3] = (byte) (i & 0xFF);

        return result;

    }

    public synchronized String readFile(int index) throws Exception {
        FileInputStream fileInputStream = new FileInputStream("/Users/yuhaikun/Desktop/test1.json");

        int len;
        byte[] bytes = new byte[1024];
        StringBuffer stringBuffer = new StringBuffer();
        while ((len = fileInputStream.read(bytes)) != -1) {
//            添加字符串到缓冲区
            stringBuffer.append(new String(bytes, 0, len));
        }
//            关闭资源
        fileInputStream.close();


//            使用fastjson将字符串转换成json
        JSONArray jsonArray = JSONObject.parseArray(stringBuffer.toString());

//        System.out.println("大小为：" + jsonArray.size());
//            便于对象去除我们需要的数据
//        for (Object schema :
//                jsonArray) {
//            JSONObject x = (JSONObject) schema;
//            System.out.println("发送的信息是：" + x);
//        }
        JSONObject x = (JSONObject) jsonArray.get(index);
        return x.toString();
    }


    @SneakyThrows
    @Override
    public void run() {
        while (true) {

//              假设发送消息的条数为20条

            if (number < 20) {

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                lock.lock();
                if (number >= 20) {
                    break;
                }
                channel = RabbitMqUtils.getChannel();
                channel.queueDeclare(QUEUE_NAME, false, false, false, null);
//                System.out.println(Thread.currentThread().getName() + "：" + number);
//                String tempString = sendMessage.getMessage(1);
                String s = readFile(number);
//                channel.basicPublish("", QUEUE_NAME, null, intToByteArray(number));
                channel.basicPublish("", QUEUE_NAME, null, s.getBytes(StandardCharsets.UTF_8));
                System.out.println(Thread.currentThread().getName() + ":" + s + "\n" + "发送第 " + number + " 条消息");
                number++;
                lock.unlock();

            } else {
                break;
            }

        }


//
//
//        System.out.println(Thread.currentThread().getName() + ":" + number);

//
//        number++;
//        lock.unlock();
    }
}

public class ThreadPool {

    public static void main(String[] args) throws Exception {
        /*
//        创建固定线程个数
        ExecutorService executorService = Executors.newFixedThreadPool(10);

//        new一个Runnable接口的对象
        NumberThread number = new NumberThread();

//        执行线程，最多十个(适合适用于Runnable)
//        executorService.execute(number);
//        执行线程，适用于callable
        executorService.submit(number);


//        关闭线程池
        executorService.shutdown();


         */
        SendMessage sendMessage = new SendMessage();
        NumberThread thread = new NumberThread();

        ExecutorService threadPool = Executors.newFixedThreadPool(10);

        try {
            for (int i = 0; i < 10; i++) {
//                使用线程池来创建线程
                threadPool.execute(new Thread(thread));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }
    }
}

