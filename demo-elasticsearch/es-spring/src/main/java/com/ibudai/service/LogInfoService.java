package com.ibudai.service;

public interface LogInfoService {

    String saveInfo(String index, String id, String info);

    String getById(String index, String docId);
}
