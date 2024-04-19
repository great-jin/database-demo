package xyz.ibudai.database.sharding.sphere.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.ibudai.database.sharding.sphere.dao.UserAccountDao;
import xyz.ibudai.database.sharding.sphere.entity.UserAccount;
import xyz.ibudai.database.sharding.sphere.service.UserAccountService;

import java.util.List;

/**
 * (UserInfo)表服务实现类
 *
 * @author budai
 * @since 2022-07-04 17:55:53
 */
@Service
public class UserAccountImpl implements UserAccountService {

    @Autowired
    private UserAccountDao userAccountDao;

    @Override
    public List<UserAccount> joinList() {
        return this.userAccountDao.joinList();
    }
}
