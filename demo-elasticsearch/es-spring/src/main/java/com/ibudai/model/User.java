package com.ibudai.model;

import java.util.Date;

public class User {

    private String id;

    private String name;

    private String message;

    private Date createTime;

    public User() {
    }

    public User(String id, String name, String message, Date createTime) {
        this.id = id;
        this.name = name;
        this.message = message;
        this.createTime = createTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
