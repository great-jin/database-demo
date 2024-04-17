package xyz.ibudai.database.redis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.ibudai.database.redis.bean.User;
import xyz.ibudai.database.redis.service.UserService;

@RestController
@RequestMapping(value = "/api/redis/cache")
public class CacheController {

    @Autowired
    UserService userService;

    @GetMapping("/get")
    public User Demo(Integer Id){
        return userService.getUser(Id);
    }
}
