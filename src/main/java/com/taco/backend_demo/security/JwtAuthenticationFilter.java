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
 *
 * 【过滤器的作用】
 * Spring Security 过滤器链中的一个环节，在用户访问受保护资源前进行身份验证
 *
 * 【核心职责】
 * 1. 从请求头中提取 JWT 令牌（格式：Bearer <token>）
 * 2. 验证令牌有效性（签名 + 过期时间）
 * 3. 为有效令牌设置 Spring Security 认证上下文，使后续流程可以获取用户信息
 *
 * 【工作流程】
 * 请求 → JWT 过滤器 → 验证令牌 → 设置认证 → 控制器处理
 *
 * 【免认证路径】
 * /api/test 路径下的请求跳过 JWT 验证
 *
 * @author taco
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // ==================== 依赖注入 ====================

    /**
     * JWT 工具类：用于解析和验证令牌
     */
    private final JwtUtils jwtUtils;

    /**
     * 自定义用户详情服务：用于从数据库加载用户信息
     */
    private final CustomUserDetailsService userDetailsService;

    /**
     * 【核心方法】过滤器主逻辑
     * <p>
     * OncePerRequestFilter 保证每个请求只执行一次
     * <p>
     * 执行流程：
     * 1. 从请求头解析 JWT 令牌
     * 2. 验证令牌有效性
     * 3. 加载用户信息并设置认证上下文
     * 4. 放行请求到下一个过滤器
     *
     * @param request HTTP 请求对象
     * @param response HTTP 响应对象
     * @param filterChain 过滤器链，必须调用 doFilter 放行请求
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            // 【步骤 1】解析 JWT 令牌
            // 从 Authorization 请求头提取，格式：Bearer <token>
            String jwt = parseJwt(request);

            // 【步骤 2】验证令牌并设置认证
            // 检查令牌是否存在且有效
            if (StringUtils.hasText(jwt) && jwtUtils.validateToken(jwt)) {
                // 【步骤 2.1】从令牌中提取用户邮箱
                String email = jwtUtils.extractEmail(jwt);

                // 【步骤 2.2】从数据库加载用户详情
                // CustomUserDetailsService 会查询数据库获取用户权限等信息
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                // 【步骤 2.3】构建认证对象
                // UsernamePasswordAuthenticationToken 是 Spring Security 的认证令牌
                // 参数说明：
                // - principal: 主体信息（这里是 UserInfo 用户对象）
                // - credentials: 凭证（null，因为已经认证过了）
                // - authorities: 权限列表
                LoginUserInfo loginUserInfo = (LoginUserInfo) userDetails;
                UserInfo userInfo = new UserInfo(
                    null,                                   // pk
                    loginUserInfo.getUserType(),            // userType
                    loginUserInfo.getEmail(),               // email
                    loginUserInfo.getName(),                // userName
                    loginUserInfo.getOrgId()                // orgId
                );
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userInfo,                           // 用户主体
                        null,                               // 凭证（不需要）
                        userDetails.getAuthorities()        // 用户权限
                );

                // 设置认证详情（如远程地址、Session ID 等）
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 【步骤 3】设置安全上下文
                // 将认证信息绑定到当前线程，后续可以通过 SecurityContextHolder 获取
                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.debug("Set authentication for user: {}", email);
            }
        } catch (Exception e) {
            // 捕获所有异常，避免过滤器中断请求
            // 认证失败不会阻断请求，只是不设置认证上下文
            log.error("Cannot set user authentication: {}", e.getMessage());
        }

        // 【步骤 4】放行请求
        // 无论认证成功与否，都继续执行过滤器链
        // 如果认证失败，后续需要认证的资源会返回 401
        filterChain.doFilter(request, response);
    }

    // ==================== 辅助方法 ====================

    /**
     * 从 HTTP 请求头中解析 JWT 令牌
     * <p>
     * 期望的 Authorization 头格式：Bearer <token>
     * 例如：Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
     *
     * @param request HTTP 请求对象
     * @return JWT 令牌字符串，如果未找到则返回 null
     */
    private String parseJwt(HttpServletRequest request) {
        // 获取 Authorization 请求头
        String headerAuth = request.getHeader("Authorization");

        // 检查请求头是否存在且以 "Bearer " 开头
        // 使用 substring(7) 去掉 "Bearer " 前缀，获取纯令牌
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}
