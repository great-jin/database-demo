package xyz.ibudai.database.redis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.ibudai.database.redis.bean.User;
import xyz.ibudai.database.redis.utils.BloomFilterHelper;
import xyz.ibudai.database.redis.utils.BloomFilterUtils;

@RestController
@RequestMapping(value = "/api/redis/bloom")
public class BloomController {

    @Autowired
    private BloomFilterUtils bloomFilterUtils;

    @Autowired
    private BloomFilterHelper bloomFilterHelper;

    @PostMapping("/add")
    public String addUser(@RequestBody User user) {
        try {
            bloomFilterUtils.add(bloomFilterHelper, String.valueOf(user.getId()), user.getName());
        } catch (Exception e) {
            throw new RuntimeException("添加失败");
        }
        return "添加成功";
    }

    @GetMapping("/get")
    public boolean getUser(@RequestParam("Id") String Id) {
        return bloomFilterUtils.include(bloomFilterHelper, "bloom", Id);
    }
}
