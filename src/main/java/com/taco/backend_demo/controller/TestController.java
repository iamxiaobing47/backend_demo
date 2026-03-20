package com.taco.backend_demo.controller;

import com.taco.backend_demo.common.response.Response;
import com.taco.backend_demo.common.response.ResponseFactory;
import com.taco.backend_demo.service.TestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 测试控制器：提供开发和调试阶段使用的测试接口
 */
@RestController
@RequestMapping("/api/test")
@Tag(name = "测试接口", description = "测试相关的接口")
public class TestController {

    @Autowired
    private TestService testService;

    /**
     * GET 测试接口
     * @return 包含"test"字符串的成功响应
     */
    @GetMapping
    @Operation(summary = "测试接口", description = "返回测试字符串")
    public ResponseEntity<Response<String>> test() {
        return ResponseFactory.success("test");
    }

    /**
     * POST 测试接口：创建测试用户账户
     * @param email 用户邮箱地址
     * @param password 用户密码（明文，将被加密存储）
     * @return 创建成功的确认信息
     */
    @PostMapping
    @Operation(summary = "测试接口", description = "创建测试用户")
    public ResponseEntity<Response<String>> createTestUser(@RequestParam String email, @RequestParam String password) {
        testService.createTestUser(email, password);
        return ResponseFactory.success("test");
    }
}
