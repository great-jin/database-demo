package xyz.ibudai.database.jdbc.oracle.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {

    final static String JDBC = "jdbc:oracle:thin:@//10.231.6.65:1521/helowin";
    final static String USERNAME = "budai";
    final static String PASSWORD = "123456";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(JDBC, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
