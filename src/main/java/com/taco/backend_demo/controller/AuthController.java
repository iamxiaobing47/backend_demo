package com.taco.backend_demo.controller;

import com.taco.backend_demo.common.response.Response;
import com.taco.backend_demo.common.response.ResponseFactory;
import com.taco.backend_demo.dto.LoginRequest;
import com.taco.backend_demo.dto.LoginResponse;
import com.taco.backend_demo.utils.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Tag(name = "认证管理", description = "用户登录注册相关接口")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Operation(summary = "用户登录", description = "使用用户名和密码登录")
    @PostMapping("/login")
    public ResponseEntity<Response<LoginResponse>> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

//        User user = (User) authentication.getPrincipal();
//        String token = jwtUtils.generateToken(user.getUsername());
        LoginResponse response = new LoginResponse();
//        response.setToken(token);
//        response.setUsername(user.getUsername());
//        response.setNickname(user.getNickname());
//        response.setUserId(user.getId());
//        response.setAvatar(user.getAvatar());
//        response.setExpiresIn(jwtUtils.getExpirationTime());

        return ResponseFactory.success(response, "LOGIN_SUCCESS");
    }
}
