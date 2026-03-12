package com.taco.backend_demo.controller;

import com.taco.backend_demo.common.message.ErrorMessageCodes;
import com.taco.backend_demo.common.message.NotificationMessageCodes;
import com.taco.backend_demo.common.response.Response;
import com.taco.backend_demo.common.response.ResponseFactory;
import com.taco.backend_demo.dto.password.ChangePasswordRequest;
import com.taco.backend_demo.entity.PasswordEntity;
import com.taco.backend_demo.mapper.mp.PasswordMapper;
import com.taco.backend_demo.dto.auth.LoginUserInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * 1. 密码控制器：处理用户密码相关的操作（如修改密码）
 * 2. 安全验证：验证当前密码的正确性后再进行密码修改
 * 3. 认证保护：所有操作都需要用户已认证
 */
@Tag(name = "密码管理", description = "用户密码相关接口")
@RestController
@RequestMapping("/api/password")
public class PasswordController {

    @Autowired
    private PasswordMapper passwordMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 1. 修改密码接口：验证当前密码后更新为新密码
     * @param request 修改密码请求对象，包含当前密码和新密码
     * @param authentication Spring Security认证对象
     * @return 密码修改成功的响应
     */
    @Operation(summary = "修改密码", description = "修改当前用户密码")
    @PutMapping
    public ResponseEntity<Response<Void>> changePassword(@Valid @RequestBody ChangePasswordRequest request, Authentication authentication) {
        // 1. 验证用户认证状态
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseFactory.fail(ErrorMessageCodes.E007);
        }

        // 2. 获取当前用户信息
        if (authentication.getPrincipal() instanceof LoginUserInfo) {
            LoginUserInfo loginUserInfo = (LoginUserInfo) authentication.getPrincipal();
            String email = loginUserInfo.getUsername();

            // 3. 验证当前密码是否正确
            PasswordEntity passwordEntity = passwordMapper.selectByEmail(email);
            if (passwordEntity == null || !passwordEncoder.matches(request.getCurrentPassword(), passwordEntity.getPassword())) {
                return ResponseFactory.fail(ErrorMessageCodes.E011);
            }

            // 4. 更新密码为新密码
            String newHashedPassword = passwordEncoder.encode(request.getNewPassword());
            passwordEntity.setPassword(newHashedPassword);
            passwordEntity.setUpdatedAt(java.time.LocalDateTime.now());
            passwordMapper.updateById(passwordEntity);

            return ResponseFactory.success(null, NotificationMessageCodes.N011);
        }

        return ResponseFactory.fail(ErrorMessageCodes.E007);
    }
}
