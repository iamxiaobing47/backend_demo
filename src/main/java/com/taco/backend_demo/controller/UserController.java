package com.taco.backend_demo.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taco.backend_demo.common.response.Response;
import com.taco.backend_demo.common.response.ResponseFactory;
import com.taco.backend_demo.dto.user.*;
import com.taco.backend_demo.entity.UserInfoEntity;
import com.taco.backend_demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理 Controller
 */
@Tag(name = "用户管理", description = "用户信息相关接口")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "创建用户", description = "创建新用户")
    @PostMapping("/create")
    public ResponseEntity<Response<Void>> createUser(@Valid @RequestBody CreateUserRequest request) {
        userService.createUser(
            request.getEmail(),
            request.getPassword(),
            request.getUserName(),
            request.getUserType(),
            request.getUserId(),
            request.getOrgId()
        );
        return ResponseFactory.success(null);
    }

    @Operation(summary = "获取用户信息", description = "根据用户 ID 获取用户信息")
    @GetMapping("/{userId}")
    public ResponseEntity<Response<UserInfoEntity>> getUser(@PathVariable String userId) {
        UserInfoEntity userInfoEntity = userService.getUserByUserId(userId);
        return ResponseFactory.success(userInfoEntity);
    }

    @Operation(summary = "更新用户信息", description = "更新当前用户信息")
    @PutMapping
    public ResponseEntity<Response<Void>> updateUser(@Valid @RequestBody UpdateUserRequest request) {
        userService.updateUser(request.getUserId(), request.getUserType(), request.getName(), request.getOrgId());
        return ResponseFactory.success(null);
    }

    @Operation(summary = "分页查询用户列表", description = "根据分页参数和筛选条件查询用户列表")
    @PostMapping("/page")
    public ResponseEntity<Response<IPage<UserInfo>>> pageUsers(@Valid @RequestBody PageUserQueryRequest request) {
        IPage<UserInfo> pageInfo = userService.pageUsers(request);
        return ResponseFactory.success(pageInfo);
    }

    @Operation(summary = "删除用户", description = "删除当前用户")
    @DeleteMapping
    public ResponseEntity<Response<Void>> deleteUser(@Valid @RequestBody DeleteUserRequest request) {
        userService.deleteUser(request.getUserId(), request.getUserType());
        return ResponseFactory.success(null);
    }
}
