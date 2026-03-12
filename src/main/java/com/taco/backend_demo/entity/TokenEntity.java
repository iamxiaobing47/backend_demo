package com.taco.backend_demo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 1. 令牌实体类：存储用户刷新令牌和过期时间信息
 * 2. 继承基础实体：包含主键和时间戳字段
 * 3. 令牌管理：支持刷新令牌的存储、验证和过期控制
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("token")
public class TokenEntity extends BaseEntity {
    /**
     * 1. 邮箱地址：关联的用户邮箱
     */
    private String email;
    
    /**
     * 2. 刷新令牌：JWT刷新令牌字符串
     */
    private String refreshToken;
    
    /**
     * 3. 过期时间：令牌的过期时间戳
     */
    private LocalDateTime expiresAt;
}
