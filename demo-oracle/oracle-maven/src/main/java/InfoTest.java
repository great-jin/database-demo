import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;
import java.util.*;

public class InfoTest {
    final String CLASSNAME = "oracle.jdbc.OracleDriver";
    final String JDBC = "jdbc:oracle:thin:@10.231.6.65:1521:helowin";
    final String USERNAME = "budai";
    final String PASSWORD = "123456";

    @Before
    public void Init() {
        try {
            Class.forName(CLASSNAME);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取所有表名
     */
    @Test
    public void TableDemo() {
        List<Map<String, String>> tableDTO = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(JDBC, USERNAME, PASSWORD)) {
            try {
                DatabaseMetaData databaseMetadata = con.getMetaData();
                String schema = "ROOT";
                String[] queryType = new String[]{"TABLE", "VIEW"};
                ResultSet rs = databaseMetadata.getTables(null, schema, null, queryType);

                while (rs.next()) {
                    Map<String, String> tableVo = new HashMap<>();
                    String tableSchema = StrUtil.isBlank(rs.getString("TABLE_SCHEM")) ? schema : rs.getString("TABLE_SCHEM");
                    tableVo.put("TableSchema", tableSchema);
                    tableVo.put("TableName", rs.getString("TABLE_NAME"));
                    tableVo.put("TableType", rs.getString("TABLE_TYPE"));
                    //                    tableVo.put("TableCatalog", rs.getString("TABLE_CAT"));
                    //                    tableVo.put("Remarks", rs.getString("REMARKS"));
                    tableDTO.add(tableVo);
                }
            } catch (Exception e) {
                /*if (!dbType.getDriver().toLowerCase(Locale.ROOT).contains("oracle")) {
                    throw e;
                }
                tableVos.clear();
                PreparedStatement prepareStatement = connection.prepareStatement(
                        "SELECT NULL AS table_cat,\n" +
                                "                o.owner AS table_schem,\n" +
                                "                        o.object_name AS table_name,\n" +
                                "                o.object_type AS table_type,\n" +
                                "                        NULL AS remarks\n" +
                                "                FROM all_objects o\n" +
                                "                WHERE o.owner LIKE ? ESCAPE '/'\n" +
                                "                AND o.object_type IN ('xxx', 'TABLE', 'VIEW')\n" +
                                "                ORDER BY table_type, table_schem, table_name"
                );
                prepareStatement.setString(1, schema);
                ResultSet rs = prepareStatement.executeQuery();
                while (rs.next()) {
                    TableVo tableVo = new TableVo();
                    tableVo.setTableCatalog(rs.getString("TABLE_CAT"));

                    String tableSchema = StringUtils.isBlank(rs.getString("TABLE_SCHEM")) ? schema
                            : rs.getString("TABLE_SCHEM");
                    tableVo.setTableSchema(tableSchema);
                    tableVo.setTableName(rs.getString("TABLE_NAME"));
                    tableVo.setTableType(rs.getString("TABLE_TYPE"));
                    tableVo.setRemarks(rs.getString("REMARKS"));
                    tableVos.add(tableVo);
                }*/
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println(tableDTO);
    }

    /**
     * 获取 Schema 名称
     */
    @Test
    public void SchemaDemo() {
        List<String> schema = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(JDBC, USERNAME, PASSWORD)) {
            try {
                DatabaseMetaData databaseMetadata = connection.getMetaData();
                ResultSet rs = databaseMetadata.getSchemas();
                while (rs.next()) {
                    schema.add(rs.getString(1));
                }
            } catch (Exception e) {
                schema.clear();
                PreparedStatement statement = connection.prepareStatement(
                        "select username as table_schem from " +
                                "all_users order by table_schem");
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    schema.add(rs.getString(1));
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println(schema);
    }

    /**
     * 获取所有 Schema
     */
    @Test
    public void Schema1Demo() {
        List<String> schema = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(JDBC, USERNAME, PASSWORD)) {
            PreparedStatement statement = connection.prepareStatement("select username as table_schem from all_users order by table_schem");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                schema.add(rs.getString(1));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println(schema);
    }
}
