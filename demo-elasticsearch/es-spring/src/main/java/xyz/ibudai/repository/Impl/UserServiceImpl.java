package xyz.ibudai.repository.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.ibudai.model.User;
import xyz.ibudai.repository.UserRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.termQuery;

@Service("userService")
public class UserServiceImpl extends AbstractRepository<User> implements UserRepository {

    @Autowired
    public ObjectMapper objectMapper;

    @Autowired
    public RestHighLevelClient restHighLevelClient;

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

    @Override
    public List<User> filterQuery(String indexName, int min, int max) {
        SearchRequest request = new SearchRequest();
        request.indices(indexName);
        // 构造查询条件
        SearchSourceBuilder builder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 查询 "min < age < max"
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("age")
                .gte(min)
                .lte(max));
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

    @Override
    public List<User> vagueQuery(String indexName, String name) {
        SearchRequest request = new SearchRequest();
        request.indices(indexName);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        // 查询名称中包含“张三”的数据，或者比“张三”多一个字符的数据，
        // 这是通过Fuzziness.ONE来控制的，比如“张三1”是可以出现的，但是“张三12”是无法出现的，这是因为他比张三多了两个字符；
        // 除了“Fuzziness.ONE”之外，还可以是“Fuzziness.TWO”等
        builder.query(QueryBuilders.fuzzyQuery("name", name)
                .fuzziness(Fuzziness.ONE));
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
}
