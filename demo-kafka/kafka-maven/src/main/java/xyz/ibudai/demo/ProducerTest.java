package xyz.ibudai.demo;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import xyz.ibudai.demo.bean.User;

import java.util.Properties;
import java.util.UUID;

public class ProducerTest {

    public static void main(String[] args) {
        final String HOST = "10.231.6.65:9092";
        final String TOPIC = "user-topic";
        final String KEY_ENCODE =
                "org.apache.kafka.common.serialization.StringSerializer";
        final String VALUE_ENCODE =
                "demo.utils.EncodeKafka";

        Properties props = new Properties();
        // kafka 地址
        props.put("bootstrap.servers", HOST);
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

        // 根据属性创建生产者
        try (KafkaProducer<String, User> producer = new KafkaProducer<>(props)) {
            // 如果 topic 不存在，则会自动创建，默认 replication-factor 为 1，partitions为0
            int i = 0;
            while (i < 10) {
                String KEY = UUID.randomUUID().toString();
                User user = new User("User " + i, "Pwd " + i);
                producer.send(new ProducerRecord<>(TOPIC, KEY, user));
                i++;
            }
            System.out.println("Topic: " + TOPIC + ", 发送结束.");
        }
    }
}
