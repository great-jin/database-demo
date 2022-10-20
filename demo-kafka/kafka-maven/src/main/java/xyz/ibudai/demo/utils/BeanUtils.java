package xyz.ibudai.demo.utils;

import java.io.*;

public class BeanUtils {

    private BeanUtils() {
    }

    /**
     * 对象序列化为 byte 数组
     */
    public static byte[] objectToByte(Object obj) {
        byte[] bytes = null;
        try (
                ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(byteArray)
        ) {
            out.writeObject(obj);
            out.flush();
            bytes = byteArray.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    /**
     * 字节数组转为对象
     */
    public static <T> T byteToObject(byte[] bytes, Class<T> tClass) {
        Object readObject;
        try (
                ByteArrayInputStream byteIn = new ByteArrayInputStream(bytes);
                ObjectInputStream objIn = new ObjectInputStream(byteIn)
        ) {
            readObject = objIn.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return (T) readObject;
    }
}
