package xyz.ibudai.entity;

import java.io.Serializable;

/**
 * (UserInfo)实体类
 *
 * @author makejava
 * @since 2022-07-11 11:41:41
 */
public class UserInfo implements Serializable {
    private static final long serialVersionUID = -31819659978203652L;

    private String id;

    private String name;

    private String gender;

    private String birthday;


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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

}

