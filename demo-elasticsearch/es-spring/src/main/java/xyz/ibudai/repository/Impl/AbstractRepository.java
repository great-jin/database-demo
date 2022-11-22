package xyz.ibudai.repository.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import xyz.ibudai.repository.Repository;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public abstract class AbstractRepository<T> implements Repository<T> {

    private Class<T> tClass;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public T get(String index, Serializable id) {
        GetRequest request = new GetRequest();
        request.index(index);
        request.id(id.toString());
        T t;
        GetResponse response;
        try {
            response = restHighLevelClient.get(request, RequestOptions.DEFAULT);
            t = objectMapper.readValue(response.getSourceAsString(), tClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return t;
    }

    @Override
    public List<T> list(String index) {
        SearchRequest request = new SearchRequest();
        request.indices(index);
        // 构造查询条件
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.matchAllQuery());
        builder.timeout(new TimeValue(5, TimeUnit.MINUTES));
        builder.trackTotalHits(true);
        request.source(builder);
        List<T> list;
        try {
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            list = Arrays.stream(response.getHits().getHits()).map(p -> {
                try {
                    return objectMapper.readValue(p.getSourceAsString(), tClass);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public String save(String index, Serializable id, T t) {
        IndexRequest request = new IndexRequest();
        IndexResponse response;
        request.index(index);
        request.id(id.toString());
        try {
            request.source(objectMapper.writeValueAsString(t), XContentType.JSON);
            response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return response.getResult().getLowercase();
    }

    @Override
    public String update(String index, Serializable id, T t) {
        UpdateRequest request = new UpdateRequest();
        request.index(index);
        request.id(id.toString());
        UpdateResponse response;
        try {
            request.doc(objectMapper.writeValueAsString(t), XContentType.JSON);
            response = restHighLevelClient.update(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return response.getResult().getLowercase();
    }

    @Override
    public String deleted(String index, Serializable id) {
        DeleteRequest request = new DeleteRequest();
        request.index(index);
        request.id(id.toString());
        DeleteResponse response;
        try {
            response = restHighLevelClient.delete(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return response.getResult().getLowercase();
    }
}
