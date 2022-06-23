package com.kafka.demo2.utils;

import java.util.Properties;

public class InitProperties {

    /**
     * 生产者连接信息
     */
    final static String PRODUCER_HOST = "10.231.6.65:9092";
    final static String KEY_ENCODE =
            "org.apache.kafka.common.serialization.StringSerializer";
    final static String VALUE_ENCODE =
            "org.apache.kafka.common.serialization.StringSerializer";

    /**
     * 消费者连接信息
     */
    final static String CONSUMER_HOST = "10.231.6.65:9092";
    final static String GROUP = "consumer";
    final static String KEY_DECODE =
            "org.apache.kafka.common.serialization.StringDeserializer";
    final static String VALUE_DECODE =
            "org.apache.kafka.common.serialization.StringDeserializer";


    /**
     * 初始化生产者
     */
    public static Properties InitProducer() {
        Properties props = new Properties();
        // kafka 地址
        props.put("bootstrap.servers", PRODUCER_HOST);
        // 这个参数用于通知 broker 接收到 message 后是否向 producer 发送确认信号
        //  0 : 表示 producer 不用等待任何确认信号，会一直发送消息，否则 producer 进入等待状态
        //  1 : 表示 producer 等待 leader 将数据写入本地log(不关心 follower 是否写入)，然后继续发送
        // -1 : 表示 leader 等待所有备份都成功写入日志，可以保证数据一定不会丢失。
        props.put("acks", "0");
        // 数据发送失败重试次数
        props.put("retries", 0);
        // producer 组将会汇总任何在请求与发送之间到达的消息记录一个单独批量的请求
        props.put("linger.ms", 1000);
        // producer 批处理消息记录，以减少请求次数
        props.put("batch.size", 16384);
        // server 等待来自 followers 的确认的最大时间，如果确认的请求数目在此时间内没有实现，则会返回一个错误
        props.put("timeout.ms", 30000);
        // 可以用于缓存数据的内存大小
        props.put("buffer.memory", 33554432);
        // 数据压缩类型，可选项: none、gzip、snappy
        props.put("compression.type", "gzip");
        // 等待元素据 fetch 成功完成所需要的时间
        props.put("metadata.fetch.timeout.ms", 30000);
        // Key 序列化方式
        props.put("key.serializer", KEY_ENCODE);
        // Value 序列化方式
        props.put("value.serializer", VALUE_ENCODE);
        return props;
    }

    /**
     * 初始化消费者
     */
    public static Properties InitConsumer() {
        Properties props = new Properties();
        // kafka 地址
        props.put("bootstrap.servers", CONSUMER_HOST);
        // 唯一标识 consumer 进程所在组的字符串
        props.put("group.id", GROUP);
        // consumer 所 fetch 的消息的 offset 将会自动的同步到 zookeeper
        props.put("enable.auto.commit", "true");
        // 设置会话超时时间
        props.put("session.timeout.ms", "30000");
        // consumer 向 zookeeper 提交 offset 的频率，单位是秒
        props.put("auto.commit.interval.ms", "1000");
        // zookeeper中没有初始化的offset时，如果offset是以下值的回应：
        // latest：自动复位 offset 为 latest 的 offset
        // earliest：自动复位 offset 为 earliest 的 offset
        // none：向 consumer 抛出异常
        props.put("auto.offset.reset", "earliest");
        // Key 解码方式
        props.put("key.deserializer", KEY_DECODE);
        // value 解码方式
        props.put("value.deserializer", VALUE_DECODE);
        return props;
    }

}
