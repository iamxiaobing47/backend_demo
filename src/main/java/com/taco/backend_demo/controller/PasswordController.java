package com.taco.backend_demo.controller;

import com.taco.backend_demo.common.message.ErrorMessageCodes;
import com.taco.backend_demo.common.message.NotificationMessageCodes;
import com.taco.backend_demo.common.response.Response;
import com.taco.backend_demo.common.response.ResponseFactory;
import com.taco.backend_demo.dto.password.ChangePasswordRequest;
import com.taco.backend_demo.entity.PasswordEntity;
import com.taco.backend_demo.mapper.mp.PasswordMapper;
import com.taco.backend_demo.security.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Tag(name = "密码管理", description = "用户密码相关接口")
@RestController
@RequestMapping("/api/password")
public class PasswordController {

    @Autowired
    private PasswordMapper passwordMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Operation(summary = "修改密码", description = "修改当前用户密码")
    @PutMapping
    public ResponseEntity<Response<Void>> changePassword(@Valid @RequestBody ChangePasswordRequest request, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseFactory.fail(ErrorMessageCodes.E007);
        }

        if (authentication.getPrincipal() instanceof LoginUser) {
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            String email = loginUser.getUsername();

            // 验证当前密码
            PasswordEntity passwordEntity = passwordMapper.selectByEmail(email);
            if (passwordEntity == null || !passwordEncoder.matches(request.getCurrentPassword(), passwordEntity.getPasswordHash())) {
                return ResponseFactory.fail(ErrorMessageCodes.E011); // Invalid current password
            }

            // 更新密码
            String newHashedPassword = passwordEncoder.encode(request.getNewPassword());
            passwordEntity.setPasswordHash(newHashedPassword);
            passwordEntity.setUpdatedAt(java.time.LocalDateTime.now());
            passwordMapper.updateById(passwordEntity);

            return ResponseFactory.success(null, NotificationMessageCodes.N011);
        }
        
        return ResponseFactory.fail(ErrorMessageCodes.E007);
    }
}