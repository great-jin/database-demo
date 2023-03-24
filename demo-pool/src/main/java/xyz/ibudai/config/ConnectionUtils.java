package xyz.ibudai.config;

import xyz.ibudai.common.DbType;
import xyz.ibudai.utils.DriverUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The type Datasource utils.
 */
public class ConnectionUtils {

    /**
     * Gets connection.
     *
     * @param dbType the db type
     * @return the connection
     */
    public static Connection getConnection(DbType dbType) {
        String[] info = DriverUtil.buildDbInfo(dbType);
        String url = info[0];
        String userName = info[1];
        String password = info[2];
        try {
            return DriverManager.getConnection(url, userName, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
