package xyz.ibudai;

import org.junit.Test;
import xyz.ibudai.config.HiveConfig;
import xyz.ibudai.util.HiveUtils;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class StatisticsTest {

    private final String databaseName = "a_db";
    private final String tableName = "tb_test";

    /**
     * 执行 Hive 分析命令
     */
    @Test
    public void demo2() {
        String analyzeSql = "";
        try (
                Connection conn = HiveConfig.getHiveConnection();
                Statement stmt = conn.createStatement()
        ) {
            analyzeSql = HiveUtils.getStatisticsSQL(conn, databaseName, tableName);
            stmt.execute(analyzeSql);
        } catch (Exception e) {
            System.out.println("执行 Hive 分析命令失败, 命令：【" + analyzeSql + "】.");
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询 Hive 表信息
     */
    @Test
    public void demo3() {
        Map<String, Object> partitions = new HashMap<>();

        int num = 0;
        String sql = HiveUtils.getDescribeSQL(databaseName, tableName, partitions);
        try (
                Connection conn = HiveConfig.getHiveConnection();
                Statement stmt = conn.createStatement()
        ) {
            stmt.execute(sql);
            ResultSet rs = stmt.getResultSet();
            while (rs.next()) {
                String dataType = rs.getString("data_type");
                if (dataType != null && dataType.replaceAll(" ", "").equals("numRows")) {
                    String countStr = rs.getString("comment");
                    countStr = countStr.replaceAll(" ", "");
                    num = Integer.parseInt(countStr);
                }
            }
        } catch (SQLException e) {
            System.out.println("执行 Hive 分区表分析命令异常, 命令：【" + sql + "】");
            throw new RuntimeException(e);
        }
        System.out.println(num);
    }
}
