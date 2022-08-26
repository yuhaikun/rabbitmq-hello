package com.atguigu.rabbitmq.five;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

public class ReceiveLogs02 {

//    交换机对名称
    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
//        声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");

//        声明一个临时队列
        /**
         * 生成一个临时队列、队列的名称是随机的
         * 当消费者断开与队列当连接的时候，队列就会自动删除
         */
        String queueName = channel.queueDeclare().getQueue();


        /**
         * 绑定交换机与队列
         */
        channel.queueBind(queueName,EXCHANGE_NAME,"");

        System.out.println("等待接收消息，把接收到的消息打印在屏幕上");

//        接收消息
        DeliverCallback deliverCallback =  (var1, var2) -> {
            System.out.println("ReceiveLogs02控制台打印接收到的消息：" + new String(var2.getBody(),"UTF-8"));
        };
//        消费者取消消息时回调接口
         channel.basicConsume(queueName,true,deliverCallback, var1 -> {});
    }
}
