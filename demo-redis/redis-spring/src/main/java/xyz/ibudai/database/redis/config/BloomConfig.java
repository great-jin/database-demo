package xyz.ibudai.database.redis.config;

import com.google.common.base.Charsets;
import com.google.common.hash.Funnel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import xyz.ibudai.database.redis.utils.BloomFilterHelper;

@Configuration
public class BloomConfig {

    /**
     * 初始化布隆过滤器，放入到 spring 容器里面
     */
    @Bean
    public BloomFilterHelper<String> initBloomFilterHelper() {
        return new BloomFilterHelper<>((Funnel<String>) (from, into) ->
                into.putString(from, Charsets.UTF_8).putString(from, Charsets.UTF_8),
                1000000, 0.01);
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(factory);
        return stringRedisTemplate;
    }
}
