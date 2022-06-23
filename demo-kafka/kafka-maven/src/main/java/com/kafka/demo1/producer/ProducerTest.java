package com.kafka.demo1.producer;

import java.util.Properties;
import java.util.UUID;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

/**
 * @author wesley
 * https://cwiki.apache.org/confluence/display/KAFKA/0.8.0+Producer+Example
 * http://kafka.apache.org/documentation.html#producerapi
 */
public class ProducerTest {

    @SuppressWarnings("deprecation")
    public static void main(String[] args) {
        // 设置配置属性
        Properties props = new Properties();
        // 配置kafka的IP和端口
        props.put("metadata.broker.list",
                "192.168.173.42:9092,192.168.173.43:9092,192.168.173.44:9092");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        // key.serializer.class默认为serializer.class
        props.put("key.serializer.class", "kafka.serializer.StringEncoder");
        // 可选配置，如果不配置，则使用默认的partitioner
        props.put("partitioner.class", "com.kafka.demo2.producer.PartitionerDemo");
        // 触发acknowledgement机制，否则是fire and forget，可能会引起数据丢失
        // 值为0,1,-1,可以参考
        props.put("request.required.acks", "1");
        ProducerConfig config = new ProducerConfig(props);

        // 创建 producer
        Producer<String, String> producer = new Producer<>(config);

        // 发送消息
        String topic = "channel-topic-123";
        String key = UUID.randomUUID().toString();
        String msg = "Producer create a message!!! " + System.currentTimeMillis();

        // 如果 topic 不存在，则会自动创建，
        // 默认 replication-factor 为 1，partitions为0
        KeyedMessage<String, String> data = new KeyedMessage<String, String>(topic, key, msg);
        System.out.println("-----Kafka Producer----createMessage----\n" + data);
        producer.send(data);

        // 关闭producer
        producer.close();
    }
}
