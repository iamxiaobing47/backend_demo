package com.taco.backend_demo.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taco.backend_demo.common.exception.BusinessException;
import com.taco.backend_demo.entity.PasswordEntity;
import com.taco.backend_demo.mapper.mp.PasswordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;

import static com.taco.backend_demo.common.message.Messages.CODE_042;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private PasswordMapper passwordMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        LambdaQueryWrapper<PasswordEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PasswordEntity::getEmail, username);

        PasswordEntity passwordEntity = passwordMapper.selectOne(wrapper);

        if (passwordEntity == null) {
            throw new BusinessException(CODE_042);
        }

        return new org.springframework.security.core.userdetails.User(
                passwordEntity.getEmail(),
                passwordEntity.getPasswordHash(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    public void updateLastLogin(String email) {
        LambdaQueryWrapper<PasswordEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PasswordEntity::getEmail, email);
        
        PasswordEntity updateEntity = new PasswordEntity();
        updateEntity.setLastLoginAt(LocalDateTime.now());
        passwordMapper.update(updateEntity, wrapper);
    }

    public void resetRetryCount(String email) {
        LambdaQueryWrapper<PasswordEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PasswordEntity::getEmail, email);
        
        PasswordEntity updateEntity = new PasswordEntity();
        updateEntity.setRetryCount(0);
        updateEntity.setIsLocked(false);
        passwordMapper.update(updateEntity, wrapper);
    }
}
