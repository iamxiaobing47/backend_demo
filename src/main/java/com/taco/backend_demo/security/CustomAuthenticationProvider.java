package com.taco.backend_demo.security;

import com.taco.backend_demo.common.constants.LoginConstants;
import com.taco.backend_demo.dto.auth.LoginUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * 自定义认证提供者：实现 Spring Security 的 AuthenticationProvider 接口
 *
 * 【AuthenticationProvider 的作用】
 * Spring Security 使用 AuthenticationProvider 进行用户认证
 * 它是认证流程的核心组件，负责验证用户凭证
 *
 * 【核心职责】
 * 1. 验证用户密码：比对明文密码和加密后的密码
 * 2. 构建认证成功的 Authentication 对象：供后续流程使用
 *
 * 【认证流程】
 * 1. 用户提交邮箱密码 → 2. CustomAuthenticationProvider 验证 → 3. 认证成功/失败
 *
 * 【重要说明】
 * - 账户状态检查（锁定、启用等）已在 CustomUserDetailsService 中完成
 * - 密码验证使用 BCrypt 算法，支持加盐和自适应复杂度
 *
 * @author taco
 */
@Slf4j
@Component  // Spring 组件，会自动被 Spring Security 扫描并使用
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    // ==================== 依赖注入 ====================

    /**
     * 用户详情服务：从数据库加载用户信息
     * 负责查询用户并检查账户状态（是否锁定、是否启用等）
     */
    private final UserDetailsService userDetailsService;

    /**
     * 密码编码器：负责密码加密和匹配验证
     * 使用 BCrypt 算法，支持 matches(raw, encoded) 方法验证密码
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * 【核心方法】执行认证逻辑
     * <p>
     * Spring Security 会在用户登录时调用此方法
     * <p>
     * 认证流程：
     * 1. 从 Authentication 对象中提取用户名和密码
     * 2. 从数据库加载用户详情（包含账户状态检查）
     * 3. 验证密码是否匹配
     * 4. 构建并返回认证成功的 Authentication 对象
     *
     * @param authentication 包含用户名和密码的认证对象
     *                       类型：UsernamePasswordAuthenticationToken
     * @return 认证成功的 Authentication 对象
     *         包含用户详情、权限列表等信息
     * @throws AuthenticationException 认证失败时抛出异常
     *                                 可能的情况：用户不存在、密码错误、账户被锁定等
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 【步骤 1】提取认证凭据
        // getName() 返回用户名（这里是邮箱）
        String email = authentication.getName();
        // getCredentials() 返回用户凭证（这里是密码）
        // 注意：凭证可能为 null，需要做空值处理
        String rawPassword = authentication.getCredentials() != null
            ? authentication.getCredentials().toString()
            : "";

        log.info("Authenticating user: {}", email);

        // 【步骤 2】加载用户详情
        // 调用 UserDetailsService 从数据库加载用户信息
        // 此步骤会检查：
        // - 用户是否存在
        // - 账户是否被锁定
        // - 账户是否被禁用
        // - 密码是否已过期
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        // 【步骤 3】验证密码匹配
        // matches(rawPassword, encodedPassword) 方法：
        // 1. 对 rawPassword 使用相同的 salt 进行 BCrypt 加密
        // 2. 比较加密结果是否与 encodedPassword 一致
        if (!passwordEncoder.matches(rawPassword, userDetails.getPassword())) {
            log.warn("Invalid password for user: {}", email);
            // 密码不匹配，抛出认证异常
            throw new AuthenticationException("Invalid credentials") {};
        }

        // 【步骤 4】构建认证令牌
        // 认证成功，返回包含用户信息的 Authentication 对象
        // Spring Security 会将此对象存入 SecurityContextHolder
        return new UsernamePasswordAuthenticationToken(
                userDetails,        // 认证主体（用户详情）
                null,               // 凭证（null，认证成功后不暴露密码）
                userDetails.getAuthorities()  // 用户权限列表
        );
    }

    /**
     * 【支持判断】判断该 Provider 是否支持指定的认证类型
     * <p>
     * Spring Security 可能有多个 AuthenticationProvider
     * 每个 Provider 只处理自己支持的认证类型
     * <p>
     * 本 Provider 只支持 UsernamePasswordAuthenticationToken 类型
     * 即用户名 + 密码的认证方式
     *
     * @param authentication 认证类型的 Class 对象
     * @return 是否支持该认证类型
     */
    @Override
    public boolean supports(Class<?> authentication) {
        // isAssignableFrom 判断 Class<?> 是否是 UsernamePasswordAuthenticationToken 或其子类
        // 如果是，返回 true，Spring Security 会使用此 Provider 进行认证
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
