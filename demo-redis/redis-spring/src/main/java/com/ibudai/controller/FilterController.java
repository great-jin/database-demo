package com.ibudai.controller;

import com.ibudai.bean.User;
import com.ibudai.Utils.BloomFilterHelper;
import com.ibudai.Utils.BloomFilterUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/filter")
public class FilterController {

    @Autowired
    BloomFilterUtils bloomFilterUtils;

    @Autowired
    private BloomFilterHelper bloomFilterHelper;

    @PostMapping("/add")
    public String addUser(@RequestBody User user) {
        try {
            bloomFilterUtils.add(bloomFilterHelper, String.valueOf(user.getId()), user);
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
