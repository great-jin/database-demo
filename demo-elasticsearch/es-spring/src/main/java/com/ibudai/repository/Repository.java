package com.ibudai.repository;

import java.io.Serializable;
import java.util.List;

public interface Repository<T> {

    T get(String index, Serializable id);

    List<T> list(String index);

    String save(String index, Serializable id, T t);

    String update(String index, Serializable id, T t);

    String deleted(String index, Serializable id);
}
