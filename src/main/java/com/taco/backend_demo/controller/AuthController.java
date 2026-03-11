package com.taco.backend_demo.controller;

import com.taco.backend_demo.common.message.ErrorMessageCodes;
import com.taco.backend_demo.common.message.NotificationMessageCodes;
import com.taco.backend_demo.common.response.Response;
import com.taco.backend_demo.common.response.ResponseFactory;
import com.taco.backend_demo.dto.auth.LoginRequest;
import com.taco.backend_demo.dto.auth.LoginResponse;
import com.taco.backend_demo.entity.PasswordEntity;
import com.taco.backend_demo.entity.TokenEntity;
import com.taco.backend_demo.mapper.mp.PasswordMapper;
import com.taco.backend_demo.mapper.mp.TokenMapper;
import com.taco.backend_demo.security.CustomUserDetailsService;
import com.taco.backend_demo.security.LoginUser;
import com.taco.backend_demo.utils.JwtUtils;
import com.taco.backend_demo.validation.PasswordStrength;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

    @Autowired
    private TokenMapper tokenMapper;

    @Operation(summary = "用户登录", description = "使用邮箱和密码登录")
    @PostMapping("/login")
    public ResponseEntity<Response<LoginResponse>> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        Authentication authentication = customAuthenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String accessToken = jwtUtils.generateToken(loginUser);
        String refreshToken = jwtUtils.generateRefreshToken(loginUser.getUsername());

        userDetailsService.updateLastLogin(request.getEmail());
        userDetailsService.resetRetryCount(request.getEmail());

        saveRefreshToken(loginUser.getUsername(), refreshToken);

        // 设置refreshToken cookie
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false); // 开发环境设为false，生产环境应为true
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge((int) (jwtUtils.getRefreshExpirationTime() / 1000)); // 转换为秒
        httpResponse.addCookie(refreshTokenCookie);
        
        // Debug log
        System.out.println("Setting refreshToken cookie: " + refreshToken);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAccessToken(accessToken);
        loginResponse.setRefreshToken(refreshToken);
        loginResponse.setUsername(loginUser.getUsername());
        loginResponse.setRole(loginUser.getRole());  // Add user role
        loginResponse.setBusinessOwnerId(loginUser.getBusinessOwnerId());  // Add business owner ID
        loginResponse.setLocationId(loginUser.getLocationId());  // Add location ID
        loginResponse.setExpiresIn(jwtUtils.getExpirationTime());
        loginResponse.setRefreshExpiresIn(jwtUtils.getRefreshExpirationTime());

        return ResponseFactory.success(loginResponse, NotificationMessageCodes.N020);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RefreshTokenRequest {
        @NotBlank(message = "E014")
        private String refreshToken;

        public String getRefreshToken() { return refreshToken; }
        public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
    }

    @Operation(summary = "刷新Token", description = "使用refresh token获取新的access token")
    @PostMapping("/refresh")
    public ResponseEntity<Response<LoginResponse>> refreshToken(@Valid @RequestBody RefreshTokenRequest request, HttpServletResponse httpResponse) {
        String refreshToken = request.getRefreshToken();

        if (!jwtUtils.validateToken(refreshToken)) {
            return ResponseFactory.fail(ErrorMessageCodes.E006);
        }

        String username = jwtUtils.extractUsername(refreshToken);
        LoginUser loginUser = userDetailsService.loadUserByUsername(username);

        String newAccessToken = jwtUtils.generateToken(loginUser);
        String newRefreshToken = jwtUtils.generateRefreshToken(username);

        saveRefreshToken(username, newRefreshToken);

        // 更新refreshToken cookie
        Cookie refreshTokenCookie = new Cookie("refreshToken", newRefreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false); // 开发环境设为false，生产环境应为true
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge((int) (jwtUtils.getRefreshExpirationTime() / 1000)); // 转换为秒
        httpResponse.addCookie(refreshTokenCookie);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAccessToken(newAccessToken);
        loginResponse.setRefreshToken(newRefreshToken);
        loginResponse.setUsername(loginUser.getUsername());
        loginResponse.setExpiresIn(jwtUtils.getExpirationTime());
        loginResponse.setRefreshExpiresIn(jwtUtils.getRefreshExpirationTime());

        return ResponseFactory.success(loginResponse, NotificationMessageCodes.N023);
    }

    private void saveRefreshToken(String email, String refreshToken) {
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setEmail(email);
        tokenEntity.setRefreshToken(refreshToken);
        tokenEntity.setExpiresAt(java.time.LocalDateTime.now().plusSeconds(jwtUtils.getRefreshExpirationTime() / 1000));
        tokenMapper.insert(tokenEntity);
    }

    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateTestUserRequest {
        @NotBlank(message = "E014")
        @Email(message = "E015")
        private String email;
        
        @NotBlank(message = "E014")
        @PasswordStrength(value = PasswordStrength.StrengthLevel.SIMPLE, message = "E016")
        private String password;
        
        public String getEmail() {
            return email;
        }
        
        public void setEmail(String email) {
            this.email = email;
        }
        
        public String getPassword() {
            return password;
        }
        
        public void setPassword(String password) {
            this.password = password;
        }
    }

    @Operation(summary = "创建测试用户", description = "快速创建加密后的测试用户")
    @PostMapping("/create-test-user")
    public ResponseEntity<Response<Void>> createTestUser(@Valid @RequestBody CreateTestUserRequest request) {
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        PasswordEntity passwordEntity = new PasswordEntity();
        passwordEntity.setEmail(request.getEmail());
        passwordEntity.setPasswordHash(hashedPassword);
        passwordEntity.setIsLocked(false);
        passwordEntity.setLoginStatus("ACTIVE");
        passwordEntity.setRetryCount(0);

        passwordMapper.insert(passwordEntity);

        return ResponseFactory.success(null, NotificationMessageCodes.N024);
    }

    @Operation(summary = "用户登出", description = "清除认证信息并删除refresh token")
    @PostMapping("/logout")
    public ResponseEntity<Response<Void>> logout(HttpServletRequest request, HttpServletResponse response) {
        // 清除refreshToken cookie
        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0); // 立即过期
        response.addCookie(refreshTokenCookie);

        // 从数据库删除refresh token（如果需要）
        // 这里可以添加逻辑来删除数据库中的refresh token记录

        return ResponseFactory.success(null, NotificationMessageCodes.N021);
    }
}
