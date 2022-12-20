package xyz.ibudai.oracle;

import org.junit.Test;
import xyz.ibudai.common.DbType;
import xyz.ibudai.config.ConnectionUtils;

import java.sql.*;
import java.util.*;

public class MetaDataTest {

    private final String schema = "ibudai";
    private final String tableName = "TB_22091901";

    /**
     * 获取 Schema 下所有表名
     */
    @Test
    public void TableDemo() {
        String[] queryType = new String[]{"TABLE", "VIEW"};
        List<Map<String, String>> tableMap = new ArrayList<>();

        try (Connection connection = ConnectionUtils.getConnection(DbType.ORACLE)) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet rs = metaData.getTables(null, schema, null, queryType);

            while (rs.next()) {
                Map<String, String> tableVo = new HashMap<>();
                String tableSchema = rs.getString("TABLE_SCHEM").isEmpty() ?
                        schema : rs.getString("TABLE_SCHEM");
                tableVo.put("TableSchema", tableSchema);
                tableVo.put("TableName", rs.getString("TABLE_NAME"));
                tableVo.put("TableType", rs.getString("TABLE_TYPE"));
                tableVo.put("TableCatalog", rs.getString("TABLE_CAT"));
                tableVo.put("Remarks", rs.getString("REMARKS"));
                tableMap.add(tableVo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(tableMap);
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
                ResultSet primaryKeyRs = metaData.getPrimaryKeys(null, schema, tableName);
                while (primaryKeyRs.next()) {
                    String name = primaryKeyRs.getString("COLUMN_NAME");
                    if (!primaryKeyList.contains(name)) {
                        primaryKeyList.add(name);
                    }
                }

                ResultSet columnRs = metaData.getColumns(null, schema, tableName, null);
                while (columnRs.next()) {
                    Map<String, String> columnVo = new LinkedHashMap<>();
                    String columnName = columnRs.getString("COLUMN_NAME");
                    Integer isPrimaryKey = primaryKeyList.contains(columnName) ? 1 : 0;
                    columnVo.put("ColumnName", columnName);
                    columnVo.put("IsPrimaryKey", isPrimaryKey == 1 ? "yes" : "no");
                    columnVo.put("DataType", columnRs.getString("DATA_TYPE"));
                    columnVo.put("TypeName", columnRs.getString("TYPE_NAME"));
                    columnVo.put("ColumnSize", columnRs.getString("COLUMN_SIZE"));
                    columnVo.put("DecimalDigits", columnRs.getString("DECIMAL_DIGITS"));
                    columnVo.put("Nullable", columnRs.getString("NULLABLE"));
                    columnVo.put("IsNullable", columnRs.getString("IS_NULLABLE"));
                    columnVo.put("Remarks", columnRs.getString("REMARKS"));
                    columnVo.put("ColumnDef", columnRs.getString("COLUMN_DEF"));
                    columnVo.put("NumPrecRadix", columnRs.getString("NUM_PREC_RADIX"));
                    columnVo.put("CharOctetLength", columnRs.getString("CHAR_OCTET_LENGTH"));
                    columnVo.put("OrdinalPosition", String.valueOf(columnRs.getInt("ORDINAL_POSITION")));
                    tableMap.add(columnVo);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(tableMap);
    }
}
