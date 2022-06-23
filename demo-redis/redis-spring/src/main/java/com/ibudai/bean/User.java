package com.ibudai.bean;

import java.util.Date;

public class User {

    private Integer id;
    private String name;
    private String password;
    private Date updateTime;

    public User() {
    }

    public User(Integer id, String name, String password, Date updateTime) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.updateTime = updateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
