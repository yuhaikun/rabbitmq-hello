package com.atguigu.rabbitmq.thread;

import java.io.Serializable;

public class Message implements Serializable {
    private long date;

    private String senderAddress;
}
