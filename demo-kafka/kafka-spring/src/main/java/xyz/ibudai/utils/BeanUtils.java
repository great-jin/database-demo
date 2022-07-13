package xyz.ibudai.utils;

import xyz.ibudai.model.User;

import java.io.*;

public class BeanUtils {

    private BeanUtils() {
    }

    public static User byteToUser(byte[] bytes) {
        return byteToObject(bytes, User.class);
    }

    /**
     * 对象序列化为 byte 数组
     *
     * @param obj
     * @return
     */
    public static byte[] objectToByte(Object obj) {
        byte[] _byte = null;
        try (ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
             ObjectOutputStream outputStream = new ObjectOutputStream(byteArray)) {
            outputStream.writeObject(obj);
            outputStream.flush();
            _byte = byteArray.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return _byte;
    }

    /**
     * 字节数组转为对象
     *
     * @param bytes
     * @return
     */
    public static <T> T byteToObject(byte[] bytes, Class<T> tClass) {
        Object readObject;
        try (ByteArrayInputStream in = new ByteArrayInputStream(bytes);
             ObjectInputStream inputStream = new ObjectInputStream(in)) {
            readObject = inputStream.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return (T) readObject;
    }
}
