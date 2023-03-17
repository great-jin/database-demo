package xyz.ibudai.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    /**
     * RedisProperties: 读取 yml 配置 redis 信息
     */
    @Autowired
    private RedisProperties redisProperties;

    @Bean
    public RedissonClient redissonClient() {
        String host = redisProperties.getHost();
        int port = redisProperties.getPort();
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + host + ":" + port)
                .setPassword(redisProperties.getPassword())
                .setDatabase(redisProperties.getDatabase());
        // 看门狗过期时间, 默认 30s, 锁续期间隔时间为设置时间的 1/3
        config.setLockWatchdogTimeout(60 * 1000);
        return Redisson.create(config);
    }
}
