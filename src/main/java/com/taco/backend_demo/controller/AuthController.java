package com.taco.backend_demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taco.backend_demo.common.message.ErrorMessageCodes;
import com.taco.backend_demo.common.message.NotificationMessageCodes;
import com.taco.backend_demo.common.response.Response;
import com.taco.backend_demo.common.response.ResponseFactory;
import com.taco.backend_demo.dto.auth.LoginRequest;
import com.taco.backend_demo.dto.auth.LoginResponse;
import com.taco.backend_demo.dto.user.UserInfo;
import com.taco.backend_demo.entity.PasswordEntity;
import com.taco.backend_demo.entity.TokenEntity;
import com.taco.backend_demo.mapper.mp.PasswordMapper;
import com.taco.backend_demo.mapper.mp.TokenMapper;
import com.taco.backend_demo.security.CustomUserDetailsService;
import com.taco.backend_demo.dto.auth.LoginUserInfo;
import com.taco.backend_demo.utils.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import static com.taco.backend_demo.common.message.ErrorMessageCodes.E006;

/**
 * 1. 认证控制器：处理用户登录、登出和令牌刷新等认证相关操作
 * 2. JWT令牌管理：生成访问令牌和刷新令牌，实现双令牌认证机制
 * 3. 安全性保障：密码重试次数重置、安全Cookie设置、令牌存储管理
 */
@Tag(name = "认证管理", description = "用户登录注册相关接口")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

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

    /**
     * 1. 用户登录接口：验证用户凭据并生成JWT令牌
     * @param request 登录请求对象，包含邮箱和密码
     * @param httpRequest HTTP请求对象
     * @param httpResponse HTTP响应对象
     * @return 包含访问令牌和用户信息的登录响应
     */
    @Operation(summary = "用户登录", description = "使用邮箱和密码登录")
    @PostMapping("/login")
    public ResponseEntity<Response<LoginResponse>> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {

        // 1. 执行用户认证：验证邮箱和密码的有效性
        Authentication authentication = customAuthenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        // 2. 重置密码重试次数：登录成功后重置失败计数
        LoginUserInfo loginUserInfo = (LoginUserInfo) authentication.getPrincipal();
        PasswordEntity passwordEntity = loginUserInfo.getPasswordEntity();
        passwordEntity.setRetryCount(0);
        passwordMapper.updateById(passwordEntity);

        // 3. 生成JWT令牌：创建访问令牌和刷新令牌
        UserInfo userInfo = new UserInfo(loginUserInfo);
        String accessToken = jwtUtils.generateAccessToken(userInfo);
        String refreshToken = jwtUtils.generateRefreshToken(userInfo);

        // 4. 存储刷新令牌：将刷新令牌保存到数据库以备后续验证
        saveRefreshToken(loginUserInfo.getUsername(), refreshToken);

        // 5. 设置刷新令牌Cookie：通过HttpOnly Cookie安全传输刷新令牌
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false); // 开发环境设为false，生产环境应为true
        refreshTokenCookie.setPath("/api/auth/refresh");
        refreshTokenCookie.setMaxAge((int) (jwtUtils.getRefreshExpirationTime() / 1000)); // 转换为秒
        httpResponse.addCookie(refreshTokenCookie);

        // 6. 构建登录响应：返回访问令牌和用户基本信息
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAccessToken(accessToken);
        loginResponse.setUserInfo(userInfo);
        return ResponseFactory.success(loginResponse);
    }

    /**
     * 2. 刷新令牌接口：使用有效的刷新令牌获取新的访问令牌
     * @param httpRequest HTTP 请求对象
     * @param httpResponse HTTP 响应对象
     * @return 包含新访问令牌和用户信息的登录响应
     */
    @Operation(summary = "刷新 Token", description = "使用 refresh token 获取新的 access token")
    @PostMapping("/refresh")
    public ResponseEntity<Response<LoginResponse>> refreshToken(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        // 1. 从 Cookie 获取刷新令牌
        Cookie[] cookies = httpRequest.getCookies();
        String refreshToken = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        // 2. 验证刷新令牌：检查令牌的有效性和签名
        if (refreshToken == null || !jwtUtils.validateToken(refreshToken)) {
            return ResponseFactory.fail(E006);
        }

        // 3. 提取用户信息：从刷新令牌中获取用户邮箱
        String email = jwtUtils.extractEmail(refreshToken);
        UserInfo userInfo = (UserInfo)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 4. 生成新的访问令牌
        String newAccessToken = jwtUtils.generateAccessToken(userInfo);

        // 5. 返回包含新访问令牌和用户信息的响应
        LoginResponse response = new LoginResponse();
        response.setAccessToken(newAccessToken);
        response.setUserInfo(userInfo);
        return ResponseFactory.success(response);
    }

    /**
     * 3. 保存刷新令牌：将刷新令牌存储到数据库中
     * @param email 用户邮箱
     * @param refreshToken 刷新令牌字符串
     */
    private void saveRefreshToken(String email, String refreshToken) {
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setEmail(email);
        tokenEntity.setRefreshToken(refreshToken);
        tokenEntity.setExpiresAt(java.time.LocalDateTime.now().plusSeconds(jwtUtils.getRefreshExpirationTime() / 1000));
        tokenMapper.insert(tokenEntity);
    }

    /**
     * 4. 用户登出接口：清除认证信息并删除刷新令牌
     * @param request HTTP请求对象
     * @param response HTTP响应对象
     * @return 成功登出的响应
     */
    @Operation(summary = "用户登出", description = "清除认证信息并删除refresh token")
    @PostMapping("/logout")
    public ResponseEntity<Response<Void>> logout(HttpServletRequest request, HttpServletResponse response) {
        // 1. 清除刷新令牌Cookie：使客户端的刷新令牌失效
        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false);
        refreshTokenCookie.setPath("/api/auth/refresh");
        refreshTokenCookie.setMaxAge(0); // 立即过期
        response.addCookie(refreshTokenCookie);

        // 2. 删除数据库中的刷新令牌：确保服务器端令牌也失效
        UserInfo userInfo = (UserInfo)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        tokenMapper.delete(new LambdaQueryWrapper<TokenEntity>().eq(TokenEntity::getEmail, userInfo.getEmail()));
        return ResponseFactory.success(null, NotificationMessageCodes.N021);
    }
}
