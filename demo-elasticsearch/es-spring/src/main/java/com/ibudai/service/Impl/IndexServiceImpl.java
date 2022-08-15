package com.ibudai.service.Impl;

import com.ibudai.service.IndexService;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service("indexService")
public class IndexServiceImpl implements IndexService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public RestHighLevelClient restHighLevelClient;

    /**
     * 数据类型：
     * 1. text: 当一个字段需要用于全文搜索(会被分词), 应该使用 text 类型
     * 2. keyword: 当一个字段需要按照精确值进行过滤、排序、聚合等操作时, 就应该使用 keyword 类型
     * 3. date: 时间类型
     * 4. boolean: 布偶类型
     * 5. range: (integer_range, long_range, float_range, double_range, date_range, ip_range)
     */
    public String mapping() {
        return "{\n" +
                "  \"properties\": {\n" +
                "      \"id\": {\n" +
                "          \"type\": \"keyword\"\n" +
                "      },\n" +
                "      \"info\": {\n" +
                "          \"type\": \"text\"\n" +
                "      },\n" +
                "      \"createDate\": {\n" +
                "          \"type\": \"keyword\"\n" +
                "      }\n" +
                "   }" +
                "}";
    }

    /**
     * 索引是否存在
     *
     * @param indexName
     * @return
     */
    @Override
    public boolean isExist(String indexName) {
        boolean isExists;
        GetIndexRequest request = new GetIndexRequest(indexName);
        try {
            isExists = restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            logger.error("判断索引存在异常" + e);
            throw new RuntimeException(e);
        }
        return isExists;
    }

    /**
     * 创建索引
     *
     * @param indexName
     * @return
     */
    @Override
    public boolean createIndex(String indexName) {
        CreateIndexRequest request = new CreateIndexRequest(indexName);
        // 配置 mapping
        request.mapping(mapping(), XContentType.JSON);
        // 配置索引分片、副本
        Settings.Builder builder = Settings.builder()
                .put("index.number_of_shards", 3)
                .put("index.number_of_replicas", 5)
                .put("index.max_result_window", 2147483647);
        request.settings(builder);
        CreateIndexResponse response;
        try {
            response = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            logger.error("索引创建异常" + e);
            throw new RuntimeException(e);
        }
        return response.isAcknowledged();
    }

    /**
     * 删除索引
     *
     * @param indexName
     * @return
     */
    @Override
    public boolean deleteIndex(String indexName) {
        DeleteIndexRequest request = new DeleteIndexRequest(indexName);
        AcknowledgedResponse response;
        try {
            response = restHighLevelClient.indices().delete(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            logger.error("索引 {} 删除异常", indexName);
            throw new RuntimeException(e);
        }
        return response.isAcknowledged();
    }
}
