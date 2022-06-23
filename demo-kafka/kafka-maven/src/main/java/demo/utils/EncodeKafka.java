package demo.utils;

import java.util.Map;

import demo.bean.User;
import org.apache.kafka.common.serialization.Serializer;

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
