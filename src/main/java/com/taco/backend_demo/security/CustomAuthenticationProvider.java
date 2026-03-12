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

/**
 * 1. 自定义认证提供者：实现Spring Security的AuthenticationProvider接口
 * 2. 密码验证逻辑：验证用户提供的密码与数据库存储的加密密码是否匹配
 * 3. 账户状态检查：验证用户账户是否被锁定等状态信息
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationProvider.class);

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PasswordMapper passwordMapper;

    /**
     * 1. 执行认证逻辑：验证用户凭据并返回认证结果
     * @param authentication 包含用户名和密码的认证对象
     * @return 认证成功的Authentication对象
     * @throws AuthenticationException 认证失败时抛出异常
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 1. 提取认证凭据：从Authentication对象中获取邮箱和密码
        String email = authentication.getName();
        String rawPassword = authentication.getCredentials() != null ? authentication.getCredentials().toString() : "";
        logger.info("Authenticating user: {}, Password length: {}", email, rawPassword.length());

        // 2. 加载用户详情：从数据库获取用户的完整认证信息
        LoginUserInfo loginUserInfo = userDetailsService.loadUserByUsername(email);

        // 3. 验证密码匹配：使用密码编码器比较明文密码和加密密码
        if (!passwordEncoder.matches(rawPassword, loginUserInfo.getPasswordEntity().getPassword())) {
            throw new BusinessException(E004);
        }

        // 4. 检查账户状态：验证用户账户是否被锁定
        if (loginUserInfo.getPasswordEntity().getIsLocked()) {
            throw new BusinessException(E002);
        }

        // 5. 构建认证令牌：创建包含用户信息和角色的认证对象
        return new UsernamePasswordAuthenticationToken(
                loginUserInfo,
                "nopassword",
                Collections.singletonList(LoginConstants.ROLE_USER)
        );
    }

    /**
     * 2. 支持的认证类型：指定此提供者支持的Authentication类型
     * @param authentication 认证对象的Class类型
     * @return true表示支持，false表示不支持
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
