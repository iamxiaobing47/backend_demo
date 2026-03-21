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
 *
 * JWT (JSON Web Token) 是一种开放标准 (RFC 7519)，用于在网络应用环境间安全地传输声明信息
 *
 * 核心功能：
 * 1. 双令牌机制：支持访问令牌（Access Token）和刷新令牌（Refresh Token）
 *    - Access Token: 短期令牌，用于访问受保护的资源
 *    - Refresh Token: 长期令牌，用于刷新 Access Token
 * 2. 令牌签名：使用 HMAC-SHA256 算法进行签名，确保令牌不被篡改
 * 3. 令牌验证：支持令牌有效期和签名验证
 *
 * @author taco
 */
@Slf4j
@Component
public class JwtUtils {

    // ==================== 配置参数 ====================

    /**
     * JWT 签名密钥
     * 从配置文件读取，默认值为 "mySecretKeyForJWTTokenGenerationMustBeLongEnough123456"
     * 注意：生产环境应使用更复杂且保密的密钥，至少 256 位
     */
    @Value("${jwt.secret:mySecretKeyForJWTTokenGenerationMustBeLongEnough123456}")
    private String secret;

    /**
     * Access Token 过期时间（毫秒）
     * 默认值：86400000 毫秒 = 24 小时
     */
    @Value("${jwt.expiration:86400000}")
    private Long accessExpiration;

    /**
     * Refresh Token 过期时间（毫秒）
     * 默认值：604800000 毫秒 = 7 天
     */
    @Value("${jwt.refresh-expiration:604800000}")
    private Long refreshExpiration;

    // ==================== 公开方法 ====================

    /**
     * 【步骤 1】从 JWT 令牌中提取用户邮箱
     * <p>
     * 工作流程：
     * 1. 调用 extractClaim 方法，传入令牌和 Claims::getSubject 函数
     * 2. getSubject 返回 JWT 的 subject 字段，即用户邮箱
     *
     * @param token JWT 令牌字符串
     * @return 用户邮箱地址
     */
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * 【步骤 2】从 JWT 令牌中提取指定的声明信息
     * <p>
     * 泛型方法说明：
     * - <T>: 泛型类型参数，表示期望返回的类型
     * - Function<Claims, T>: 函数式接口，接收 Claims 对象，返回类型 T 的结果
     * <p>
     * 工作流程：
     * 1. 调用 extractAllClaims 解析令牌，获取所有声明
     * 2. 应用 claimsResolver 函数，提取所需的声明值
     *
     * @param token JWT 令牌字符串
     * @param claimsResolver 声明解析函数，用于从 Claims 中提取特定字段
     * @return 提取的声明值
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        // 先解析令牌获取所有声明
        final Claims claims = extractAllClaims(token);
        // 再应用解析函数提取具体字段
        return claimsResolver.apply(claims);
    }

    /**
     * 【步骤 3】生成访问令牌（Access Token）
     * <p>
     * Access Token 特点：
     * - 有效期较短（默认 24 小时）
     * - 包含用户基本信息（userId）
     * - 用于访问受保护的 API 资源
     * <p>
     * 工作流程：
     * 1. 创建声明 Map，添加用户 ID
     * 2. 调用 createToken 创建令牌，使用 accessExpiration 作为过期时间
     *
     * @param userInfo 用户信息对象
     * @return 生成的 Access Token
     */
    public String generateAccessToken(UserInfo userInfo) {
        // 创建自定义声明，将用户类型加入令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put("userType", userInfo.getUserType());
        // 使用 accessExpiration 创建短期令牌
        return createToken(claims, userInfo.getEmail(), accessExpiration);
    }

    /**
     * 【步骤 4】生成刷新令牌（Refresh Token）
     * <p>
     * Refresh Token 特点：
     * - 有效期较长（默认 7 天）
     * - 仅用于刷新 Access Token，不用于访问资源
     * - 存储在安全的 HttpOnly Cookie 中
     * <p>
     * 工作流程：
     * 1. 创建声明 Map，添加用户 ID
     * 2. 调用 createToken 创建令牌，使用 refreshExpiration 作为过期时间
     *
     * @param userInfo 用户信息对象
     * @return 生成的 Refresh Token
     */
    public String generateRefreshToken(UserInfo userInfo) {
        // 创建自定义声明，将用户类型加入令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put("userType", userInfo.getUserType());
        // 使用 refreshExpiration 创建长期令牌
        return createToken(claims, userInfo.getEmail(), refreshExpiration);
    }

    /**
     * 【步骤 5】验证令牌是否有效
     * <p>
     * 验证内容：
     * 1. 签名验证：确保令牌未被篡改
     * 2. 格式验证：确保令牌格式正确
     * 3. 有效期验证：确保令牌未过期
     * <p>
     * 异常处理：
     * - SignatureException: 签名不匹配
     * - MalformedJwtException: 令牌格式错误
     * - UnsupportedJwtException: 不支持的令牌类型
     * - IllegalArgumentException: 令牌内容为空
     *
     * @param token JWT 令牌字符串
     * @return 令牌是否有效
     */
    public boolean validateToken(String token) {
        try {
            // 解析并验证令牌
            Jwts.parser()
                    .verifyWith(getSigningKey())  // 使用密钥验证签名
                    .build()
                    .parseSignedClaims(token);    // 解析令牌
            // 检查是否过期
            return !isTokenExpired(token);
        } catch (SignatureException e) {
            // 签名验证失败
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            // 令牌格式错误
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            // 不支持的令牌类型
            log.error("Unsupported JWT token: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            // 令牌内容为空
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    /**
     * 获取访问令牌过期时间（毫秒）
     *
     * @return accessExpiration 毫秒值
     */
    public Long getExpirationTime() {
        return accessExpiration;
    }

    /**
     * 获取刷新令牌过期时间（毫秒）
     *
     * @return refreshExpiration 毫秒值
     */
    public Long getRefreshExpirationTime() {
        return refreshExpiration;
    }

    // ==================== 私有方法 ====================

    /**
     * 【核心】创建 JWT 令牌
     * <p>
     * JWT 结构：Header.Payload.Signature
     * - Header: 包含算法和令牌类型
     * - Payload: 包含声明信息
     * - Signature: 签名，用于验证完整性
     * <p>
     * 参数说明：
     * 1. claims: 自定义声明（如 userId、角色等）
     * 2. subject: 主题（这里是用户邮箱）
     * 3. expirationTime: 过期时间（毫秒）
     *
     * @param claims 自定义声明 Map
     * @param subject 主题（用户邮箱）
     * @param expirationTime 过期时间（毫秒）
     * @return 生成的 JWT 字符串
     */
    private String createToken(Map<String, Object> claims, String subject, Long expirationTime) {
        return Jwts.builder()
                .claims(claims)                                    // 设置自定义声明
                .subject(subject)                                  // 设置主题（邮箱）
                .issuedAt(new Date(System.currentTimeMillis()))    // 设置签发时间
                .expiration(new Date(System.currentTimeMillis() + expirationTime))  // 设置过期时间
                .signWith(getSigningKey())                         // 使用密钥签名
                .compact();                                        // 构建并返回紧凑格式的 JWT
    }

    /**
     * 解析 JWT 令牌的所有声明
     * <p>
     * 工作流程：
     * 1. 使用密钥验证签名
     * 2. 解析令牌
     * 3. 获取 Payload（声明内容）
     *
     * @param token JWT 令牌字符串
     * @return Claims 对象，包含所有声明
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())    // 验证签名
                .build()
                .parseSignedClaims(token)       // 解析令牌
                .getPayload();                  // 获取 Payload
    }

    /**
     * 检查令牌是否过期
     * <p>
     * 原理：比较令牌的过期时间和当前时间
     *
     * @param token JWT 令牌字符串
     * @return 是否过期
     */
    private boolean isTokenExpired(String token) {
        // before 方法：如果过期时间在当前时间之前，返回 true（已过期）
        return extractExpiration(token).before(new Date());
    }

    /**
     * 获取令牌的过期时间
     * <p>
     * 通过 extractClaim 方法提取 "exp" 字段（过期时间）
     *
     * @param token JWT 令牌字符串
     * @return 过期时间 Date 对象
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * 获取签名密钥
     * <p>
     * 原理：
     * 1. 将 secret 字符串转换为字节数组
     * 2. 使用 Keys.hmacShaKeyFor 生成 HMAC-SHA256 密钥
     *
     * @return SecretKey 签名密钥
     */
    private SecretKey getSigningKey() {
        // 将密钥字符串转换为 UTF-8 字节数组
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        // 生成 HMAC-SHA256 密钥
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
