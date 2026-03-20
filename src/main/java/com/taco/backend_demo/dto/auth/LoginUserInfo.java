package com.taco.backend_demo.dto.auth;

import com.taco.backend_demo.entity.PasswordEntity;
import com.taco.backend_demo.entity.UserInfoEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;

/**
 * Spring Security 登录用户信息封装类
 * 用于 Spring Security 认证流程中的用户信息封装
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LoginUserInfo extends UserInfoEntity implements UserDetails {

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
    public LoginUserInfo(UserInfoEntity userInfo, PasswordEntity passwordEntity, Collection<? extends GrantedAuthority> authorities) {
        super(userInfo);
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
        return super.getEmail();
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
