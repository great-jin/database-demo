package xyz.ibudai.database.redis.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

public class Channel2Listener implements MessageListener {

    Logger logger = LoggerFactory.getLogger(Channel2Listener.class);

    @Override
    public void onMessage(Message message, byte[] pattern) {
        logger.info("订阅者-2号, 接收到消息：" + new String(message.getBody()));
    }
}
