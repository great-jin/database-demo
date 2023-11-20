package xyz.ibudai.repository;

import xyz.ibudai.model.User;

import java.util.List;

/**
 * The interface User repository.
 */
public interface UserRepository extends Repository<User> {

    /**
     * 单条件精准查询
     *
     * @param indexName the index name
     * @param user      the user
     * @return the list
     */
    List<User> singleQuery(String indexName, User user);

    /**
     * 多条件查询
     *
     * @param indexName the index name
     * @param user      the user
     * @return the list
     */
    List<User> multipleQuery(String indexName, User user);

    /**
     * 过滤查询
     *
     * @param indexName the index name
     * @param min       the min
     * @param max       the max
     * @return the list
     */
    List<User> filterQuery(String indexName, int min, int max);

    /**
     * 模糊查询
     *
     * @param indexName the index name
     * @param name      the name
     * @return the list
     */
    List<User> vagueQuery(String indexName, String name);
}
