package xyz.ibudai.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

public class Channel1Listener implements MessageListener {

    Logger logger = LoggerFactory.getLogger(Channel1Listener.class);

    @Override
    public void onMessage(Message message, byte[] pattern) {
        logger.info("订阅者-1号, 接收到消息：" + new String(message.getBody()));
    }
}
