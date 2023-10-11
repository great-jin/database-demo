package xyz.ibudai.model.common;

import java.util.Properties;

public class DbConst {

    /**
     * MySQLs connection info
     */
    public final static String MYSQL_URL = "jdbc:mysql://10.231.6.21:3306/test_db";
    public final static String MYSQL_USERNAME = "root";
    public final static String MYSQL_PASSWORD = "budai#123456";
    // com.mysql.jdbc.Driver
    public final static String MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";
    public final static String MYSQL_DRIVER_PATH = "E:\\Workspace\\Driver\\mysql-connector-j-8.0.32.jar";

    /**
     * Oracle connection info
     */
    public final static String ORACLE_URL = "jdbc:oracle:thin:@//10.231.6.21:1521/helowin";
    public final static String ORACLE_USERNAME = "ibudai";
    public final static String ORACLE_PASSWORD = "budai#123456";
    public final static String ORACLE_DRIVER = "oracle.jdbc.OracleDriver";
    public final static String ORACLE_DRIVER_PATH = "E:\\Workspace\\Driver\\ojdbc6-11.2.0.3.jar";
    private final static Properties ORACLE_PROPS = new Properties();

    static {
        // For metadata get comment info null
        ORACLE_PROPS.put("remarksReporting", "true");
        // For metadata get schema name null
        ORACLE_PROPS.put("useFetchSizeWithLongColumn", "true");
    }

    /**
     * Hive connection info
     */
    public final static String HIVE_URL = "jdbc:hive2://192.168.173.43:10000/a_db";
    public final static String HIVE_USERNAME = "ibudai";
    public final static String HIVE_PASSWORD = "budai#123456";
    public final static String HIVE_DRIVER = "org.apache.hive.jdbc.HiveDriver";
    public final static String HIVE_DRIVER_PATH = "E:\\Workspace\\Driver\\ojdbc6-11.2.0.3.jar";
}
