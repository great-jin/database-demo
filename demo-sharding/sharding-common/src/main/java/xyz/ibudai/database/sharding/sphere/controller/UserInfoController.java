package xyz.ibudai.database.sharding.sphere.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.ibudai.database.sharding.sphere.entity.UserInfo;
import xyz.ibudai.database.sharding.sphere.service.UserInfoService;

import java.util.List;

/**
 * (UserInfo)表控制层
 *
 * @author budai
 * @since 2022-07-04 17:55:53
 */
@RestController
@RequestMapping("/api/sharding/userInfo")
public class UserInfoController {

    /**
     * 服务对象
     */
    @Autowired
    private UserInfoService userInfoService;

    /**
     * 分页查询
     *
     * @return 查询结果
     */
    @GetMapping("list")
    public ResponseEntity<List<UserInfo>> listAll() {
        return ResponseEntity.ok(this.userInfoService.listAll());
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public ResponseEntity<UserInfo> queryById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(this.userInfoService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param userInfo 实体
     * @return 新增结果
     */
    @PostMapping("add")
    public ResponseEntity<UserInfo> add(@RequestBody UserInfo userInfo) {
        return ResponseEntity.ok(this.userInfoService.insert(userInfo));
    }

    /**
     * 编辑数据
     *
     * @param userInfo 实体
     * @return 编辑结果
     */
    @PutMapping("edit")
    public ResponseEntity<UserInfo> edit(UserInfo userInfo) {
        return ResponseEntity.ok(this.userInfoService.update(userInfo));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping("deleteById")
    public ResponseEntity<Boolean> deleteById(Integer id) {
        return ResponseEntity.ok(this.userInfoService.deleteById(id));
    }
}

