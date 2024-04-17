package xyz.ibudai.database.redis.service;

import xyz.ibudai.database.redis.bean.User;

public interface UserService {

    User getUser(Integer Id);
}
