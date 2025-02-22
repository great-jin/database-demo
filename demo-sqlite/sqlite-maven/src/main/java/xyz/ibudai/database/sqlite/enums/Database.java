package xyz.ibudai.database.sqlite.enums;

import lombok.Getter;

@Getter
public enum Database {

    TEST_DB("db/test.db");


    private final String path;

    Database(String path) {
        this.path = path;
    }

    public static String getJdbcUrl(Database database) {
        return "jdbc:sqlite:" + database.getPath();
    }
}
