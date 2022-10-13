package xyz.ibudai.demo.utils;

import org.apache.kafka.common.serialization.Serializer;
import xyz.ibudai.demo.bean.User;

import java.util.Map;

/**
 * 内容解码工具类
 */
public class EncodeKafka implements Serializer<User> {

    @Override
    public void configure(Map configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String topic, User user) {
        return BeanUtils.objectToByte(user);
    }

    @Override
    public void close() {
        System.out.println("EncodeKafka is close.");
    }
}
