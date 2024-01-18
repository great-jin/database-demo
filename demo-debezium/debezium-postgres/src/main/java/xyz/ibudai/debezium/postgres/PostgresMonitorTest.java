package xyz.ibudai.debezium.postgres;

import io.debezium.connector.postgresql.PostgresConnector;
import io.debezium.connector.postgresql.PostgresConnectorConfig;
import io.debezium.relational.RelationalDatabaseConnectorConfig;
import org.apache.kafka.connect.runtime.ConnectorConfig;
import xyz.ibudai.debezium.common.config.PropsConfig;
import org.junit.Before;
import org.junit.Test;
import xyz.ibudai.debezium.common.task.JsonEventTask;

import java.util.Properties;

public class PostgresMonitorTest {

    private static Properties props;

    @Before
    public void init() {
        props = PropsConfig.create();
        props.setProperty(ConnectorConfig.NAME_CONFIG, "postgres-engine");
        props.setProperty(ConnectorConfig.CONNECTOR_CLASS_CONFIG, PostgresConnector.class.getName());

        // plugin mode
        props.setProperty(PostgresConnectorConfig.PLUGIN_NAME.name(), "pgoutput");

        /* begin connector properties */
        props.setProperty(RelationalDatabaseConnectorConfig.HOSTNAME.name(), "10.231.6.21");
        props.setProperty(RelationalDatabaseConnectorConfig.PORT.name(), "5432");
        props.setProperty(RelationalDatabaseConnectorConfig.USER.name(), "postgres");
        props.setProperty(RelationalDatabaseConnectorConfig.PASSWORD.name(), "123456");
    }

    @Test
    public void demo1() {
        props.setProperty(RelationalDatabaseConnectorConfig.DATABASE_NAME.name(), "postgres");
        props.setProperty(RelationalDatabaseConnectorConfig.SCHEMA_INCLUDE_LIST.name(), "public");
        props.setProperty(RelationalDatabaseConnectorConfig.TABLE_INCLUDE_LIST.name(), "public.tb_test01");

        new JsonEventTask(props).run();
    }

    @Test
    public void demo2() {
        String POSTGRES_OFFSETS_VALUE = "{\"lsn_commit\":%s,\"lsn\":%s}";
        System.out.println(String.format(POSTGRES_OFFSETS_VALUE, 100, 100));
    }
}
