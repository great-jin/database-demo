package xyz.ibudai;

import org.junit.Test;
import xyz.ibudai.config.HiveConnection;
import xyz.ibudai.util.HiveUtils;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsTest {

    private final String databaseName = "a_db";
    private final String tableName = "tb_test";

    /**
     * 生成表信息
     */
    @Test
    public void demo1() {
        String sql = "analyze table a_db.tb_test compute statistics";

        try (
                Connection conn = HiveConnection.getHiveConnection();
                Statement stmt = conn.createStatement();
        ) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 执行 Hive 分析命令
     */
    @Test
    public void demo2() {
        try (Connection conn = HiveConnection.getHiveConnection()) {
            String createSQl = HiveUtils.getCreateSQL(conn, databaseName, tableName);
            List<String> partitionField = HiveUtils.getPartitionFiled(createSQl);
            boolean isPartition = partitionField.size() > 0;

            StringBuilder analyzeSql = new StringBuilder("analyze table ");
            analyzeSql.append(databaseName).append(".").append(tableName);
            if (isPartition) {
                StringBuilder builder = new StringBuilder();
                for (String field : partitionField) {
                    builder.append(field).append(",");
                }
                builder.deleteCharAt(builder.length() - 1);
                analyzeSql.append(" partition(").append(builder).append(")");
            }
            analyzeSql.append(" compute statistics");

            try (Statement stmt1 = conn.createStatement()) {
                stmt1.execute(analyzeSql.toString());
            } catch (Exception e) {
                System.out.println("执行 Hive 分析命令失败.");
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            System.out.println("生成 Hive 分析命令失败.");
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询 Hive 表信息
     */
    @Test
    public void demo3() {
        StringBuilder descSQL = new StringBuilder("desc formatted ");
        Map<String, Object> partitions = new HashMap<>();

        try (Connection conn = HiveConnection.getHiveConnection()) {
            String createSQL = HiveUtils.getCreateSQL(conn, databaseName, tableName);
            descSQL.append(databaseName).append(".").append(tableName);
            if (createSQL.contains("PARTITIONED BY") && !partitions.isEmpty()) {
                descSQL.append("partition");
                for (String name : partitions.keySet()) {
                    Object value = partitions.get(name);
                    descSQL.append(name).append("='").append(value).append("',");
                }
                descSQL.deleteCharAt(descSQL.length() - 1);
                descSQL.append(")");
            }
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(descSQL.toString());
                ResultSet rs = stmt.getResultSet();
                while (rs.next()) {
                    String dataType = rs.getString("data_type");
                    if (dataType != null && dataType.replaceAll(" ", "").equals("numRows")) {
                        System.out.println(rs.getString("comment"));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
