package xyz.ibudai.mysql;

import org.junit.Test;
import xyz.ibudai.common.DbType;
import xyz.ibudai.config.ConnectionUtils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MetaDataTest {

    @Test
    public void SMDemo() {
        List<Map<String, String>> infoMap = new ArrayList<>();

        try (Connection conn = ConnectionUtils.getConnection(DbType.MYSQL)) {
            DatabaseMetaData dbMetaData = conn.getMetaData();
            ResultSet rs = dbMetaData.getTables(null, null, null, new String[]{"TABLE"});
            while (rs.next()) {
                Map<String, String> table = new HashMap<>();
                table.put("Table", rs.getString("TABLE_NAME"));
                table.put("Catalog", rs.getString("TABLE_CAT"));
                table.put("Type", rs.getString("TABLE_TYPE"));
                table.put("Remark", rs.getString("REMARKS"));
                infoMap.add(table);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(infoMap);
    }
}
