package xyz.ibudai;

import org.junit.Test;
import xyz.ibudai.config.HiveConfig;
import xyz.ibudai.util.HiveUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsTest {

    private final String databaseName = "a_db";
    private final String tableName = "tb_test3";

    /**
     * 执行 Hive 分析命令
     */
    @Test
    public void analyzeDemo() {
        String analyzeSql = "";
        try (
                Connection conn = HiveConfig.getHiveConnection();
                Statement stmt = conn.createStatement()
        ) {
            analyzeSql = HiveUtils.getStatisticsSQL(conn, databaseName, tableName);
            stmt.execute(analyzeSql);
        } catch (Exception e) {
            System.out.println("执行 Hive 分析命令失败, 命令：【" + analyzeSql + "】.");
            e.printStackTrace();
        }
    }

    /**
     * 查询 Hive 统计信息
     */
    @Test
    public void descDemo() {
        try (
                Connection conn = HiveConfig.getHiveConnection();
                Statement stmt = conn.createStatement()
        ) {
            String createSQL = HiveUtils.getCreateDDLSQL(conn, databaseName, tableName);
            List<String> partitionList = HiveUtils.getPartitionField(createSQL);
            List<String> partitionValues = new ArrayList<>();
            if (!partitionList.isEmpty()) {
                String partitionSQL = "show partitions " + databaseName + "." + tableName;
                stmt.execute(partitionSQL);
                ResultSet rs = stmt.getResultSet();
                while (rs.next()) {
                    partitionValues.add(rs.getString(1));
                }
                for (int i = 0; i < partitionValues.size(); i++) {
                    partitionValues.set(i, partitionValues.get(i).replaceAll("/", ","));
                }
            }

            int count = 0;
            int totalSize = 0;
            if (partitionValues.size() > 0) {
                for (String it : partitionValues) {
                    String descSQL = "desc formatted " + databaseName + "." + tableName + " partition(" + it + ")";
                    stmt.execute(descSQL);
                    ResultSet rs = stmt.getResultSet();
                    Map<String, Integer> infoMap = HiveUtils.getCountInfo(rs);
                    count += infoMap.get("numRows");
                    totalSize += infoMap.get("totalSize");
                }
            } else {
                String descSQL = "desc formatted " + databaseName + "." + tableName;
                stmt.execute(descSQL);
                ResultSet rs = stmt.getResultSet();
                Map<String, Integer> infoMap = HiveUtils.getCountInfo(rs);
                count += infoMap.get("numRows");
                totalSize += infoMap.get("totalSize");
            }
            System.out.println("Count: " + count);
            System.out.println("TotalSize: " + totalSize);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
