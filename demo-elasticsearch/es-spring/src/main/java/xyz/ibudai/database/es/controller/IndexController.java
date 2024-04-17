package xyz.ibudai.database.es.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.ibudai.database.es.repository.IndexRepository;

@RestController
@RequestMapping("api/es/index")
public class IndexController {

    @Autowired
    IndexRepository indexService;

    @GetMapping("exist")
    public boolean isExist(@RequestParam("indexName") String indexName) {
        return indexService.isExist(indexName);
    }

    @GetMapping("create")
    public boolean createIndex(@RequestParam("indexName") String indexName) {
        return indexService.createIndex(indexName);
    }

    @GetMapping("delete")
    public boolean deleteIndex(@RequestParam("indexName") String indexName) {
        return indexService.deleteIndex(indexName);
    }
}
