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
        // 检查邮箱是否已存在
        QueryWrapper<PasswordEntity> passwordQuery = new QueryWrapper<>();
        passwordQuery.eq("email", request.getEmail());
        PasswordEntity existingPassword = passwordMapper.selectOne(passwordQuery);
        if (existingPassword != null) {
            throw new BusinessException(ErrorMessageCodes.E008); // User already exists
        }

        // 创建密码记录
        PasswordEntity passwordEntity = new PasswordEntity();
        passwordEntity.setEmail(request.getEmail());
        passwordEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        passwordEntity.setIsLocked(false);
        passwordEntity.setLoginStatus("ACTIVE");
        passwordEntity.setRetryCount(0);
        passwordMapper.insert(passwordEntity);

        // 根据角色创建对应的用户记录
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
            throw new BusinessException(ErrorMessageCodes.E010); // Parameter error
        }

        return ResponseFactory.success(null);
    }

    @Operation(summary = "获取用户信息", description = "根据用户ID获取用户信息")
    @GetMapping("/{userId}")
    public ResponseEntity<Response<UserInfoEntity>> getUser(@PathVariable String userId) {
        LambdaQueryWrapper<UserInfoEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfoEntity::getUserId, userId);
        UserInfoEntity userInfoEntity = vUserInfoMapper.selectOne(queryWrapper);
        if (userInfoEntity == null) {
            throw new BusinessException(ErrorMessageCodes.E010); // User not found
        }
        return ResponseFactory.success(userInfoEntity);
    }

    @Operation(summary = "批量查询用户信息", description = "根据用户ID列表批量查询用户信息")
    @PostMapping("/batch")
    public ResponseEntity<Response<List<UserInfo>>> batchGetUsers(@Valid @RequestBody BatchUserQueryRequest request) {
        List<String> userIds = request.getUserIds();
        if (userIds == null || userIds.isEmpty()) {
            return ResponseFactory.success(null);
        }

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
            throw new BusinessException(ErrorMessageCodes.E010); // Parameter error
        }
        return ResponseFactory.success(null);
    }


    @Operation(summary = "删除用户", description = "删除当前用户")
    @DeleteMapping
    public ResponseEntity<Response<Void>> deleteUser(@Valid @RequestBody DeleteUserRequest request) {
        if (request == null || request.getUserId() == null || request.getUserType() == null) {
            throw new BusinessException(ErrorMessageCodes.E010); // Parameter error
        }

        if ("BUSINESS_USER".equals(request.getUserType())) {
            LambdaQueryWrapper<BusinessUserEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(BusinessUserEntity::getBusinessUserId, request.getUserId());
            businessUserMapper.delete(queryWrapper);
        } else if ("STAFF_USER".equals(request.getUserType())) {
            LambdaQueryWrapper<StaffUserEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(StaffUserEntity::getStaffUserId, request.getUserId());
            staffUserMapper.delete(queryWrapper);
        } else {
            throw new BusinessException(ErrorMessageCodes.E010); // Parameter error
        }

        return ResponseFactory.success(null);
    }
}