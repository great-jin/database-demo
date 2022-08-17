package com.ibudai.controller;

import com.ibudai.bean.User;
import com.ibudai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cache")
public class CacheController {

    @Autowired
    UserService userService;

    @GetMapping("/get")
    public User Demo(Integer Id){
        return userService.getUser(Id);
    }
}
