package com.taco.backend_demo.dto.auth;
import com.taco.backend_demo.entity.PasswordEntity;
import com.taco.backend_demo.entity.UserInfoEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;

/**
 * Spring Security登录用户信息封装类（极简版）
 * 仅重写核心必要方法，其他方法使用默认实现（返回true）
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LoginUserInfo extends UserInfoEntity implements UserDetails {

    /**
     * 密码实体（包含加密密码和其他状态）
     */
    private PasswordEntity PasswordEntity;

    /**
     * 用户权限集合
     */
    private Collection<? extends GrantedAuthority> authorities;

    /**
     * 核心构造函数：仅接收必要参数
     */
    public LoginUserInfo(UserInfoEntity userInfo, PasswordEntity passwordEntity, Collection<? extends GrantedAuthority> authorities) {
        super(userInfo); // 调用父类的拷贝构造函数
        this.PasswordEntity = passwordEntity;
        this.authorities = authorities;
    }

    // ==================== 仅重写核心必要的UserDetails接口方法 ====================
    /**
     * 【核心】获取用户名（登录账号）：返回邮箱
     */
    @Override
    public String getUsername() {
        return super.getEmail();
    }

    /**
     * 【核心】获取加密密码
     */
    @Override
    public String getPassword() {
        return this.getPasswordEntity().getPassword();
    }

    /**
     * 【核心】获取用户权限（空值保护）
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities == null ? Collections.emptyList() : this.authorities;
    }

    // ==================== 其他接口方法：极简默认实现（无需重写父类逻辑） ====================
    @Override
    public boolean isAccountNonExpired() {
        return true; // 账号默认不过期（如需扩展，再单独处理）
    }

    @Override
    public boolean isAccountNonLocked() {
        // 如需实现锁定逻辑，只需在这里关联passwordEntity的isLocked字段
        // 示例：return !passwordEntity.getIsLocked();
        return true; // 默认未锁定
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 密码默认不过期
    }

    @Override
    public boolean isEnabled() {
        return true; // 账号默认启用
    }
}