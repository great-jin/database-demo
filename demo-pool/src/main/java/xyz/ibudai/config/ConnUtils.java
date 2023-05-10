package xyz.ibudai.config;

import xyz.ibudai.model.common.DbType;
import xyz.ibudai.model.DbEntity;
import xyz.ibudai.utils.DriverUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The type Datasource utils.
 */
public class ConnUtils {

    /**
     * Gets connection.
     *
     * @param dbType the db type
     * @return the connection
     */
    public static Connection getConnection(DbType dbType) {
        DbEntity dbEntity = DriverUtil.buildDbInfo(dbType);
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
