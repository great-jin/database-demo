package xyz.ibudai.metadata;

import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Test;
import xyz.ibudai.consts.DbType;
import xyz.ibudai.config.BasicPool;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type My sql test.
 */
public class MySQLTest {

    private final String schemaName = "test_db";

    private final String[] queryType = new String[]{"TABLE"};

    /**
     * Table info demo.
     */
    @Test
    public void tableInfoDemo() {
        BasicDataSource dataSource = BasicPool.buildDatasource(DbType.MYSQL);
        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData dbMetaData = conn.getMetaData();
            ResultSet rs = dbMetaData.getTables(schemaName, null, null, queryType);

            List<Map<String, String>> infoMap = new ArrayList<>();
            while (rs.next()) {
                Map<String, String> table = new HashMap<>();
                table.put("Name", rs.getString("TABLE_NAME"));
                table.put("Catalog", rs.getString("TABLE_CAT"));
                table.put("Type", rs.getString("TABLE_TYPE"));
                table.put("Remark", rs.getString("REMARKS"));
                infoMap.add(table);
            }
            System.out.println(infoMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
