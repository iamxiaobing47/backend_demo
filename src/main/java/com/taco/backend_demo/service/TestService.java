package com.taco.backend_demo.service;

/**
 * 测试服务接口
 */
public interface TestService {

    /**
     * 创建测试用户
     * @param email 用户邮箱
     * @param password 用户密码
     */
    void createTestUser(String email, String password);
}
