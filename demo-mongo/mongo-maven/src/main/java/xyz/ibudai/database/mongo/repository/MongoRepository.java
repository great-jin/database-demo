package xyz.ibudai.database.mongo.repository;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MongoRepository implements AutoCloseable {

    private final String database;
    private final MongoClient mongoClient;

    public MongoRepository(String database, MongoClient mongoClient) {
        this.database = database;
        this.mongoClient = mongoClient;
    }

    @Override
    public void close() throws Exception {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }

    protected MongoDatabase getDatabase() {
        return mongoClient.getDatabase(database);
    }

    public <T> void insert(String collection, T t, Class<T> tClass) {
        MongoCollection<T> mongoCollection = getDatabase()
                .getCollection(collection, tClass);
        mongoCollection.insertOne(t);
    }

    public <T> void insertAll(String collection, List<T> list, Class<T> tClass) {
        MongoCollection<T> mongoCollection = getDatabase()
                .getCollection(collection, tClass);
        mongoCollection.insertMany(list);
    }

    public <T> void count(String collection, Map<String, ?> condition, Class<T> tClass) {
        MongoCollection<T> mongoCollection = getDatabase()
                .getCollection(collection, tClass);
        mongoCollection.countDocuments(new Document(condition));
    }

    public <T> T findOne(String collection, Map<String, ?> condition, Class<T> tClass) {
        MongoCollection<T> mongoCollection = getDatabase()
                .getCollection(collection, tClass);
        return mongoCollection.find(new Document(condition)).first();
    }

    public <T> List<T> find(String collection, Map<String, ?> condition, Class<T> tClass) {
        MongoCollection<T> mongoCollection = getDatabase()
                .getCollection(collection, tClass);
        FindIterable<T> iterable = mongoCollection.find(new Document(condition));
        List<T> list = new ArrayList<>();
        for (T t : iterable) {
            list.add(t);
        }
        return list;
    }

    public <T> boolean delete(String collection, Map<String, ?> condition, Class<T> tClass) {
        MongoCollection<T> mongoCollection = getDatabase()
                .getCollection(collection, tClass);
        return mongoCollection.deleteMany(new Document(condition)).getDeletedCount() > 0;
    }

    public <T> boolean deleteOne(String collection, Map<String, ?> condition, Class<T> tClass) {
        MongoCollection<T> mongoCollection = getDatabase()
                .getCollection(collection, tClass);
        return mongoCollection.deleteOne(new Document(condition)).getDeletedCount() > 0;
    }
}
