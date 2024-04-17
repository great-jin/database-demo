package xyz.ibudai.zk.node.controller;

import xyz.ibudai.zk.node.service.CuratorService;
import xyz.ibudai.zk.node.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/provider")
public class ProviderController {

    @Autowired
    private CuratorService curatorService;

    @Autowired
    private ProviderService providerService;

    /**
     * 调用方法
     *
     * @return String
     */
    @GetMapping("/callMethod")
    public String callMethod() {
        return providerService.callMethod();
    }

    /**
     * 上线服务
     *
     * @return String
     */
    @GetMapping("/online")
    public String registeredAddress() {
        return curatorService.registeredAddress();
    }

    /**
     * 下线服务
     *
     * @return String
     */
    @GetMapping("/offline")
    public String cancellationAddress() {
        return curatorService.cancellationAddress();
    }

}


