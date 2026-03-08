package com.taco.backend_demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("passwords")
public class PasswordEntity extends BaseEntity {
    private String email;
    private String passwordHash;
    private Boolean isLocked;
    private String loginStatus;
    private Integer retryCount;
    private LocalDateTime lockedAt;
    private LocalDateTime lastLoginAt;
}
