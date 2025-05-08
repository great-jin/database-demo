package xyz.ibudai.database.mongo.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class MongoRepository {

    @Autowired
    private MongoTemplate mongoTemplate;


    /**
     * 重复时报错
     */
    public <T> void save(T t) {
        mongoTemplate.save(t);
    }

    /**
     * 重复时覆盖
     */
    public <T> void insert(T t) {
        this.insertAll(Collections.singleton(t));
    }

    public <T> void insertAll(Collection<T> collection) {
        mongoTemplate.insertAll(collection);
    }

    public <T> long count(Class<T> tClass, Map<String, Object> condition) {
        return mongoTemplate.count(createQuery(condition), tClass);
    }

    public <T> T findById(Object id, Class<T> tClass) {
        return mongoTemplate.findById(id, tClass);
    }

    public <T> T find(Class<T> tClass, Map<String, Object> condition) {
        return mongoTemplate.findOne(createQuery(condition), tClass);
    }

    public <T> List<T> listAll(Class<T> tClass, Map<String, Object> condition) {
        return mongoTemplate.find(createQuery(condition), tClass);
    }

    public <T> void delete(Class<T> tClass, Map<String, Object> condition) {
        mongoTemplate.remove(createQuery(condition), tClass);
    }

    protected Query createQuery(Map<String, Object> conditions) {
        Criteria criteria = new Criteria();
        for (Map.Entry<String, Object> entry : conditions.entrySet()) {
            criteria.and(entry.getKey()).is(entry.getValue());
        }
        return new Query(criteria);
    }
}
