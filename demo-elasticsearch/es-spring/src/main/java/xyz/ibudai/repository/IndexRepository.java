package xyz.ibudai.repository;

import java.util.List;
import java.util.Set;

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
     * Gets alias indices.
     *
     * @param alias the alias
     * @return the alias indices
     */
    Set<String> getAliasIndices(String alias);

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
