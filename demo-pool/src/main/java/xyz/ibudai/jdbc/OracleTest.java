package xyz.ibudai.jdbc;

import com.mysql.cj.util.StringUtils;
import oracle.jdbc.OracleDatabaseMetaData;
import org.hibernate.dialect.OracleDialect;
import org.junit.Test;
import xyz.ibudai.common.DbType;
import xyz.ibudai.config.BasicPool;
import xyz.ibudai.config.ConnectionUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

public class OracleTest {

    private final String schemaName = "IBUDAI";
    private final String tableName = "TB_121501";
    private final String[] queryType = new String[]{"TABLE", "VIEW"};

    /**
     * 获取 Schema 下所有表名
     */
    @Test
    public void TableDemo() {
        DataSource dataSource = BasicPool.buildDatasource(DbType.ORACLE);
        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet rs = metaData.getTables(null, schemaName, null, queryType);

            List<Map<String, String>> tableMap = new ArrayList<>();
            while (rs.next()) {
                Map<String, String> tableVo = new HashMap<>();
                String schema = rs.getString("TABLE_SCHEM");
                String tableSchema = StringUtils.isNullOrEmpty(schema) ? schemaName : schema;
                tableVo.put("Schema", tableSchema);
                tableVo.put("Name", rs.getString("TABLE_NAME"));
                tableVo.put("Type", rs.getString("TABLE_TYPE"));
                tableVo.put("Catalog", rs.getString("TABLE_CAT"));
                tableVo.put("Remark", rs.getString("REMARKS"));
                tableMap.add(tableVo);
            }
            System.out.println(tableMap);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取 Schema 名称
     */
    @Test
    public void SchemaDemo() {
        List<String> schemaList = new ArrayList<>();
        try (Connection connection = ConnectionUtils.getConnection(DbType.ORACLE)) {
            try {
                DatabaseMetaData metaData = connection.getMetaData();
                ResultSet rs = metaData.getSchemas();
                while (rs.next()) {
                    schemaList.add(rs.getString(1));
                }
            } catch (Exception e) {
                schemaList.clear();
                PreparedStatement statement = connection.prepareStatement(
                        "select username as table_schem from all_users order by table_schem");
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    schemaList.add(rs.getString(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(schemaList);
    }

    /**
     * 获取表结构字段信息
     */
    @Test
    public void columnsInfo() throws SQLException {
        List<Map<String, String>> tableMap = new ArrayList<>();
        try (Connection connection = ConnectionUtils.getConnection(DbType.ORACLE)) {
            try {
                // 获取主键列表
                List<String> primaryKeyList = new ArrayList<>();
                DatabaseMetaData metaData = connection.getMetaData();
                ResultSet primaryKeyRs = metaData.getPrimaryKeys(null, schemaName, tableName);
                while (primaryKeyRs.next()) {
                    String name = primaryKeyRs.getString("COLUMN_NAME");
                    if (!primaryKeyList.contains(name)) {
                        primaryKeyList.add(name);
                    }
                }

                ResultSet columnRs = metaData.getColumns(null, schemaName, tableName, null);
                while (columnRs.next()) {
                    Map<String, String> columnVo = new LinkedHashMap<>();
                    String columnName = columnRs.getString("COLUMN_NAME");
                    Integer isPrimaryKey = primaryKeyList.contains(columnName) ? 1 : 0;
                    columnVo.put("ColumnName", columnName);
                    columnVo.put("TypeName", columnRs.getString("TYPE_NAME"));
                    columnVo.put("ColumnSize", columnRs.getString("COLUMN_SIZE"));
                    columnVo.put("Nullable", columnRs.getString("NULLABLE"));
                    columnVo.put("IsPrimaryKey", isPrimaryKey == 1 ? "yes" : "no");
                    columnVo.put("NumPrecRadix", columnRs.getString("NUM_PREC_RADIX"));
                    columnVo.put("CharOctetLength", columnRs.getString("CHAR_OCTET_LENGTH"));
                    columnVo.put("Remarks", columnRs.getString("REMARKS"));
                    tableMap.add(columnVo);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(tableMap);
    }
    public static void main(String[] args) {
        String url = "jdbc:oracle:thin:@localhost:1521:orcl";
        String username = "username";
        String password = "password";
        String tableName = "employee";
        String schemaName = null;
        String[] types = { "TABLE" };
        OracleDialect dialect = new OracleDialect();

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            DatabaseMetaData metaData = new OracleDatabaseMetaData(conn);
            ResultSet rs = metaData.getTables(null, schemaName, tableName, types);
            while (rs.next()) {
                String name = rs.getString("TABLE_NAME");
                System.out.println("Table: " + name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
