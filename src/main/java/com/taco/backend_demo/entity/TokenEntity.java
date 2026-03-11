package com.taco.backend_demo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("token")
public class TokenEntity extends BaseEntity {
    private String email;
    
    @TableField("refresh_token")
    private String refreshToken;
    
    @TableField("expires_at")
    private LocalDateTime expiresAt;
}
