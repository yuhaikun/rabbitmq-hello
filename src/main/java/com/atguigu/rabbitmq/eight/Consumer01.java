package com.atguigu.rabbitmq.eight;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;
import java.util.Map;

public class Consumer01 {
//    普通交换机的名称
    public static final String NORMAL_EXCHANGE = "normal_exchange";
//    死信交换机的名称
    public static final String DEAD_EXCHANAGE = "dead_exchange";
//    普通队列的名称
    public static final String NORMAL_QUEUE = "normal_queue";
//    死信队列的名称
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqUtils.getChannel();
//        声明交换机
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANAGE, BuiltinExchangeType.DIRECT);
//        声明队列

        Map<String,Object> arguments = new HashMap<>();
//        过期时间
//        arguments.put("x-message-ttl",10000);
//        正常队列设置死信交换机
        arguments.put("x-dead-letter-exchange",DEAD_EXCHANAGE);
//        设置死信RoutingKey
        arguments.put("x-dead-letter-routing-key","lisi");
//        设置正常队列的长度限制
//        arguments.put("x-max-length",6);

        channel.queueDeclare(NORMAL_QUEUE,false,false,false,arguments);
        channel.queueDeclare(DEAD_QUEUE,false,false,false,null);
//        绑定交换机与队列
        channel.queueBind(NORMAL_QUEUE,NORMAL_EXCHANGE,"zhangsan");

        channel.queueBind(DEAD_QUEUE,DEAD_EXCHANAGE,"lisi");

        System.out.println("等待接收消息...");

        DeliverCallback deliverCallback = (var1,var2) -> {
            String message = new String(var2.getBody(), "UTF-8");
//            防止空指针异常
            if (message.equals("info5")) {
                System.out.println("Consumer01接收的消息是：" + message + "此消息是被C1拒绝的");
//                不重新塞回原队列
                channel.basicReject(var2.getEnvelope().getDeliveryTag(),false);
            }else{
                System.out.println("Consumer01接收的消息是：" + message);
                channel.basicAck(var2.getEnvelope().getDeliveryTag(),false);
            }

        };
//        开启手动应答
        channel.basicConsume(NORMAL_QUEUE,false,deliverCallback,var1 ->{});


    }
}
