package com.taco.backend_demo.utils;

import com.taco.backend_demo.dto.user.UserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JWT 工具类：提供 JWT 令牌的生成、解析和验证功能
 * <p>
 * 核心功能：
 * 1. 双令牌机制：支持访问令牌（Access Token）和刷新令牌（Refresh Token）
 * 2. 令牌签名：使用 HMAC-SHA256 算法进行签名
 * 3. 令牌验证：支持令牌有效期和签名验证
 */
@Slf4j
@Component
public class JwtUtils {

    @Value("${jwt.secret:mySecretKeyForJWTTokenGenerationMustBeLongEnough123456}")
    private String secret;

    @Value("${jwt.expiration:86400000}")
    private Long accessExpiration;

    @Value("${jwt.refresh-expiration:604800000}")
    private Long refreshExpiration;

    /**
     * 从 JWT 令牌中提取用户邮箱
     */
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * 从 JWT 令牌中提取指定的声明信息
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 生成访问令牌
     */
    public String generateAccessToken(UserInfo userInfo) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userInfo.getUserId());
        return createToken(claims, userInfo.getEmail(), accessExpiration);
    }

    /**
     * 生成刷新令牌
     */
    public String generateRefreshToken(UserInfo userInfo) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userInfo.getUserId());
        return createToken(claims, userInfo.getEmail(), refreshExpiration);
    }

    /**
     * 验证令牌是否有效
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return !isTokenExpired(token);
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    /**
     * 获取访问令牌过期时间（毫秒）
     */
    public Long getExpirationTime() {
        return accessExpiration;
    }

    /**
     * 获取刷新令牌过期时间（毫秒）
     */
    public Long getRefreshExpirationTime() {
        return refreshExpiration;
    }

    // ==================== 私有方法 ====================

    /**
     * 创建 JWT 令牌
     */
    private String createToken(Map<String, Object> claims, String subject, Long expirationTime) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * 解析 JWT 令牌的所有声明
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 检查令牌是否过期
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * 获取令牌的过期时间
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * 获取签名密钥
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
