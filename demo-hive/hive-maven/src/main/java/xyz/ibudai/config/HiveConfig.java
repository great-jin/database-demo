package xyz.ibudai.config;

import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.metastore.HiveMetaStoreClient;
import org.apache.hadoop.hive.metastore.api.MetaException;

import java.sql.Connection;
import java.sql.DriverManager;

public class HiveConfig {

    private final static String URL = "jdbc:hive2://192.168.173.43:10000/a_db?connectTimeout=60000?socketTimeout=60000";
    private final static String DRIVER_NAME = "org.apache.hive.jdbc.HiveDriver";

    /**
     * 获取 Hive JDBC 连接
     */
    public static Connection getHiveConnection() {
        try {
            Class.forName(DRIVER_NAME);
            return DriverManager.getConnection(URL);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取 Hive metastore 客户端
     */
    public static HiveMetaStoreClient getHiveClient() throws MetaException {
        // hadoop-2.7.3
        System.setProperty("hadoop.home.dir", "D:\\Develop\\Hadoop\\hadoop-2.7.3");
        HiveConf conf = new HiveConf();
        conf.set("hive.metastore.uris", "thrift://192.168.173.43:9083");
        return new HiveMetaStoreClient(conf);
    }
}
