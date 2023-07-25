package xyz.ibudai.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class RedisUtils {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 判断 key 是否存在
     *
     * @param key 键
     */
    public Boolean hasKey(String key) {
        if (key == null) {
            throw new IllegalArgumentException("key can not be null!");
        }

        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取 key 过期时间
     *
     * @param key 键
     * @return 时间, 0代表为永久有效
     */
    public Long getExpire(String key, TimeUnit timeUnit) {
        if (key == null) {
            throw new IllegalArgumentException("key can not be null!");
        }

        Long expireTime = -1L;
        if (hasKey(key)) {
            expireTime = redisTemplate.getExpire(key, timeUnit);
        }
        return expireTime;
    }

    /**
     * 设置 key 过期时间
     *
     * @param key 键
     * @return 时间, 0代表为永久有效
     */
    public Boolean setExpire(String key, long time, TimeUnit timeUnit) {
        Boolean flag;
        try {
            if (hasKey(key)) {
                flag = redisTemplate.expire(key, time, timeUnit);
            } else {
                throw new RuntimeException("The key doesn't exist.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return flag;
    }

    /**
     * 缓存放入, 默认无限期
     *
     * @param key   键
     * @param value 值
     */
    public void set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 缓存放入并设置过期时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间, 0 将设置无限期
     */
    public void setWithTime(String key, Object value, long time, TimeUnit timeUnit) {
        try {
            if (time <= 0) {
                set(key, value);
            } else {
                redisTemplate.opsForValue().set(key, value, time, timeUnit);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 缓存获取
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        if (key == null) {
            throw new IllegalArgumentException("key can not be null!");
        }

        Object object = null;
        if (hasKey(key)) {
            object = redisTemplate.opsForValue().get(key);
        }
        return object;
    }

    /**
     * 删除数据
     *
     * @param key 键
     */
    public Boolean delete(String key) {
        if (key == null) {
            throw new IllegalArgumentException("key can not be null!");
        }

        Boolean flag = false;
        if (hasKey(key)) {
            flag = redisTemplate.delete(key);
        }
        return flag;
    }

    /**
     * 批量删除缓存
     *
     * @param keys 可以传一个值或多个
     */
    public void batchRemove(String... keys) {
        if (keys == null || keys.length == 0) {
            return;
        }
        if (keys.length == 1) {
            redisTemplate.delete(keys[0]);
        } else {
            redisTemplate.delete((Collection<String>) CollectionUtils.arrayToList(keys));
        }
    }

    /**
     * 读取集合批量删除
     *
     * @param keys 可以传一个值 或多个
     */
    public Boolean batchRemove(Set<String> keys) {
        if (Objects.isNull(keys) || keys.isEmpty()) {
            return false;
        }

        Boolean flag;
        if (keys.size() == 1) {
            flag = redisTemplate.delete((String) keys.toArray()[0]);
        } else {
            Long num = redisTemplate.delete(keys);
            flag = !Objects.isNull(num) && num > 0;
        }
        return flag;
    }
}

