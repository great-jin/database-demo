package com.ibudai.service;

import com.ibudai.model.User;

import java.util.List;

public interface UserService {

    User getById(String indexName, String docId);

    List<User> queryAll(String indexName);

    List<User> singleQuery(String indexName, User user);

    List<User> multipleQuery(String indexName, User user);

    List<User> filterQuery(String indexName, int min, int max);

    List<User> vagueQuery(String indexName, String name);

    String insert(String index, User user);

    String update(String index, User user);

    String delete(String index, String docId);
}
