package xyz.ibudai.common;

public class JDBCInfo {

    /**
     * MySQLs connection info
     */
    public final static String MYSQL_URL = "jdbc:mysql://10.231.6.21:3306/test_db";
    public final static String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    public final static String MYSQL_DRIVER_1 = "com.mysql.cj.jdbc.Driver";
    public final static String MYSQL_PATH = "E:\\Workspace\\Driver\\mysql-connector-java.jar";
    public final static String MYSQL_USERNAME = "root";
    public final static String MYSQL_PASSWORD = "budai#123456";

    /**
     * Oracle connection info
     */
    public final static String ORACLE_URL = "jdbc:oracle:thin:@//10.231.6.21:1521/helowin";
    public final static String ORACLE_DRIVER = "oracle.jdbc.OracleDriver";
    public final static String ORACLE_PATH = "E:\\Workspace\\Driver\\ojdbc6-11.2.0.3.jar";
    public final static String ORACLE_PATH_1 = "E:\\Workspace\\Driver\\ojdbc8-12.2.0.1.jar";
    public final static String ORACLE_USERNAME = "ibudai";
    public final static String ORACLE_PASSWORD = "budai#123456";

    /**
     * Hive connection info
     */
    public final static String HIVE_URL = "jdbc:hive2://192.168.173.43:10000/a_db";
    public final static String HIVE_DRIVER = "org.apache.hive.jdbc.HiveDriver";
    public final static String HIVE_PATH = "E:\\Workspace\\Driver\\ojdbc6-11.2.0.3.jar";
    public final static String HIVE_USERNAME = "ibudai";
    public final static String HIVE_PASSWORD = "budai#123456";
}
