package xyz.ibudai.basic;

import xyz.ibudai.model.DbEntity;
import xyz.ibudai.utils.DriverShim;

import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
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
        String driverPath = dbEntity.getDriverLocation();

        URLClassLoader classLoader = null;
        try {
            // Load driver class and register (use URLClassLoader to dynamic load)
            ClassLoader parent = Thread.currentThread().getContextClassLoader();
            classLoader = new URLClassLoader(new URL[]{new URL("file:" + driverPath)}, parent);
            Class<?> driver = classLoader.loadClass(driverClass);
            Constructor<Driver> constructor = (Constructor<Driver>) driver.getConstructor();
            DriverManager.registerDriver(new DriverShim(constructor.newInstance()));
            // Get connection
            return DriverManager.getConnection(url, userName, password);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                classLoader.close();
            } catch (Exception ignored) {
            }
        }
    }
}
