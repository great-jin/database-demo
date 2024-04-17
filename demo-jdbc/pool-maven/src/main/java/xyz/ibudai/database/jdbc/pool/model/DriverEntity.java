package xyz.ibudai.database.jdbc.pool.model;

import lombok.Data;

@Data
public class DriverEntity {

    /**
     * Driver file name
     */
    private String fileName;

    /**
     * Driver store location
     */
    private String location;

    /**
     * Driver main class name
     */
    private String className;

}
