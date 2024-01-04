package xyz.ibudai.repository;

import xyz.ibudai.model.Condition;
import xyz.ibudai.model.PageDetail;

import java.io.Serializable;
import java.util.List;

public interface Repository<T> {

    List<T> list(String... index);

    List<T> listByCondition(String index, List<Condition> conditions);

    PageDetail<T> page(String index, Integer limit, Integer offset);

    PageDetail<T> pageByCondition(String index, Integer limit, Integer offset, List<Condition> conditions);

    T get(String index, Serializable id);

    String save(String index, Serializable id, T t);

    String update(String index, Serializable id, T t);

    String delete(String index, Serializable id);
}
