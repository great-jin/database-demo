package xyz.ibudai.database.sharding.sphere.service;

import xyz.ibudai.database.sharding.sphere.entity.UserAccount;

import java.util.List;

/**
 * (UserInfo)表服务接口
 *
 * @author budai
 * @since 2022-07-04 17:55:53
 */
public interface UserAccountService {

    /**
     * 连表查询
     *
     * @return 查询结果
     */
    List<UserAccount> joinList();

}
