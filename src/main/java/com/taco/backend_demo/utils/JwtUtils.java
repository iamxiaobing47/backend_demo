package com.taco.backend_demo.utils;

import com.taco.backend_demo.dto.user.UserInfo;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * 1. JWT工具类：提供JWT令牌的生成、解析和验证功能
 * 2. 双令牌机制：支持访问令牌（Access Token）和刷新令牌（Refresh Token）
 * 3. 安全配置：使用HMAC-SHA算法进行令牌签名，支持密钥和过期时间配置
 */
@Component
public class JwtUtils {

    /**
     * 1. JWT签名密钥：用于令牌的签名和验证
     */
    @Value("${jwt.secret:mySecretKeyForJWTTokenGenerationMustBeLongEnough123456}")
    private String secret;

    /**
     * 2. 访问令牌过期时间：默认24小时（86400000毫秒）
     */
    @Value("${jwt.expiration:86400000}")
    private Long accessExpiration;

    /**
     * 3. 刷新令牌过期时间：默认7天（604800000毫秒）
     */
    @Value("${jwt.refresh-expiration:604800000}")
    private Long refreshExpiration;

    /**
     * 4. 提取邮箱地址：从JWT令牌中获取用户邮箱（Subject）
     * @param token JWT令牌字符串
     * @return 用户邮箱地址
     */
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * 5. 提取过期时间：从JWT令牌中获取令牌的过期时间
     * @param token JWT令牌字符串
     * @return 令牌过期时间
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * 6. 提取声明信息：从JWT令牌中提取指定的声明信息
     * @param token JWT令牌字符串
     * @param claimsResolver 声明解析函数
     * @param <T> 返回值类型
     * @return 解析后的声明值
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 7. 提取所有声明：解析JWT令牌并获取所有声明信息
     * @param token JWT令牌字符串
     * @return 令牌的所有声明信息
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 8. 检查令牌是否过期：比较令牌过期时间与当前时间
     * @param token JWT令牌字符串
     * @return true表示已过期，false表示未过期
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * 9. 生成访问令牌：创建短期有效的访问令牌
     * @param loginUserInfo 用户登录信息
     * @return 访问令牌字符串
     */
    public String generateAccessToken(UserInfo loginUserInfo) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", loginUserInfo.getUserId());
        return createToken(claims, loginUserInfo.getEmail(), accessExpiration);
    }

    /**
     * 10. 生成刷新令牌：创建长期有效的刷新令牌
     * @param loginUserInfo 用户登录信息
     * @return 刷新令牌字符串
     */
    public String generateRefreshToken(UserInfo loginUserInfo) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", loginUserInfo.getUserId());
        return createToken(claims, loginUserInfo.getEmail(), refreshExpiration);
    }

    /**
     * 11. 创建JWT令牌：根据声明、主题和过期时间生成令牌
     * @param claims 令牌声明信息
     * @param subject 令牌主题（通常是用户标识）
     * @param expirationTime 过期时间（毫秒）
     * @return JWT令牌字符串
     */
    private String createToken(Map<String, Object> claims, String subject, Long expirationTime) {
        return Jwts.builder()
                .subject(subject)
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * 12. 获取签名密钥：将字符串密钥转换为SecretKey对象
     * @return HMAC-SHA签名密钥
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 13. 验证令牌有效性：检查令牌是否有效且用户名匹配
     * @param token JWT令牌字符串
     * @param userDetails Spring Security用户详情
     * @return true表示令牌有效，false表示无效
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractEmail(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * 14. 验证令牌基本有效性：检查令牌签名和过期状态
     * @param token JWT令牌字符串
     * @return true表示令牌有效，false表示无效
     */
    public Boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 15. 获取访问令牌过期时间
     * @return 访问令牌过期时间（毫秒）
     */
    public Long getExpirationTime() {
        return accessExpiration;
    }

    /**
     * 16. 获取刷新令牌过期时间
     * @return 刷新令牌过期时间（毫秒）
     */
    public Long getRefreshExpirationTime() {
        return refreshExpiration;
    }
}
