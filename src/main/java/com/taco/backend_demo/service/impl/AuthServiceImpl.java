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
 *
 * 【核心职责】
 * 1. 用户登录认证：验证用户名密码，颁发 JWT 令牌
 * 2. JWT 令牌刷新：使用 Refresh Token 换取新的 Access Token
 * 3. 用户登出：清除令牌和认证状态
 * 4. 刷新令牌管理：存储和验证 Refresh Token
 *
 * 【双令牌机制说明】
 * - Access Token: 短期令牌（24 小时），用于访问 API
 * - Refresh Token: 长期令牌（7 天），仅用于刷新 Access Token
 *
 * 【登录流程】
 * 1. 用户输入邮箱密码 → 2. 验证凭证 → 3. 生成双令牌 → 4. 返回 Access Token + 设置 Refresh Token Cookie
 *
 * 【刷新流程】
 * 1. 从 Cookie 获取 Refresh Token → 2. 验证有效性 → 3. 生成新 Access Token
 *
 * @author taco
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    // ==================== 依赖注入 ====================

    /**
     * 自定义认证提供者：负责验证用户凭证（邮箱 + 密码）
     */
    private final CustomAuthenticationProvider authenticationProvider;

    /**
     * JWT 工具类：负责生成和验证令牌
     */
    private final JwtUtils jwtUtils;

    /**
     * 密码编码器：负责密码加密和验证
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * 密码 Mapper：操作用户密码表
     */
    private final PasswordMapper passwordMapper;

    /**
     * 令牌 Mapper：操作刷新令牌表
     */
    private final TokenMapper tokenMapper;

    /**
     * 【登录方法】处理用户登录请求
     * <p>
     * 完整流程：
     * 1. 执行认证（验证邮箱密码）
     * 2. 重置密码重试次数（登录成功，清零）
     * 3. 生成 Access Token 和 Refresh Token
     * 4. 保存 Refresh Token 到数据库
     * 5. 设置 Refresh Token Cookie
     * 6. 返回登录结果
     *
     * @param request 登录请求（包含邮箱和密码）
     * @param httpRequest HTTP 请求对象
     * @param httpResponse HTTP 响应对象
     * @return 登录响应（包含 Access Token 和用户信息）
     */
    @Override
    @Transactional
    public LoginResponse login(LoginRequest request, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        // 【步骤 1】执行用户认证
        // authenticate 方法会调用 CustomAuthenticationProvider 验证凭证
        // 如果验证失败，会抛出 AuthenticationException
        Authentication authentication = authenticationProvider.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // 【步骤 2】重置密码重试次数
        // 登录成功后，将密码错误重试次数清零，避免用户被误锁定
        LoginUserInfo loginUserInfo = (LoginUserInfo) authentication.getPrincipal();
        PasswordEntity passwordEntity = loginUserInfo.getPasswordEntity();
        passwordEntity.setRetryCount(0);
        passwordMapper.updateById(passwordEntity);

        log.info("User logged in successfully: {}", request.getEmail());

        // 【步骤 3】生成 JWT 令牌
        // 构建用户信息对象，用于生成令牌
        UserInfo userInfo = new UserInfo(
            null,                                           // pk (不需要)
            loginUserInfo.getUserType(),                    // userType
            loginUserInfo.getEmail(),                       // email
            loginUserInfo.getName(),                        // userName
            loginUserInfo.getOrgId()                        // orgId
        );
        // 生成 Access Token（短期，用于访问 API）
        String accessToken = jwtUtils.generateAccessToken(userInfo);
        // 生成 Refresh Token（长期，用于刷新 Access Token）
        String refreshToken = jwtUtils.generateRefreshToken(userInfo);

        // 【步骤 4】存储刷新令牌
        // 将 Refresh Token 保存到数据库，用于后续验证和登出
        saveRefreshToken(loginUserInfo.getUsername(), refreshToken);

        // 【步骤 5】设置刷新令牌 Cookie
        // 将 Refresh Token 放入 HttpOnly Cookie，防止 XSS 攻击
        setRefreshTokenCookie(httpResponse, refreshToken);

        // 【步骤 6】构建登录响应
        // 只返回 Access Token 和用户信息，Refresh Token 通过 Cookie 传递
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAccessToken(accessToken);
        loginResponse.setUserInfo(userInfo);
        return loginResponse;
    }

    /**
     * 【刷新令牌】使用 Refresh Token 换取新的 Access Token
     * <p>
     * 完整流程：
     * 1. 从 Cookie 获取 Refresh Token
     * 2. 验证令牌有效性（签名 + 过期时间）
     * 3. 验证令牌是否存在于数据库（防止令牌被盗用）
     * 4. 加载用户信息
     * 5. 生成新的 Access Token
     * 6. 返回响应
     *
     * @param httpRequest HTTP 请求对象
     * @param httpResponse HTTP 响应对象
     * @return 刷新后的响应（包含新的 Access Token 和用户信息）
     */
    @Override
    @Transactional
    public LoginResponse refreshToken(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        // 【步骤 1】从 Cookie 获取刷新令牌
        String refreshToken = getRefreshTokenFromCookie(httpRequest);

        // 检查令牌是否存在
        if (refreshToken == null) {
            log.warn("Refresh token not found in cookie");
            throw new BusinessException(E006);  // 令牌不存在
        }

        // 【步骤 2】验证令牌有效性
        // 检查签名是否正确、是否过期
        if (!jwtUtils.validateToken(refreshToken)) {
            log.warn("Invalid refresh token");
            throw new BusinessException(E006);  // 令牌无效
        }

        // 【步骤 3】验证令牌是否存在于数据库
        // 防止令牌被盗用：只有数据库中存在的令牌才有效
        String email = jwtUtils.extractEmail(refreshToken);
        boolean tokenExists = tokenExists(email, refreshToken);
        if (!tokenExists) {
            log.warn("Refresh token not found in database for user: {}", email);
            throw new BusinessException(E006);  // 令牌不存在
        }

        // 【步骤 4】加载用户信息
        // 从数据库查询用户密码实体
        PasswordEntity passwordEntity = passwordMapper.selectOne(new LambdaQueryWrapper<PasswordEntity>()
            .eq(PasswordEntity::getEmail, email));

        if (passwordEntity == null) {
            throw new BusinessException(E007);  // 用户不存在
        }

        // 构建用户信息对象（刷新令牌时只需要邮箱）
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(email);

        // 【步骤 5】生成新的访问令牌
        // 使用 Refresh Token 换取新的 Access Token
        String newAccessToken = jwtUtils.generateAccessToken(userInfo);

        log.info("Access token refreshed for user: {}", email);

        // 【步骤 6】返回响应
        LoginResponse response = new LoginResponse();
        response.setAccessToken(newAccessToken);
        response.setUserInfo(userInfo);
        return response;
    }

    /**
     * 【登出方法】处理用户登出请求
     * <p>
     * 完整流程：
     * 1. 获取当前登录用户
     * 2. 清除 Refresh Token Cookie
     * 3. 删除数据库中的 Refresh Token
     *
     * @param request HTTP 请求对象
     * @param response HTTP 响应对象
     */
    @Override
    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        // 【步骤 1】获取当前用户
        // 从 Spring Security 上下文获取当前认证的用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            log.warn("No authenticated user found for logout");
            return;
        }

        // 提取用户邮箱
        String email;
        if (authentication.getPrincipal() instanceof UserInfo userInfo) {
            email = userInfo.getEmail();
        } else {
            log.warn("Unknown principal type: {}", authentication.getPrincipal().getClass());
            return;
        }

        // 【步骤 2】清除刷新令牌 Cookie
        // 设置 MaxAge 为 0，删除浏览器中的 Cookie
        clearRefreshTokenCookie(response);

        // 【步骤 3】删除数据库中的刷新令牌
        // 确保该 Refresh Token 不能再被使用
        tokenMapper.delete(new LambdaQueryWrapper<TokenEntity>()
            .eq(TokenEntity::getEmail, email));

        log.info("User logged out: {}", email);
    }

    // ==================== 私有方法 ====================

    /**
     * 【工具】从 Cookie 获取刷新令牌
     * <p>
     * 遍历所有 Cookie，查找名为 "refreshToken" 的 Cookie 值
     *
     * @param request HTTP 请求对象
     * @return Refresh Token 字符串，未找到返回 null
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
     * 【工具】验证刷新令牌是否存在于数据库
     * <p>
     * 通过邮箱和令牌内容查询，确保令牌未被注销
     *
     * @param email 用户邮箱
     * @param refreshToken 刷新令牌
     * @return 令牌是否存在
     */
    private boolean tokenExists(String email, String refreshToken) {
        TokenEntity tokenEntity = tokenMapper.selectOne(new LambdaQueryWrapper<TokenEntity>()
            .eq(TokenEntity::getEmail, email)
            .eq(TokenEntity::getRefreshToken, refreshToken));
        return tokenEntity != null;
    }

    /**
     * 【工具】保存刷新令牌到数据库
     * <p>
     * 创建新的 TokenEntity 并插入数据库
     * 同时设置过期时间，便于后续清理过期令牌
     *
     * @param email 用户邮箱
     * @param refreshToken 刷新令牌
     */
    private void saveRefreshToken(String email, String refreshToken) {
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setEmail(email);
        tokenEntity.setRefreshToken(refreshToken);
        // 设置过期时间（当前时间 + 令牌有效期）
        tokenEntity.setExpiresAt(LocalDateTime.now()
            .plusSeconds(jwtUtils.getRefreshExpirationTime() / 1000));
        tokenMapper.insert(tokenEntity);
    }

    /**
     * 【工具】设置刷新令牌 Cookie
     * <p>
     * Cookie 配置说明：
     * - HttpOnly: true，防止 XSS 攻击
     * - Secure: false（生产环境应设为 true），防止中间人攻击
     * - Path: 限制 Cookie 发送路径
     * - MaxAge: 有效期（秒）
     *
     * @param response HTTP 响应对象
     * @param refreshToken 刷新令牌
     */
    private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);                        // 防止 JavaScript 访问
        cookie.setSecure(false); // 生产环境应设为 true  // 仅 HTTPS 传输
        cookie.setPath("/api/auth/refresh");             // 限制 Cookie 发送路径
        cookie.setMaxAge((int) (jwtUtils.getRefreshExpirationTime() / 1000));  // 有效期（秒）
        response.addCookie(cookie);
    }

    /**
     * 【工具】清除刷新令牌 Cookie
     * <p>
     * 设置 MaxAge 为 0，浏览器会自动删除该 Cookie
     *
     * @param response HTTP 响应对象
     */
    private void clearRefreshTokenCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/api/auth/refresh");
        cookie.setMaxAge(0);  // 立即过期
        response.addCookie(cookie);
    }
}
