package com.taco.backend_demo.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taco.backend_demo.common.constants.LoginConstants;
import com.taco.backend_demo.common.exception.BusinessException;
import com.taco.backend_demo.dto.auth.LoginUserInfo;
import com.taco.backend_demo.entity.PasswordEntity;
import com.taco.backend_demo.entity.UserInfoEntity;
import com.taco.backend_demo.mapper.mp.PasswordMapper;
import com.taco.backend_demo.mapper.mp.VUserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

import static com.taco.backend_demo.common.message.ErrorMessageCodes.E001;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private PasswordMapper passwordMapper;
    
    @Autowired
    private VUserInfoMapper vUserInfoMapper;

    @Override
    public LoginUserInfo loadUserByUsername(String username) throws UsernameNotFoundException {

        // 1. 获取密码实体
        LambdaQueryWrapper<PasswordEntity> passwordQuery = new LambdaQueryWrapper<>();
        passwordQuery.eq(PasswordEntity::getEmail, username);
        PasswordEntity passwordEntity = passwordMapper.selectOne(passwordQuery);
        if (passwordEntity == null) {
            throw new BusinessException(E001);
        }

        // 2. 获取用户信息实体
        LambdaQueryWrapper<UserInfoEntity> userInfoQuery = new LambdaQueryWrapper<>();
        userInfoQuery.eq(UserInfoEntity::getEmail, username);
        UserInfoEntity userInfo = vUserInfoMapper.selectOne(userInfoQuery);
        if (userInfo == null) {
            throw new BusinessException(E001);
        }

        return new LoginUserInfo(userInfo, passwordEntity, Collections.singletonList(LoginConstants.ROLE_GUEST));
    }
}