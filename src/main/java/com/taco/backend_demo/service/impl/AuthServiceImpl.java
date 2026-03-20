package com.taco.backend_demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taco.backend_demo.common.exception.BusinessException;
import com.taco.backend_demo.dto.auth.LoginRequest;
import com.taco.backend_demo.dto.auth.LoginResponse;
import com.taco.backend_demo.dto.user.UserInfo;
import com.taco.backend_demo.entity.PasswordEntity;
import com.taco.backend_demo.entity.TokenEntity;
import com.taco.backend_demo.mapper.mp.PasswordMapper;
import com.taco.backend_demo.mapper.mp.TokenMapper;
import com.taco.backend_demo.security.CustomUserDetailsService;
import com.taco.backend_demo.dto.auth.LoginUserInfo;
import com.taco.backend_demo.service.AuthService;
import com.taco.backend_demo.utils.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.taco.backend_demo.common.message.ErrorMessageCodes.E006;

/**
 * 认证服务实现类
 */
@Service
public class AuthServiceImpl implements AuthService {

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

    @Override
    public LoginResponse login(LoginRequest request, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        // 1. 执行用户认证：验证邮箱和密码的有效性
        Authentication authentication = customAuthenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // 2. 重置密码重试次数：登录成功后重置失败计数
        LoginUserInfo loginUserInfo = (LoginUserInfo) authentication.getPrincipal();
        PasswordEntity passwordEntity = loginUserInfo.getPasswordEntity();
        passwordEntity.setRetryCount(0);
        passwordMapper.updateById(passwordEntity);

        // 3. 生成 JWT 令牌：创建访问令牌和刷新令牌
        UserInfo userInfo = new UserInfo(loginUserInfo);
        String accessToken = jwtUtils.generateAccessToken(userInfo);
        String refreshToken = jwtUtils.generateRefreshToken(userInfo);

        // 4. 存储刷新令牌：将刷新令牌保存到数据库以备后续验证
        saveRefreshToken(loginUserInfo.getUsername(), refreshToken);

        // 5. 设置刷新令牌 Cookie：通过 HttpOnly Cookie 安全传输刷新令牌
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false); // 开发环境设为 false，生产环境应为 true
        refreshTokenCookie.setPath("/api/auth/refresh");
        refreshTokenCookie.setMaxAge((int) (jwtUtils.getRefreshExpirationTime() / 1000)); // 转换为秒
        httpResponse.addCookie(refreshTokenCookie);

        // 6. 构建登录响应：返回访问令牌和用户基本信息
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAccessToken(accessToken);
        loginResponse.setUserInfo(userInfo);
        return loginResponse;
    }

    @Override
    public LoginResponse refreshToken(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
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
            throw new BusinessException(E006);
        }

        // 3. 提取用户信息：从刷新令牌中获取用户邮箱
        SecurityContextHolder.getContext().getAuthentication();
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 4. 生成新的访问令牌
        String newAccessToken = jwtUtils.generateAccessToken(userInfo);

        // 5. 返回包含新访问令牌和用户信息的响应
        LoginResponse response = new LoginResponse();
        response.setAccessToken(newAccessToken);
        response.setUserInfo(userInfo);
        return response;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        // 1. 清除刷新令牌 Cookie：使客户端的刷新令牌失效
        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false);
        refreshTokenCookie.setPath("/api/auth/refresh");
        refreshTokenCookie.setMaxAge(0); // 立即过期
        response.addCookie(refreshTokenCookie);

        // 2. 删除数据库中的刷新令牌：确保服务器端令牌也失效
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        tokenMapper.delete(new LambdaQueryWrapper<TokenEntity>().eq(TokenEntity::getEmail, userInfo.getEmail()));
    }

    /**
     * 保存刷新令牌
     */
    private void saveRefreshToken(String email, String refreshToken) {
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setEmail(email);
        tokenEntity.setRefreshToken(refreshToken);
        tokenEntity.setExpiresAt(java.time.LocalDateTime.now().plusSeconds(jwtUtils.getRefreshExpirationTime() / 1000));
        tokenMapper.insert(tokenEntity);
    }
}
