package com.ibudai.service.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibudai.model.User;
import com.ibudai.service.UserService;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service("userService")
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    public ObjectMapper objectMapper;

    @Autowired
    public RestHighLevelClient restHighLevelClient;

    @Override
    public User getById(String indexName, String id) {
        GetRequest request = new GetRequest(indexName);
        request.id(id);
        User user;
        GetResponse response;
        try {
            response = restHighLevelClient.get(request, RequestOptions.DEFAULT);
            user = objectMapper.readValue(response.getSourceAsString(), User.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    /**
     * getResult(): CREATED、UPDATED、DELETED、NOT_FOUND、NOOP
     *
     * @param index
     * @param user
     * @return
     */
    @Override
    public boolean insert(String index, User user) {
        IndexRequest request = new IndexRequest(index);
        IndexResponse response;
        // 设置文档 id, 等价主键
        request.id(user.getId());
        try {
            request.source(objectMapper.writeValueAsString(user), XContentType.JSON);
            response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return response.getResult().getLowercase().equals("created");
    }

    @Override
    public boolean update(String index, User user) {
        UpdateRequest request = new UpdateRequest();
        request.index(index).id(user.getId());
        UpdateResponse response;
        try {
            request.doc(objectMapper.writeValueAsString(user), XContentType.JSON);
            response = restHighLevelClient.update(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return response.getResult().getLowercase().equals("updated");
    }

    @Override
    public boolean delete(String index, String id) {
        DeleteRequest request = new DeleteRequest(index);
        request.id(id);
        DeleteResponse response;
        try {
            response = restHighLevelClient.delete(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return response.getResult().getLowercase().equals("deleted");
    }
}
