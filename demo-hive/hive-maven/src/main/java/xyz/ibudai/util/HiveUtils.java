package xyz.ibudai.util;

import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HiveUtils {

    /**
     * 获取 Hive 所有库
     */
    public static List<String> getAllSchema(Connection conn) {
        String sql = "show databases";
        List<String> schemas = new ArrayList<>();
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            ResultSet rs = stmt.getResultSet();
            while (rs.next()) {
                schemas.add(rs.getString("database_name"));
            }
        } catch (Exception e) {
            System.out.println("获取 Hive 库下所有 Schema 失败。");
            throw new RuntimeException(e);
        }
        return schemas;
    }

    /**
     * 获取 Hive 指定库下所有表
     */
    public static List<String> getTablesBySchema(Connection conn, String schema) {
        String checkSQL = "use " + schema;
        String querySQL = "show tables";

        List<String> tableName = new ArrayList<>();
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(checkSQL);
            stmt.execute(querySQL);
            ResultSet rs = stmt.getResultSet();
            while (rs.next()) {
                tableName.add(rs.getString("tab_name"));
            }
        } catch (Exception e) {
            System.out.println("获取 Hive 库【" + schema + "】下所有表失败。");
            throw new RuntimeException(e);
        }
        return tableName;
    }

    /**
     * 解析 Hive 表分区字段
     */
    public static List<String> getPartitionField(String sql) {
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

    /**
     * 获取 Hive 建表语句
     */
    public static String getCreateDDLSQL(Connection conn, String databaseName, String tableName) {
        String createSQL = "show create table " + databaseName + "." + tableName;

        StringBuilder builder = new StringBuilder();
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createSQL);
            ResultSet rs = stmt.getResultSet();
            while (rs.next()) {
                builder.append(rs.getString("createtab_stmt"));
            }
        } catch (Exception e) {
            System.out.println("获取 Hive 建表语句失败.");
            throw new RuntimeException(e);
        }
        return builder.toString();
    }

    /**
     * 构造 Hive 表统计分析语句
     */
    public static String getStatisticsSQL(Connection conn, String databaseName, String tableName) {
        StringBuilder builder = new StringBuilder("analyze table ");
        builder.append(databaseName).append(".").append(tableName);

        String createSQL = getCreateDDLSQL(conn, databaseName, tableName);
        List<String> partitionList = getPartitionField(createSQL);
        boolean isPartition = partitionList.size() > 0;
        if (isPartition) {
            StringBuilder partitions = new StringBuilder();
            for (String s : partitionList) {
                partitions.append(s).append(",");
            }
            partitions.deleteCharAt(partitions.length() - 1);
            builder.append(" partition(").append(partitions).append(")");
        }
        builder.append(" compute statistics");
        return builder.toString();
    }

    /**
     * 构造 Hive 表行数查询语句
     */
    public static String getDescribeSQL(String databaseName, String tableName, Map<String, Object> partitions) {
        StringBuilder builder = new StringBuilder("desc formatted ");
        builder.append(databaseName).append(".").append(tableName);

        if (!partitions.isEmpty()) {
            builder.append(" partition(");
            for (String name : partitions.keySet()) {
                Object value = partitions.get(name);
                builder.append(name).append("='").append(value).append("',");
            }
            builder.deleteCharAt(builder.length() - 1);
            builder.append(")");
        }
        return builder.toString();
    }

    /**
     * 获取详细统计信息
     */
    public static Map<String, Integer> getCountInfo(ResultSet rs) throws SQLException {
        Map<String, Integer> infoMap = new HashMap<>();
        while (rs.next()) {
            String dataType = rs.getString("data_type");
            if (dataType == null) {
                continue;
            }
            dataType = dataType.replaceAll(" ", "");
            String countStr;
            switch (dataType) {
                case "numFiles":
                    countStr = rs.getString("comment");
                    countStr = countStr.replaceAll(" ", "");
                    infoMap.put("numFiles", Integer.parseInt(countStr));
                case "numRows":
                    countStr = rs.getString("comment");
                    countStr = countStr.replaceAll(" ", "");
                    infoMap.put("numRows", Integer.parseInt(countStr));
                    break;
                case "totalSize":
                    countStr = rs.getString("comment");
                    countStr = countStr.replaceAll(" ", "");
                    infoMap.put("totalSize", Integer.parseInt(countStr));
            }
        }
        return infoMap;
    }
}
