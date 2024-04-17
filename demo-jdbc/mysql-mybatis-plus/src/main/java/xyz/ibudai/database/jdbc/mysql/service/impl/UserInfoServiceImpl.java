package xyz.ibudai.database.jdbc.mysql.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import xyz.ibudai.database.jdbc.mysql.dao.UserInfoDao;
import xyz.ibudai.database.jdbc.mysql.entity.UserInfo;
import xyz.ibudai.database.jdbc.mysql.service.UserInfoService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (UserInfo)表服务实现类
 *
 * @author ibudai
 * @since 2022-11-11 15:36:20
 */
@Service("userInfoService")
public class UserInfoServiceImpl extends ServiceImpl<UserInfoDao, UserInfo> implements UserInfoService {

    @Autowired
    private UserInfoDao userInfoDao;

    @Autowired
    private UserInfoService userInfoService;

    public void demo() {
        List<UserInfo> list1 = userInfoService.list(new QueryWrapper<UserInfo>()
                .eq("name", "alex"));

        UserInfo userInfo = new UserInfo(111, "Beth", "female");
        userInfoService.save(userInfo);

        userInfoService.updateById(userInfo);
        userInfoService.update(userInfo, new UpdateWrapper<UserInfo>()
                .eq("id", "alex"));

        userInfoService.remove(new QueryWrapper<UserInfo>()
                .eq("name", "alex"));
    }
}

