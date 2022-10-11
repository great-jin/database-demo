package xyz.ibudai.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import xyz.ibudai.model.User;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GZIPTest {

    @Test
    public void demo() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        User user = new User("111", "Alex", "123456");
        // Compress data
        byte[] bytes = compress(objectMapper.writeValueAsBytes(user));

        // Uncompress data
        User user1 = objectMapper.readValue(uncompress(bytes), User.class);
        System.out.println(user1);
    }


    /**
     * 压缩为 GZIP 字节数组
     */
    public static byte[] compress(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            throw new IllegalArgumentException("输入字节不能为空");
        }

        try (
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                GZIPOutputStream gzip = new GZIPOutputStream(out);
        ) {
            gzip.write(bytes);
            gzip.close();
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * GZIP 解压缩
     */
    public static byte[] uncompress(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            throw new IllegalArgumentException("输入字节不能为空");
        }

        try (
                ByteArrayInputStream in = new ByteArrayInputStream(bytes);
                GZIPInputStream unzip = new GZIPInputStream(in);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {
            byte[] buffer = new byte[1024];
            int n;
            while ((n = unzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
