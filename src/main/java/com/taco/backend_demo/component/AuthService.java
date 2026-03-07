package com.taco.backend_demo.component;

import com.taco.backend_demo.dto.LoginRequest;
import com.taco.backend_demo.dto.LoginResponse;
import com.taco.backend_demo.entity.User;
import com.taco.backend_demo.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Component
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoginLogMapper loginLogMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    public LoginResponse login(LoginRequest request, HttpServletRequest httpRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (Exception e) {
            saveLoginLog(request.getUsername(), "FAIL", e.getMessage(), httpRequest);
            throw new BadCredentialsException("用户名或密码错误");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        User user = userMapper.findByUsername(request.getUsername());
        if (user == null || user.getEnabled() == null || !user.getEnabled()) {
            saveLoginLog(request.getUsername(), "FAIL", "用户已被禁用", httpRequest);
            throw new BadCredentialsException("用户已被禁用");
        }

        String token = jwtUtils.generateToken(user.getUsername());
        
        saveLoginLog(request.getUsername(), "SUCCESS", "登录成功", httpRequest);

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUsername(user.getUsername());
        response.setNickname(user.getNickname());
        response.setUserId(user.getId());
        response.setAvatar(user.getAvatar());
        response.setExpiresIn(jwtUtils.getExpirationTime());
        
        return response;
    }

    public void saveLoginLog(String username, String status, String message, HttpServletRequest request) {
        LoginLog loginLog = new LoginLog();
        loginLog.setUsername(username);
        loginLog.setIp(getClientIp(request));
        loginLog.setUserAgent(request.getHeader("User-Agent"));
        loginLog.setStatus(status);
        loginLog.setMessage(message);
        loginLog.setLoginTime(LocalDateTime.now());
        loginLogMapper.insert(loginLog);
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userMapper.findByUsername(username);
    }

    public User register(User user) {
        if (userMapper.countByUsername(user.getUsername()) > 0) {
            throw new RuntimeException("用户名已存在");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userMapper.insert(user);
        return user;
    }
}
