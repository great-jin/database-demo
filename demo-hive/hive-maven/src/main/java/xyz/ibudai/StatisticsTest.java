package xyz.ibudai;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import xyz.ibudai.config.HiveConfig;
import xyz.ibudai.util.HiveUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

public class StatisticsTest {

    private final String schema = "a_db";
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
            analyzeSql = HiveUtils.getStatisticsSQL(conn, schema, tableName);
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
            String createSQL = HiveUtils.getCreateDDLSQL(conn, schema, tableName);
            List<String> partitionList = HiveUtils.getPartitionField(createSQL);
            List<String> partitionValues = new ArrayList<>();
            if (!partitionList.isEmpty()) {
                String partitionSQL = "show partitions " + schema + "." + tableName;
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
                    String descSQL = "desc formatted " + schema + "." + tableName + " partition(" + it + ")";
                    stmt.execute(descSQL);
                    ResultSet rs = stmt.getResultSet();
                    Map<String, Integer> infoMap = HiveUtils.getCountInfo(rs);
                    count += infoMap.get("numRows");
                    totalSize += infoMap.get("totalSize");
                }
            } else {
                String descSQL = "desc formatted " + schema + "." + tableName;
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

    @Test
    public void demo3() {
        List<String> tables = new ArrayList<>();
        tables.add("tb_test");
        tables.add("tb_test3");
        List<Map<String, Object>> before = getTableDetail(schema, tables);
        System.out.println(before);
    }

    @Test
    public void demo4() {
        List<String> tables = new ArrayList<>();
        tables.add("tb_test");
        tables.add("tb_test3");
        List<Map<String, Object>> before = getTableDetail(schema, tables);
        List<Map<String, Object>> change = getChangeTable(before, before);
        System.out.println(change);
    }

    public List<Map<String, Object>> getTableDetail(String schema, List<String> tables) {
        List<Map<String, Object>> infoMapList = new ArrayList<>();
        try (
                Connection conn = HiveConfig.getHiveConnection();
                Statement stmt = conn.createStatement()
        ) {
            for (String tb : tables) {
                Map<String, Object> infoMap = new HashMap<>();
                infoMap.put("schema", schema);
                infoMap.put("tableName", tb);

                String createSQL = HiveUtils.getCreateDDLSQL(conn, schema, tb);
                List<String> partitionFields = HiveUtils.getPartitionField(createSQL);
                if (partitionFields.isEmpty()) {
                    infoMap.put("isPartition", false);
                    String noPartitonSQL = "desc formatted " + schema + "." + tb;
                    stmt.execute(noPartitonSQL);
                    Map<String, Integer> map = HiveUtils.getCountInfo(stmt.getResultSet());
                    infoMap.put("totalSize", map.get("totalSize"));
                } else {
                    infoMap.put("isPartition", true);
                    String sql1 = "show partitions " + schema + "." + tb;
                    List<List<String>> pValueList = new ArrayList<>();
                    stmt.execute(sql1);
                    ResultSet rs = stmt.getResultSet();
                    while (rs.next()) {
                        String[] par = rs.getString(1).split("/");
                        pValueList.add(Arrays.asList(par));
                    }

                    List<Map<String, Object>> list = new ArrayList<>();
                    for (List<String> it : pValueList) {
                        Map<String, Object> map = new HashMap<>();
                        String partitionValues = StringUtils.join(it, ",");
                        String partitonSQL = "desc formatted " + schema + "." + tb +
                                " partition(" + partitionValues + ")";
                        stmt.execute(partitonSQL);
                        Map<String, Integer> countMap = HiveUtils.getCountInfo(stmt.getResultSet());
                        map.put("partition", partitionValues);
                        map.put("totalSize", countMap.get("totalSize"));
                        list.add(map);
                    }
                    infoMap.put("totalSize", list);
                }
                infoMapList.add(infoMap);
            }
            return infoMapList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Map<String, Object>> getChangeTable(List<Map<String, Object>> before, List<Map<String, Object>> after) {
        List<Map<String, Object>> list = new ArrayList<>();
        after.forEach(af -> {
            String afterTableName = (String) af.get("tableName");
            boolean isPartition = (boolean) af.get("isPartition");
            before.forEach(bf -> {
                String beforeTableName = (String) bf.get("tableName");
                if (beforeTableName.equals(afterTableName)) {
                    if (!isPartition) {
                        int beforeSize = (Integer) bf.get("totalSize");
                        int afterSize = (Integer) af.get("totalSize");
                        if (afterSize - beforeSize > 1000) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("tableName", afterTableName);
                            map.put("partition", "");
                            list.add(map);
                        }
                    } else {
                        List<Map<String, Object>> afterMap = (List<Map<String, Object>>) af.get("totalSize");
                        List<Map<String, Object>> beforeMap = (List<Map<String, Object>>) af.get("totalSize");
                        afterMap.forEach(am -> {
                            String afterPartition = (String) am.get("partition");
                            beforeMap.forEach(bm -> {
                                String beforePartition = (String) am.get("partition");
                                if (afterPartition.equals(beforePartition)) {
                                    int afterSize = (Integer) am.get("totalSize");
                                    int beforeSize = (Integer) bm.get("totalSize");
                                    if (afterSize - beforeSize > 1000) {
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("tableName", afterTableName);
                                        map.put("partition", afterPartition);
                                        list.add(map);
                                    }
                                }
                            });
                        });
                    }
                }
            });
        });
        return list;
    }
}
