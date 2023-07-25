package xyz.ibudai.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import xyz.ibudai.listener.Channel2Listener;
import xyz.ibudai.listener.Channel1Listener;

@Configuration
public class ChannelConfig {

    /**
     * 配置类中注入连接工厂
     */
    @Autowired
    private LettuceConnectionFactory connectionFactory;

    /**
     * 创建监听器 - 订阅者 1号
     */
    @Bean
    public Channel1Listener listener1Test() {
        return new Channel1Listener();
    }

    /**
     * 创建监听器 - 订阅者 2号
     */
    @Bean
    public Channel2Listener listener2Test() {
        return new Channel2Listener();
    }

    /**
     * 创建频道
     */
    @Bean
    public ChannelTopic channelCore() {
        return new ChannelTopic("myChannel");
    }

    /**
     * 建立频道与监听器绑定关系
     */
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        // 注入连接工厂
        container.setConnectionFactory(connectionFactory);
        // 频道绑定监听器 1 号
        container.addMessageListener(listener1Test(), channelCore());
        // 频道绑定监听器 2 号
        container.addMessageListener(listener2Test(), channelCore());
        return container;
    }
}
