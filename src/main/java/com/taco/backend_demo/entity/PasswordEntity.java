package com.taco.backend_demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 1. 密码实体类：存储用户密码和相关安全状态信息
 * 2. 继承基础实体：包含主键和时间戳字段
 * 3. 安全控制：支持账户锁定、登录状态和重试次数管理
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("password")
public class PasswordEntity extends BaseEntity {
    /**
     * 1. 邮箱地址：用户的登录邮箱，作为唯一标识
     */
    private String email;
    
    /**
     * 2. 密码哈希：加密后的用户密码
     */
    private String password;
    
    /**
     * 3. 账户锁定状态：true表示账户被锁定，false表示正常
     */
    private Boolean isLocked;
    
    /**
     * 4. 登录状态：记录用户的当前登录状态
     */
    private String loginStatus;
    
    /**
     * 5. 密码重试次数：记录连续密码错误的次数
     */
    private Integer retryCount;
}
