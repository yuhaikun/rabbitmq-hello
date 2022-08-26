package com.atguigu.rabbitmq.seven;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

public class ReceiveLogsTopic02 {

    public static final String EXCHANGE_NAME = "topic_logs";
//    接收消息
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
//        声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME,"topic");
//        声明队列
        String queueName = "Q2";
        channel.queueDeclare(queueName,false,false,false,null);
        channel.queueBind(queueName,EXCHANGE_NAME,"*.*.rabbit");
        channel.queueBind(queueName,EXCHANGE_NAME,"lazy.#");

        System.out.println("ReceiveLogsTopic02等待接收消息......");

        DeliverCallback deliverCallback = (var1,var2) -> {
            System.out.println(new String(var2.getBody(),"UTF-8"));
            System.out.println("接收队列："+ queueName + " 绑定键：" + var2.getEnvelope().getRoutingKey());
        };
        channel.basicConsume(queueName,true,deliverCallback, var2 -> {});

    }
}
