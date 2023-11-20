package xyz.ibudai.repository;

/**
 * The interface Index service.
 */
public interface IndexRepository {

    /**
     * Mapping string.
     *
     * @return the string
     */
    default String mapping() {
        return "";
    }

    /**
     * 索引是否存在
     *
     * @param indexName the index name
     * @return boolean
     */
    boolean isExist(String indexName);

    /**
     * 创建索引
     *
     * @param indexName the index name
     * @return boolean
     */
    boolean createIndex(String indexName);

    /**
     * 删除索引
     *
     * @param indexName the index name
     * @return boolean
     */
    boolean deleteIndex(String indexName);
}
