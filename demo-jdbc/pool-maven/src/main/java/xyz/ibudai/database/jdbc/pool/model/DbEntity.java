package xyz.ibudai.database.jdbc.pool.model;

import lombok.Data;

import java.util.Properties;

@Data
public class DbEntity {

    private String url;

    private String user;

    private String password;

    /**
     * Driver class name
     */
    private String driverClassName;

    /**
     * Driver location
     */
    private String driverLocation;

    /**
     * Database prop config
     */
    private Properties databaseProp;
}
