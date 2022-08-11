package com.ibudai.service.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibudai.service.IndexService;
import com.ibudai.service.LogInfoService;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service("logInfoService")
public class LogInfoServiceImpl implements LogInfoService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IndexService indexService;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public String saveInfo(String index, String id, String info) {
        if (!indexService.isExist(index)) {
            indexService.createIndex(index);
        }
        HashMap<String, String> infoMap = new HashMap<>();
        infoMap.put("info", info);
        IndexRequest request = new IndexRequest();
        IndexResponse response;
        request.index(index);
        request.id(id);
        try {
            request.source(objectMapper.writeValueAsString(infoMap), XContentType.JSON);
            response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return response.getResult().getLowercase();
    }

    @Override
    public String getById(String index, String docId) {
        GetRequest request = new GetRequest();
        request.index(index);
        request.id(docId);
        Map<String, String> infoMap;
        GetResponse response;
        try {
            response = restHighLevelClient.get(request, RequestOptions.DEFAULT);
            infoMap = objectMapper.readValue(response.getSourceAsString(), Map.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return infoMap.get("info");
    }
}
