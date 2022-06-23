package com.kafka.demo1.consumer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

/**
 * @author wesley
 *
 * http://kafka.apache.org/documentation.html#consumerapi
 * https://cwiki.apache.org/confluence/display/KAFKA/Consumer+Group+Example
 * https://cwiki.apache.org/confluence/display/KAFKA/0.8.0+SimpleConsumer+Example
 */
public class ConsumerTest1 extends Thread {

    public static void main(String[] args) {
        ConsumerTest1 consumerThread = new ConsumerTest1("channel-topic-123");
        consumerThread.start();
    }

    // 消费者连接
    private final ConsumerConnector consumer;
    // 要消费的话题
    private final String topic;

    public ConsumerTest1(String topic) {
        consumer = kafka.consumer.Consumer.createJavaConsumerConnector(createConsumer());
        this.topic = topic;
    }

    // 配置相关信息
    private static ConsumerConfig createConsumer() {
        Properties props = new Properties();
        // 配置要连接的zookeeper地址与端口
        props.put("zookeeper.connect", "localhost:2181");
        // 配置zookeeper的组id
        props.put("group.id", "consumers");
        // 配置zookeeper连接超时间隔
        props.put("zookeeper.session.timeout.ms", "10000");
        // 配置zookeeper异步执行时间
        props.put("zookeeper.sync.time.ms", "200");
        // 配置自动提交时间间隔
        props.put("auto.commit.interval.ms", "1000");
        return new ConsumerConfig(props);
    }

    public void run() {
        Map<String, Integer> topicMap = new HashMap<String, Integer>();
        topicMap.put(topic, 1);
        Map<String, List<KafkaStream<byte[], byte[]>>> streamMap = consumer.createMessageStreams(topicMap);

        KafkaStream<byte[], byte[]> stream = streamMap.get(topic).get(0);
        ConsumerIterator<byte[], byte[]> it = stream.iterator();
        System.out.println("*********Results********");
        while (true) {
            if (it.hasNext()) {
                // 打印得到的消息
                System.err.println(Thread.currentThread()
                        + " get data:" + new String(it.next().message()));
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
