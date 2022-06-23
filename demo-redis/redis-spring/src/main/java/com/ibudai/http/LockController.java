package com.ibudai.http;

import com.ibudai.Utils.RedisLock;
import com.ibudai.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lock")
public class LockController {

    @Autowired
    RedisLock redisLock;

    @GetMapping("/put")
    public void Demo() {
        String token = null;
        try {
            token = redisLock.lock("lock_name", 10000, 11000);
            if (token != null) {
                System.out.print("我拿到了锁哦");
                // 执行业务代码
            } else {
                System.out.print("我没有拿到锁唉");
            }
        } finally {
            if (token != null) {
                redisLock.unlock("lock_name", token);
            }
        }
    }
}
