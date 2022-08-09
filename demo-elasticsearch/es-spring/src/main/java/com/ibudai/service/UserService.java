package com.ibudai.service;

import com.ibudai.model.User;

public interface UserService {

    User getById(String indexName, String id);

    boolean insert(String index, User user);

    boolean update(String index, User user);

    boolean delete(String index, String id);
}
