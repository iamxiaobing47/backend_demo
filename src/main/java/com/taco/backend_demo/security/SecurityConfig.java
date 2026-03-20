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
 * <p>
 * 核心配置：
 * 1. JWT 无状态认证：基于 JWT 令牌的认证机制，不依赖服务器端会话
 * 2. 前后端分离支持：配置 CORS、禁用 CSRF、自定义异常响应格式
 * 3. 安全过滤器链：配置 JWT 过滤器和权限规则
 * <p>
 * 注意：CustomAuthenticationProvider 通过 @Component 自动注册，无需在此配置
 */
@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * 密码加密器：使用 BCrypt 算法进行密码加密
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    /**
     * CORS 配置：支持前后端分离架构的跨域请求
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
            "http://localhost:3000",
            "http://localhost:3001",
            "http://localhost:3002"
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * 安全过滤器链：配置 HTTP 安全策略和权限规则
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 1. 禁用 CSRF 保护（JWT 无状态认证不需要）
            .csrf(csrf -> csrf.disable())
            // 2. 启用 CORS 配置
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            // 3. 禁用 HTTP Basic 认证
            .httpBasic(basic -> basic.disable())
            // 4. 无状态会话管理
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // 5. 自定义异常处理
            .exceptionHandling(ex -> {
                ObjectMapper objectMapper = new ObjectMapper();
                // 认证失败：未登录用户访问受保护资源
                ex.authenticationEntryPoint((request, response, authException) -> {
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    Response<Void> res = ResponseFactory.<Void>fail(E007).getBody();
                    response.getWriter().write(objectMapper.writeValueAsString(res));
                });
                // 授权失败：已登录但权限不足
                ex.accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    Response<Void> res = ResponseFactory.<Void>fail(E008).getBody();
                    response.getWriter().write(objectMapper.writeValueAsString(res));
                });
            })
            // 6. 权限规则配置
            .authorizeHttpRequests(auth -> auth
                // 免认证路径
                .requestMatchers("/api/auth/**", "/api/test/**").permitAll()
                // Swagger 文档路径
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                // 其他所有请求都需要认证
                .anyRequest().authenticated()
            )
            // 7. 添加 JWT 过滤器
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
