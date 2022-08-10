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
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.termQuery;

@Service("userService")
public class UserServiceImpl implements UserService {

    final private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public ObjectMapper objectMapper;

    @Autowired
    public RestHighLevelClient restHighLevelClient;

    @Override
    public User getById(String indexName, String docId) {
        GetRequest request = new GetRequest();
        request.index(indexName);
        request.id(docId);
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
        } catch (IOException e) {
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return userList;
    }

    /**
     * 过滤查询
     *
     * @param indexName
     * @param min
     * @param max
     * @return
     */
    @Override
    public List<User> filterQuery(String indexName, int min, int max) {
        SearchRequest request = new SearchRequest();
        request.indices(indexName);
        // 构造查询条件
        SearchSourceBuilder builder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 查询年龄大于等于 min，小于等于 max 的结果
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("age").gte(min).lte(max));
        builder.query(boolQueryBuilder);
        request.source(builder);
        List<User> userList = new ArrayList<>();
        try {
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            for (SearchHit hit : response.getHits().getHits()) {
                userList.add(objectMapper.readValue(hit.getSourceAsString(), User.class));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return userList;
    }

    /**
     * 模糊查询
     *
     * @param indexName
     * @param name
     * @return
     */
    @Override
    public List<User> vagueQuery(String indexName, String name) {
        SearchRequest request = new SearchRequest();
        request.indices(indexName);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        // 查询名称中包含“张三”的数据，或者比“张三”多一个字符的数据，
        // 这是通过Fuzziness.ONE来控制的，比如“张三1”是可以出现的，但是“张三12”是无法出现的，这是因为他比张三多了两个字符；
        // 除了“Fuzziness.ONE”之外，还可以是“Fuzziness.TWO”等
        builder.query(QueryBuilders.fuzzyQuery("name", name).fuzziness(Fuzziness.ONE));
        request.source(builder);
        List<User> userList = new ArrayList<>();
        try {
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            for (SearchHit hit : response.getHits().getHits()) {
                userList.add(objectMapper.readValue(hit.getSourceAsString(), User.class));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return userList;
    }

    /**
     * getResult(): CREATED、UPDATED、DELETED、NOT_FOUND、NOOP
     *
     * @param indexName
     * @param user
     * @return
     */
    @Override
    public String insert(String indexName, User user) {
        IndexRequest request = new IndexRequest();
        IndexResponse response;
        // 设置文档 id, 等价主键
        request.index(indexName);
        request.id(user.getId());
        try {
            request.source(objectMapper.writeValueAsString(user), XContentType.JSON);
            response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            logger.error("数据新增异常" + e);
            throw new RuntimeException(e);
        }
        return response.getResult().getLowercase();
    }

    @Override
    public String update(String indexName, User user) {
        UpdateRequest request = new UpdateRequest();
        request.index(indexName);
        request.id(user.getId());
        UpdateResponse response;
        try {
            request.doc(objectMapper.writeValueAsString(user), XContentType.JSON);
            response = restHighLevelClient.update(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            logger.error("数据更新异常" + e);
            throw new RuntimeException(e);
        }
        return response.getResult().getLowercase();
    }

    @Override
    public String delete(String indexName, String docId) {
        DeleteRequest request = new DeleteRequest();
        request.index(indexName);
        request.id(docId);
        DeleteResponse response;
        try {
            response = restHighLevelClient.delete(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            logger.error("数据删除异常" + e);
            throw new RuntimeException(e);
        }
        return response.getResult().getLowercase();
    }
}
