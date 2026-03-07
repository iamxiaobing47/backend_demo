package com.taco.backend_demo.component;

import com.taco.backend_demo.dto.LoginRequest;
import com.taco.backend_demo.dto.LoginResponse;
import com.taco.backend_demo.entity.LoginLog;
import com.taco.backend_demo.entity.User;
import com.taco.backend_demo.mapper.LoginLogMapper;
import com.taco.backend_demo.mapper.UserMapper;
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
            throw new BadCredentialsException("用户名或密码错误");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        User user = userMapper.findByUsername(request.getUsername());
        if (user == null || user.getEnabled() == null || !user.getEnabled()) {
            throw new BadCredentialsException("用户已被禁用");
        }

        String token = jwtUtils.generateToken(user.getUsername());
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUsername(user.getUsername());
        response.setNickname(user.getNickname());
        response.setUserId(user.getId());
        response.setAvatar(user.getAvatar());
        response.setExpiresIn(jwtUtils.getExpirationTime());
        
        return response;
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userMapper.findByUsername(username);
    }
}
