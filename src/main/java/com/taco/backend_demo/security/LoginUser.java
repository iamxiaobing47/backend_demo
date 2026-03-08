package com.taco.backend_demo.security;

import com.taco.backend_demo.entity.PasswordEntity;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Data
public class LoginUser implements UserDetails {
    
    private Long id;
    private String email;
    private String passwordHash;
    private Boolean isLocked;
    private String loginStatus;
    private Integer retryCount;
    private Collection<? extends GrantedAuthority> authorities;

    public LoginUser() {
    }

    public LoginUser(PasswordEntity passwordEntity, Collection<? extends GrantedAuthority> authorities) {
        this.id = passwordEntity.getPk();
        this.email = passwordEntity.getEmail();
        this.passwordHash = passwordEntity.getPasswordHash();
        this.isLocked = passwordEntity.getIsLocked();
        this.loginStatus = passwordEntity.getLoginStatus();
        this.retryCount = passwordEntity.getRetryCount();
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities != null ? authorities : Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !Boolean.TRUE.equals(isLocked);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return "ACTIVE".equals(loginStatus);
    }
}