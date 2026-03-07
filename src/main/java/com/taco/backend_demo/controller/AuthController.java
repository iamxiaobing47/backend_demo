package com.taco.backend_demo.controller;

import com.taco.backend_demo.common.response.Response;
import com.taco.backend_demo.common.response.ResponseFactory;
import com.taco.backend_demo.component.AuthService;
import com.taco.backend_demo.dto.LoginRequest;
import com.taco.backend_demo.dto.LoginResponse;
import com.taco.backend_demo.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "认证管理", description = "用户登录注册相关接口")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

//    @Operation(summary = "用户登录", description = "使用用户名和密码登录")
//    @PostMapping("/login")
//    public Response<LoginResponse> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
//        LoginResponse response = authService.login(request, httpRequest);
//        return ResponseFactory.success(response, "LOGIN_SUCCESS");
//    }
//
//    @Operation(summary = "用户注册", description = "注册新用户")
//    @PostMapping("/register")
//    public Response<User> register(@RequestBody User user) {
//        User registeredUser = authService.register(user);
//        registeredUser.setPassword(null);
//        return ResponseFactory.success(registeredUser, "REGISTER_SUCCESS");
//    }
//
//    @Operation(summary = "获取当前用户信息", description = "获取已登录用户的信息")
//    @GetMapping("/current")
//    public Response<User> getCurrentUser() {
//        User user = authService.getCurrentUser();
//        user.setPassword(null);
//        return ResponseFactory.success(user, "GET_USER_SUCCESS");
//    }
//
//    @Operation(summary = "退出登录", description = "退出登录")
//    @PostMapping("/logout")
//    public Response<Void> logout() {
//        return ResponseFactory.success(null, "LOGOUT_SUCCESS");
//    }
}
