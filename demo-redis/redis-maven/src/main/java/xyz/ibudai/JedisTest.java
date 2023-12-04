package xyz.ibudai;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class JedisTest {

    private Jedis jedis;

    @Before
    public void init() {
        jedis = new Jedis("10.231.6.21", 6379);
        // 认证配置
        jedis.auth("123456");
        // 选择数据库
        jedis.select(1);
    }

    @After
    public void destroy() {
        try {
            jedis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 1. SetParams
     * <p>
     * ==> nx()：只在键不存在时，才对键进行设置操作，默认为 false。
     * ==> xx()：只在键已经存在时，才对键进行设置操作，默认为 false。
     * ==> ex(int seconds)：设置键的过期时间，单位是秒。
     * ==> px(long milliseconds)：设置键的过期时间，单位是毫秒。
     */
    @Test
    public void demo1() {
        String prefix = "jedis:test:";
        SetParams params = new SetParams()
                .ex(TimeUnit.MINUTES.toSeconds(5));
        for (int i = 0; i < 10; i++) {
            String key = prefix + i;
            jedis.set(key, UUID.randomUUID().toString(), params);
        }
        System.out.println("done");
    }
}
