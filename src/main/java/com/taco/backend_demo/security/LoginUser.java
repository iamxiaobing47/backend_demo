package com.taco.backend_demo.security;

import com.taco.backend_demo.entity.BusinessUserEntity;
import com.taco.backend_demo.entity.PasswordEntity;
import com.taco.backend_demo.entity.StaffUserEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser implements UserDetails {
    
    private Long id;
    private String email;
    private String passwordHash;
    private Boolean isLocked;
    private String loginStatus;
    private Integer retryCount;
    private Collection<? extends GrantedAuthority> authorities;
    private String role; // 'employee' or 'business_owner'
    private String businessOwnerId; // businessId for business owner users
    private String locationId; // locationId for employee users

    public LoginUser(PasswordEntity passwordEntity, Collection<? extends GrantedAuthority> authorities) {
        this.id = passwordEntity.getPk();
        this.email = passwordEntity.getEmail();
        this.passwordHash = passwordEntity.getPasswordHash();
        this.isLocked = passwordEntity.getIsLocked();
        this.loginStatus = passwordEntity.getLoginStatus();
        this.retryCount = passwordEntity.getRetryCount();
        this.authorities = authorities;
    }

    public LoginUser(PasswordEntity passwordEntity, Collection<? extends GrantedAuthority> authorities, 
                     String role, String businessOwnerId, String locationId) {
        this(passwordEntity, authorities);
        this.role = role;
        this.businessOwnerId = businessOwnerId;
        this.locationId = locationId;
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
    
    public String getRole() {
        return role;
    }
    
    public String getBusinessOwnerId() {
        return businessOwnerId;
    }
    
    public String getLocationId() {
        return locationId;
    }
}