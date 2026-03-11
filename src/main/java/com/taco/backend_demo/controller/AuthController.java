package com.taco.backend_demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taco.backend_demo.common.message.ErrorMessageCodes;
import com.taco.backend_demo.common.message.NotificationMessageCodes;
import com.taco.backend_demo.common.response.Response;
import com.taco.backend_demo.common.response.ResponseFactory;
import com.taco.backend_demo.dto.auth.LoginRequest;
import com.taco.backend_demo.dto.auth.LoginResponse;
import com.taco.backend_demo.dto.auth.RefreshTokenRequest;
import com.taco.backend_demo.dto.user.UserInfo;
import com.taco.backend_demo.entity.PasswordEntity;
import com.taco.backend_demo.entity.TokenEntity;
import com.taco.backend_demo.mapper.mp.PasswordMapper;
import com.taco.backend_demo.mapper.mp.TokenMapper;
import com.taco.backend_demo.security.CustomUserDetailsService;
import com.taco.backend_demo.dto.auth.LoginUserInfo;
import com.taco.backend_demo.utils.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import static com.taco.backend_demo.common.message.ErrorMessageCodes.E006;

@Tag(name = "认证管理", description = "用户登录注册相关接口")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager customAuthenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private PasswordMapper passwordMapper;

    @Autowired
    private TokenMapper tokenMapper;

    @Operation(summary = "用户登录", description = "使用邮箱和密码登录")
    @PostMapping("/login")
    public ResponseEntity<Response<LoginResponse>> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {

        // 1. 验证用户登录信息
        Authentication authentication = customAuthenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        // 2. 登录成功，更新用户登录信息
        LoginUserInfo loginUserInfo = (LoginUserInfo) authentication.getPrincipal();
        PasswordEntity passwordEntity = loginUserInfo.getPasswordEntity();
        passwordEntity.setRetryCount(0);
        passwordMapper.updateById(passwordEntity);

        // 3. 生成JWT Token
        UserInfo userInfo = new UserInfo(loginUserInfo);
        String accessToken = jwtUtils.generateAccessToken(userInfo);
        String refreshToken = jwtUtils.generateRefreshToken(userInfo);

        // 4. 保存refreshToken到数据库
        saveRefreshToken(loginUserInfo.getUsername(), refreshToken);

        // 5. 设置refreshToken cookie
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false); // 开发环境设为false，生产环境应为true
        refreshTokenCookie.setPath("/api/auth/refresh");
        refreshTokenCookie.setMaxAge((int) (jwtUtils.getRefreshExpirationTime() / 1000)); // 转换为秒
        httpResponse.addCookie(refreshTokenCookie);

        // 6. 返回accessToken和userInfo
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAccessToken(accessToken);
        loginResponse.setUserInfo(userInfo);
        return ResponseFactory.success(loginResponse);
    }

    @Operation(summary = "刷新Token", description = "使用refresh token获取新的access token")
    @PostMapping("/refresh")
    public ResponseEntity<Response<LoginResponse>> refreshToken(@Valid @RequestBody RefreshTokenRequest request, HttpServletResponse httpResponse) {
        // 1. 验证refreshToken
        String refreshToken = request.getRefreshToken();
        if (!jwtUtils.validateToken(refreshToken)) {
            return ResponseFactory.fail(E006);
        }

        // 2. 从refreshToken中提取email
        String email = jwtUtils.extractEmail(refreshToken);
        UserInfo userInfo = (UserInfo)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 3. 生成新的accessToken
        String newRefreshToken = jwtUtils.generateRefreshToken(userInfo);

        // 更新refreshToken cookie
        Cookie refreshTokenCookie = new Cookie("refreshToken", newRefreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false); // 开发环境设为false，生产环境应为true
        refreshTokenCookie.setPath("/api/auth/refresh");
        refreshTokenCookie.setMaxAge((int) (jwtUtils.getRefreshExpirationTime() / 1000)); // 转换为秒
        httpResponse.addCookie(refreshTokenCookie);
        return ResponseFactory.success(new LoginResponse());
    }

    private void saveRefreshToken(String email, String refreshToken) {
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setEmail(email);
        tokenEntity.setRefreshToken(refreshToken);
        tokenEntity.setExpiresAt(java.time.LocalDateTime.now().plusSeconds(jwtUtils.getRefreshExpirationTime() / 1000));
        tokenMapper.insert(tokenEntity);
    }

    @Operation(summary = "用户登出", description = "清除认证信息并删除refresh token")
    @PostMapping("/logout")
    public ResponseEntity<Response<Void>> logout(HttpServletRequest request, HttpServletResponse response) {
        // 清除refreshToken cookie
        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false);
        refreshTokenCookie.setPath("/api/auth/refresh");
        refreshTokenCookie.setMaxAge(0); // 立即过期
        response.addCookie(refreshTokenCookie);

        // 从数据库删除refresh token
        UserInfo userInfo = (UserInfo)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        tokenMapper.delete(new LambdaQueryWrapper<TokenEntity>().eq(TokenEntity::getEmail, userInfo.getEmail()));
        return ResponseFactory.success(null, NotificationMessageCodes.N021);
    }
}
