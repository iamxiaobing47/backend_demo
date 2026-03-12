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
import org.springframework.web.bind.annotation.*;

/**
 * 1. 测试控制器：提供开发和调试阶段使用的测试接口
 * 2. 免认证访问：所有/api/test路径下的接口都跳过JWT认证（见JwtAuthenticationFilter）
 * 3. 功能验证：用于快速验证系统基本功能是否正常工作
 */
@RestController
@RequestMapping("/api/test")
@Tag(name = "测试接口", description = "测试相关的接口")
public class TestController {

    @Autowired
    private PasswordMapper passwordMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 1. GET测试接口：返回简单的测试字符串，验证服务是否正常运行
     * @return 包含"test"字符串的成功响应
     */
    @GetMapping
    @Operation(summary = "测试接口", description = "返回测试字符串")
    public ResponseEntity<Response<String>> test() {
        return ResponseFactory.success("test");
    }

    /**
     * 2. POST测试接口：创建测试用户账户，用于验证用户注册流程
     * @param email 用户邮箱地址
     * @param password 用户密码（明文，将被加密存储）
     * @return 创建成功的确认信息
     */
    @PostMapping
    @Operation(summary = "测试接口", description = "创建测试用户")
    public ResponseEntity<Response<String>> createTestUser(@RequestParam String email, @RequestParam String password) {
        // 1. 创建密码实体：存储测试用户的加密密码和账户状态
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
