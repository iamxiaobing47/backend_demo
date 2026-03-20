package com.taco.backend_demo.service;

import com.taco.backend_demo.dto.auth.LoginResponse;
import com.taco.backend_demo.dto.auth.LoginRequest;
import com.taco.backend_demo.dto.user.UserInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 认证服务接口
 */
public interface AuthService {

    /**
     * 用户登录
     * @param request 登录请求
     * @param httpRequest HTTP 请求
     * @param httpResponse HTTP 响应
     * @return 登录响应
     */
    LoginResponse login(LoginRequest request, HttpServletRequest httpRequest, HttpServletResponse httpResponse);

    /**
     * 刷新令牌
     * @param httpRequest HTTP 请求
     * @param httpResponse HTTP 响应
     * @return 登录响应
     */
    LoginResponse refreshToken(HttpServletRequest httpRequest, HttpServletResponse httpResponse);

    /**
     * 用户登出
     * @param request HTTP 请求
     * @param response HTTP 响应
     */
    void logout(HttpServletRequest request, HttpServletResponse response);
}
