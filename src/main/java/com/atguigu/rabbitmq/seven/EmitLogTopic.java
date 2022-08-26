package com.atguigu.rabbitmq.seven;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class EmitLogTopic {

    public static final String EXCHANGE_NAME = "topic_logs";
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        Map<String,String> bindingMap  = new HashMap<>();

        bindingMap.put("quick.orange.rabbit","被队列 Q1Q2 接收到");
        bindingMap.put("lazy.orange.elephant","被队列 Q1Q2 接收到");
        bindingMap.put("quick.orange.fox","被队列 Q1 接收到");
        bindingMap.put("lazy.brown.fox","被队列 Q2 接收到");
        bindingMap.put("lazy.pink.rabbit","虽然满足两个绑定但只被队列 Q2 接收一次");
        bindingMap.put("quick.brown.fox ","不匹配任何绑定不会被任何队列接收到会被丢弃");
        bindingMap.put("quick.orange.male.rabbit","是四个单词不匹配任何绑定会被丢弃");
        bindingMap.put("lazy.orange.male.rabbit","是四个单词匹配Q2");

        for (Map.Entry<String, String> stringStringEntry : bindingMap.entrySet()) {
            String routingKey = stringStringEntry.getKey();

            String message = stringStringEntry.getValue();

            channel.basicPublish(EXCHANGE_NAME,routingKey,null,message.getBytes(StandardCharsets.UTF_8));

            System.out.println("生产者发出消息" + message);

        }

    }
}
