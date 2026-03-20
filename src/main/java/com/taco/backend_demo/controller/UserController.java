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
 *
 * 【Controller 的作用】
 * 接收 HTTP 请求，调用 Service 处理业务逻辑，返回 HTTP 响应
 *
 * 【核心功能】
 * 1. 创建用户：POST /api/users/create
 * 2. 获取用户信息：GET /api/users/{userId}
 * 3. 更新用户信息：PUT /api/users
 * 4. 分页查询用户：POST /api/users/page
 * 5. 删除用户：DELETE /api/users
 *
 * 【注解说明】
 * - @Tag: Swagger/OpenAPI 文档注解，用于生成 API 文档
 * - @RestController: 标记为 RESTful 控制器，返回 JSON 数据
 * - @RequestMapping: 设置基础请求路径
 *
 * 【请求流程】
 * 请求 → 路由匹配 → Controller → Service → Repository → 返回响应
 *
 * @author taco
 */
@Tag(name = "用户管理", description = "用户信息相关接口")
@RestController
@RequestMapping("/api/users")
public class UserController {

    // ==================== 依赖注入 ====================

    /**
     * 用户服务：处理用户相关的所有业务逻辑
     */
    @Autowired
    private UserService userService;

    /**
     * 【接口 1】创建用户
     * <p>
     * 接口信息：
     * - 路径：POST /api/users/create
     * - 请求体：CreateUserRequest（JSON 格式）
     * - 响应：成功/失败状态
     * <p>
     * 参数说明：
     * - email: 用户邮箱（登录账号）
     * - password: 用户密码
     * - userName: 用户昵称
     * - userType: 用户类型（BUSINESS_USER/STAFF_USER）
     * - userId: 业务用户 ID 或员工用户 ID
     * - orgId: 组织 ID（业务 ID 或位置 ID）
     *
     * @param request 创建用户请求体
     * @return 响应实体
     */
    @Operation(summary = "创建用户", description = "创建新用户")
    @PostMapping("/create")
    public ResponseEntity<Response<Void>> createUser(@Valid @RequestBody CreateUserRequest request) {
        // 调用 Service 创建用户
        userService.createUser(
            request.getEmail(),
            request.getPassword(),
            request.getUserName(),
            request.getUserType(),
            request.getUserId(),
            request.getOrgId()
        );
        // 返回成功响应（无数据）
        return ResponseFactory.success(null);
    }

    /**
     * 【接口 2】获取用户信息
     * <p>
     * 接口信息：
     * - 路径：GET /api/users/{userId}
     * - 路径参数：userId（用户 ID）
     * - 响应：用户信息实体
     *
     * @param userId 用户 ID（从 URL 路径获取）
     * @return 响应实体（包含用户信息）
     */
    @Operation(summary = "获取用户信息", description = "根据用户 ID 获取用户信息")
    @GetMapping("/{userId}")
    public ResponseEntity<Response<UserInfoEntity>> getUser(@PathVariable String userId) {
        // 调用 Service 查询用户
        UserInfoEntity userInfoEntity = userService.getUserByUserId(userId);
        // 返回成功响应（包含用户信息）
        return ResponseFactory.success(userInfoEntity);
    }

    /**
     * 【接口 3】更新用户信息
     * <p>
     * 接口信息：
     * - 路径：PUT /api/users
     * - 请求体：UpdateUserRequest（JSON 格式）
     * - 响应：成功/失败状态
     * <p>
     * 参数说明：
     * - userId: 用户 ID
     * - userType: 用户类型
     * - name: 新昵称
     * - orgId: 新组织 ID
     *
     * @param request 更新用户请求体
     * @return 响应实体
     */
    @Operation(summary = "更新用户信息", description = "更新当前用户信息")
    @PutMapping
    public ResponseEntity<Response<Void>> updateUser(@Valid @RequestBody UpdateUserRequest request) {
        // 调用 Service 更新用户
        userService.updateUser(request.getUserId(), request.getUserType(), request.getName(), request.getOrgId());
        // 返回成功响应（无数据）
        return ResponseFactory.success(null);
    }

    /**
     * 【接口 4】分页查询用户列表
     * <p>
     * 接口信息：
     * - 路径：POST /api/users/page
     * - 请求体：PageUserQueryRequest（JSON 格式）
     * - 响应：分页结果（包含用户列表和总数）
     * <p>
     * 查询条件：
     * - pageNum: 当前页码（从 1 开始）
     * - pageSize: 每页大小
     * - userType: 用户类型过滤（可选）
     * - email: 邮箱模糊查询（可选）
     * - userName: 用户名模糊查询（可选）
     *
     * @param request 分页查询请求体
     * @return 响应实体（包含分页结果）
     */
    @Operation(summary = "分页查询用户列表", description = "根据分页参数和筛选条件查询用户列表")
    @PostMapping("/page")
    public ResponseEntity<Response<IPage<UserInfo>>> pageUsers(@Valid @RequestBody PageUserQueryRequest request) {
        // 调用 Service 分页查询
        IPage<UserInfo> pageInfo = userService.pageUsers(request);
        // 返回成功响应（包含分页结果）
        return ResponseFactory.success(pageInfo);
    }

    /**
     * 【接口 5】删除用户
     * <p>
     * 接口信息：
     * - 路径：DELETE /api/users
     * - 请求体：DeleteUserRequest（JSON 格式）
     * - 响应：成功/失败状态
     * <p>
     * 参数说明：
     * - userId: 用户 ID
     * - userType: 用户类型（用于确定删除哪个表的记录）
     *
     * @param request 删除用户请求体
     * @return 响应实体
     */
    @Operation(summary = "删除用户", description = "删除当前用户")
    @DeleteMapping
    public ResponseEntity<Response<Void>> deleteUser(@Valid @RequestBody DeleteUserRequest request) {
        // 调用 Service 删除用户
        userService.deleteUser(request.getUserId(), request.getUserType());
        // 返回成功响应（无数据）
        return ResponseFactory.success(null);
    }
}
