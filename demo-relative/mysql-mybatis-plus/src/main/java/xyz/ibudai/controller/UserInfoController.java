package xyz.ibudai.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import xyz.ibudai.entity.UserInfo;
import xyz.ibudai.service.UserInfoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * (UserInfo)表控制层
 *
 * @author ibudai
 * @since 2022-11-11 15:36:20
 */
@RestController
@RequestMapping("api/userInfo")
public class UserInfoController {
    /**
     * 服务对象
     */
    @Resource
    private UserInfoService userInfoService;

    /**
     * 分页查询所有数据
     *
     * @param page     分页对象
     * @param userInfo 查询实体
     * @return 所有数据
     */
    @GetMapping
    public Page<UserInfo> selectAll(Page<UserInfo> page, UserInfo userInfo) {
        return this.userInfoService.page(page, new QueryWrapper<>(userInfo));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public UserInfo selectOne(@PathVariable Serializable id) {
        return this.userInfoService.getById(id);
    }

    /**
     * 新增数据
     *
     * @param userInfo 实体对象
     * @return 新增结果
     */
    @PostMapping
    public boolean insert(@RequestBody UserInfo userInfo) {
        return this.userInfoService.save(userInfo);
    }

    /**
     * 修改数据
     *
     * @param userInfo 实体对象
     * @return 修改结果
     */
    @PutMapping
    public boolean update(@RequestBody UserInfo userInfo) {
        return this.userInfoService.updateById(userInfo);
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public boolean delete(@RequestParam("idList") List<Long> idList) {
        return this.userInfoService.removeByIds(idList);
    }
}

