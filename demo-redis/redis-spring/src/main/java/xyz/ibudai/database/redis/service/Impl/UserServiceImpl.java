package xyz.ibudai.database.redis.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import xyz.ibudai.database.redis.bean.User;
import xyz.ibudai.database.redis.dao.UserDao;
import xyz.ibudai.database.redis.service.UserService;

@Service("userService")
@CacheConfig(cacheNames = "user")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    /**
     * unless = "#result == null" 放行空值不缓存
     */
    @Override
    @Cacheable(key = "#Id", unless = "#result == null")
    public User getUser(Integer Id) {
        return userDao.queryById(Id);
    }
}
