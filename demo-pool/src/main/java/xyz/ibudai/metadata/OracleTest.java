package xyz.ibudai.metadata;

import com.mysql.cj.util.StringUtils;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Test;
import xyz.ibudai.consts.DbType;
import xyz.ibudai.config.BasicPool;
import xyz.ibudai.config.ConnUtils;

import java.sql.*;
import java.util.*;

public class OracleTest {

    private final String schemaName = "IBUDAI";
    private final String tableName = "TB_121501";
    private final String[] queryType = new String[]{"TABLE", "VIEW"};

    /**
     * 获取 Schema 名称
     */
    @Test
    public void SchemaDemo() {
        List<String> schemaList = new ArrayList<>();
        try (Connection connection = ConnUtils.getConnection(DbType.ORACLE)) {
            try {
                DatabaseMetaData metaData = connection.getMetaData();
                ResultSet rs = metaData.getSchemas();
                while (rs.next()) {
                    schemaList.add(rs.getString(1));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(schemaList);
    }

    /**
     * 获取 Schema 下所有表名
     */
    @Test
    public void TableDemo() {
        BasicDataSource dataSource = BasicPool.buildDatasource(DbType.ORACLE);
        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet rs = metaData.getTables(null, schemaName, null, queryType);

            List<Map<String, String>> tableInfoList = new ArrayList<>();
            while (rs.next()) {
                Map<String, String> tableVo = new HashMap<>();
                String schema = rs.getString("TABLE_SCHEM");
                String tableSchema = StringUtils.isNullOrEmpty(schema) ? schemaName : schema;
                tableVo.put("Schema", tableSchema);
                tableVo.put("Name", rs.getString("TABLE_NAME"));
                tableVo.put("Type", rs.getString("TABLE_TYPE"));
                tableVo.put("Catalog", rs.getString("TABLE_CAT"));
                tableVo.put("Remark", rs.getString("REMARKS"));
                tableInfoList.add(tableVo);
            }
            System.out.println(tableInfoList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取表结构字段信息
     */
    @Test
    public void columnsInfo() {
        try (Connection connection = ConnUtils.getConnection(DbType.ORACLE)) {
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

            List<Map<String, String>> tableFieldList = new ArrayList<>();
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
                tableFieldList.add(columnVo);
            }
            System.out.println(tableFieldList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
