package xyz.ibudai.database.hive;

import org.apache.hadoop.hive.metastore.HiveMetaStoreClient;
import org.apache.hadoop.hive.metastore.api.Database;
import org.apache.hadoop.hive.metastore.api.FieldSchema;
import org.apache.hadoop.hive.metastore.api.Partition;
import org.apache.hadoop.hive.metastore.api.Table;
import org.junit.Before;
import org.junit.Test;
import xyz.ibudai.database.hive.config.HiveConfig;

import java.util.ArrayList;
import java.util.List;

public class MetaTest {

    private final String schema = "a_db";
    private final String tableName = "tb_test3";

    private HiveMetaStoreClient client;

    @Before
    public void init() throws Exception {
        client = HiveConfig.getHiveClient();
    }

    @Test
    public void demo1() throws Exception {
        // 获取数据库信息
        Database database = client.getDatabase(schema);
        System.out.println("Database: " + database);

        // 获取获取所有表
        List<String> tablesList = client.getAllTables(schema);
        System.out.println("Tables: " + tablesList);

        // 获取表信息
        Table table = client.getTable(schema, tableName);
        System.out.println("Parameters: " + table.getParameters());

        // 获取表字段
        List<FieldSchema> fieldList = table.getSd().getCols();
        System.out.println("Field: " + fieldList);

        // 获取表分区字段
        List<String> partition = new ArrayList<>();
        if (table.getParametersSize() > 0) {
            List<FieldSchema> partitions = table.getPartitionKeys();
            System.out.println("Partitions: " + partitions);
            partitions.forEach(it -> {
                partition.add(it.getName());
            });
        }
    }

    /**
     * 获取分区数量
     */
    @Test
    public void demo2() throws Exception {
        // 统计所有分区数据
        List<Partition> result = client.listPartitions(schema, tableName, (short) -1);
        System.out.println("All partition: " + result);

        // 统计指定分区数据
        List<String> partition = new ArrayList<>();
        partition.add("year=2022/month=11");
        List<Partition> result1 = client.getPartitionsByNames(schema, tableName, partition);
        System.out.println("Specify partition: " + result1);
    }
}
