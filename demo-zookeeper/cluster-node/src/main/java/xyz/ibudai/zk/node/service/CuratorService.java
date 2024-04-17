package xyz.ibudai.zk.node.service;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class CuratorService implements ApplicationRunner {

    private final Logger logger = LoggerFactory.getLogger(CuratorService.class);

    @Value("${server.port}")
    private String port;

    /**
     * 服务节点名称
     */
    @Value("${cluster.node-path}")
    private String nodePath;

    /**
     * 服务节点名称
     */
    @Value("${cluster.node-name}")
    private String nodeName;

    /**
     * 服务节点的全路径
     */
    private String fullPath;

    private final AtomicBoolean isAvailable = new AtomicBoolean(false);

    @Autowired
    private CuratorFramework zkClient;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 连接 Zookeeper 服务
        zkClient.start();
        // 注册本机地址到 Zookeeper
        registeredAddress();
    }

    /**
     * 注册服务地址
     * <p>
     * CreateMode:
     */
    public String registeredAddress() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            String host = localHost.getHostAddress() + ":" + port;
            // 注册节点
            fullPath = zkClient.create()
                    // 父级目录不存在时创建
                    .creatingParentsIfNeeded()
                    // 设置策略, EPHEMERAL: 当会话结束, 节点会被自动删除
                    .withMode(CreateMode.EPHEMERAL)
                    // 注册节点并存入数据, 通过 zkClient.getData().forPath(node) 获取
                    .forPath(nodePath + "/" + nodeName, host.getBytes());
            logger.info("服务节点 [{}] 已上线.", nodeName);
            monitorNode(fullPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ">>> 服务节点 [" + nodeName + "] 已上线.";
    }

    /**
     * 注销服务地址
     */
    public String cancellationAddress() {
        try {
            Stat stat = zkClient.checkExists().forPath(fullPath);
            if (stat != null) {
                zkClient.delete().forPath(fullPath);
                logger.info("节点 [{}] 已下线.", nodeName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ">>> 服务节点 [" + nodeName + "] 已下线.";
    }

    /**
     * usingWatcher(): 为指定的节点设置一个监听器(Watcher)，在该节点发生变化时可以接收到通知
     * <p>
     * Watcher 只会在节点发生变化时触发一次，触发后会自动被删除，需要重新注册 Watcher 才能再次观察节点变化
     */
    private void monitorNode(String nodePath) {
        try {
            Stat stat = zkClient.checkExists().usingWatcher((CuratorWatcher) event -> {
                if (event.getType() == Watcher.Event.EventType.NodeDeleted) {
                    // node is deleted, check its state
                    Stat st = zkClient.checkExists().forPath(nodePath);
                    if (st != null && st.getEphemeralOwner() == 0) {
                        logger.info("node is unavailable, it may be a failure.");
                        isAvailable.set(false);
                        // restart node
                        registeredAddress();
                    } else {
                        logger.info("node is still available, it is a normal down.");
                        isAvailable.set(true);
                    }
                }
            }).forPath(nodePath);
            isAvailable.set(stat != null && stat.getEphemeralOwner() != 0);
        } catch (Exception e) {
            logger.error("Node monitor error.", e);
        }
    }
}

