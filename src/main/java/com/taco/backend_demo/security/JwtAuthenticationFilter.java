package com.taco.backend_demo.security;

import com.taco.backend_demo.dto.auth.LoginUserInfo;
import com.taco.backend_demo.dto.user.UserInfo;
import com.taco.backend_demo.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 认证过滤器：拦截所有请求并验证 JWT 令牌的有效性
 * <p>
 * 核心职责：
 * 1. 从请求头中提取 JWT 令牌
 * 2. 验证令牌有效性
 * 3. 为有效令牌设置 Spring Security 认证上下文
 * <p>
 * 免认证路径：/api/test 路径下的请求跳过 JWT 验证
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            // 1. 解析 JWT 令牌
            String jwt = parseJwt(request);

            if (StringUtils.hasText(jwt) && jwtUtils.validateToken(jwt)) {
                // 2. 提取用户邮箱并加载用户详情
                String email = jwtUtils.extractEmail(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                // 3. 构建认证上下文
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        new UserInfo((LoginUserInfo) userDetails),
                        null,
                        userDetails.getAuthorities()
                );

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 4. 设置安全上下文
                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.debug("Set authentication for user: {}", email);
            }
        } catch (Exception e) {
            log.error("Cannot set user authentication: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 从 HTTP 请求头中解析 JWT 令牌
     *
     * @param request HTTP 请求对象
     * @return JWT 令牌字符串，如果未找到则返回 null
     */
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}
