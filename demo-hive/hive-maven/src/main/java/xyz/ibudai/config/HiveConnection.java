package xyz.ibudai.config;

import java.sql.Connection;
import java.sql.DriverManager;

public class HiveConnection {

    private static String url = "jdbc:hive2://192.168.173.43:10000/a_db";
    private static String driverName = "org.apache.hive.jdbc.HiveDriver";


    public static Connection getHiveConnection() {
        try {
            Class.forName(driverName);
            return DriverManager.getConnection(url);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
