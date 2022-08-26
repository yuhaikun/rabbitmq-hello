package com.atguigu.rabbitmq.thread.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class SendMessage {

    FileInputStream fileInputStream = new FileInputStream("/Users/yuhaikun/Desktop/test1.json");

    int len;
    byte[] bytes = new byte[1024];
    StringBuffer stringBuffer = new StringBuffer();

    public SendMessage() throws Exception {
    }

    public String getMessage(int index) throws Exception {



        while ((len = fileInputStream.read(bytes))!= -1) {
//            添加字符串到缓冲区
            stringBuffer.append(new String(bytes, 0, len));
        }
//            关闭资源
        fileInputStream.close();



//            使用fastjson将字符串转换成json
        JSONArray jsonArray = JSONObject.parseArray(stringBuffer.toString());

        System.out.println("大小为：" + jsonArray.size());
//            便于对象去除我们需要的数据
//        for (Object schema :
//                jsonArray) {
//            JSONObject x = (JSONObject) schema;
//            System.out.println("发送的信息是：" + x);
//        }
        JSONObject x = (JSONObject) jsonArray.get(index);
        return x.toString();
    }

//    public static void main(String[] args) throws Exception {
//        SendMessage sendMessage = new SendMessage();
//        String message = sendMessage.getMessage(1);
//        System.out.println("打印的信息为："+ message);
//    }
}
