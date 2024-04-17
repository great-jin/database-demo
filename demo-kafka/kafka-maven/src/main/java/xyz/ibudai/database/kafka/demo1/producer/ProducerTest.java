package xyz.ibudai.database.kafka.demo1.producer;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.util.Properties;
import java.util.UUID;

/**
 * @author wesley
 * https://cwiki.apache.org/confluence/display/KAFKA/0.8.0+Producer+Example
 * http://kafka.apache.org/documentation.html#producerapi
 */
public class ProducerTest {

    @SuppressWarnings("deprecation")
    public static void main(String[] args) {
        Properties props = new Properties();
        // 配置 kafka 的 IP 和端口
        props.put("metadata.broker.list",
                "192.168.173.42:9092,192.168.173.43:9092,192.168.173.44:9092");
        // key.serializer.class 默认为 serializer.class
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("key.serializer.class", "kafka.serializer.StringEncoder");
        // 可选配置，如果不配置，则使用默认的 Partitioner
        props.put("partitioner.class", "demo2.producer.PartitionerDemo");
        // acknowledgement机制，否则是 fire and forget，可能会引起数据丢失, 值为 0, 1, -1
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
        KeyedMessage<String, String> data = new KeyedMessage<>(topic, key, msg);
        System.out.println("-----Kafka Producer----createMessage----\n" + data);
        producer.send(data);

        // 关闭producer
        producer.close();
    }
}
