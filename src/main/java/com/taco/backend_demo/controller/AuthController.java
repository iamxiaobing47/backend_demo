package com.taco.backend_demo.controller;

import com.taco.backend_demo.common.response.Response;
import com.taco.backend_demo.common.response.ResponseFactory;
import com.taco.backend_demo.dto.LoginRequest;
import com.taco.backend_demo.dto.LoginResponse;
import com.taco.backend_demo.entity.PasswordEntity;
import com.taco.backend_demo.mapper.mp.PasswordMapper;
import com.taco.backend_demo.security.CustomUserDetailsService;
import com.taco.backend_demo.utils.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Tag(name = "认证管理", description = "用户登录注册相关接口")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager customAuthenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private PasswordMapper passwordMapper;

    @Operation(summary = "用户登录", description = "使用邮箱和密码登录")
    @PostMapping("/login")
    public ResponseEntity<Response<LoginResponse>> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        Authentication authentication = customAuthenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtils.generateToken(userDetails);

        userDetailsService.updateLastLogin(request.getEmail());
        userDetailsService.resetRetryCount(request.getEmail());

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUsername(userDetails.getUsername());
        response.setExpiresIn(jwtUtils.getExpirationTime());

        return ResponseFactory.success(response, "LOGIN_SUCCESS");
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateTestUserRequest {
        private String email;
        private String password;
        private String nickname;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getNickname() { return nickname; }
        public void setNickname(String nickname) { this.nickname = nickname; }
    }

    @Operation(summary = "创建测试用户", description = "快速创建加密后的测试用户")
    @PostMapping("/create-test-user")
    public ResponseEntity<Response<Void>> createTestUser(@RequestBody CreateTestUserRequest request) {
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        PasswordEntity passwordEntity = new PasswordEntity();
        passwordEntity.setEmail(request.getEmail());
        passwordEntity.setPasswordHash(hashedPassword);
        passwordEntity.setIsLocked(false);
        passwordEntity.setLoginStatus("ACTIVE");
        passwordEntity.setRetryCount(0);

        passwordMapper.insert(passwordEntity);

        return ResponseFactory.success(null, "TEST_USER_CREATED");
    }
}
