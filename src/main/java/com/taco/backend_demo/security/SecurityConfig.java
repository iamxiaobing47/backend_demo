package com.taco.backend_demo.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taco.backend_demo.common.response.Response;
import com.taco.backend_demo.common.response.ResponseFactory;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static com.taco.backend_demo.common.message.ErrorMessageCodes.E007;
import static com.taco.backend_demo.common.message.ErrorMessageCodes.E008;

/**
 * Spring Security 核心配置类
 *
 * 【Spring Security 的作用】
 * Spring Security 是一个强大的安全框架，提供认证（Authentication）和授权（Authorization）功能
 *
 * 【核心配置】
 * 1. JWT 无状态认证：基于 JWT 令牌的认证机制，不依赖服务器端会话（Stateless）
 * 2. 前后端分离支持：配置 CORS、禁用 CSRF、自定义异常响应格式
 * 3. 安全过滤器链：配置 JWT 过滤器和权限规则
 *
 * 【过滤器链顺序】
 * 请求 → CORS 过滤器 → JWT 认证过滤器 → 控制器
 *
 * 【重要说明】
 * - CustomAuthenticationProvider 通过 @Component 自动注册，无需在此配置
 * - 使用 @EnableMethodSecurity 启用方法级权限控制
 *
 * @author taco
 */
@Slf4j
@Configuration      // 标记为配置类，Spring 会扫描并加载
@EnableWebSecurity  // 启用 Web 安全配置
@EnableMethodSecurity(prePostEnabled = true)  // 启用方法级权限控制（支持@PreAuthorize 等注解）
public class SecurityConfig {

    // ==================== 依赖注入 ====================

    /**
     * JWT 认证过滤器：通过构造函数注入
     * 负责在每次请求中解析和验证 JWT 令牌
     */
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * 构造函数注入
     * Spring 会自动注入 JwtAuthenticationFilter
     *
     * @param jwtAuthenticationFilter JWT 认证过滤器
     */
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    // ==================== Bean 配置 ====================

    /**
     * 【密码加密器】使用 BCrypt 算法进行密码加密
     * <p>
     * BCrypt 特点：
     * - 自适应：通过 strength 参数控制计算复杂度
     * - 加盐：自动生成随机盐值，防止彩虹表攻击
     * - 不可逆：无法从加密结果还原原始密码
     * <p>
     * strength=12 表示迭代次数为 2^12=4096 次
     * 数值越大越安全，但计算时间也越长
     *
     * @return PasswordEncoder 加密器实例
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCryptPasswordEncoder 是 Spring Security 提供的 BCrypt 实现
        return new BCryptPasswordEncoder(12);
    }

    /**
     * 【CORS 配置】支持前后端分离架构的跨域请求
     * <p>
     * CORS（Cross-Origin Resource Sharing）：跨域资源共享
     * 用于解决浏览器的同源策略限制，允许前端应用访问不同域的 API
     * <p>
     * 配置说明：
     * - allowedOrigins: 允许的源域名（开发环境）
     * - allowedMethods: 允许的 HTTP 方法
     * - allowedHeaders: 允许的请求头
     * - allowCredentials: 是否允许携带凭证（Cookie、Authorization 头等）
     * - maxAge: 预检请求的缓存时间（秒）
     *
     * @return CorsConfigurationSource CORS 配置源
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 允许的源域名（生产环境应配置具体的前端域名）
        configuration.setAllowedOrigins(List.of(
            "http://localhost:3000",   // Vue/React 开发服务器
            "http://localhost:3001",   // 其他前端服务
            "http://localhost:3002"    // 其他前端服务
        ));
        // 允许的 HTTP 方法
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // 允许的请求头（* 表示所有）
        configuration.setAllowedHeaders(List.of("*"));
        // 允许携带凭证（如 Cookie、Authorization 头）
        configuration.setAllowCredentials(true);
        // 预检请求缓存时间（秒），减少 OPTIONS 请求次数
        configuration.setMaxAge(3600L);

        // 创建配置源并注册
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 应用到所有路径
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * 【安全过滤器链】配置 HTTP 安全策略和权限规则
     * <p>
     * SecurityFilterChain 是 Spring Security 的核心配置，定义了：
     * 1. 哪些请求需要认证
     * 2. 哪些请求可以公开访问
     * 3. 使用什么方式进行认证
     * 4. 如何处理认证失败和授权失败
     * <p>
     * 配置流程：
     * 1. 禁用 CSRF（JWT 不需要）
     * 2. 启用 CORS
     * 3. 禁用 HTTP Basic
     * 4. 设置无状态会话
     * 5. 配置异常处理
     * 6. 设置权限规则
     * 7. 添加 JWT 过滤器
     *
     * @param http HttpSecurity 配置对象
     * @return SecurityFilterChain 安全过滤器链
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 【步骤 1】禁用 CSRF 保护
            // JWT 使用令牌认证，不依赖 Cookie 会话，不需要 CSRF 保护
            .csrf(csrf -> csrf.disable())

            // 【步骤 2】启用 CORS 配置
            // 允许跨域请求，支持前后端分离架构
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // 【步骤 3】禁用 HTTP Basic 认证
            // 使用 JWT 认证，不需要传统的 Basic Auth
            .httpBasic(basic -> basic.disable())

            // 【步骤 4】无状态会话管理
            // STATELESS 表示不创建 HTTP Session，每次请求都通过 JWT 认证
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // 【步骤 5】自定义异常处理
            // 认证失败和授权失败时返回统一的 JSON 响应格式
            .exceptionHandling(ex -> {
                ObjectMapper objectMapper = new ObjectMapper();
                // 认证失败（401）：未登录用户访问受保护资源
                ex.authenticationEntryPoint((request, response, authException) -> {
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    Response<Void> res = ResponseFactory.<Void>fail(E007).getBody();
                    response.getWriter().write(objectMapper.writeValueAsString(res));
                });
                // 授权失败（403）：已登录但权限不足
                ex.accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    Response<Void> res = ResponseFactory.<Void>fail(E008).getBody();
                    response.getWriter().write(objectMapper.writeValueAsString(res));
                });
            })

            // 【步骤 6】权限规则配置
            .authorizeHttpRequests(auth -> auth
                // 免认证路径：登录、注册、测试接口
                .requestMatchers("/api/auth/**", "/api/test/**").permitAll()
                // Swagger 文档路径：公开访问
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                // 其他所有请求都需要认证
                .anyRequest().authenticated()
            )

            // 【步骤 7】添加 JWT 过滤器
            // 在 UsernamePasswordAuthenticationFilter 之前执行 JWT 认证
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
