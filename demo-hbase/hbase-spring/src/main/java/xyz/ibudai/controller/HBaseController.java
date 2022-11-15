package xyz.ibudai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.ibudai.service.DMLService;

import java.io.IOException;

@RestController
@RequestMapping("api/hbase")
public class HBaseController {

    @Autowired
    private DMLService dmlService;

    @GetMapping("create")
    public void create() throws IOException {
        dmlService.createTable("test_db", "c1");
    }
}
