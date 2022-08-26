package com.atguigu.rabbitmq.six;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

public class ReceiveLogsDirect02 {

    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
//      声明一个队列
        channel.queueDeclare("disk",false,false,false,null);
//
        channel.queueBind("disk",EXCHANGE_NAME,"error");

        DeliverCallback deliverCallback =  (var1, var2) -> {
            System.out.println("ReceiveLogsDirect02控制台打印接收到的消息：" + new String(var2.getBody(),"UTF-8"));
        };
        channel.basicConsume("disk",true,deliverCallback, var1 ->{} );



    }
}
