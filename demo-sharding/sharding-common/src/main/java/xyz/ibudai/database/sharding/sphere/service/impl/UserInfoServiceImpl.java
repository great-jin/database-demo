package xyz.ibudai.database.sharding.sphere.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.ibudai.database.sharding.sphere.dao.UserInfoDao;
import xyz.ibudai.database.sharding.sphere.entity.UserInfo;
import xyz.ibudai.database.sharding.sphere.service.UserInfoService;

import java.util.List;

/**
 * (UserInfo)表服务实现类
 *
 * @author budai
 * @since 2022-07-04 17:55:53
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoDao userInfoDao;

    @Override
    public UserInfo queryById(Integer id) {
        return this.userInfoDao.queryById(id);
    }

    @Override
    public List<UserInfo> listAll() {
        return this.userInfoDao.listAll();
    }

    @Override
    public UserInfo insert(UserInfo userInfo) {
        this.userInfoDao.insert(userInfo);
        return userInfo;
    }

    @Override
    public UserInfo update(UserInfo userInfo) {
        this.userInfoDao.update(userInfo);
        return this.queryById(userInfo.getId());
    }

    @Override
    public boolean deleteById(Integer id) {
        return this.userInfoDao.deleteById(id) > 0;
    }
}
