package xyz.ibudai.utils;

import java.io.*;

public class BeanUtils {

    private BeanUtils() {
    }

    /**
     * 对象序列化为 byte 数组
     */
    public static byte[] objectToByte(Object obj) {
        try (
                ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(byteArray)
        ) {
            out.writeObject(obj);
            out.flush();
            return byteArray.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 字节数组转为对象
     *
     * @param bytes
     * @return
     */
    public static <T> T byteToObject(byte[] bytes, Class<T> tClass) {
        try (
                ByteArrayInputStream byteIn = new ByteArrayInputStream(bytes);
                ObjectInputStream objIn = new ObjectInputStream(byteIn)
        ) {
            return (T) objIn.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
