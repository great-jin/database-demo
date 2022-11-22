package xyz.ibudai.repository;

public interface IndexService {

    String mapping();

    /**
     * 索引是否存在
     *
     * @param indexName
     * @return
     */
    boolean isExist(String indexName);

    /**
     * 创建索引
     *
     * @param indexName
     * @return
     */
    boolean createIndex(String indexName);

    /**
     * 删除索引
     *
     * @param indexName
     * @return
     */
    boolean deleteIndex(String indexName);
}
