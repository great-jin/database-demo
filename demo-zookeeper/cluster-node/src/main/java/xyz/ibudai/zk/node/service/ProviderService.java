package xyz.ibudai.zk.node.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Service
public class ProviderService {

    @Value("${server.port}")
    private String port;

    public String callMethod() {
        String msg = null;
        try {
            msg = "调用了服务提供者 " + InetAddress.getLocalHost().getHostAddress() + ":" + port + " 的方法";
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return msg;
    }
}
