package xyz.ibudai.database.kafka.demo2;

import org.apache.kafka.clients.admin.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class KafkaUtil {

    private AdminClient client;

    @Before
    public void init() {
        String brokerList = "10.231.6.65:9092";
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
        client = KafkaAdminClient.create(props);
    }

    @After
    public void destroy() {
        client.close();
    }

    @Test
    public void getTopicList() throws ExecutionException, InterruptedException {
        Set<String> topics = client.listTopics().names().get();
        System.out.println(topics);
    }

    @Test
    public void createTopic() throws InterruptedException {
        // NewTopic(name, numPartitions, replicationFactor)
        NewTopic newTopic = new NewTopic("test-0920", 1, (short) 1);
        CreateTopicsResult result = client.createTopics(Collections.singletonList(newTopic));
        // 避免客户端连接太快断开而导致 Topic 没有创建成功
        Thread.sleep(500);
        System.out.println(result);
    }

    @Test
    public void deleteTopic() throws ExecutionException, InterruptedException {
        // 获取 Topic 列表
        client.deleteTopics(Arrays.asList("test-0920")).all().get();
    }
}
