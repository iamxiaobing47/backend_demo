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

/**
 * 1. 自定义用户详情服务：实现Spring Security的UserDetailsService接口
 * 2. 用户认证数据加载：根据用户名（邮箱）加载用户的认证和授权信息
 * 3. 异常处理：用户不存在时抛出业务异常，统一错误码处理
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private PasswordMapper passwordMapper;
    
    @Autowired
    private VUserInfoMapper vUserInfoMapper;

    /**
     * 1. 根据用户名加载用户详情：Spring Security认证流程的核心方法
     * @param username 用户名（实际为邮箱地址）
     * @return 包含用户认证和授权信息的LoginUserInfo对象
     * @throws UsernameNotFoundException 当用户不存在时抛出异常
     */
    @Override
    public LoginUserInfo loadUserByUsername(String username) throws UsernameNotFoundException {

        // 1. 查询密码实体：验证用户邮箱是否存在并获取密码信息
        LambdaQueryWrapper<PasswordEntity> passwordQuery = new LambdaQueryWrapper<>();
        passwordQuery.eq(PasswordEntity::getEmail, username);
        PasswordEntity passwordEntity = passwordMapper.selectOne(passwordQuery);
        if (passwordEntity == null) {
            throw new BusinessException(E001);
        }

        // 2. 查询用户信息实体：获取用户的完整个人信息
        LambdaQueryWrapper<UserInfoEntity> userInfoQuery = new LambdaQueryWrapper<>();
        userInfoQuery.eq(UserInfoEntity::getEmail, username);
        UserInfoEntity userInfo = vUserInfoMapper.selectOne(userInfoQuery);
        if (userInfo == null) {
            throw new BusinessException(E001);
        }

        // 3. 构建用户详情对象：组合密码信息、用户信息和默认角色
        return new LoginUserInfo(userInfo, passwordEntity, Collections.singletonList(LoginConstants.ROLE_GUEST));
    }
}
