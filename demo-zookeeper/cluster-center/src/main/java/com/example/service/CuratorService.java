package com.example.service;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CuratorService implements ApplicationRunner {

    private final Logger logger = LoggerFactory.getLogger(CuratorService.class);

    /**
     * 服务地址临时节点的父节点
     */
    @Value("${cluster.node-path}")
    private String nodePath;

    /**
     * 当前在线的服务节点
     */
    private List<String> nodeList;

    private static int requestNum = 0;

    @Autowired
    private CuratorFramework zkClient;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 构建 CuratorFramework 客户端，并开启会话
        zkClient.start();
        // 获取服务列表
        Stat stat = zkClient.checkExists().forPath(nodePath);
        if (stat == null) {
            throw new RuntimeException("服务地址未注册到 Zookeeper.");
        } else {
            nodeList = zkClient.getChildren().forPath(nodePath);
        }
        // 开启对 PROVIDER_NODE 子节点变化事件的监听
        monitorPath();
    }

    /**
     * PathChildrenCache 是 Curator 提供的一种缓存节点的方式
     * 可以监控一个节点的所有子节点变化，包括子节点的新增、删除和修改操作。
     * <p>
     * 新版本中已弃用，替换为 CuratorCacheListener 设置监听
     */
    public void monitorPath() throws Exception {
        PathChildrenCache cache = new PathChildrenCache(zkClient, nodePath, true);
        cache.getListenable().addListener((client, event) -> {
            nodeList = client.getChildren().forPath(nodePath);
            logger.info("节点变更，当前在线节点: [{}]", nodeList);
        });
        cache.start();
    }

    /*public void startMonitoring() {
        // 构建 CuratorCache 实例
        CuratorCache cache = CuratorCache.build(zkClient, nodePath);
        // 构建 CuratorCacheListener 的事件监听
        CuratorCacheListener listener = CuratorCacheListener.builder()
                // 开启对子节点状态变化的监听
                .forPathChildrenCache(nodePath, zkClient, (curator, event) -> {
                    // 重新获取服务列表
                    nodeList = curator.getChildren().forPath(nodePath);
                })
                // 初始化
                .forInitialized(() -> {
                    System.out.println(">>> CuratorCacheListener 初始化");
                })
                // 构建
                .build();
        // 注册 CuratorCacheListener 到 CuratorCache
        cache.listenable().addListener(listener);
        // CuratorCache 开启缓存
        cache.start();
    }*/

    /**
     * 轮询策略, 按顺序获取服务地址
     *
     * @return 服务地址
     */
    public String roundRobin() {
        if (nodeList.isEmpty()) {
            throw new RuntimeException(">>> 服务提供者地址列表为空");
        }

        byte[] data;
        try {
            int i = requestNum % nodeList.size();
            requestNum++;
            String node = nodeList.get(i);
            // 获取节点服务信息
            node = nodePath + "/" + node;
            Stat stat = zkClient.checkExists().forPath(node);
            if (stat != null) {
                data = zkClient.getData().forPath(node);
            } else {
                logger.error("[{}] does not exist.", node);
                throw new RuntimeException("does not exist");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new String(data);
    }
}

