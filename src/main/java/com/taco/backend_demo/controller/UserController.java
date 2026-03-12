package com.taco.backend_demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taco.backend_demo.common.exception.BusinessException;
import com.taco.backend_demo.common.message.ErrorMessageCodes;
import com.taco.backend_demo.common.response.Response;
import com.taco.backend_demo.common.response.ResponseFactory;
import com.taco.backend_demo.dto.user.*;
import com.taco.backend_demo.entity.BusinessUserEntity;
import com.taco.backend_demo.entity.PasswordEntity;
import com.taco.backend_demo.entity.StaffUserEntity;
import com.taco.backend_demo.entity.UserInfoEntity;
import com.taco.backend_demo.mapper.mp.BusinessUserMapper;
import com.taco.backend_demo.mapper.mp.PasswordMapper;
import com.taco.backend_demo.mapper.mp.StaffUserMapper;
import com.taco.backend_demo.mapper.mp.VUserInfoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.taco.backend_demo.common.message.ErrorMessageCodes.E008;

@Tag(name = "用户管理", description = "用户信息相关接口")
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private PasswordMapper passwordMapper;

    @Autowired
    private StaffUserMapper staffUserMapper;

    @Autowired
    private BusinessUserMapper businessUserMapper;

    @Autowired
    private VUserInfoMapper vUserInfoMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Operation(summary = "创建用户", description = "创建新用户")
    @PostMapping("/create")
    public ResponseEntity<Response<Void>> createUser(@Valid @RequestBody CreateUserRequest request) {
        // 1. 验证邮箱唯一性：检查该邮箱是否已被注册
        QueryWrapper<PasswordEntity> passwordQuery = new QueryWrapper<>();
        passwordQuery.eq("email", request.getEmail());
        PasswordEntity existingPassword = passwordMapper.selectOne(passwordQuery);
        if (existingPassword != null) {
            throw new BusinessException(ErrorMessageCodes.E008);
        }

        // 2. 创建密码实体：存储加密后的密码和账户状态信息
        PasswordEntity passwordEntity = new PasswordEntity();
        passwordEntity.setEmail(request.getEmail());
        passwordEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        passwordEntity.setIsLocked(false);
        passwordEntity.setLoginStatus("ACTIVE");
        passwordEntity.setRetryCount(0);
        passwordMapper.insert(passwordEntity);

        // 3. 根据用户类型创建对应的业务用户实体：
        //    - BUSINESS_USER: 商户用户，关联商户ID
        //    - STAFF_USER: 员工用户，关联位置ID
        if ("BUSINESS_USER".equals(request.getUserType())) {
            BusinessUserEntity businessUser = new BusinessUserEntity();
            businessUser.setEmail(request.getEmail());
            businessUser.setName(request.getUserName());
            businessUser.setBusinessId(request.getOrgId());
            businessUser.setBusinessUserId(request.getUserId());
            businessUserMapper.insert(businessUser);
        } else if ("STAFF_USER".equals(request.getUserType())) {
            StaffUserEntity staffUser = new StaffUserEntity();
            staffUser.setEmail(request.getEmail());
            staffUser.setName(request.getUserName());
            staffUser.setLocationId(request.getOrgId());
            staffUser.setStaffUserId(request.getUserId());
            staffUserMapper.insert(staffUser);
        } else {
            throw new BusinessException(ErrorMessageCodes.E010);
        }

        return ResponseFactory.success(null);
    }

    @Operation(summary = "获取用户信息", description = "根据用户ID获取用户信息")
    @GetMapping("/{userId}")
    public ResponseEntity<Response<UserInfoEntity>> getUser(@PathVariable String userId) {
        // 1. 查询用户信息视图：通过用户ID获取完整的用户信息
        LambdaQueryWrapper<UserInfoEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfoEntity::getUserId, userId);
        UserInfoEntity userInfoEntity = vUserInfoMapper.selectOne(queryWrapper);
        if (userInfoEntity == null) {
            throw new BusinessException(ErrorMessageCodes.E010);
        }
        return ResponseFactory.success(userInfoEntity);
    }

    @Operation(summary = "批量查询用户信息", description = "根据用户ID列表批量查询用户信息")
    @PostMapping("/batch")
    public ResponseEntity<Response<List<UserInfo>>> batchGetUsers(@Valid @RequestBody BatchUserQueryRequest request) {
        // 1. 参数验证：检查用户ID列表是否为空
        List<String> userIds = request.getUserIds();
        if (userIds == null || userIds.isEmpty()) {
            return ResponseFactory.success(null);
        }

        // 2. 批量查询用户信息：通过IN查询获取多个用户的完整信息
        QueryWrapper<UserInfoEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("user_id", userIds);
        List<UserInfoEntity> userInfoEntities = vUserInfoMapper.selectList(queryWrapper);
        return ResponseFactory.success(userInfoEntities.stream()
                .map(UserInfo::new)
                .collect(Collectors.toList()));
    }

    @Operation(summary = "更新用户信息", description = "更新当前用户信息")
    @PutMapping
    public ResponseEntity<Response<Void>> updateUser(@Valid @RequestBody UpdateUserRequest request) {
        // 1. 根据用户类型更新对应的用户实体信息：
        //    - BUSINESS_USER: 更新商户用户的基本信息和商户ID
        //    - STAFF_USER: 更新员工用户的基本信息和位置ID
        if ("BUSINESS_USER".equals(request.getUserType())) {
            LambdaQueryWrapper<BusinessUserEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(BusinessUserEntity::getBusinessUserId, request.getUserId());
            BusinessUserEntity businessUser = businessUserMapper.selectOne(queryWrapper);
            businessUser.setName(request.getName());
            businessUser.setBusinessId(request.getOrgId());
            businessUserMapper.updateById(businessUser);
        } else if ("STAFF_USER".equals(request.getUserType())) {
            LambdaQueryWrapper<StaffUserEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(StaffUserEntity::getStaffUserId, request.getUserId());
            StaffUserEntity staffUser = staffUserMapper.selectOne(queryWrapper);
            staffUser.setName(request.getName());
            staffUser.setLocationId(request.getOrgId());
            staffUserMapper.updateById(staffUser);
        } else {
            throw new BusinessException(ErrorMessageCodes.E010);
        }
        return ResponseFactory.success(null);
    }


    @Operation(summary = "分页查询用户列表", description = "根据分页参数和筛选条件查询用户列表")
    @PostMapping("/page")
    public ResponseEntity<Response<PageResult<UserInfo>>> pageUsers(@Valid @RequestBody PageUserQueryRequest request) {
        // 1. 计算分页参数（PostgreSQL 语法：LIMIT ? OFFSET ?）
        int limit = request.getPageSize();
        int offset = (request.getPageNum() - 1) * request.getPageSize();

        // 2. 构建查询条件
        LambdaQueryWrapper<UserInfoEntity> queryWrapper = new LambdaQueryWrapper<>();

        // 用户类型过滤
        if (request.getUserType() != null && !request.getUserType().isEmpty()) {
            queryWrapper.eq(UserInfoEntity::getUserType, request.getUserType());
        }

        // 邮箱模糊查询
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            queryWrapper.like(UserInfoEntity::getEmail, request.getEmail());
        }

        // 用户名模糊查询
        if (request.getUserName() != null && !request.getUserName().isEmpty()) {
            queryWrapper.like(UserInfoEntity::getUserName, request.getUserName());
        }

        // 3. 查询总数
        Long total = vUserInfoMapper.selectCount(queryWrapper);

        // 4. 分页查询数据（PostgreSQL 兼容的 LIMIT/OFFSET 语法）
        queryWrapper.last("LIMIT " + limit + " OFFSET " + offset);
        List<UserInfoEntity> userInfoEntities = vUserInfoMapper.selectList(queryWrapper);

        // 5. 构建返回结果
        PageResult<UserInfo> result = PageResult.of(
            userInfoEntities.stream().map(UserInfo::new).collect(Collectors.toList()),
            total,
            request.getPageNum(),
            request.getPageSize()
        );

        return ResponseFactory.success(result);
    }

    @Operation(summary = "删除用户", description = "删除当前用户")
    @DeleteMapping
    public ResponseEntity<Response<Void>> deleteUser(@Valid @RequestBody DeleteUserRequest request) {
        // 1. 参数验证：确保请求参数完整有效
        if (request == null || request.getUserId() == null || request.getUserType() == null) {
            throw new BusinessException(ErrorMessageCodes.E010);
        }

        // 2. 根据用户类型执行对应的删除操作：
        //    - BUSINESS_USER: 删除商户用户记录
        //    - STAFF_USER: 删除员工用户记录
        if ("BUSINESS_USER".equals(request.getUserType())) {
            LambdaQueryWrapper<BusinessUserEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(BusinessUserEntity::getBusinessUserId, request.getUserId());
            businessUserMapper.delete(queryWrapper);
        } else if ("STAFF_USER".equals(request.getUserType())) {
            LambdaQueryWrapper<StaffUserEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(StaffUserEntity::getStaffUserId, request.getUserId());
            staffUserMapper.delete(queryWrapper);
        } else {
            throw new BusinessException(ErrorMessageCodes.E010);
        }

        return ResponseFactory.success(null);
    }
}