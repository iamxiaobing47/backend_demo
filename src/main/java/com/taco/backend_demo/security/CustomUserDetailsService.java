package com.taco.backend_demo.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taco.backend_demo.common.constants.LoginConstants;
import com.taco.backend_demo.common.exception.BusinessException;
import com.taco.backend_demo.dto.auth.LoginUserInfo;
import com.taco.backend_demo.entity.BusinessUserEntity;
import com.taco.backend_demo.entity.PasswordEntity;
import com.taco.backend_demo.entity.StaffUserEntity;
import com.taco.backend_demo.mapper.mp.BusinessUserMapper;
import com.taco.backend_demo.mapper.mp.PasswordMapper;
import com.taco.backend_demo.mapper.mp.StaffUserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
 *
 * 【UserDetailsService 的作用】
 * Spring Security 使用 UserDetailsService 从数据库加载用户信息
 * 它是认证流程的第一步，负责查询用户并检查账户状态
 *
 * 【核心职责】
 * 1. 根据用户名（邮箱）加载用户认证信息
 * 2. 验证账户状态（是否锁定、是否启用等）
 * 3. 返回包含权限信息的 UserDetails 对象
 *
 * 【工作流程】
 * 1. 接收用户名（邮箱） → 2. 查询数据库 → 3. 检查账户状态 → 4. 返回 UserDetails
 *
 * 【重要说明】
 * - 用户名是邮箱地址，不是传统的 username
 * - 密码验证由 CustomAuthenticationProvider 完成
 *
 * @author taco
 */
@Slf4j
@Service  // Spring 服务组件，会被自动扫描
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    // ==================== 依赖注入 ====================

    /**
     * 密码 Mapper：操作用户密码表（password）
     * 用于查询用户密码和账户状态信息
     */
    private final PasswordMapper passwordMapper;

    /**
     * 业务用户 Mapper：查询业务用户信息
     */
    private final BusinessUserMapper businessUserMapper;

    /**
     * 员工用户 Mapper：查询员工用户信息
     */
    private final StaffUserMapper staffUserMapper;

    /**
     * 【核心方法】根据用户名加载用户详情
     * <p>
     * Spring Security 会在用户认证时调用此方法
     * <p>
     * 完整流程：
     * 1. 查询密码实体：验证用户邮箱是否存在
     * 2. 检查账户锁定状态：防止被锁定的用户登录
     * 3. 检查账户启用状态：只有 ACTIVE 状态才能登录
     * 4. 查询用户信息实体：从 business_user 或 staff_user 获取信息
     * 5. 构建并返回 LoginUserInfo 对象
     *
     * @param username 用户名（邮箱地址）
     *                 例如：user@example.com
     * @return UserDetails 对象，包含用户认证和授权信息
     *         - 用户邮箱（作为用户名）
     *         - 加密后的密码
     *         - 权限列表
     * @throws UsernameNotFoundException 用户不存在时抛出异常
     *                                   Spring Security 会捕获并返回 401
     * @throws BusinessException         账户被锁定时抛出异常
     *                                   返回对应的错误码和消息
     */
    @Override
    @Transactional(readOnly = true)  // 只读事务，提高查询性能
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Loading user by username: {}", username);

        // 【步骤 1】查询密码实体
        // 验证用户邮箱是否存在并获取密码信息
        PasswordEntity passwordEntity = passwordMapper.selectOne(
            new LambdaQueryWrapper<PasswordEntity>()
                .eq(PasswordEntity::getEmail, username)
        );

        // 用户不存在，返回通用错误（不暴露"用户不存在"的信息）
        if (passwordEntity == null) {
            log.warn("User not found with email: {}", username);
            throw new BusinessException(E001);  // 用户不存在
        }

        // 【步骤 2】检查账户锁定状态
        // Boolean.TRUE.equals() 是安全的空值检查方式
        if (Boolean.TRUE.equals(passwordEntity.getIsLocked())) {
            log.warn("User account is locked: {}", username);
            throw new BusinessException(E002);  // 账户被锁定
        }

        // 【步骤 3】检查账户启用状态
        // 只有 loginStatus 为 "ACTIVE" 才能登录
        if (!"ACTIVE".equals(passwordEntity.getLoginStatus())) {
            log.warn("User account is not active: {}", username);
            throw new BusinessException(E001);  // 用户不存在（统一错误）
        }

        // 【步骤 4】查询用户信息实体
        // 先尝试从 business_user 表查询
        BusinessUserEntity businessUser = businessUserMapper.selectOne(
            new LambdaQueryWrapper<BusinessUserEntity>()
                .eq(BusinessUserEntity::getEmail, username)
        );

        String userType;
        String name;
        String orgId;

        if (businessUser != null) {
            // 业务用户
            userType = "BUSINESS_USER";
            name = businessUser.getName();
            orgId = String.valueOf(businessUser.getBusinessPk());
        } else {
            // 尝试从 staff_user 表查询
            StaffUserEntity staffUser = staffUserMapper.selectOne(
                new LambdaQueryWrapper<StaffUserEntity>()
                    .eq(StaffUserEntity::getEmail, username)
            );

            if (staffUser == null) {
                log.warn("User info not found for email: {}", username);
                throw new BusinessException(E001);
            }

            // 员工用户
            userType = "STAFF_USER";
            name = staffUser.getName();
            orgId = String.valueOf(staffUser.getLocationPk());
        }

        // 【步骤 5】构建用户详情对象
        return new LoginUserInfo(username, name, userType, orgId, passwordEntity,
            Collections.singletonList(LoginConstants.ROLE_USER));
    }
}
