package xyz.ibudai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.ibudai.model.User;
import xyz.ibudai.repository.UserService;

import java.util.List;

@RestController
@RequestMapping("api/es/document")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("getById")
    public User getById(@RequestParam("indexName") String indexName, @RequestParam("id") String id) {
        return userService.get(indexName, id);
    }

    @GetMapping("list")
    public List<User> queryAll(@RequestParam("indexName") String indexName) {
        return userService.list(indexName);
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
        return userService.save(indexName, user.getId(), user);
    }

    @PostMapping("edit")
    public String update(@RequestParam("indexName") String indexName,
                         @RequestBody User user) {
        return userService.update(indexName, user.getId(), user);
    }

    @GetMapping("delete")
    public String delete(@RequestParam("indexName") String indexName,
                         @RequestParam("id") String id) {
        return userService.deleted(indexName, id);
    }
}
