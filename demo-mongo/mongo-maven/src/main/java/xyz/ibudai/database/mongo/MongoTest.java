package xyz.ibudai.database.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.junit.After;
import org.junit.Before;
import xyz.ibudai.database.mongo.repository.MongoRepository;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MongoTest {

    private MongoRepository mongoRepository;


    @Before
    public void init() {
        String databaseName = "test_col";
        String url = "mongodb://root:123456@192.168.0.21:27017/test_col?authSource=admin";

        // Build client setting
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyToSocketSettings(builder -> {
                    builder.connectTimeout(60, TimeUnit.SECONDS);
                    builder.readTimeout(60, TimeUnit.SECONDS);
                })
                .applyToClusterSettings(builder -> {
                    builder.hosts(new ArrayList<>());
                    builder.serverSelectionTimeout(60, TimeUnit.SECONDS).build();
                })
                .codecRegistry(CodecRegistries.fromRegistries(
                        MongoClientSettings.getDefaultCodecRegistry(),
                        CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())
                ))
                .applyConnectionString(new ConnectionString(url))
                .build();

        mongoRepository = new MongoRepository(databaseName, MongoClients.create(settings));
    }

    @After
    public void destroy() throws Exception {
        mongoRepository.close();
    }
}
