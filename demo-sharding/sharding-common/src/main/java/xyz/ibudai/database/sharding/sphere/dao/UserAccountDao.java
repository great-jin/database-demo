package xyz.ibudai.database.sharding.sphere.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xyz.ibudai.database.sharding.sphere.entity.UserAccount;
import xyz.ibudai.database.sharding.sphere.entity.UserInfo;

import java.util.List;

/**
 * (UserInfo)表数据库访问层
 *
 * @author budai
 * @since 2022-07-04 17:55:53
 */
@Mapper
public interface UserAccountDao {

    /**
     * 查询指定行数据
     *
     * @return 对象列表
     */
    List<UserAccount> joinList();

}

