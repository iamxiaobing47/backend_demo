package com.taco.backend_demo.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taco.backend_demo.common.exception.BusinessException;
import com.taco.backend_demo.entity.PasswordEntity;
import com.taco.backend_demo.mapper.mp.PasswordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;

import static com.taco.backend_demo.common.message.Messages.CODE_042;
import static com.taco.backend_demo.common.message.Messages.CODE_043;
import static com.taco.backend_demo.common.message.Messages.CODE_045;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationProvider.class);

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PasswordMapper passwordMapper;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String rawPassword = authentication.getCredentials() != null ? authentication.getCredentials().toString() : "";

        logger.info("Authenticating user: {}, Password length: {}", username, rawPassword.length());

        UserDetails userDetails;
        try {
            userDetails = userDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            throw new BusinessException(CODE_042);
        }

        LambdaQueryWrapper<PasswordEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PasswordEntity::getEmail, username);
        PasswordEntity passwordEntity = passwordMapper.selectOne(wrapper);

        if (passwordEntity == null) {
            throw new BusinessException(CODE_042);
        }

        if (passwordEntity.getIsLocked()) {
            throw new BusinessException(CODE_043);
        }

        if (!passwordEncoder.matches(rawPassword, passwordEntity.getPasswordHash())) {
            throw new BusinessException(CODE_045);
        }

        return new UsernamePasswordAuthenticationToken(
                userDetails,
                rawPassword,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
