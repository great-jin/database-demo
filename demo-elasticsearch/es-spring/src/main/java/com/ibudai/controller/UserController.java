package com.ibudai.controller;

import com.ibudai.model.User;
import com.ibudai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("es/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("getById")
    public User getById(@RequestParam("indexName") String indexName, @RequestParam("id") String id) {
        return userService.getById(indexName, id);
    }

    @GetMapping("list")
    public List<User> queryAll(@RequestParam("indexName") String indexName) {
        return userService.queryAll(indexName);
    }

    @PostMapping("single")
    public List<User> singleQuery(@RequestParam("indexName") String indexName, @RequestBody User user) {
        return userService.singleQuery(indexName, user);
    }

    @PostMapping("multiple")
    public List<User> multipleQuery(@RequestParam("indexName") String indexName, @RequestBody User user) {
        return userService.multipleQuery(indexName, user);
    }

    @PostMapping("add")
    public String insert(@RequestParam("indexName") String indexName,
                         @RequestBody User user) {
        return userService.insert(indexName, user);
    }

    @PostMapping("edit")
    public String update(@RequestParam("indexName") String indexName,
                         @RequestBody User user) {
        return userService.update(indexName, user);
    }

    @GetMapping("delete")
    public String delete(@RequestParam("indexName") String indexName,
                         @RequestParam("id") String id) {
        return userService.delete(indexName, id);
    }
}
