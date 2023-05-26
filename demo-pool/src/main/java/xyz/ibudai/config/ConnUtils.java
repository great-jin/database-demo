package xyz.ibudai.config;

import xyz.ibudai.model.DbEntity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnUtils {

    /**
     * Gets connection.
     *
     * @param dbEntity the db entity
     * @return the connection
     */
    public static Connection getConnection(DbEntity dbEntity) {
        String url = dbEntity.getUrl();
        String userName = dbEntity.getUser();
        String password = dbEntity.getPassword();
        try {
            return DriverManager.getConnection(url, userName, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
