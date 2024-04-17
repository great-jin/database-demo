package xyz.ibudai.database.redis;

import org.junit.Before;
import org.junit.Test;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class RedissionTest {

    private RedissonClient redissonClient;

    @Before
    public void connect() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://10.231.6.65:6379")
                .setPassword("123456");
        redissonClient = Redisson.create(config);
    }

    @Test
    public void demo() {
        System.out.println(redissonClient.getConfig());
    }
}
