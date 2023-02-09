package xyz.ibudai;

import org.junit.Test;
import xyz.ibudai.config.BasicPoolUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MySQLMeta {

    private final String owner = "hive";
    private final String schema = "a_db";
    private final String table = "tb_test";
    private final String partitionTable = "tb_test3";

    @Test
    public void demo1() {
        try (
                Connection conn = BasicPoolUtil.buildDatasource().getConnection();
                Statement stmt = conn.createStatement()
        ) {
            String sql = "select d.NAME, t.TBL_NAME," +
                    "SUM(case when p.param_key = 'numFiles' then p.param_value else 0 end) as numFiles," +
                    "SUM(case when p.param_key = 'numRows' then p.param_value else 0 end) as numRows," +
                    "SUM(case when p.param_key = 'totalSize' then p.param_value else 0 end) as totalSize " +
                    "from TABLE_PARAMS p " +
                    "left join TBLS t " +
                    "on p.TBL_ID = t.TBL_ID " +
                    "left join DBS d " +
                    "on t.DB_ID = d.DB_ID " +
                    "where d.OWNER_NAME = '" + owner + "' " +
                    "and d.NAME = '" + schema + "' " +
                    "and t.TBL_NAME = '" + table + "'";
            stmt.execute(sql);
            try (ResultSet rs = stmt.getResultSet()) {
                List<Map<String, String>> rowsList = new ArrayList<>();
                while (rs.next()) {
                    Map<String, String> row = new HashMap<>();
                    for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                        row.put(rs.getMetaData().getColumnName(i), rs.getString(i));
                    }
                    rowsList.add(row);
                }
                System.out.println(rowsList);
            } catch (SQLException ignored) {
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void demo2() {
        try (
                Connection conn = BasicPoolUtil.buildDatasource().getConnection();
                Statement stmt = conn.createStatement()
        ) {
            String sql = "select p.PART_NAME, v.PARAM_KEY, v.PARAM_VALUE " +
                    "from TBLS t " +
                    "left join DBS d " +
                    "on t.DB_ID = d.DB_ID " +
                    "left join PARTITIONS p " +
                    "on t.TBL_ID = p.TBL_ID " +
                    "left join PARTITION_PARAMS v " +
                    "on p.PART_ID = v.PART_ID " +
                    "where d.OWNER_NAME = '" + owner + "' " +
                    "and d.NAME = '" + schema + "' " +
                    "and t.TBL_NAME = '" + partitionTable + "'";
            try (ResultSet rs = stmt.executeQuery(sql)) {
                List<Map<String, String>> rowsList = new ArrayList<>();
                while (rs.next()) {
                    Map<String, String> row = new HashMap<>();
                    for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                        row.put(rs.getMetaData().getColumnName(i), rs.getString(i));
                    }
                    rowsList.add(row);
                }
                Map<String, Object> partitionInfo = buildPartitionInfo(rowsList);
                Map<String, Integer> metaNumMap = buildTableMeta(partitionInfo);
                System.out.println(metaNumMap);
                System.out.println(partitionInfo);
            } catch (SQLException ignored) {
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Object> buildPartitionInfo(List<Map<String, String>> rowsList) {
        Map<String, Object> metaInfoMap = new HashMap<>();
        List<String> partitions = rowsList.stream()
                .map(it -> it.get("PART_NAME"))
                .distinct()
                .collect(Collectors.toList());
        partitions.forEach(it -> {
            List<Map<String, String>> list = rowsList.stream()
                    .filter(t -> t.get("PART_NAME").equals(it))
                    .collect(Collectors.toList());
            Map<String, String> map = new HashMap<>();
            map.put("numFiles", list.stream().filter(l -> l.get("PARAM_KEY").equals("numFiles")).collect(Collectors.toList()).get(0).get("PARAM_VALUE"));
            map.put("numRows", list.stream().filter(l -> l.get("PARAM_KEY").equals("numRows")).collect(Collectors.toList()).get(0).get("PARAM_VALUE"));
            map.put("totalSize", list.stream().filter(l -> l.get("PARAM_KEY").equals("totalSize")).collect(Collectors.toList()).get(0).get("PARAM_VALUE"));
            metaInfoMap.put(it, map);
        });
        return metaInfoMap;
    }

    public Map<String, Integer> buildTableMeta(Map<String, Object> metaInfoMap) {
        Map<String, Integer> metaNumMap = new HashMap<>();
        AtomicInteger allRowNum = new AtomicInteger();
        AtomicInteger allTotalSize = new AtomicInteger();
        metaInfoMap.keySet().forEach(it -> {
            allRowNum.addAndGet(Integer.parseInt(((Map<String, String>) metaInfoMap.get(it)).get("numRows")));
            allTotalSize.addAndGet(Integer.parseInt(((Map<String, String>) metaInfoMap.get(it)).get("totalSize")));
        });
        metaNumMap.put("numRows", allRowNum.get());
        metaNumMap.put("totalSize", allTotalSize.get());
        return metaNumMap;
    }
}
