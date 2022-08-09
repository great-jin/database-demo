package com.ibudai.service.Impl;

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
        AcknowledgedResponse response = null;
        try {
            response = restHighLevelClient.indices().delete(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.isAcknowledged();
    }
}
