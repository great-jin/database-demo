package xyz.ibudai.repository;

import xyz.ibudai.model.User;

import java.util.List;

public interface UserService extends Repository<User> {

    List<User> singleQuery(String indexName, User user);

    List<User> multipleQuery(String indexName, User user);

    List<User> filterQuery(String indexName, int min, int max);

    List<User> vagueQuery(String indexName, String name);
}
