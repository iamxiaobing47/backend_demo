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
 * <p>
 * 核心职责：
 * 1. 验证用户密码
 * 2. 构建认证成功的 Authentication 对象
 * <p>
 * 注意：账户状态检查（锁定、启用等）已在 CustomUserDetailsService 中完成
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    /**
     * 执行认证逻辑
     *
     * @param authentication 包含用户名和密码的认证对象
     * @return 认证成功的 Authentication 对象
     * @throws AuthenticationException 认证失败时抛出异常
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 1. 提取认证凭据
        String email = authentication.getName();
        String rawPassword = authentication.getCredentials() != null
            ? authentication.getCredentials().toString()
            : "";

        log.info("Authenticating user: {}", email);

        // 2. 加载用户详情（包含账户状态检查）
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        // 3. 验证密码匹配
        if (!passwordEncoder.matches(rawPassword, userDetails.getPassword())) {
            log.warn("Invalid password for user: {}", email);
            throw new AuthenticationException("Invalid credentials") {};
        }

        // 4. 构建认证令牌
        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null, // 认证成功后不暴露密码
                userDetails.getAuthorities()
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
