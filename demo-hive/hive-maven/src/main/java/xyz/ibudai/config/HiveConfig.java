package xyz.ibudai.config;

import java.sql.Connection;
import java.sql.DriverManager;

public class HiveConfig {

    private final static String URL = "jdbc:hive2://192.168.173.43:10000/a_db";
    private final static String DRIVER_NAME = "org.apache.hive.jdbc.HiveDriver";

    public static Connection getHiveConnection() {
        try {
            Class.forName(DRIVER_NAME);
            return DriverManager.getConnection(URL);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
