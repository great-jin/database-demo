package xyz.ibudai.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import xyz.ibudai.model.MinioRespond;
import xyz.ibudai.utils.MinioUtil;

import java.awt.print.PrinterException;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Arrays;

@Slf4j
@RestController
@RequestMapping(value = "/api/minio")
public class MinioController {

    @Autowired
    private MinioUtil minioUtil;

    /**
     * 上传文件至存储桶
     *
     * @param bucket
     * @param multipartFile
     * @return
     */
    @PostMapping("/upload")
    public boolean UploadFile(@RequestParam(name = "bucket") String bucket,
                              @RequestParam(name = "file") MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            return false;
        }

        boolean flag = false;
        try {
            MinioRespond minioRespond = minioUtil.uploadFile(multipartFile, bucket);
            if (minioRespond.getObjectWriteResponse() != null) {
                flag = true;
            }
        } catch (Exception e) {
            log.error("上传文件失败, 异常信息:【{}】", Arrays.asList(e.getStackTrace()));
        }
        return flag;
    }

    /**
     * 下载存储桶文件
     *
     * @param bucket
     * @param fileName
     * @return
     * @throws Exception
     */
    @PostMapping("/download")
    public ResponseEntity<byte[]> Download(@RequestParam(name = "bucket") String bucket,
                                           @RequestParam(name = "fileName") String fileName) throws Exception {
        ResponseEntity<byte[]> responseEntity;
        try (
                InputStream in = minioUtil.getObject(bucket, fileName);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {
            int ch;
            byte[] buffer = new byte[256];
            while (-1 != (ch = in.read(buffer))) {
                out.write(buffer, 0, ch);
            }
            byte[] bytes = out.toByteArray();

            // 设置 Header
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Accept-Ranges", "bytes");
            httpHeaders.add("Content-Length", bytes.length + "");
            httpHeaders.add("Content-disposition", "attachment; filename=" + fileName);
            httpHeaders.add("Content-Type", "text/plain;charset=utf-8");
            responseEntity = new ResponseEntity<>(bytes, httpHeaders, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new PrinterException();
        }
        return responseEntity;
    }

    /**
     * 删除桶内文件
     *
     * @param bucket
     * @param fileName
     */
    @PostMapping("/delete")
    public void Delete(@RequestParam(name = "bucket") String bucket,
                       @RequestParam(name = "fileName") String fileName) {
        try {
            minioUtil.removeObject(bucket, fileName);
        } catch (Exception e) {
            log.error("删除文件失败, 异常信息:【{}】", Arrays.asList(e.getStackTrace()));
        }
    }
}
