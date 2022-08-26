package com.atguigu.rabbitmq.thread;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Test {

    public static void main(String[] args) throws Exception {
        FileInputStream fileInputStream = new FileInputStream("/Users/yuhaikun/Desktop/test1.json");

        int len;
        byte[] bytes = new byte[1024];
        StringBuffer stringBuffer = new StringBuffer();

        while ((len = fileInputStream.read(bytes))!= -1) {
//            添加字符串到缓冲区
            stringBuffer.append(new String(bytes, 0, len));
        }
//            关闭资源
            fileInputStream.close();



//            使用fastjson将字符串转换成json
            JSONArray jsonArray = JSONObject.parseArray(stringBuffer.toString());
//            便于对象去除我们需要的数据
            for (Object schema :
                    jsonArray) {
                JSONObject x = (JSONObject) schema;
                System.out.println("发送的信息是：" + x);
            }


    }
}