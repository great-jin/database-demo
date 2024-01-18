package xyz.ibudai.debezium.mysql;

import io.debezium.connector.mysql.MySqlConnector;
import io.debezium.connector.mysql.MySqlConnectorConfig;
import io.debezium.relational.RelationalDatabaseConnectorConfig;
import org.apache.kafka.connect.runtime.ConnectorConfig;
import xyz.ibudai.debezium.common.config.PropsConfig;
import org.junit.Before;
import org.junit.Test;
import xyz.ibudai.debezium.common.task.JsonEventTask;
import xyz.ibudai.debezium.common.task.RecordEventTask;

import java.util.Properties;

public class MySQLMonitorTest {

    private static Properties props;

    @Before
    public void init() {
        props = PropsConfig.create();
        props.setProperty(ConnectorConfig.NAME_CONFIG, "mysql-engine");
        props.setProperty(ConnectorConfig.CONNECTOR_CLASS_CONFIG, MySqlConnector.class.getName());

        // bin log server id
        props.setProperty(MySqlConnectorConfig.SERVER_ID.name(), "1");

        /* begin connector properties */
        props.setProperty(RelationalDatabaseConnectorConfig.HOSTNAME.name(), "10.231.6.21");
        props.setProperty(RelationalDatabaseConnectorConfig.PORT.name(), "3306");
        props.setProperty(RelationalDatabaseConnectorConfig.USER.name(), "root");
        props.setProperty(RelationalDatabaseConnectorConfig.PASSWORD.name(), "budai#123456");
    }

    @Test
    public void demo1() {
        props.setProperty(RelationalDatabaseConnectorConfig.DATABASE_NAME.name(), "db_debezium");
        props.setProperty(RelationalDatabaseConnectorConfig.DATABASE_INCLUDE_LIST.name(), "db_debezium");
        props.setProperty(RelationalDatabaseConnectorConfig.TABLE_INCLUDE_LIST.name(), "tb_include");

        new JsonEventTask(props).run();
    }

    @Test
    public void demo2() {
        props.setProperty(RelationalDatabaseConnectorConfig.DATABASE_NAME.name(), "db_debezium");
        props.setProperty(RelationalDatabaseConnectorConfig.DATABASE_INCLUDE_LIST.name(), "db_debezium");
        props.setProperty(RelationalDatabaseConnectorConfig.TABLE_INCLUDE_LIST.name(), "tb_include");

        new RecordEventTask(props).run();
    }
}
