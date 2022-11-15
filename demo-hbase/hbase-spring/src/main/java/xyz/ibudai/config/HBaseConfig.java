package xyz.ibudai.config;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.ibudai.model.prop.HBaseProperties;

import java.io.IOException;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(HBaseProperties.class)
public class HBaseConfig {

    private final HBaseProperties hBaseProperties;

    public HBaseConfig(HBaseProperties props) {
        this.hBaseProperties = props;
    }

    @Bean
    public org.apache.hadoop.conf.Configuration configuration() {
        org.apache.hadoop.conf.Configuration conf = HBaseConfiguration.create();
        Map<String, String> config = hBaseProperties.getConfig();
        config.forEach(conf::set);
        return conf;
    }

    @Bean
    public Connection getConnection() throws IOException {
        return ConnectionFactory.createConnection(configuration());
    }

    @Bean
    public HBaseAdmin hBaseAdmin() throws IOException {
        return (HBaseAdmin) getConnection().getAdmin();
    }
}
