package xyz.ibudai.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GZIPTest {

    /**
     * 压缩为 GZIP 字节数组
     */
    public static byte[] compress(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            throw new IllegalArgumentException("输入字节不能为空");
        }

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            GZIPOutputStream gzip = new GZIPOutputStream(out);
            gzip.write(bytes);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("压缩数据失败, {}", e.getCause());
        }
    }

    /**
     * GZIP 解压缩
     */
    public static byte[] uncompress(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            throw new IllegalArgumentException("输入字节不能为空");
        }

        try (ByteArrayInputStream in = new ByteArrayInputStream(bytes);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            GZIPInputStream unzip = new GZIPInputStream(in);
            byte[] buffer = new byte[1024];
            int n;
            while ((n = unzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("解压缩数据失败, {}", e.getCause());
        }
    }
}
