package xyz.ibudai.database.redis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.ibudai.database.redis.bean.User;
import xyz.ibudai.database.redis.utils.RedisUtils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "/api/redis/basic")
public class BasicController {

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 添加数据到 Redis
     */
    @PostMapping("/add")
    public void add(@RequestBody User user) {
        redisUtils.set(String.valueOf(user.getId()), user);
    }

    /**
     * 先从 Redis 读数据，未找到再读取数据库
     */
    @GetMapping("/get")
    public User get(@RequestParam("Id") String Id) {
        User user;
        // 查询缓存数据
        user = (User) redisUtils.get(Id);
        // 缓存命中则返回数据, 否则读取数据库
        if (user == null) {
            user = new User(123, "Alex", "123", new Date());
            // 将查出来的数据存入 Redis
            redisUtils.setWithTime(Id, user, 5, TimeUnit.MINUTES);
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

