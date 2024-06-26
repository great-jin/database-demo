package xyz.ibudai.database.minio.model;

import io.minio.ObjectWriteResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MinioRespond {

    String originName;

    String fileName;

    ObjectWriteResponse objectWriteResponse;
}
