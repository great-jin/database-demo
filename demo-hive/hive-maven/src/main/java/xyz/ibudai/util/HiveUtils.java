package xyz.ibudai.util;

import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class HiveUtils {

    /**
     * 获取 Hive 建表语句
     */
    public static String getCreateSQL(Connection conn, String databaseName, String tableName) {
        StringBuilder result = new StringBuilder();
        String createSQL = "show create table " + databaseName + "." + tableName;
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createSQL);
            ResultSet rs = stmt.getResultSet();
            while (rs.next()) {
                result.append(rs.getString("createtab_stmt"));
            }
        } catch (Exception e) {
            System.out.println("获取建表语句失败.");
            throw new RuntimeException(e);
        }
        return result.toString();
    }

    /**
     * 解析分区字段
     */
    public static List<String> getPartitionFiled(String sql) {
        List<String> partitionFields = new ArrayList<>();
        if (sql.contains("PARTITIONED BY")) {
            String result = sql.substring(sql.indexOf("PARTITIONED BY"), sql.indexOf("ROW FORMAT SERDE"));
            result = result.substring(result.indexOf("(") + 1, result.indexOf(")"));
            result = result.replaceAll(" ", "");
            String[] array = StringUtils.split(result, ",");

            for (String it : array) {
                int start = StringUtils.ordinalIndexOf(it, "`", 1) + 1;
                int end = StringUtils.ordinalIndexOf(it, "`", 2);
                partitionFields.add(it.substring(start, end));
            }
        }
        return partitionFields;
    }
}
