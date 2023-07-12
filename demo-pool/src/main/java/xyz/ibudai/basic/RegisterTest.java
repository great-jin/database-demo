package xyz.ibudai.basic;

import xyz.ibudai.model.DbEntity;

import java.sql.Connection;
import java.sql.DriverManager;

public class RegisterTest {

    /**
     * Gets connection.
     *
     * @param dbEntity the db entity
     * @return the connection
     */
    public Connection getConnection(DbEntity dbEntity) {
        String url = dbEntity.getUrl();
        String userName = dbEntity.getUser();
        String password = dbEntity.getPassword();
        String driverClass = dbEntity.getDriverClassName();
        try {
            // Load driver class and register
            // Normal is job of maven, can use URLClassLoader to dynamic load
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Class<?> aClass = classLoader.loadClass(driverClass);
            DriverManager.registerDriver((java.sql.Driver) aClass.newInstance());
            // Get connection
            return DriverManager.getConnection(url, userName, password);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
