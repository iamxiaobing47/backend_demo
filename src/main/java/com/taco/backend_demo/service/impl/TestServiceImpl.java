package com.taco.backend_demo.service.impl;

import com.taco.backend_demo.entity.PasswordEntity;
import com.taco.backend_demo.mapper.mp.PasswordMapper;
import com.taco.backend_demo.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 测试服务实现类
 */
@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private PasswordMapper passwordMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void createTestUser(String email, String password) {
        // 1. 创建密码实体：存储测试用户的加密密码和账户状态
        PasswordEntity passwordEntity = new PasswordEntity();
        passwordEntity.setEmail(email);
        passwordEntity.setPassword(passwordEncoder.encode(password));
        passwordEntity.setIsLocked(false);
        passwordEntity.setLoginStatus("ACTIVE");
        passwordEntity.setRetryCount(0);
        passwordMapper.insert(passwordEntity);
    }
}
