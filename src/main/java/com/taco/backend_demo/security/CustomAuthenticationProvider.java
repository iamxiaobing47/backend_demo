package com.taco.backend_demo.security;

import com.taco.backend_demo.common.constants.LoginConstants;
import com.taco.backend_demo.common.exception.BusinessException;
import com.taco.backend_demo.dto.auth.LoginUserInfo;
import com.taco.backend_demo.mapper.mp.PasswordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;

import static com.taco.backend_demo.common.message.ErrorMessageCodes.E002;
import static com.taco.backend_demo.common.message.ErrorMessageCodes.E004;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationProvider.class);

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PasswordMapper passwordMapper;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 1.从Authentication中提取用户名（邮箱）和密码
        String email = authentication.getName();
        String rawPassword = authentication.getCredentials() != null ? authentication.getCredentials().toString() : "";
        logger.info("Authenticating user: {}, Password length: {}", email, rawPassword.length());

        // 2.从数据库加载用户信息
        LoginUserInfo loginUserInfo = userDetailsService.loadUserByUsername(email);

        // 3. 验证密码是否匹配
        if (!passwordEncoder.matches(rawPassword, loginUserInfo.getPasswordEntity().getPassword())) {
            throw new BusinessException(E004);
        }

        // 4. 检查用户是否被锁定
        if (loginUserInfo.getPasswordEntity().getIsLocked()) {
            throw new BusinessException(E002);
        }

        return new UsernamePasswordAuthenticationToken(
                loginUserInfo,
                "nopassword",
                Collections.singletonList(LoginConstants.ROLE_USER)
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
