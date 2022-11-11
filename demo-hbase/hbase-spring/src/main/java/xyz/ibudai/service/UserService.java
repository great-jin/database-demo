package xyz.ibudai.service;

import java.io.IOException;
import java.util.Map;

public interface UserService {

    void createTable(String name, String colFamily) throws IOException;


    void putData(String name, String colFamily, String rowKey, Map<String, String> data) throws IOException;


    void getData(String name) throws IOException;
}
