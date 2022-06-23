package demo;

import demo.bean.User;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;

class ConsumerTest {

    public static void main(String[] args) {
        final String HOST = "10.231.6.65:9092";
        final String TOPIC = "user-topic";
        final String GROUP = "consumer";
        final String KEY_DECODE =
                "org.apache.kafka.common.serialization.StringDeserializer";
        final String VALUE_DECODE =
                "demo.utils.DecodeKafka";

        Properties props = new Properties();
        // kafka 地址
        props.put("bootstrap.servers", HOST);
        // 唯一标识 consumer 进程所在组的字符串
        props.put("group.id", GROUP);
        // consumer 所 fetch 的消息的 offset 将会自动的同步到 zookeeper
        props.put("enable.auto.commit", "true");
        // 设置会话超时时间
        props.put("session.timeout.ms", "30000");
        // consumer 向 zookeeper 提交 offset 的频率，单位是秒
        props.put("auto.commit.interval.ms", "1000");
        // Key 解码方式
        props.put("key.deserializer", KEY_DECODE);
        // value 解码方式
        props.put("value.deserializer", VALUE_DECODE);

        // 根据属性创建消费者，并订阅 Topic
        KafkaConsumer<String, User> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList(TOPIC));
        System.out.println("Subscribed to topic: " + TOPIC);

        // 接收订阅消息
        while (true) {
            ConsumerRecords<String, User> records = consumer.poll(100);
            for (ConsumerRecord<String, User> record : records) {
                User result = record.value();
                System.out.println(result.toString());
            }
        }
    }
}
