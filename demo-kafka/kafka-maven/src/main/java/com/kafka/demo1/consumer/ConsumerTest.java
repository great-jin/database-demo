package com.kafka.task2.consumer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.kafka.demo1.consumer.ConsumerTask;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

/**
 * https://cwiki.apache.org/confluence/display/KAFKA/Consumer+Group+Example
 * <p>
 * http://kafka.apache.org/documentation.html#consumerapi
 */
public class ConsumerTest {

    public static void main(String[] arg) {
        ConsumerTest task = new ConsumerTest(
                "localhost:2181",
                "consumer",
                "topic-123");

        task.run(3);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException ie) {

        }
        task.shutdown();
    }

    private final ConsumerConnector consumer;
    private final String topic;
    private ExecutorService executor;

    public ConsumerTest(String a_zookeeper, String a_groupId, String a_topic) {
        consumer = Consumer.createJavaConsumerConnector(createConsumer(a_zookeeper, a_groupId));
        this.topic = a_topic;
    }

    /**
     * see http://kafka.apache.org/08/configuration.html --3.2 Consumer Configs
     * <p>
     * http://kafka.apache.org/documentation.html#consumerconfigs
     */
    private static ConsumerConfig createConsumer(String a_zookeeper,
                                                 String a_groupId) {
        Properties props = new Properties();
        props.put("zookeeper.connect", a_zookeeper);    //配置ZK地址
        props.put("group.id", a_groupId);               //必填字段
        props.put("zookeeper.session.timeout.ms", "400");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000");
        return new ConsumerConfig(props);
    }

    public void run(int numThreads) {
        System.out.println("-----Consumers begin to execute-------");

        Map<String, Integer> topicCountMap = new HashMap<>();
        topicCountMap.put(topic, new Integer(numThreads));
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer
                .createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);
        System.err.println("-----Need to consume content----" + streams);

        // now launch all the threads
        executor = Executors.newFixedThreadPool(numThreads);

        // now create an object to consume the messages
        int threadNumber = 0;
        for (final KafkaStream<byte[], byte[]> stream : streams) {
            System.out.println("-----Consumers begin to consume-------" + stream.toString());
            executor.submit(new ConsumerTask(stream, threadNumber));
            threadNumber++;
        }
    }

    public void shutdown() {
        if (consumer != null)
            consumer.shutdown();
        if (executor != null)
            executor.shutdown();
        try {
            if (!executor.awaitTermination(5000, TimeUnit.MILLISECONDS)) {
                System.out.println("Timed out waiting for consumer threads to shut down, exiting uncleanly");
            }
        } catch (InterruptedException e) {
            System.out.println("Interrupted during shutdown, exiting uncleanly");
        }
    }
}
