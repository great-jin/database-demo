package xyz.ibudai.connector;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.apache.calcite.adapter.jdbc.JdbcSchema;
import org.apache.calcite.jdbc.CalciteConnection;
import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.SchemaPlus;
import org.junit.Test;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class CalciteTest {

    private final String schemaName = "IBUDAI";

    private final String tableName = "TB_121501";

    private final String[] queryType = new String[]{"TABLE", "VIEW"};

    @Test
    public void demo() throws SQLException {
        Properties info = new Properties();
        info.setProperty("model.driver", "oracle.jdbc.OracleDriver");
        info.setProperty("model.url", "jdbc:oracle:thin:@//10.231.6.21:1521/helowin");
        Connection connection = DriverManager.getConnection("jdbc:calcite:", info);
        CalciteConnection calciteConnection = connection.unwrap(CalciteConnection.class);
        SchemaPlus rootSchema = calciteConnection.getRootSchema();
        Map<String, Object> properties = new HashMap<>();
        properties.put("jdbcDriver", "oracle.jdbc.OracleDriver");
        properties.put("jdbcUrl", "jdbc:oracle:thin:@//10.231.6.21:1521/helowin");
        properties.put("jdbcUser", "ibudai");
        properties.put("jdbcPassword", "budai#123456");
        Schema jdbcSchema = JdbcSchema.create(rootSchema, schemaName, properties);
        System.out.println(jdbcSchema.getTableNames());
    }

    @Test
    public void demo1() throws Exception {
        Properties info = new Properties();
        info.setProperty("lex", "JAVA");
        info.setProperty("model", "inline:"
                + "{\n"
                + "  \"version\": \"1.0\",\n"
                + "  \"defaultSchema\": \"IBUDAI\",\n"
                + "  \"schemas\": [\n"
                + "    {\n"
                + "      \"name\": \"IBUDAI\",\n"
                + "      \"type\": \"jdbc\",\n"
                + "      \"jdbcDriver\": \"oracle.jdbc.OracleDriver\",\n"
                + "      \"jdbcUrl\": \"jdbc:oracle:thin:@//10.231.6.21:1521/helowin\",\n"
                + "      \"jdbcUser\": \"IBUDAI\",\n"
                + "      \"jdbcPassword\": \"budai#123456\"\n"
                + "    }\n"
                + "  ]\n"
                + "}");
        Class.forName("org.apache.calcite.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:calcite:", info);
        CalciteConnection calciteConnection = connection.unwrap(CalciteConnection.class);
        SchemaPlus rootSchema = calciteConnection.getRootSchema();
        SchemaPlus schema = rootSchema.getSubSchema("IBUDAI");
        System.out.println(schema.getTableNames());
        connection.close();
    }

    @Test
    public void mySqlDemo() throws Exception {
        // check driver exist
        Class.forName("org.apache.calcite.jdbc.Driver");
        Class.forName("com.mysql.jdbc.Driver");
        // the properties for calcite connection
        Properties info = new Properties();
        info.setProperty("lex", "JAVA");
        info.setProperty("remarks", "true");
        // SqlParserImpl can analysis sql dialect for sql parse
        info.setProperty("parserFactory", "org.apache.calcite.sql.parser.impl.SqlParserImpl#FACTORY");

        // create calcite connection and schema
        Connection connection = DriverManager.getConnection("jdbc:calcite:", info);
        CalciteConnection calciteConnection = connection.unwrap(CalciteConnection.class);
        SchemaPlus rootSchema = calciteConnection.getRootSchema();

        // code for mysql datasource
        MysqlDataSource dataSource = new MysqlDataSource();
        // please change host and port maybe like "jdbc:mysql://127.0.0.1:3306/test"
        dataSource.setUrl("jdbc:mysql://10.231.6.21:3306/test_db");
        dataSource.setUser("root");
        dataSource.setPassword("budai#123456");
        // mysql schema, the sub schema for rootSchema, "test" is a schema in mysql
        Schema schema = JdbcSchema.create(rootSchema, "test_db", dataSource, null, "test_db");
        rootSchema.add("test_db", schema);
        System.out.println(schema.getTableNames());
        connection.close();
    }
}
