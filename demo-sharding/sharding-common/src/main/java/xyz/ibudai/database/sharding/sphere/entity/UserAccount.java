package xyz.ibudai.database.sharding.sphere.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * (UserInfo)实体类
 *
 * @author budai
 * @since 2022-07-04 17:55:53
 */
@Data
public class UserAccount implements Serializable {
    private static final long serialVersionUID = -68517628975800619L;

    private Integer id;

    private Integer userId;

    private String password;

}

