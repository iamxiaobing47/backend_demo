package com.taco.backend_demo.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taco.backend_demo.common.response.Response;
import com.taco.backend_demo.common.response.ResponseFactory;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
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
 * 1. Spring Security核心配置类：定义应用程序的安全策略和过滤器链
 * 2. JWT无状态认证：基于JWT令牌的认证机制，不依赖服务器端会话
 * 3. 前后端分离支持：配置CORS、禁用CSRF、自定义异常响应格式
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAuthenticationProvider customAuthenticationProvider;

    /**
     * 1. 构造函数注入：初始化安全配置所需的依赖组件
     * @param userDetailsService 用户详情服务
     * @param jwtAuthenticationFilter JWT认证过滤器
     * @param customAuthenticationProvider 自定义认证提供者
     */
    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService, 
                          JwtAuthenticationFilter jwtAuthenticationFilter,
                          @Lazy CustomAuthenticationProvider customAuthenticationProvider) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.customAuthenticationProvider = customAuthenticationProvider;
    }

    /**
     * 2. 密码加密器：使用BCrypt算法进行密码加密，强度设置为12
     * @return BCrypt密码加密器实例
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    /**
     * 3. 默认认证管理器：Spring Security默认的认证管理器
     * @param authConfig 认证配置对象
     * @return 认证管理器实例
     * @throws Exception 配置异常
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * 4. 自定义认证管理器：使用自定义认证提供者的认证管理器
     * @return 自定义认证管理器实例
     */
    @Bean
    public AuthenticationManager customAuthenticationManager() {
        return new org.springframework.security.authentication.ProviderManager(
                List.of(customAuthenticationProvider)
        );
    }

    /**
     * 5. 跨域配置：支持前后端分离架构的CORS配置
     * @return CORS配置源
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 1. 允许的前端域名：开发环境的本地端口
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:3001", "http://localhost:3002"));
        // 2. 允许的HTTP方法：RESTful API常用方法
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // 3. 允许的请求头：支持所有请求头
        configuration.setAllowedHeaders(List.of("*"));
        // 4. 允许携带凭证：支持Cookie等认证信息
        configuration.setAllowCredentials(true);
        // 5. 预检请求缓存：减少OPTIONS请求次数
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * 6. 安全过滤器链：配置HTTP安全策略和权限规则
     * @param http HttpSecurity对象
     * @return 安全过滤器链
     * @throws Exception 配置异常
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. 禁用CSRF保护：JWT无状态认证不需要CSRF
                .csrf(AbstractHttpConfigurer::disable)
                // 2. 启用CORS配置：支持跨域请求
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // 3. 禁用HTTP Basic认证：使用JWT替代
                .httpBasic(AbstractHttpConfigurer::disable)
                // 4. 无状态会话管理：JWT不需要服务器端会话
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 5. 自定义异常处理：返回统一的JSON错误响应
                .exceptionHandling(ex -> {
                    ObjectMapper objectMapper = new ObjectMapper();
                    // 5.1 认证失败处理：未登录用户访问受保护资源
                    ex.authenticationEntryPoint((request, response, authException) -> {
                        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        Response<Void> res = ResponseFactory.<Void>fail(E007).getBody();
                        response.getWriter().write(objectMapper.writeValueAsString(res));
                    });
                    // 5.2 授权失败处理：已登录但权限不足
                    ex.accessDeniedHandler((request, response, accessDeniedException) -> {
                        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        Response<Void> res = ResponseFactory.<Void>fail(E008).getBody();
                        response.getWriter().write(objectMapper.writeValueAsString(res));
                    });
                })
                // 6. 权限规则配置：定义URL访问控制策略
                .authorizeHttpRequests(auth -> auth
                        // 6.1 免认证路径：认证接口和测试接口
                        .requestMatchers("/api/auth/**", "/api/test/**").permitAll()
                        // 6.2 Swagger文档路径：API文档接口
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                        // 6.3 默认策略：其他所有请求都需要认证
                        .anyRequest().authenticated()
                );

        // 7. 添加JWT过滤器：在用户名密码过滤器之前执行JWT验证
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
