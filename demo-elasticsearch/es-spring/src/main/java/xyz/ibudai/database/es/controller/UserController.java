package xyz.ibudai.database.es.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.ibudai.database.es.model.Condition;
import xyz.ibudai.database.es.model.QueryType;
import xyz.ibudai.database.es.entity.User;
import xyz.ibudai.database.es.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/es/document")
public class UserController {

    @Autowired
    private UserRepository userService;

    @GetMapping("getById")
    public User getById(@RequestParam("indexName") String indexName,
                        @RequestParam("id") String id) {
        return userService.get(indexName, id);
    }

    @GetMapping("list")
    public List<User> queryAll(@RequestParam("indexName") String indexName) {
        return userService.list(indexName);
    }

    @PostMapping("listByCondition")
    public List<User> listByCondition(@RequestParam("indexName") String indexName,
                                      @RequestBody User user) {
        List<Condition> conditions = new ArrayList<>();
        List<String> ids = Stream.of("1", "2", "3").collect(Collectors.toList());
        conditions.add(new Condition(QueryType.IN, "id", ids));
        conditions.add(new Condition(QueryType.EQUAL, "name", user.getName()));
        return userService.listByCondition(indexName, conditions);
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
        return userService.delete(indexName, id);
    }
}
