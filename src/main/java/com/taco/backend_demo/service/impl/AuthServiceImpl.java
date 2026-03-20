package com.taco.backend_demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taco.backend_demo.common.exception.BusinessException;
import com.taco.backend_demo.dto.auth.LoginRequest;
import com.taco.backend_demo.dto.auth.LoginResponse;
import com.taco.backend_demo.dto.user.UserInfo;
import com.taco.backend_demo.entity.PasswordEntity;
import com.taco.backend_demo.entity.TokenEntity;
import com.taco.backend_demo.dto.auth.LoginUserInfo;
import com.taco.backend_demo.service.AuthService;
import com.taco.backend_demo.security.CustomAuthenticationProvider;
import com.taco.backend_demo.utils.JwtUtils;
import com.taco.backend_demo.mapper.mp.PasswordMapper;
import com.taco.backend_demo.mapper.mp.TokenMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.taco.backend_demo.common.message.ErrorMessageCodes.E006;
import static com.taco.backend_demo.common.message.ErrorMessageCodes.E007;

/**
 * 认证服务实现类
 * <p>
 * 核心职责：
 * 1. 用户登录认证
 * 2. JWT 令牌刷新
 * 3. 用户登出
 * 4. 刷新令牌管理
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final CustomAuthenticationProvider authenticationProvider;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final PasswordMapper passwordMapper;
    private final TokenMapper tokenMapper;

    @Override
    @Transactional
    public LoginResponse login(LoginRequest request, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        // 1. 执行用户认证
        Authentication authentication = authenticationProvider.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // 2. 重置密码重试次数
        LoginUserInfo loginUserInfo = (LoginUserInfo) authentication.getPrincipal();
        PasswordEntity passwordEntity = loginUserInfo.getPasswordEntity();
        passwordEntity.setRetryCount(0);
        passwordMapper.updateById(passwordEntity);

        log.info("User logged in successfully: {}", request.getEmail());

        // 3. 生成 JWT 令牌
        UserInfo userInfo = new UserInfo(loginUserInfo);
        String accessToken = jwtUtils.generateAccessToken(userInfo);
        String refreshToken = jwtUtils.generateRefreshToken(userInfo);

        // 4. 存储刷新令牌
        saveRefreshToken(loginUserInfo.getUsername(), refreshToken);

        // 5. 设置刷新令牌 Cookie
        setRefreshTokenCookie(httpResponse, refreshToken);

        // 6. 构建登录响应
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAccessToken(accessToken);
        loginResponse.setUserInfo(userInfo);
        return loginResponse;
    }

    @Override
    @Transactional
    public LoginResponse refreshToken(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        // 1. 从 Cookie 获取刷新令牌
        String refreshToken = getRefreshTokenFromCookie(httpRequest);

        if (refreshToken == null) {
            log.warn("Refresh token not found in cookie");
            throw new BusinessException(E006);
        }

        // 2. 验证令牌有效性
        if (!jwtUtils.validateToken(refreshToken)) {
            log.warn("Invalid refresh token");
            throw new BusinessException(E006);
        }

        // 3. 验证令牌是否存在于数据库
        String email = jwtUtils.extractEmail(refreshToken);
        boolean tokenExists = tokenExists(email, refreshToken);
        if (!tokenExists) {
            log.warn("Refresh token not found in database for user: {}", email);
            throw new BusinessException(E006);
        }

        // 4. 加载用户信息
        PasswordEntity passwordEntity = passwordMapper.selectOne(new LambdaQueryWrapper<PasswordEntity>()
            .eq(PasswordEntity::getEmail, email));

        if (passwordEntity == null) {
            throw new BusinessException(E007);
        }

        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(email);

        // 5. 生成新的访问令牌
        String newAccessToken = jwtUtils.generateAccessToken(userInfo);

        log.info("Access token refreshed for user: {}", email);

        // 6. 返回响应
        LoginResponse response = new LoginResponse();
        response.setAccessToken(newAccessToken);
        response.setUserInfo(userInfo);
        return response;
    }

    @Override
    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        // 1. 获取当前用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            log.warn("No authenticated user found for logout");
            return;
        }

        String email;
        if (authentication.getPrincipal() instanceof UserInfo userInfo) {
            email = userInfo.getEmail();
        } else {
            log.warn("Unknown principal type: {}", authentication.getPrincipal().getClass());
            return;
        }

        // 2. 清除刷新令牌 Cookie
        clearRefreshTokenCookie(response);

        // 3. 删除数据库中的刷新令牌
        tokenMapper.delete(new LambdaQueryWrapper<TokenEntity>()
            .eq(TokenEntity::getEmail, email));

        log.info("User logged out: {}", email);
    }

    // ==================== 私有方法 ====================

    /**
     * 从 Cookie 获取刷新令牌
     */
    private String getRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 验证刷新令牌是否存在于数据库
     */
    private boolean tokenExists(String email, String refreshToken) {
        TokenEntity tokenEntity = tokenMapper.selectOne(new LambdaQueryWrapper<TokenEntity>()
            .eq(TokenEntity::getEmail, email)
            .eq(TokenEntity::getRefreshToken, refreshToken));
        return tokenEntity != null;
    }

    /**
     * 保存刷新令牌到数据库
     */
    private void saveRefreshToken(String email, String refreshToken) {
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setEmail(email);
        tokenEntity.setRefreshToken(refreshToken);
        tokenEntity.setExpiresAt(LocalDateTime.now()
            .plusSeconds(jwtUtils.getRefreshExpirationTime() / 1000));
        tokenMapper.insert(tokenEntity);
    }

    /**
     * 设置刷新令牌 Cookie
     */
    private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // 生产环境应设为 true
        cookie.setPath("/api/auth/refresh");
        cookie.setMaxAge((int) (jwtUtils.getRefreshExpirationTime() / 1000));
        response.addCookie(cookie);
    }

    /**
     * 清除刷新令牌 Cookie
     */
    private void clearRefreshTokenCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/api/auth/refresh");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
