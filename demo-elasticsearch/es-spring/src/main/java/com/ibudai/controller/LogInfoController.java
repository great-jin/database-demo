package com.ibudai.controller;

import com.ibudai.service.LogInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("es/log")
public class LogInfoController {

    @Autowired
    LogInfoService logInfoService;

    @GetMapping("save")
    public String save(@RequestParam("id") String id) {
        String result = "";
        try {
            Integer.parseInt("abc");
        } catch (Exception e) {
            String builder = e + "\n" +
                    Arrays.toString(e.getStackTrace());
            return logInfoService.saveInfo("log_info", id, builder);
        }
        return result;
    }


    @GetMapping("get")
    public String getById(@RequestParam("id") String id) {
        return logInfoService.getById("log_info", id);
    }
}
