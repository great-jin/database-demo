package xyz.ibudai;

import org.junit.Test;
import xyz.ibudai.config.BasicPoolUtil;
import xyz.ibudai.util.MetaUtils;

import java.sql.Connection;
import java.sql.SQLException;
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
            List<Map<String, String>> meta1 = MetaUtils.getNonePartitionTableNum(conn, schema, table);
            System.out.println("【" + table + "】 num: " + meta1);

            List<Map<String, String>> tableDetails = MetaUtils.getPartitionTableDetails(conn, schema, partitionTable);
            Map<String, Object> partitionInfo = MetaUtils.calculatePartitionInfo(tableDetails);
            Map<String, Integer> tableNum = MetaUtils.calculateTableNum(partitionInfo);
            System.out.println("【" + partitionTable + "】 num: " + tableNum);
            System.out.println("【" + partitionTable + "】 partition: " + partitionInfo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
