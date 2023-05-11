package com.example.service;

import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

@Component
public class ShutdownService implements ApplicationListener<ContextClosedEvent> {

    @Autowired
    private CuratorFramework zkClient;

    @Autowired
    private CuratorService curatorService;

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        try {
            curatorService.cancellationAddress();
            zkClient.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
