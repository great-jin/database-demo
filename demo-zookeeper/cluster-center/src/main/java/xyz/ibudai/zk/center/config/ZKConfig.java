package xyz.ibudai.zk.center.config;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryForever;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ZKConfig {

    @Value("${cluster.zookeeper.ip}")
    private String zkHost;
    @Value("${cluster.zookeeper.timeout}")
    private int zkTimeout;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public CuratorFramework zkClient() {
        return CuratorFrameworkFactory.builder()
                // Zookeeper 地址
                .connectString(zkHost)
                // 会话超时
                .sessionTimeoutMs(zkTimeout)
                // 重连策略
                .retryPolicy(new RetryForever(10000))
                .build();
    }
}
