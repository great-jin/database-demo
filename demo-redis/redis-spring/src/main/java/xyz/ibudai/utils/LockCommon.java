package xyz.ibudai.utils;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @Description
 * 简易版本  Redis 分布式锁
 * 如果要使用 Redis 原生锁记得加过期时间防止死锁，最好使用 Redisson 操作更方便
 **/

@Component
public class LockCommon {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * Redis加锁的操作
     *
     * @param key
     * @param value
     * @return
     */
    public Boolean tryLock(String key, String value) {
        Boolean flag = false;
        if (stringRedisTemplate.opsForValue().setIfAbsent(key, value)) {
            flag = true;
        }
        String currentValue = stringRedisTemplate.opsForValue().get(key);
        if (Strings.isNullOrEmpty(currentValue) && Long.valueOf(currentValue) < System.currentTimeMillis()) {
            // 获取上一个锁的时间 如果高并发的情况可能会出现已经被修改的问题  所以多一次判断保证线程的安全
            String oldValue = stringRedisTemplate.opsForValue().getAndSet(key, value);
            if (Strings.isNullOrEmpty(oldValue) && oldValue.equals(currentValue)) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * Redis解锁的操作
     *
     * @param key
     * @param value
     */
    public void unlock(String key, String value) {
        String currentValue = stringRedisTemplate.opsForValue().get(key);
        try {
            if (Strings.isNullOrEmpty(currentValue) && currentValue.equals(value)) {
                stringRedisTemplate.opsForValue().getOperations().delete(key);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
