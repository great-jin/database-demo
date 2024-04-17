package xyz.ibudai.database.es.repository.Impl;

import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.GetAliasesResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.cluster.metadata.AliasMetadata;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import xyz.ibudai.database.es.repository.IndexRepository;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

@Service("indexService")
public class IndexRepositoryImpl implements IndexRepository {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public RestHighLevelClient restHighLevelClient;

    /**
     * Ⅰ. 指定映射
     * (1)精确控制：可以精确控制每个字段的类型、属性和分析器。
     * (2)避免错误推断：避免 Elasticsearch 根据现有文档来自动推断映射，避免错误的映射。
     * (3)繁琐：对于大型索引，手动指定映射可能会变得非常繁琐。
     * <p>
     * Ⅱ. 不指定映射
     * (1)自动推断：Elasticsearch 会根据实际插入的文档来自动推断字段的类型。
     * (2)灵活性：对于刚开始使用 Elasticsearch 或者索引结构可能变化较大的情况，可以更灵活地适应变化。
     * (3)错误推断：自动推断可能导致字段类型不符合期望，特别是当有不同类型的文档插入同一索引时。
     * <p>
     * Ⅲ. 数据类型
     * (1)text: 当一个字段需要用于全文搜索(会被分词), 应该使用 text 类型
     * (2)keyword: 当一个字段需要按照精确值进行过滤、排序、聚合等操作时, 就应该使用 keyword 类型
     * (3)date: 时间类型
     * (4)boolean: 布偶类型
     * (5)range: (integer_range, long_range, float_range, double_range, date_range, ip_range)
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

    @Override
    public boolean isExist(String indexName) {
        boolean isExists;
        GetIndexRequest request = new GetIndexRequest(indexName);
        try {
            isExists = restHighLevelClient.indices()
                    .exists(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            logger.error("判断索引存在异常" + e);
            throw new RuntimeException(e);
        }
        return isExists;
    }

    @Override
    public Set<String> getAliasIndices(String alias) {
        Set<String> indices;
        try {
            GetAliasesRequest aliasesRequest = new GetAliasesRequest(alias);
            GetAliasesResponse response = restHighLevelClient.indices()
                    .getAlias(aliasesRequest, RequestOptions.DEFAULT);
            Map<String, Set<AliasMetadata>> aliases = response.getAliases();
            indices = aliases.keySet();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return indices;
    }

    @Override
    public boolean createIndex(String indexName) {
        CreateIndexRequest request = new CreateIndexRequest(indexName);
        // 配置 mapping, 未指定则自动映射
        String mapping = mapping();
        if (StringUtils.hasLength(mapping)) {
            request.mapping(mapping, XContentType.JSON);
        }
        // 配置索引分片、副本
        Settings.Builder builder = Settings.builder()
                .put("index.number_of_shards", 3)
                .put("index.number_of_replicas", 5)
                .put("index.max_result_window", Integer.MAX_VALUE);
        // 配置索引别名
        Alias alias = new Alias("alias_" + indexName);
        request.alias(alias);
        // 构建请求
        request.settings(builder);
        CreateIndexResponse response;
        try {
            response = restHighLevelClient.indices()
                    .create(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            logger.error("索引创建异常" + e);
            throw new RuntimeException(e);
        }
        return response.isAcknowledged();
    }

    @Override
    public boolean deleteIndex(String indexName) {
        DeleteIndexRequest request = new DeleteIndexRequest(indexName);
        AcknowledgedResponse response;
        try {
            response = restHighLevelClient.indices()
                    .delete(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            logger.error("索引 {} 删除异常", indexName);
            throw new RuntimeException(e);
        }
        return response.isAcknowledged();
    }
}
