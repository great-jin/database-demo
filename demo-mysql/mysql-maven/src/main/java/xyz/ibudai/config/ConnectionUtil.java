package xyz.ibudai.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {

    final static String JDBC = "jdbc:mysql://10.231.6.65:3306/test_db?useSSL=true&useUnicode=true&characterEncoding=utf-8";
    final static String USERNAME = "root";
    final static String PASSWORD = "budai@123456";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(JDBC, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
