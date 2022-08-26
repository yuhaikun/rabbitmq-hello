package com.atguigu.rabbitmq.eight;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;
import java.util.Map;

public class Consumer02 {

    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqUtils.getChannel();
        System.out.println("等待接收消息...");
        DeliverCallback deliverCallback = (var1,var2) -> {
            System.out.println("Consumer01接收的消息是：" + new String(var2.getBody(),"UTF-8"));
        };
        channel.basicConsume(DEAD_QUEUE,deliverCallback,var1 ->{});
    }
}
