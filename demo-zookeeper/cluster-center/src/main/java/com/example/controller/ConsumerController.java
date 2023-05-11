package com.example.controller;

import com.example.service.CuratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/consumer")
public class ConsumerController {

    @Autowired
    private CuratorService curatorService;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 调用方法
     *
     * @return String
     */
    @GetMapping("/callMethod")
    public String callMethod(String apiPath) {
        // 轮询策略获取服务地址
        String path = curatorService.roundRobin() + apiPath;
        // 使用 RestTemplate 远程调用服务
        return restTemplate.getForObject("http://" + path, String.class);
    }
}


