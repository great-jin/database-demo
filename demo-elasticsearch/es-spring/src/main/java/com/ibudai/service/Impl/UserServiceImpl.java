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
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.termQuery;

@Service("userService")
public class UserServiceImpl implements UserService {

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
     * 查询所有数据
     *
     * @param indexName
     * @return
     */
    @Override
    public List<User> queryAll(String indexName) {
        SearchRequest request = new SearchRequest();
        request.indices(indexName);
        // 构造查询条件
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.matchAllQuery());
        request.source(builder);
        List<User> userList = new ArrayList<>();
        try {
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            for (SearchHit hit : response.getHits().getHits()) {
                userList.add(objectMapper.readValue(hit.getSourceAsString(), User.class));
            }
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
        return userList;
    }

    /**
     * 单条件精准查询
     *
     * @param indexName
     * @param user
     * @return
     */
    @Override
    public List<User> singleQuery(String indexName, User user) {
        SearchRequest request = new SearchRequest();
        request.indices(indexName);
        // 构造查询条件
        SearchSourceBuilder builder = new SearchSourceBuilder();
        if (!Strings.isNullOrEmpty(user.getId())) {
            builder.query(QueryBuilders.termQuery("id", user.getId()));
        }
        request.source(builder);
        List<User> userList = new ArrayList<>();
        try {
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            for (SearchHit hit : response.getHits().getHits()) {
                userList.add(objectMapper.readValue(hit.getSourceAsString(), User.class));
            }
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
        return userList;
    }

    /**
     * 多条件查询
     *
     * @param indexName
     * @param user
     * @return
     */
    @Override
    public List<User> multipleQuery(String indexName, User user) {
        SearchRequest request = new SearchRequest();
        request.indices(indexName);
        // 构造查询条件
        SearchSourceBuilder builder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (!Strings.isNullOrEmpty(user.getId())) {
            boolQueryBuilder.must(termQuery("id", user.getId()));
        }
        if (!Strings.isNullOrEmpty(user.getName())) {
            boolQueryBuilder.must(termQuery("name", user.getName()));
        }
        builder.query(boolQueryBuilder);
        request.source(builder);
        List<User> userList = new ArrayList<>();
        try {
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            for (SearchHit hit : response.getHits().getHits()) {
                userList.add(objectMapper.readValue(hit.getSourceAsString(), User.class));
            }
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
        return userList;
    }

    /**
     * getResult(): CREATED、UPDATED、DELETED、NOT_FOUND、NOOP
     *
     * @param index
     * @param user
     * @return
     */
    @Override
    public String insert(String index, User user) {
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
        return response.getResult().getLowercase();
    }

    @Override
    public String update(String index, User user) {
        UpdateRequest request = new UpdateRequest();
        request.index(index).id(user.getId());
        UpdateResponse response;
        try {
            request.doc(objectMapper.writeValueAsString(user), XContentType.JSON);
            response = restHighLevelClient.update(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return response.getResult().getLowercase();
    }

    @Override
    public String delete(String index, String id) {
        DeleteRequest request = new DeleteRequest(index);
        request.id(id);
        DeleteResponse response;
        try {
            response = restHighLevelClient.delete(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return response.getResult().getLowercase();
    }
}
