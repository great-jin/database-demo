package xyz.ibudai.database.hive;

import org.junit.Test;
import xyz.ibudai.database.hive.config.BasicPoolUtil;
import xyz.ibudai.database.hive.util.MetaUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MySQLMeta {

    private final String owner = "hive";
    private final String schema = "a_db";
    private final String table = "tb_test";
    private final String partitionTable = "tb_test3";

    @Test
    public void demo1() {
        try (Connection conn = BasicPoolUtil.buildDatasource().getConnection()) {
            boolean isPartition1 = MetaUtils.isPartitionTable(conn, schema, table);
            System.out.println("【" + table + "】 is partition? " + isPartition1);

            boolean isPartition2 = MetaUtils.isPartitionTable(conn, schema, partitionTable);
            System.out.println("【" + partitionTable + "】 is partition? " + isPartition2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void demo2() {
        try (Connection conn = BasicPoolUtil.buildDatasource().getConnection()) {
            Map<String, Integer> tableNum1 = MetaUtils.getNonePartitionTableNum(conn, schema, table);
            System.out.println("【" + table + "】 num: " + tableNum1);
            System.out.println();

            List<Map<String, String>> tableDetails = MetaUtils.getPartitionTableDetails(conn, schema, partitionTable);
            Map<String, Object> partitionInfo = MetaUtils.calculatePartitionInfo(tableDetails);
            System.out.println("【" + partitionTable + "】 partition: " + partitionInfo);

            Map<String, Integer> tableNum2 = MetaUtils.calculateTableNum(partitionInfo);
            System.out.println("【" + partitionTable + "】 num: " + tableNum2);
            System.out.println();

            List<Map<String, Object>> tableInfoList1 = new ArrayList<>();
            Map<String, Object> tableInfo1 = new HashMap<>();
            tableInfo1.put("tableName", tableInfo1);
            tableInfo1.put("totalSize", tableNum2.get("totalSize"));
            tableInfo1.put("isPartition", true);
            tableInfo1.put("partition", partitionInfo);
            tableInfoList1.add(tableInfo1);

            List<Map<String, Object>> tableInfoList2 = new ArrayList<>();
            Map<String, Object> tableInfo2 = new HashMap<>();
            tableInfo2.put("tableName", partitionTable);
            tableInfo2.put("totalSize", tableNum2.get("totalSize"));
            tableInfo2.put("isPartition", true);
            Map<String, Object> map1 = new HashMap<>();
            Map<String, Integer> m1 = new HashMap<>();
            m1.put("numRows", 15);
            m1.put("totalSize", 468);
            map1.put("year=2023/month=11", m1);
            Map<String, Integer> m2 = new HashMap<>();
            m2.put("numRows", 2);
            m2.put("totalSize", 140);
            map1.put("year=2022/month=12", m2);
            map1.put("year=2022/month=11", m2);
            tableInfo2.put("partition", map1);
            tableInfoList2.add(tableInfo2);

            List<Map<String, Object>> changeList = MetaUtils.getChangeTableAndPartition(tableInfoList1, tableInfoList2, 0);
            System.out.println("Change table: " + changeList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
