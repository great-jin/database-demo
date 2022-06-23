package demo2;

import demo2.utils.InitProperties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.UUID;

public class ProducerTest {

    final static String TOPIC = "str-test";

    public static void main(String[] args) {
        // 获取生产者配置信息
        Properties properties = InitProperties.InitProducer();
        // 根据属性创建生产者
        try (KafkaProducer<String, String> producer = new KafkaProducer<>(properties)) {
            // 如果 topic 不存在，则会自动创建，默认 replication-factor 为 1，partitions为0
            int i = 0;
            while (i < 5) {
                String KEY = UUID.randomUUID().toString();
                String VALUES = KEY.substring(0, 8);
                producer.send(new ProducerRecord<>(TOPIC, KEY, VALUES));
                i++;
            }
            System.out.println("Topic: " + TOPIC + ", 发送结束.");
        }
    }
}
