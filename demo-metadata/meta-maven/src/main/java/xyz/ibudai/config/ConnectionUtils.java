package xyz.ibudai.config;

import xyz.ibudai.common.DbType;
import xyz.ibudai.common.JDBCInfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtils {

    public static Connection getConnection(DbType dbType) {
        String url = "";
        String userName = "";
        String password = "";
        try {
            switch (dbType) {
                case MYSQL:
                    url = JDBCInfo.MYSQL_URL;
                    userName = JDBCInfo.MYSQL_USERNAME;
                    password = JDBCInfo.MYSQL_PASSWORD;
                    break;
                case ORACLE:
                    url = JDBCInfo.ORACLE_URL;
                    userName = JDBCInfo.ORACLE_USERNAME;
                    password = JDBCInfo.ORACLE_PASSWORD;
                    break;
            }
            return DriverManager.getConnection(url, userName, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
