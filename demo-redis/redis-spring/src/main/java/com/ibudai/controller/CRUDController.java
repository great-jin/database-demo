package com.ibudai.controller;

import com.ibudai.utils.RedisUtils;
import com.ibudai.bean.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@RestController
@RequestMapping(value = "/redis")
public class CRUDController {

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 添加数据到 Redis
     */
    @PostMapping("/add")
    public boolean add(@RequestBody User user) {
        return redisUtils.set(String.valueOf(user.getId()), user);
    }

    /**
     * 先从 Redis 读数据，未找到再读取数据库
     */
    @GetMapping("/get")
    public User get(@RequestParam("Id") String Id) {
        User user;
        user = (User) redisUtils.get(Id);
        if (user == null) {
            user = new User(123, "Alex", "123", new Date());
            // 将查出来的数据存入Redis数据库
            redisUtils.set(Id, user, 5);
        }
        return user;
    }

    /**
     * 更新数据之后同时更新缓存数据信息，保持数据一致性
     */
    @PostMapping("/update")
    public void update(@RequestBody User user) {
        user = new User(123, "Alex", "321", new Date());
        redisUtils.delete("User:" + user.getId());
    }
}

