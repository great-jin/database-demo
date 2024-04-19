package xyz.ibudai.database.sharding.sphere.service;

import xyz.ibudai.database.sharding.sphere.entity.UserInfo;

import java.util.List;

/**
 * (UserInfo)表服务接口
 *
 * @author budai
 * @since 2022-07-04 17:55:53
 */
public interface UserInfoService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    UserInfo queryById(Integer id);

    /**
     * 全量查询
     *
     * @return 查询结果
     */
    List<UserInfo> listAll();

    /**
     * 新增数据
     *
     * @param userInfo 实例对象
     * @return 实例对象
     */
    UserInfo insert(UserInfo userInfo);

    /**
     * 修改数据
     *
     * @param userInfo 实例对象
     * @return 实例对象
     */
    UserInfo update(UserInfo userInfo);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
