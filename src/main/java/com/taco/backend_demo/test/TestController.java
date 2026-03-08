package com.taco.backend_demo.test;


import com.taco.backend_demo.common.response.Response;
import com.taco.backend_demo.common.response.ResponseFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@Tag(name = "测试接口", description = "测试相关的接口")
public class TestController {

    @GetMapping
    @Operation(summary = "测试接口", description = "返回测试字符串")
    public ResponseEntity<Response<String>> test() {
        return ResponseFactory.success("test");
    }
}