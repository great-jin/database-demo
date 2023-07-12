package xyz.ibudai.basic;

import org.junit.Before;
import org.junit.Test;
import xyz.ibudai.config.BasicPool;
import xyz.ibudai.model.DbEntity;
import xyz.ibudai.model.common.DbType;
import xyz.ibudai.utils.LoaderUtil;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

public class MetaTest {

    private final String schemaName = "IBUDAI";
    private final String tableName = "TB_121501";
    private final String[] queryType = new String[]{"TABLE", "VIEW"};

    private DataSource dataSource;

    @Before
    public void init() {
        DbEntity dbEntity = LoaderUtil.buildDbInfo(DbType.ORACLE);
        dataSource = BasicPool.buildDatasource(dbEntity);
    }

    /**
     * Get all schemas
     */
    @Test
    public void SchemaDemo() {
        List<String> schemaList = new ArrayList<>();
        try (Connection conn = dataSource.getConnection()) {
            try {
                DatabaseMetaData metaData = conn.getMetaData();
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
     * Get schema tables
     */
    @Test
    public void TableDemo() {
        List<Map<String, String>> tableInfoList = new ArrayList<>();
        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet rs = metaData.getTables(null, schemaName, null, queryType);
            while (rs.next()) {
                Map<String, String> tableVo = new HashMap<>();
                String schema = rs.getString("TABLE_SCHEM");
                boolean isEmpty = Objects.isNull(schema) || Objects.equals(schema, "");
                String tableSchema = isEmpty ? schemaName : schema;
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
     * Get table column info
     */
    @Test
    public void columnsInfo() {
        try (Connection conn = dataSource.getConnection()) {
            // 获取主键列表
            List<String> primaryKeyList = new ArrayList<>();
            DatabaseMetaData metaData = conn.getMetaData();
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
                int isPrimaryKey = primaryKeyList.contains(columnName) ? 1 : 0;
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
