package com.ibudai.service;

import com.ibudai.model.User;
import com.ibudai.repository.AbstractRepository;
import com.ibudai.repository.Repository;

import java.util.List;

public interface UserService extends Repository<User> {

    List<User> singleQuery(String indexName, User user);

    List<User> multipleQuery(String indexName, User user);

    List<User> filterQuery(String indexName, int min, int max);

    List<User> vagueQuery(String indexName, String name);
}
