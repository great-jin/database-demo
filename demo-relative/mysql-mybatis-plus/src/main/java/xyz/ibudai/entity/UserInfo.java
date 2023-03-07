package xyz.ibudai.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.util.Date;

/**
 * (UserInfo)表实体类
 *
 * @author ibudai
 * @since 2022-11-11 15:36:20
 */
@SuppressWarnings("serial")
public class UserInfo extends Model<UserInfo> {

    private Integer id;

    private String name;

    private String gender;

    /**
     * 标注表中无该字段，不进行映射
     */
    @TableField(exist = false)
    private Date updateTime;

    public UserInfo() {
    }

    public UserInfo(Integer id, String name, String gender) {
        this.id = id;
        this.name = name;
        this.gender = gender;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}

