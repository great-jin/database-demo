package com.ibudai.http.channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

public class Listener1Test implements MessageListener {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void onMessage(Message message, byte[] pattern) {
        logger.info("订阅者-2号,接收到消息：" + new String(message.getBody()));
    }
}
