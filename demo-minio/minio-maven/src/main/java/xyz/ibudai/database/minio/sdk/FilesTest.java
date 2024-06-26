package xyz.ibudai.database.minio.sdk;

import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import org.junit.Before;
import org.junit.Test;

import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class FilesTest {

    public MinioClient minioClient;

    @Before
    public void init() throws Exception {
        minioClient = MinioClient.builder()
                // 填入 Minio API
                .endpoint("http://10.231.6.65:9000")
                // 填入用户名、密码
                .credentials("minioadmin", "minio123456")
                .build();
    }

    @Test
    public void FileExist() throws Exception {
        String bucketName = "testbucket";
        String objectName = "bdg.jpg";

        List<String> list = new ArrayList<>();
        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(bucketName)
                        .build());
        for (Result<Item> result : results) {
            if (!result.get().isDir()) {
                list.add(result.get().objectName());
            }
        }
        System.out.println(list.contains(objectName));
    }

    /**
     * Describe：从指定存储桶获取文件
     */
    @Test
    public void GetMinioFile() throws Exception {
        try (InputStream is = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket("newbucket")
                        .object("user.csv")
                        .build())) {
            int ch;
            while ((ch = is.read()) != -1) {
                // 控制台打印内容
                System.out.write(ch);
            }
        } catch (XmlParserException | ServerException | NoSuchAlgorithmException
                | InsufficientDataException | InvalidKeyException | IOException e) {
            // 应针对异常进行分类处理，这里demo测试就简单抛出。
            throw new PrinterException();
        } catch (InvalidResponseException | ErrorResponseException | InternalException e) {
            // 应针对异常进行分类处理，这里demo测试就简单抛出。
            throw new PrinterException();
        }
    }

    /**
     * Describe：将文件存入指定顶存储桶内
     * <p>
     * Minio 以完整文件名为唯一标识，如果文件名重复，则会直接覆盖
     * 在存入文件时，建议在原有文件名之前拼接一个UUID或者时间戳
     */
    @Test
    public void PutMinioFile() throws Exception {
        StringBuilder builder = new StringBuilder();
        // 最终在存储桶中的文件名格式：<uuid>_user.csv
        builder.append(UUID.randomUUID());
        builder.append("_");
        builder.append("user.csv");

        // 生产环境中文件流通常通过接口参数传入
        File file = new File("src/main/resources/files/user.csv");
        if (file.isFile()) {
            FileInputStream fis = new FileInputStream(file);
            try {
                ObjectWriteResponse owr = minioClient.putObject(PutObjectArgs.builder()
                        .bucket("testbucket")
                        .object(builder.toString())
                        .stream(fis, fis.available(), -1)
                        .build());

                System.out.println(owr.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("文件不存在！");
        }
    }

    /**
     * Describe：从指定存储桶内删除文件
     */
    @Test
    public void DeleteMinioFile() throws Exception {
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket("newbucket")
                        .object("user.csv")
                        .build());
    }

    @Test
    public void GetFilesUrl() throws Exception {
        String bucketName = "testbucket";
        String objectName = "bg.csv";
        Integer expires = 7;

        String url = minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(bucketName)
                        .object(objectName)
                        .expiry(expires, TimeUnit.HOURS)
                        .build());
        System.out.println(url);
    }

    /**
     * Describe：列出当前存储桶下所有文件相关信息
     */
    @Test
    public void ListEntireMinio() throws Exception {
        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket("bucket")
                        .build());

        StringBuilder builder = new StringBuilder();
        for (Result<Item> result : results) {
            // 对查询结果进行简单拼接
            builder.append(result.get().objectName());
            builder.append(", ");
            builder.append(result.get().lastModified());
            builder.append(", ");
            builder.append(result.get().size());
            builder.append("\n");
        }
        System.out.println(builder);
    }

    /**
     * Describe：列出当前存储桶下某个时间点之后存入的文件相关信息
     */
    @Test
    public void ListPartMinio() throws Exception {
        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket("bucket")
                        .build());
        StringBuilder builder = new StringBuilder();
        // 设置时间点
        Timestamp earliest = Timestamp.valueOf("2021-12-01 00:00:001");

        for (Result<Item> result : results) {
            Timestamp timestamp = Timestamp.from(result.get().lastModified().toInstant());
            // 如果文件最新更新时间在上述指定的时间之后进行打印
            if (timestamp.after(earliest)) {
                // 打印：文件名，最后创建时间，文件大小
                builder.append(result.get().objectName());
                builder.append(", ");
                builder.append(timestamp);
                builder.append(", ");
                builder.append(result.get().size());
                builder.append("\n");
            }
        }
        System.out.println(builder);
    }

    /**
     * Describe：指定从某一个字符之后开始列出文件信息
     * <p>
     * 例如 newbucket 桶内存有三个文件： a.csv, b.csv, c.csv,
     * .startAfter("b") 最终只会输出：b.csv, c.csv
     */
    @Test
    public void ListAlphabetMinio() throws Exception {
        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket("newbucket")
                        .startAfter("b")
                        .build());

        StringBuilder builder = new StringBuilder();
        for (Result<Item> result : results) {
            // 打印文件名，最后创建时间，文件大小
            builder.append(result.get().objectName());
            builder.append(", ");
            builder.append(result.get().lastModified());
            builder.append(", ");
            builder.append(result.get().size());
            builder.append("\n");
        }

        System.out.println(builder);
    }
}
