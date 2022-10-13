package xyz.ibudai.demo.utils;

import org.apache.kafka.common.serialization.Deserializer;
import xyz.ibudai.demo.bean.User;

import java.util.Map;

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