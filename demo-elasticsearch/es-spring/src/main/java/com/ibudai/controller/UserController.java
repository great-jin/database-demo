package com.ibudai.controller;

import com.ibudai.model.User;
import com.ibudai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("es/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("getById")
    public User getById(@RequestParam("indexName") String indexName,
                        @RequestParam("id") String id) {
        return userService.getById(indexName, id);
    }

    @PostMapping("add")
    public boolean insert(@RequestParam("indexName") String indexName,
                          @RequestBody User user) {
        return userService.insert(indexName, user);
    }

    @PostMapping("edit")
    public boolean update(@RequestParam("indexName") String indexName,
                          @RequestBody User user) {
        return userService.update(indexName, user);
    }

    @GetMapping("delete")
    public boolean delete(@RequestParam("indexName") String indexName,
                          @RequestParam("id") String id) {
        return userService.delete(indexName, id);
    }
}
