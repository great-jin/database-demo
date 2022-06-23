package com.ibudai.http.channel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("channel")
public class SendTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/put")
    public void publish(@RequestParam String message) {
        // 发送消息
        redisTemplate.convertAndSend("myChannel", message);
    }
}
