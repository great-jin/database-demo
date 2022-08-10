package com.ibudai.service.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.ibudai.service.IndexService;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;

import java.io.IOException;

@Service("indexService")
public class IndexServiceImpl implements IndexService {

    final private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public RestHighLevelClient restHighLevelClient;

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
        CreateIndexResponse response;
        try {
            response = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
            restHighLevelClient.close();
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
        } catch (IOException e) {
            logger.error("索引删除异常" + e);
            throw new RuntimeException(e);
        }
        return response.isAcknowledged();
    }
}
