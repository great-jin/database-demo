package xyz.ibudai.database.hbase.service;

import java.io.IOException;
import java.util.Map;

public interface DMLService {

    /**
     * 创建 HBase 表
     */
    void createTable(String name, String colFamily) throws IOException;

    /**
     * 添加数据
     */
    void putData(String name, String colFamily, String rowKey, Map<String, String> data) throws IOException;

    /**
     * 获取数据
     */
    void getData(String name) throws IOException;

    /**
     * 查询数据
     */
    void scanData(String name) throws IOException;
}
