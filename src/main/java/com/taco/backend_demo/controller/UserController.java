package com.taco.backend_demo.controller;

import com.taco.backend_demo.common.message.ErrorMessageCodes;
import com.taco.backend_demo.common.message.NotificationMessageCodes;
import com.taco.backend_demo.common.response.Response;
import com.taco.backend_demo.common.response.ResponseFactory;
import com.taco.backend_demo.dto.user.CreateUserRequest;
import com.taco.backend_demo.dto.user.UpdateUserRequest;
import com.taco.backend_demo.dto.user.UserInfoDTO;
import com.taco.backend_demo.entity.BusinessUserEntity;
import com.taco.backend_demo.entity.PasswordEntity;
import com.taco.backend_demo.entity.StaffUserEntity;
import com.taco.backend_demo.mapper.mp.BusinessUserMapper;
import com.taco.backend_demo.mapper.mp.PasswordMapper;
import com.taco.backend_demo.mapper.mp.StaffUserMapper;
import com.taco.backend_demo.security.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Tag(name = "用户管理", description = "用户信息相关接口")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private PasswordMapper passwordMapper;

    @Autowired
    private BusinessUserMapper businessUserMapper;

    @Autowired
    private StaffUserMapper staffUserMapper;

    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户的基本信息")
    @GetMapping("/current")
    public ResponseEntity<Response<UserInfoDTO>> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseFactory.fail(ErrorMessageCodes.E007); // Not authenticated
        }
        
        if (authentication.getPrincipal() instanceof LoginUser) {
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            
            UserInfoDTO userInfo = new UserInfoDTO(
                    loginUser.getUsername(),  // email
                    loginUser.getUsername(),  // username
                    loginUser.getRole(),
                    loginUser.getBusinessOwnerId(),
                    loginUser.getLocationId()
            );
            
            return ResponseFactory.success(userInfo, NotificationMessageCodes.N025);
        }
        
        return ResponseFactory.fail(ErrorMessageCodes.E007); // Not authenticated
    }

    @Operation(summary = "创建用户", description = "创建新用户")
    @PostMapping
    public ResponseEntity<Response<Void>> createUser(@Valid @RequestBody CreateUserRequest request) {
        // 检查邮箱是否已存在
        PasswordEntity existingPassword = passwordMapper.selectByEmail(request.getEmail());
        if (existingPassword != null) {
            return ResponseFactory.fail(ErrorMessageCodes.E008); // User already exists
        }

        // 根据角色创建对应的用户记录
        if ("business_owner".equals(request.getRole())) {
            BusinessUserEntity businessUser = new BusinessUserEntity();
            businessUser.setEmail(request.getEmail());
            businessUser.setName(request.getName());
            businessUser.setPosition(request.getPosition());
            businessUser.setBusinessId(request.getBusinessId());
            businessUser.setCreatedAt(LocalDateTime.now());
            businessUser.setUpdatedAt(LocalDateTime.now());
            businessUserMapper.insert(businessUser);
        } else if ("employee".equals(request.getRole())) {
            StaffUserEntity staffUser = new StaffUserEntity();
            staffUser.setEmail(request.getEmail());
            staffUser.setName(request.getName());
            staffUser.setPosition(request.getPosition());
            staffUser.setLocationId(request.getLocationId());
            staffUser.setCreatedAt(LocalDateTime.now());
            staffUser.setUpdatedAt(LocalDateTime.now());
            staffUserMapper.insert(staffUser);
        } else {
            return ResponseFactory.fail(ErrorMessageCodes.E010); // Parameter error
        }

        return ResponseFactory.success(null, NotificationMessageCodes.N024);
    }

    @Operation(summary = "获取用户信息", description = "根据用户ID获取用户信息")
    @GetMapping("/{userId}")
    public ResponseEntity<Response<UserInfoDTO>> getUser(@PathVariable Long userId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseFactory.fail(ErrorMessageCodes.E007);
        }

        if (authentication.getPrincipal() instanceof LoginUser) {
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            String email = loginUser.getUsername();
            
            // 获取密码信息
            PasswordEntity passwordEntity = passwordMapper.selectByEmail(email);
            if (passwordEntity == null) {
                return ResponseFactory.fail(ErrorMessageCodes.E010); // User not found
            }
            
            UserInfoDTO userInfo = new UserInfoDTO();
            userInfo.setEmail(email);
            userInfo.setUsername(email);
            userInfo.setRole(loginUser.getRole());
            userInfo.setBusinessOwnerId(loginUser.getBusinessOwnerId());
            userInfo.setLocationId(loginUser.getLocationId());
            
            return ResponseFactory.success(userInfo, NotificationMessageCodes.N025);
        }
        
        return ResponseFactory.fail(ErrorMessageCodes.E007);
    }

    @Operation(summary = "更新用户信息", description = "更新当前用户信息")
    @PutMapping
    public ResponseEntity<Response<Void>> updateUser(@Valid @RequestBody UpdateUserRequest request, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseFactory.fail(ErrorMessageCodes.E007);
        }

        if (authentication.getPrincipal() instanceof LoginUser) {
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            String email = loginUser.getUsername();
            String role = loginUser.getRole();

            // 更新对应的用户表
            if ("business_owner".equals(role)) {
                BusinessUserEntity businessUser = businessUserMapper.selectByEmail(email);
                if (businessUser == null) {
                    return ResponseFactory.fail(ErrorMessageCodes.E010);
                }
                if (request.getName() != null) {
                    businessUser.setName(request.getName());
                }
                if (request.getPosition() != null) {
                    businessUser.setPosition(request.getPosition());
                }
                if (request.getBusinessId() != null) {
                    businessUser.setBusinessId(request.getBusinessId());
                }
                businessUser.setUpdatedAt(LocalDateTime.now());
                businessUserMapper.updateById(businessUser);
            } else if ("employee".equals(role)) {
                StaffUserEntity staffUser = staffUserMapper.selectByEmail(email);
                if (staffUser == null) {
                    return ResponseFactory.fail(ErrorMessageCodes.E010);
                }
                if (request.getName() != null) {
                    staffUser.setName(request.getName());
                }
                if (request.getPosition() != null) {
                    staffUser.setPosition(request.getPosition());
                }
                if (request.getLocationId() != null) {
                    staffUser.setLocationId(request.getLocationId());
                }
                staffUser.setUpdatedAt(LocalDateTime.now());
                staffUserMapper.updateById(staffUser);
            }

            return ResponseFactory.success(null, NotificationMessageCodes.N011);
        }
        
        return ResponseFactory.fail(ErrorMessageCodes.E007);
    }

    @Operation(summary = "删除用户", description = "删除当前用户")
    @DeleteMapping
    public ResponseEntity<Response<Void>> deleteUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseFactory.fail(ErrorMessageCodes.E007);
        }

        if (authentication.getPrincipal() instanceof LoginUser) {
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            String email = loginUser.getUsername();
            String role = loginUser.getRole();

            // 删除对应的用户表记录
            if ("business_owner".equals(role)) {
                businessUserMapper.deleteByEmail(email);
            } else if ("employee".equals(role)) {
                staffUserMapper.deleteByEmail(email);
            }

            // 删除密码记录
            passwordMapper.deleteByEmail(email);

            return ResponseFactory.success(null, NotificationMessageCodes.N012);
        }
        
        return ResponseFactory.fail(ErrorMessageCodes.E007);
    }
}