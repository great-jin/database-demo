package xyz.ibudai.database.sharding.sphere.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.ibudai.database.sharding.sphere.entity.UserAccount;
import xyz.ibudai.database.sharding.sphere.entity.UserInfo;
import xyz.ibudai.database.sharding.sphere.service.UserAccountService;
import xyz.ibudai.database.sharding.sphere.service.UserInfoService;

import java.util.List;

/**
 * (UserInfo)表控制层
 *
 * @author budai
 * @since 2022-07-04 17:55:53
 */
@RestController
@RequestMapping("/api/sharding/userAccount")
public class UserAccountController {

    /**
     * 服务对象
     */
    @Autowired
    private UserAccountService userAccountService;

    /**
     * 分页查询
     *
     * @return 查询结果
     */
    @GetMapping("join")
    public ResponseEntity<List<UserAccount>> joinList() {
        return ResponseEntity.ok(this.userAccountService.joinList());
    }
}

