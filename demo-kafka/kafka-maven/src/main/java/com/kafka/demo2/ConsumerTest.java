package com.kafka.demo2;

import com.kafka.demo.bean.User;
import com.kafka.demo2.utils.InitProperties;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;

class ConsumerTest {

    final static String TOPIC = "str-test";

    public static void main(String[] args) {
        // 获取消费者配置信息
        Properties properties = InitProperties.InitConsumer();
        // 根据属性创建消费者，并订阅 Topic
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Arrays.asList(TOPIC));
        System.out.println("Subscribed to topic: " + TOPIC);

        // 接收订阅消息
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                System.out.println(record.value());
            }
        }
    }
}
