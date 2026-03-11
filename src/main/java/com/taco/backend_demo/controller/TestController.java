package com.taco.backend_demo.controller;


import com.taco.backend_demo.common.response.Response;
import com.taco.backend_demo.common.response.ResponseFactory;
import com.taco.backend_demo.entity.PasswordEntity;
import com.taco.backend_demo.mapper.mp.PasswordMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@Tag(name = "测试接口", description = "测试相关的接口")
public class TestController {

    @Autowired
    private PasswordMapper passwordMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    @Operation(summary = "测试接口", description = "返回测试字符串")
    public ResponseEntity<Response<String>> test() {
        return ResponseFactory.success("test");
    }

    @PostMapping
    @Operation(summary = "测试接口", description = "创建测试用户")
    public ResponseEntity<Response<String>> test(String email,String password) {
        PasswordEntity passwordEntity = new PasswordEntity();
        passwordEntity.setEmail(email);
        passwordEntity.setPassword(passwordEncoder.encode(password));
        passwordEntity.setIsLocked(false);
        passwordEntity.setLoginStatus("ACTIVE");
        passwordEntity.setRetryCount(0);
        passwordMapper.insert(passwordEntity);
        return ResponseFactory.success("test");
    }
}