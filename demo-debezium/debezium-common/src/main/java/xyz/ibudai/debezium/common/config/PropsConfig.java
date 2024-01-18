package xyz.ibudai.debezium.common.config;

import java.util.Properties;

public class PropsConfig {

    public static Properties create() {
        final Properties props = new Properties();

        // 前缀，必填且唯一不要修改
        props.setProperty("topic.prefix", "debezium_connector");

        // 偏移量刷新间隔
        props.setProperty("offset.flush.interval.ms", "60000");

        /**
         * 偏移量记录方式（内存）
         * <p>
         * {@link org.apache.kafka.connect.storage.MemoryOffsetBackingStore}
         * {@link io.debezium.relational.history.MemorySchemaHistory}
         */
        props.setProperty("offset.storage", "org.apache.kafka.connect.storage.MemoryOffsetBackingStore");
        props.setProperty("schema.history.internal", "io.debezium.relational.history.MemorySchemaHistory");

        /**
         * 偏移量记录方式（文件）
         * <p>
         * {@link org.apache.kafka.connect.storage.FileOffsetBackingStore}
         * {@link io.debezium.storage.file.history.FileSchemaHistory}
         */
        /*props.setProperty("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore");
        props.setProperty("offset.storage.file.filename",
                "E:\\Workspace\\debezium\\postgres\\offsets.dat");
        props.setProperty("schema.history.internal", "io.debezium.storage.file.history.FileSchemaHistory");
        props.setProperty("schema.history.internal.file.filename",
                "E:\\Workspace\\debezium\\postgres\\schemahistory.dat");*/
        return props;
    }
}
