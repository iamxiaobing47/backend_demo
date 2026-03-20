package com.taco.backend_demo.controller;

import com.taco.backend_demo.common.response.Response;
import com.taco.backend_demo.common.response.ResponseFactory;
import com.taco.backend_demo.dto.auth.LoginRequest;
import com.taco.backend_demo.dto.auth.LoginResponse;
import com.taco.backend_demo.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器：处理用户登录、登出和令牌刷新等认证相关操作
 */
@Tag(name = "认证管理", description = "用户登录注册相关接口")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 用户登录接口
     * @param request 登录请求对象，包含邮箱和密码
     * @param httpRequest HTTP 请求对象
     * @param httpResponse HTTP 响应对象
     * @return 包含访问令牌和用户信息的登录响应
     */
    @Operation(summary = "用户登录", description = "使用邮箱和密码登录")
    @PostMapping("/login")
    public ResponseEntity<Response<LoginResponse>> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        LoginResponse loginResponse = authService.login(request, httpRequest, httpResponse);
        return ResponseFactory.success(loginResponse);
    }

    /**
     * 刷新令牌接口
     * @param httpRequest HTTP 请求对象
     * @param httpResponse HTTP 响应对象
     * @return 包含新访问令牌和用户信息的登录响应
     */
    @Operation(summary = "刷新 Token", description = "使用 refresh token 获取新的 access token")
    @PostMapping("/refresh")
    public ResponseEntity<Response<LoginResponse>> refreshToken(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        LoginResponse response = authService.refreshToken(httpRequest, httpResponse);
        return ResponseFactory.success(response);
    }

    /**
     * 用户登出接口
     * @param request HTTP 请求对象
     * @param response HTTP 响应对象
     * @return 成功登出的响应
     */
    @Operation(summary = "用户登出", description = "清除认证信息并删除 refresh token")
    @PostMapping("/logout")
    public ResponseEntity<Response<Void>> logout(HttpServletRequest request, HttpServletResponse response) {
        authService.logout(request, response);
        return ResponseFactory.success(null);
    }
}
