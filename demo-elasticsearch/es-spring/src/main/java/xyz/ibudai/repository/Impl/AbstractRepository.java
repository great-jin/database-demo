package xyz.ibudai.repository.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
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
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import xyz.ibudai.model.Condition;
import xyz.ibudai.model.PageDetail;
import xyz.ibudai.model.QueryType;
import xyz.ibudai.repository.Repository;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public abstract class AbstractRepository<T> implements Repository<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRepository.class);

    private final Class<T> tClass;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @SuppressWarnings({"unchecked", "rawtypes"})
    public AbstractRepository() {
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.tClass = (Class) type.getActualTypeArguments()[0];
    }

    @Override
    public List<T> list(String... index) {
        SearchRequest request = new SearchRequest(index);
        // build condition
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.matchAllQuery());
        builder.timeout(new TimeValue(5, TimeUnit.MINUTES));
        // data limit, default return 10 row
        // set size by "builder.size(Integer)"
        builder.trackTotalHits(true);
        // set request config
        request.source(builder);
        return search(request).getData();
    }

    @Override
    public List<T> listByCondition(String index, List<Condition> conditions) {
        return pageByCondition(index, -1, -1, conditions).getData();
    }

    @Override
    public PageDetail<T> page(String index, Integer limit, Integer offset) {
        return pageByCondition(index, limit, offset, null);
    }

    @Override
    public PageDetail<T> pageByCondition(String index, Integer limit, Integer offset, List<Condition> conditions) {
        SearchRequest request = new SearchRequest(index);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.trackTotalHits(true);
        if (Objects.nonNull(limit) && limit != -1) {
            if (limit < 1 || Objects.isNull(offset) || offset < 1) {
                throw new IllegalArgumentException("Paging params is illegal");
            }
            // paging query
            builder.size(limit);
            builder.from((offset - 1) * limit);
        }

        // build query condition
        if (Objects.isNull(conditions) || conditions.isEmpty()) {
            // query all data
            builder.query(QueryBuilders.matchAllQuery());
        } else {
            builder.query(createQueryBuilder(conditions));
        }
        // set request config
        request.source(builder);
        return search(request);
    }

    private BoolQueryBuilder createQueryBuilder(List<Condition> conditionList) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        for (Condition condition : conditionList) {
            QueryType queryType = condition.getQueryType();
            String fieldName = condition.getFieldName();
            Object fieldValues = condition.getFieldValues();
            QueryBuilder queryBuilder;
            switch (queryType) {
                case EQUAL:
                    // termQuery(): "where fieldName = fieldValues"
                    queryBuilder = QueryBuilders.termQuery(fieldName, fieldValues);
                    break;
                case NOT_EQUAL:
                    queryBuilder = QueryBuilders.boolQuery()
                            .mustNot(QueryBuilders.matchQuery(fieldName, fieldValues));
                    break;
                case IN:
                    // termsQuery(): "where fieldName in (fieldValues...)"
                    queryBuilder = QueryBuilders.termsQuery(fieldName, fieldValues);
                    break;
                case NOT_IN:
                    queryBuilder = QueryBuilders.boolQuery()
                            .mustNot(QueryBuilders.termsQuery(fieldName, fieldValues));
                    break;
                case LIKE:
                    queryBuilder = QueryBuilders.fuzzyQuery(fieldName, fieldValues)
                            .fuzziness(Fuzziness.AUTO);
                    break;
                case GREATER:
                    // gt(): "where fieldName > fieldValues"
                    queryBuilder = QueryBuilders.rangeQuery(fieldName).gt(fieldValues);
                    break;
                case GREATER_OR_EQ:
                    // gte(): "where fieldName >= fieldValues"
                    queryBuilder = QueryBuilders.rangeQuery(fieldName).gte(fieldValues);
                    break;
                case LESS:
                    // lt(): "where fieldName < fieldValues"
                    queryBuilder = QueryBuilders.rangeQuery(fieldName).lt(fieldValues);
                    break;
                case LESS_OR_EQ:
                    // lte(): "where fieldName <= fieldValues"
                    queryBuilder = QueryBuilders.rangeQuery(fieldName).lte(fieldValues);
                    break;
                default:
                    continue;
            }
            // "filter()" more efficient then "must()"
            boolQueryBuilder.filter(queryBuilder);
        }
        return boolQueryBuilder;
    }

    private PageDetail<T> search(SearchRequest request) {
        int total;
        List<T> dataList;
        try {
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            total = (int) response.getHits().getTotalHits().value;
            dataList = Arrays.stream(response.getHits().getHits()).map(p -> {
                try {
                    return objectMapper.readValue(p.getSourceAsString(), tClass);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new PageDetail<>(total, dataList);
    }

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
    public Boolean bulkSave(String index, List<T> data) {
        if (Objects.isNull(data) || data.isEmpty()) {
            throw new IllegalArgumentException("data is empty");
        }

        boolean success = true;
        try {
            BulkRequest bulkRequest = new BulkRequest();
            for (T document : data) {
                IndexRequest indexRequest = new IndexRequest(index)
                        // 设置文档内容
                        .source(objectMapper.writeValueAsString(document), XContentType.JSON);
                bulkRequest.add(indexRequest);
            }
            BulkResponse response = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            if (response.hasFailures()) {
                success = false;
                LOGGER.error("Bulk request failed: {}", response.buildFailureMessage());
            }
        } catch (Exception e) {
            success = false;
            LOGGER.error("Bulk request error.", e);
        }
        return success;
    }

    @Override
    public Boolean batchSave(String index, Integer batchSize, List<T> data) {
        if (Objects.isNull(data) || data.isEmpty()) {
            throw new IllegalArgumentException("data is empty");
        }
        if (Objects.isNull(batchSize) || batchSize <= 0) {
            // Set default value
            batchSize = 200;
        }

        boolean success = true;
        try {
            int start = 0;
            int size = data.size();
            int end = Math.min(size, batchSize);
            while (start <= end) {
                if (start == end) {
                    break;
                }
                try {
                    List<T> tempList = data.subList(start, end);
                    success = bulkSave(index, tempList);
                } catch (Exception e) {
                    LOGGER.error("Save error.", e);
                }
                start = end;
                end = size - end < batchSize ? size : end + batchSize;
            }
        } catch (Exception e) {
            success = false;
            LOGGER.error("Batch save error.", e);
        }
        return success;
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
    public String delete(String index, Serializable id) {
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
