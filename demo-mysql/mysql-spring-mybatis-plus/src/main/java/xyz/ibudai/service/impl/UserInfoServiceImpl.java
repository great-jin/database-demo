package xyz.ibudai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.ibudai.dao.UserInfoDao;
import xyz.ibudai.entity.UserInfo;
import xyz.ibudai.service.UserInfoService;
import org.springframework.stereotype.Service;

/**
 * (UserInfo)表服务实现类
 *
 * @author ibudai
 * @since 2022-11-11 15:36:20
 */
@Service("userInfoService")
public class UserInfoServiceImpl extends ServiceImpl<UserInfoDao, UserInfo> implements UserInfoService {

}

