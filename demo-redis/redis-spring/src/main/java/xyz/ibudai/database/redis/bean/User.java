package xyz.ibudai.database.redis.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Integer id;

    private String name;

    private String password;

    private Date updateTime;

}
