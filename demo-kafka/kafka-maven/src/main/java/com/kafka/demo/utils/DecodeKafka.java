package com.kafka.demo.utils;

import java.util.Map;

import com.kafka.demo.bean.User;
import org.apache.kafka.common.serialization.Deserializer;

/**
 * 内容编码工具类
 */
public class DecodeKafka implements Deserializer<User> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public User deserialize(String topic, byte[] data) {
        return BeanUtils.byteToUser(data);
    }

    @Override
    public void close() {
        System.out.println("DecodeKafka is close.");
    }
}