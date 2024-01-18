package xyz.ibudai.debezium.jdbc;

import io.debezium.connector.jdbc.JdbcSinkConnector;
import io.debezium.connector.jdbc.JdbcSinkConnectorConfig;
import org.apache.kafka.connect.runtime.ConnectorConfig;
import org.junit.Before;
import org.junit.Test;
import xyz.ibudai.debezium.common.config.PropsConfig;
import xyz.ibudai.debezium.common.task.JsonEventTask;

import java.util.Properties;

public class JdbcMonitorTest {

    private static Properties props;

    @Before
    public void init() {
        props = PropsConfig.create();
        props.setProperty(ConnectorConfig.NAME_CONFIG, "jdbc-engine");
        props.setProperty(ConnectorConfig.CONNECTOR_CLASS_CONFIG, JdbcSinkConnector.class.getName());

        props.setProperty("tasks.max", "1");

        props.setProperty(JdbcSinkConnectorConfig.CONNECTION_URL, "jdbc:mysql://10.231.6.21:3306/db_debezium");
        props.setProperty(JdbcSinkConnectorConfig.CONNECTION_USER, "root");
        props.setProperty(JdbcSinkConnectorConfig.CONNECTION_PASSWORD, "budai#123456");

        props.setProperty(JdbcSinkConnectorConfig.INSERT_MODE, "upsert");
        props.setProperty(JdbcSinkConnectorConfig.DELETE_ENABLED, "true");
        props.setProperty(JdbcSinkConnectorConfig.PRIMARY_KEY_MODE, "record_key");
        props.setProperty(JdbcSinkConnectorConfig.SCHEMA_EVOLUTION, "basic");
        props.setProperty(JdbcSinkConnectorConfig.DATABASE_TIME_ZONE, "UTC");
    }

    @Test
    public void demo1() {
        new JsonEventTask(props).run();
    }
}
