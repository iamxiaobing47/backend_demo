package com.taco.backend_demo.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taco.backend_demo.common.constants.LoginConstants;
import com.taco.backend_demo.common.exception.BusinessException;
import com.taco.backend_demo.dto.auth.LoginUserInfo;
import com.taco.backend_demo.entity.PasswordEntity;
import com.taco.backend_demo.entity.UserInfoEntity;
import com.taco.backend_demo.mapper.mp.PasswordMapper;
import com.taco.backend_demo.mapper.mp.VUserInfoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static com.taco.backend_demo.common.message.ErrorMessageCodes.E001;
import static com.taco.backend_demo.common.message.ErrorMessageCodes.E002;

/**
 * 自定义用户详情服务：实现 Spring Security 的 UserDetailsService 接口
 * <p>
 * 核心职责：
 * 1. 根据用户名（邮箱）加载用户认证信息
 * 2. 验证账户状态（是否锁定、启用等）
 * 3. 返回包含权限信息的 UserDetails 对象
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final PasswordMapper passwordMapper;
    private final VUserInfoMapper vUserInfoMapper;

    /**
     * 根据用户名加载用户详情
     *
     * @param username 用户名（邮箱地址）
     * @return 包含用户认证和授权信息的 UserDetails 对象
     * @throws UsernameNotFoundException 用户不存在时抛出异常
     * @throws BusinessException         账户被锁定时抛出异常
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Loading user by username: {}", username);

        // 1. 查询密码实体：验证用户邮箱是否存在并获取密码信息
        PasswordEntity passwordEntity = passwordMapper.selectOne(
            new LambdaQueryWrapper<PasswordEntity>()
                .eq(PasswordEntity::getEmail, username)
        );

        if (passwordEntity == null) {
            log.warn("User not found with email: {}", username);
            throw new BusinessException(E001);
        }

        // 2. 检查账户锁定状态
        if (Boolean.TRUE.equals(passwordEntity.getIsLocked())) {
            log.warn("User account is locked: {}", username);
            throw new BusinessException(E002);
        }

        // 3. 检查账户启用状态
        if (!"ACTIVE".equals(passwordEntity.getLoginStatus())) {
            log.warn("User account is not active: {}", username);
            throw new BusinessException(E001);
        }

        // 4. 查询用户信息实体：获取用户的完整个人信息
        UserInfoEntity userInfo = vUserInfoMapper.selectOne(
            new LambdaQueryWrapper<UserInfoEntity>()
                .eq(UserInfoEntity::getEmail, username)
        );

        if (userInfo == null) {
            log.warn("User info not found for email: {}", username);
            throw new BusinessException(E001);
        }

        // 5. 构建用户详情对象
        return new LoginUserInfo(userInfo, passwordEntity,
            Collections.singletonList(LoginConstants.ROLE_USER));
    }
}
