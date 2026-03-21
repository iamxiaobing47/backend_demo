package com.taco.backend_demo.dto.auth;

import com.taco.backend_demo.dto.user.UserInfo;
import com.taco.backend_demo.entity.PasswordEntity;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Spring Security 登录用户信息封装类
 * 用于 Spring Security 认证流程中的用户信息封装
 */
@Data
public class LoginUserInfo implements UserDetails {

    /**
     * 用户邮箱（用户名）
     */
    private String email;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 用户类型：BUSINESS_USER 或 STAFF_USER
     */
    private String userType;

    /**
     * 组织 ID（业务 pk 或位置 pk）
     */
    private String orgId;

    /**
     * 密码实体（包含加密密码和其他状态）
     */
    private PasswordEntity passwordEntity;

    /**
     * 用户权限集合
     */
    private Collection<? extends GrantedAuthority> authorities;

    /**
     * 核心构造函数
     */
    public LoginUserInfo(String email, String name, String userType, String orgId, PasswordEntity passwordEntity, Collection<? extends GrantedAuthority> authorities) {
        this.email = email;
        this.name = name;
        this.userType = userType;
        this.orgId = orgId;
        this.passwordEntity = passwordEntity;
        this.authorities = authorities;
    }

    /**
     * 从 UserInfo 创建 LoginUserInfo
     */
    public LoginUserInfo(UserInfo userInfo, PasswordEntity passwordEntity, Collection<? extends GrantedAuthority> authorities) {
        this.email = userInfo.getEmail();
        this.name = userInfo.getUserName();
        this.userType = userInfo.getUserType();
        this.orgId = userInfo.getOrgId();
        this.passwordEntity = passwordEntity;
        this.authorities = authorities;
    }

    // ==================== UserDetails 接口方法 ====================

    @Override
    public String getPassword() {
        return this.passwordEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities == null ? Collections.emptyList() : this.authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !Boolean.TRUE.equals(this.passwordEntity.getIsLocked());
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return "ACTIVE".equals(this.passwordEntity.getLoginStatus());
    }
}
