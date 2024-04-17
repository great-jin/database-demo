package xyz.ibudai.zk.center.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.ibudai.zk.center.service.CuratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/center")
public class ConsumerController {

    private final Logger logger = LoggerFactory.getLogger(CuratorService.class);

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
        String msg = restTemplate.getForObject("http://" + path, String.class);
        logger.info(msg);
        return msg;
    }
}


