package com.taco.backend_demo.security;

import com.taco.backend_demo.dto.auth.LoginUserInfo;
import com.taco.backend_demo.dto.user.UserInfo;
import com.taco.backend_demo.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 1. JWT认证过滤器：拦截所有请求并验证JWT令牌的有效性
 * 2. 安全上下文管理：为有效令牌设置Spring Security认证上下文
 * 3. 免认证路径：/api/test路径下的请求跳过JWT验证
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    /**
     * 1. 内部过滤逻辑：处理每个HTTP请求的JWT认证流程
     * @param request HTTP请求对象
     * @param response HTTP响应对象  
     * @param filterChain 过滤器链
     * @throws ServletException Servlet异常
     * @throws IOException IO异常
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        // 1. 免认证路径检查：/api/test路径下的请求直接放行
        if (request.getRequestURI().startsWith("/api/test")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 2. 解析并验证JWT令牌：从请求头中提取并验证令牌
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtils.validateToken(jwt)) {
                // 3. 提取用户信息：从JWT中获取用户邮箱
                String email = jwtUtils.extractEmail(jwt);

                // 4. 加载用户详情：通过用户邮箱获取完整的用户认证信息
                LoginUserInfo loginUserInfo = userDetailsService.loadUserByUsername(email);
                
                // 5. 创建认证令牌：构建Spring Security认证对象
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    new UserInfo(loginUserInfo), null, loginUserInfo.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 6. 设置安全上下文：将认证信息存储到SecurityContext中
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 2. 解析JWT令牌：从HTTP请求头中提取Bearer令牌
     * @param request HTTP请求对象
     * @return JWT令牌字符串，如果未找到则返回null
     */
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}
