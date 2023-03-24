package xyz.ibudai.connector;

import org.apache.ddlutils.Platform;
import org.apache.ddlutils.PlatformFactory;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;
import org.junit.Test;
import xyz.ibudai.common.DbType;
import xyz.ibudai.config.BasicPool;

import javax.sql.DataSource;

public class DDLUtilsTest {

    @Test
    public void demo() {
        // Establish a connection to the database
        DataSource dataSource = BasicPool.buildDatasource(DbType.MYSQL);
        // Use DDLUtils to extract the database model
        Platform platform = PlatformFactory.createNewPlatformInstance(dataSource);
        Database database = platform.readModelFromDatabase("a_db");

        // Print the tables in the database
        for (Table table : database.getTables()) {
            System.out.println("Table name: " + table.getName());
            System.out.println("Columns:");
            for (Column column : table.getColumns()) {
                System.out.println(column.getName() + " (" + column.getType() + ")");
            }
        }
    }
}
