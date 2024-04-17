package xyz.ibudai.database.hive.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MetaUtils {

    public static boolean isPartitionTable(Connection conn, String schema, String tableName) {
        boolean isPartition = true;
        String sql = "select p.PARAM_KEY from TABLE_PARAMS p " +
                "left join TBLS t " +
                "on p.TBL_ID = t.TBL_ID " +
                "left join DBS d " +
                "on d.DB_ID = t.DB_ID " +
                "where d.NAME = '" + schema + "' " +
                "and t.TBL_NAME ='" + tableName + "'";
        try (
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)
        ) {
            while (rs.next()) {
                if (rs.getString(1).equalsIgnoreCase("numRows")) {
                    isPartition = false;
                    break;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return isPartition;
    }

    public static Map<String, Integer> getNonePartitionTableNum(Connection conn, String schema, String tableName) {
        Map<String, Integer> numMap = new HashMap<>();
        String sql = "select SUM(case when p.PARAM_KEY = 'numRows' then p.PARAM_VALUE else 0 end) as numRows," +
                "SUM(case when p.PARAM_KEY = 'totalSize' then p.PARAM_VALUE else 0 end) as totalSize " +
                "from TABLE_PARAMS p " +
                "left join TBLS t " +
                "on p.TBL_ID = t.TBL_ID " +
                "left join DBS d " +
                "on t.DB_ID = d.DB_ID " +
                "where d.NAME = '" + schema + "' " +
                "and t.TBL_NAME = '" + tableName + "'";
        try (
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)
        ) {
            while (rs.next()) {
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    numMap.put(rs.getMetaData().getColumnName(i), rs.getInt(i));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return numMap;
    }

    public static List<Map<String, String>> getPartitionTableDetails(Connection conn, String schema, String tableName) {
        List<Map<String, String>> rowsList = new ArrayList<>();
        String sql = "select p.PART_NAME, v.PARAM_KEY, v.PARAM_VALUE " +
                "from TBLS t " +
                "left join DBS d " +
                "on t.DB_ID = d.DB_ID " +
                "left join PARTITIONS p " +
                "on t.TBL_ID = p.TBL_ID " +
                "left join PARTITION_PARAMS v " +
                "on p.PART_ID = v.PART_ID " +
                "where d.NAME = '" + schema + "' " +
                "and t.TBL_NAME = '" + tableName + "'";
        try (
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)
        ) {
            while (rs.next()) {
                Map<String, String> row = new HashMap<>();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    row.put(rs.getMetaData().getColumnName(i), rs.getString(i));
                }
                rowsList.add(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rowsList;
    }

    /**
     * 计算每个分区下文件大小
     *
     * @return
     */
    public static Map<String, Object> calculatePartitionInfo(List<Map<String, String>> tableDetails) {
        Map<String, Object> metaInfoMap = new HashMap<>();
        List<String> partitions = tableDetails.stream()
                .map(it -> it.get("PART_NAME"))
                .distinct()
                .collect(Collectors.toList());
        partitions.forEach(it -> {
            List<Map<String, String>> list = tableDetails.stream()
                    .filter(t -> t.get("PART_NAME").equals(it))
                    .collect(Collectors.toList());
            Map<String, Integer> map = new HashMap<>();
            String strRows = list.stream().filter(l -> l.get("PARAM_KEY").equals("numRows")).collect(Collectors.toList()).get(0).get("PARAM_VALUE");
            String strSize = list.stream().filter(l -> l.get("PARAM_KEY").equals("totalSize")).collect(Collectors.toList()).get(0).get("PARAM_VALUE");
            map.put("numRows", Integer.parseInt(strRows));
            map.put("totalSize", Integer.parseInt(strSize));
            metaInfoMap.put(it, map);
        });
        return metaInfoMap;
    }

    /**
     * 所有分区数据合计
     *
     * @return
     */
    public static Map<String, Integer> calculateTableNum(Map<String, Object> partitionInfo) {
        Map<String, Integer> metaNumMap = new HashMap<>();
        AtomicInteger allRowNum = new AtomicInteger();
        AtomicInteger allTotalSize = new AtomicInteger();
        partitionInfo.keySet().forEach(it -> {
            allRowNum.addAndGet(((Map<String, Integer>) partitionInfo.get(it)).get("numRows"));
            allTotalSize.addAndGet(((Map<String, Integer>) partitionInfo.get(it)).get("totalSize"));
        });
        metaNumMap.put("numRows", allRowNum.get());
        metaNumMap.put("totalSize", allTotalSize.get());
        return metaNumMap;
    }

    public static List<Map<String, Object>> getChangeTableAndPartition(List<Map<String, Object>> beforeTableList,
                                                                       List<Map<String, Object>> afterTableList,
                                                                       int threshold) {
        Map<String, Object> beforeTbMap = beforeTableList.stream()
                .collect(Collectors.toMap(e -> (String) e.get("tableName"), e -> e));
        List<Map<String, Object>> changeTbList = new ArrayList<>();
        afterTableList.forEach(af -> {
            String afterTbName = (String) af.get("tableName");
            if (beforeTbMap.get(afterTbName) != null) {
                Map<String, Object> beforeTbInfo = (Map<String, Object>) beforeTbMap.get(afterTbName);
                boolean isPartition = (boolean) af.get("isPartition");
                if (!isPartition) {
                    int afterSize = (Integer) af.get("totalSize");
                    int beforeSize = (Integer) beforeTbInfo.get("totalSize");
                    if (Math.abs(afterSize - beforeSize) > threshold) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("tableName", afterTbName);
                        map.put("partition", "");
                        changeTbList.add(map);
                    }
                } else {
                    Map<String, Object> afterPartitionMap = (Map<String, Object>) af.get("partition");
                    Map<String, Object> beforePartitionMap = (Map<String, Object>) beforeTbInfo.get("partition");
                    afterPartitionMap.keySet().forEach(it -> {
                        Map<String, Integer> afterNUm = (Map<String, Integer>) afterPartitionMap.get(it);
                        Map<String, Integer> beforeNum = (Map<String, Integer>) beforePartitionMap.get(it);
                        if (Math.abs(afterNUm.get("totalSize") - beforeNum.get("totalSize")) > threshold) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("tableName", afterTbName);
                            map.put("partition", it.replace("/", ","));
                            changeTbList.add(map);
                        }
                    });
                }
            } else {
                Map<String, Object> map = new HashMap<>();
                map.put("tableName", afterTbName);
                map.put("partition", "");
                changeTbList.add(map);
            }
        });
        return changeTbList;
    }
}
